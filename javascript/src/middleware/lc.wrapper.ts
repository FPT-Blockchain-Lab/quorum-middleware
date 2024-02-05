import BN from "bn.js";
import Web3 from "web3";
import { AbiItem, encodePacked, keccak256, Mixed } from "web3-utils";
import { LCContractABIs } from "../abi/lc";
import { LCManagement, RouterService, LCFactory, LC as LCContract } from "../bindings/lc";
import { OrgManager } from "../bindings/permission";
import { DEFAULT_CONFIG, LC_ENUM } from "../config";
import { StageContent, Stage, AmendStage } from "./interfaces";
import { LC } from "./lc";
import { Permission } from "./permission";
import { Utils } from "./utils";

export class LCWrapper {
    private readonly web3: Web3;
    private readonly LCManagement: LCManagement;
    private readonly RouterService: RouterService;
    private readonly LCFactory: LCFactory;
    private readonly OrgManager: OrgManager;

    constructor(web3: Web3, config = DEFAULT_CONFIG) {
        if (
            !config.lCContractAddresses.LCManagement ||
            !config.lCContractAddresses.RouterService ||
            !config.lCContractAddresses.LCFactory ||
            !config.permissionContractAddresses.OrgManager
        ) {
            throw new Error(`required LCManagement RouterService LCFactory OrgManager to be defined`);
        }

        const { LCManagement, RouterService, LCFactory } = LC.loadContract(web3, config);
        const { OrgManager } = Permission.loadContract(web3, config);

        this.web3 = web3;
        this.LCManagement = LCManagement;
        this.RouterService = RouterService;
        this.LCFactory = LCFactory;
        this.OrgManager = OrgManager;
    }

    /**
     *
     * @param parties involved parties in LC contract
     * @param content content of LC
     * @param from executor
     * @returns
     */
    async createLC(parties: string[], content: Omit<StageContent, "approvalSignature" | "rootHash">, lcType: LC_ENUM, from: string) {
        // Check acknowledge signature
        if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
            throw new Error("Invalid acknowledge signature.");
        }

        // Format approval content to create approval message hash
        const approvalContent = {
            rootHash: Utils.DEFAULT_ROOT_HASH,
            prevHash: content.prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            acknowledgeSignature: content.acknowledgeSignature,
        };

        // Get approval signature
        const approvalSignature = await this.approvalSignature(approvalContent, from);

        const _content = {
            rootHash: Utils.DEFAULT_ROOT_HASH,
            prevHash: content.prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            numOfDocuments: content.numOfDocuments,
            acknowledgeSignature: content.acknowledgeSignature,
            approvalSignature: approvalSignature,
        };

        // Validate data
        await this.validateData(lcType, parties, _content, from);

        // Generate data to create LC
        const data = await this.generateDataForCreateLC(parties, _content);

        const gas = await this.LCFactory.methods.create(...data, lcType).estimateGas({ from });

        return this.LCFactory.methods.create(...data, lcType).send({ from, maxPriorityFeePerGas: undefined, maxFeePerGas: undefined });
    }

    /**
     *
     * @param parties involved parties in LC contract
     * @param content content of LC
     * @param from executor
     * @returns
     */
    // async createUPASLC(parties: string[], content: Omit<StageContent, "approvalSignature" | "rootHash">, from: string) {
    //     // Check acknowledge signature
    //     if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
    //         throw new Error("Invalid acknowledge signature.");
    //     }

    //     // Format approval content to create approval message hash
    //     const approvalContent = {
    //         rootHash: Utils.DEFAULT_ROOT_HASH,
    //         prevHash: content.prevHash,
    //         contentHash: content.contentHash,
    //         url: content.url,
    //         signedTime: content.signedTime,
    //         acknowledgeSignature: content.acknowledgeSignature,
    //     };

    //     // Get approval signature
    //     const approvalSignature = await this.approvalSignature(approvalContent, from);

    //     const _content = {
    //         rootHash: Utils.DEFAULT_ROOT_HASH,
    //         prevHash: content.prevHash,
    //         contentHash: content.contentHash,
    //         url: content.url,
    //         signedTime: content.signedTime,
    //         numOfDocuments: content.numOfDocuments,
    //         acknowledgeSignature: content.acknowledgeSignature,
    //         approvalSignature: approvalSignature,
    //     };

    //     // Validate data
    //     await this.validateData(LC.LCTYPE.UPAS_LC.toString(), parties, _content, from);

    //     // Generate data to create LC
    //     const data = await this.generateDataForCreateLC(parties, _content);

    //     const gas = await this.LCFactory.methods.create(...data).estimateGas({ from });

    //     return this.LCFactory.methods.create(...data).send({ from, gas });
    // }

    /**
     *
     * @param documentId hash of LC number
     * @param stage
     * @param subStage
     * @param content content of stage
     * @param from executor
     * @returns
     */
    async approveLC(
        documentId: number | string | BN,
        stage: number,
        subStage: number,
        content: Omit<StageContent, "approvalSignature" | "rootHash" | "prevHash">,
        from: string
    ) {
        const { lcContract: StandardLC, type: _typeOf } = await this.getLCContract(documentId);

        // Validate stage and sub-stage order and get stage content
        const stageInfo = await this.getStageInfo(StandardLC, documentId, stage, subStage, "approve");

        // Get message hash
        const prevHash = LC.generateStageHash({
            rootHash: stageInfo[0],
            signedTime: new BN(stageInfo[1]),
            prevHash: stageInfo[2],
            contentHash: stageInfo[4],
            url: stageInfo[5],
            acknowledgeSignature: stageInfo[6],
            approvalSignature: stageInfo[7],
        });

        if (stage == LC.Stage.PHAT_HANH_LC || stage == LC.Stage.CHAP_NHAN_THANH_TOAN || stage == LC.Stage.UPAS_NHTT_NHXT) {
            if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
                throw new Error("Invalid acknowledge signature.");
            }
        }

        const ROOT_HASH = await this.RouterService.methods.getRootHash(documentId).call();

        // Format approval content to create approval message hash
        const approvalContent = {
            rootHash: ROOT_HASH,
            prevHash: prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            acknowledgeSignature: content.acknowledgeSignature,
        };

        // Get approval signature
        const approvalSignature = await this.approvalSignature(approvalContent, from);

        // Check org by stage
        await this.checkOrgByStage(StandardLC, stage, _typeOf, from);

        // Check is org whitelist and account belong to org
        // await this.validateAccountOrg(org, from);

        const data: [
            number | string | BN,
            number | string | BN,
            number | string | BN,
            [
                string | number[],
                number | string | BN,
                string | number[],
                number | string | BN,
                (string | number[])[],
                string,
                string | number[],
                string | number[]
            ]
        ] = [
            documentId,
            stage,
            subStage,
            [
                ROOT_HASH,
                content.signedTime.toString(),
                prevHash,
                content.numOfDocuments,
                content.contentHash,
                content.url,
                content.acknowledgeSignature,
                approvalSignature,
            ],
        ];

        const gas = await this.RouterService.methods.approve(...data).estimateGas({ from });

        return this.RouterService.methods.approve(...data).send({ from, maxPriorityFeePerGas: undefined, maxFeePerGas: undefined });
    }

    /**
     *
     * @param documentId hash of LC number
     * @param from executor
     * @returns transaction result
     */
    async closeLC(documentId: number | string | BN, from: string) {
        const gas = await this.RouterService.methods.closeLC(documentId).estimateGas({ from });

        return this.RouterService.methods.closeLC(documentId).send({ from, maxPriorityFeePerGas: undefined, maxFeePerGas: undefined });
    }

    /**
     * Submit amend request
     * @param documentId hash of LC number
     * @param stage amend stage
     * @param subStage amend sub stage
     * @param content amend stage content
     * @param migrateStages an array of migrating stages
     * @param from address of proposer
     * @returns transaction result
     */
    async submitAmendment(
        documentId: number | string | BN,
        stage: number,
        subStage: number,
        content: Omit<StageContent, "approvalSignature" | "rootHash" | "prevHash">,
        migrateStages: Stage[],
        from: string
    ) {
        const { lcContract: StandardLC } = await this.getLCContract(documentId);

        // get stage content for amend
        const amendStageContent = await this.getAmendInfo(StandardLC, documentId, stage, subStage);
        const amendStageInfo = amendStageContent.stageInfo;
        const amendStage = amendStageContent.amendStage;
        const amendSubStage = amendStageContent.amendSubStage;

        if (amendStageInfo[7] === Utils.EMPTY_BYTES) throw new Error("Stage Amend not found");

        // Get root hash and prevHash
        const { rootHash, prevHash } = await this.getRoothashAndPrevhash(documentId, amendStageInfo);

        // Get content of migrate stage
        const migrating_stages = await this.calMigrateStages(documentId, migrateStages);

        if (amendStage == LC.Stage.PHAT_HANH_LC || amendStage == LC.Stage.CHAP_NHAN_THANH_TOAN || amendStage == LC.Stage.UPAS_NHTT_NHXT) {
            if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
                throw new Error("Invalid acknowledge signature.");
            }
        }

        // Format approval content to create approval message hash
        const approvalContent = {
            rootHash: rootHash,
            prevHash: prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            acknowledgeSignature: content.acknowledgeSignature,
        };

        // Get approval signature
        const approvalSignature = await this.approvalSignature(approvalContent, from);

        // Format amendment data to create amend message
        const amendStageFormat = {
            stage: amendStage,
            subStage: amendSubStage,
            content: {
                rootHash: rootHash,
                prevHash: prevHash,
                contentHash: content.contentHash,
                url: content.url,
                numOfDocuments: content.numOfDocuments,
                signedTime: content.signedTime,
                acknowledgeSignature: content.acknowledgeSignature,
                approvalSignature: approvalSignature,
            },
        };

        // Get amend signature
        const amendSignature = await this.amendSingature(migrating_stages, amendStageFormat, from);

        const data: [
            number | string | BN,
            (string | number[])[],
            [
                number | string | BN,
                number | string | BN,
                [
                    string | number[],
                    number | string | BN,
                    string | number[],
                    number | string | BN,
                    (string | number[])[],
                    string,
                    string | number[],
                    string | number[]
                ]
            ],
            string | number[]
        ] = [
            documentId,
            migrating_stages,
            [
                amendStage,
                amendSubStage,
                [
                    rootHash,
                    content.signedTime.toString(),
                    prevHash,
                    content.numOfDocuments,
                    content.contentHash,
                    content.url,
                    content.acknowledgeSignature,
                    approvalSignature,
                ],
            ],
            amendSignature,
        ];
        const gas = await this.RouterService.methods.submitAmendment(...data).estimateGas({ from });

        return this.RouterService.methods.submitAmendment(...data).send({ from, maxPriorityFeePerGas: undefined, maxFeePerGas: undefined });
    }

    /**
     * Approve amend request
     * @param documentId hash of LC number
     * @param proposer address of proposer
     * @param nonce
     * @param from address of approver
     * @returns transaction result
     */
    async approveAmendment(documentId: number | string | BN, proposer: string, nonce: BN, from: string) {
        // Generate requestId
        const requestId = LC.generateRequestId(proposer, nonce);

        const [amendmentRequest, isApproved] = await Promise.all([
            this.RouterService.methods.getAmendmentRequest(documentId, requestId).call(),
            this.RouterService.methods.isAmendApproved(documentId, requestId).call(),
        ]);

        if (!amendmentRequest) throw new Error("Amend request not found.");
        if (isApproved) throw new Error("Amend request has been approved.");

        const content = Object.assign({}, amendmentRequest[3][2]);
        // Format amendmentRequest
        const amendStage = {
            stage: parseInt(amendmentRequest[3][0], 10),
            subStage: parseInt(amendmentRequest[3][1], 10),
            content: {
                rootHash: content[0],
                prevHash: content[2],
                contentHash: content[4],
                url: content[5],
                numOfDocuments: parseInt(content[3], 10),
                signedTime: new BN(content[1]),
                acknowledgeSignature: content[6],
                approvalSignature: content[7],
            },
        };
        // Get amend signature
        const amendSig = await this.amendSingature(amendmentRequest[2], amendStage, from);

        const gas = await this.RouterService.methods.approveAmendment(documentId, requestId, amendSig).estimateGas({ from });

        return this.RouterService.methods
            .approveAmendment(documentId, requestId, amendSig)
            .send({ from, maxPriorityFeePerGas: undefined, maxFeePerGas: undefined });
    }

    /**
     * Only execute by proposer of amend request
     * @param documentId hash of LC number
     * @param nonce
     * @param from address of proposer
     * @returns transaction result
     */
    async fulfillAmendment(documentId: number | string | BN, nonce: BN, from: string) {
        // Generate requestId
        const requestId = LC.generateRequestId(from, nonce);

        const request = await this.RouterService.methods.getAmendmentRequest(documentId, requestId).call();
        if (!request) throw new Error("Amend request not found.");

        const gas = await this.RouterService.methods.fulfillAmendment(documentId, requestId).estimateGas({ from });

        return this.RouterService.methods.fulfillAmendment(documentId, requestId).send({ from, maxPriorityFeePerGas: undefined, maxFeePerGas: undefined });
    }

    /**
     * calculate LC general stages
     * @param lastestStages array of lastest general stage
     * @returns LC general stages
     */
    private _calculateStages(lastestStages: number[]): Stage[] {
        let res: Stage[] = [];

        for (let i = 0; i < lastestStages.length; i++) {
            for (let j = 1; j < lastestStages[i]; j++) {
                res = [...res, { stage: j + 1, subStage: i + 1 }];
            }
        }

        return res;
    }

    /**
     * get LC stages (included general and root stage)
     * @param lc instance of LC contract
     * @returns LC stages
     */
    private async _getLCStatus(lcContract: LCContract) {
        const rootSubStage = +(await lcContract.methods.numOfSubStage(1).call());
        const lcStatus = await lcContract.methods.getStatus().call();
        let rootStages: Stage[] = [];
        for (let i = 1; i <= rootSubStage; i++) {
            rootStages = [...rootStages, { stage: 1, subStage: i }];
        }
        const lcStages = this._calculateStages(lcStatus.map((stage) => parseInt(stage, 10)));

        return [...rootStages, ...lcStages];
    }

    /**
     * get LC contract
     * @param documentId hash of LC number
     * @returns instance of LC contract
     */
    async getLCContract(documentId: number | string | BN) {
        const { _contract, _lcType } = await this.RouterService.methods.getAddress(documentId).call();
        if (/^0x0+$/.test(_contract)) throw new Error("DocumentId not found");

        return {
            lcContract: new this.web3.eth.Contract(LCContractABIs.LC as any[] as AbiItem[], _contract) as any as LCContract,
            type: LC_ENUM[_lcType as keyof typeof LC_ENUM],
        };
    }

    /**
     * get LC status sort by root hash
     * @param documentId hash of LC number
     * @param lcContract instance of LC contract
     * @returns array of LC status sort by root hash
     */
    async getLCStatus(documentId: number | string | BN, lcContract: LCContract) {
        const stages = await this._getLCStatus(lcContract);
        const stageContents = await Promise.all(
            stages.map(async (stage) => {
                const content = await this.RouterService.methods.getStageContent(documentId, stage.stage, stage.subStage).call();
                return { stage, rootHash: content[0] };
            })
        );
        const rootList = await lcContract.methods.getRootList().call();
        let roots: Mixed[] = [];
        let migrateStages: Stage[] = [{ stage: 1, subStage: 1 }];

        for (let i = 0; i < rootList.length; i++) {
            roots = [
                ...roots,
                {
                    v: rootList[i],
                    t: "bytes32",
                },
            ];

            const hash = keccak256(encodePacked(...roots) ?? "");
            const generalStages = stageContents.filter((stageContent) => stageContent.rootHash == hash && stageContent.stage.stage != 1);

            migrateStages = [...migrateStages, ...generalStages.map((i) => i.stage)];
            if (i != rootList.length - 1) {
                migrateStages.push({ stage: 1, subStage: i + 2 });
            }
        }

        return migrateStages;
    }

    /**
     * Submit root amend
     * @param documentId hash of LC number
     * @param content amend stage content
     * @param from address of proposer
     * @returns transaction result
     */
    async submitRootAmendment(documentId: number | string | BN, content: Omit<StageContent, "approvalSignature" | "rootHash" | "prevHash">, from: string) {
        const { lcContract: LCContract } = await this.getLCContract(documentId);
        const rootSubStage = +(await LCContract.methods.numOfSubStage(1).call());
        const migrateStages = await this.getLCStatus(documentId, LCContract);

        return this.submitAmendment(documentId, 1, rootSubStage, content, migrateStages, from);
    }

    /**
     * Submit amend another stage in LC
     * Not allowed to submit root stage
     * @param documentId hash of LC number
     * @param stage amend stage
     * @param subStage amend sub stage
     * @param content amend stage content
     * @param from address of proposer
     * @returns transaction result
     */
    async submitGeneralAmendment(
        documentId: number | string | BN,
        stage: number,
        subStage: number,
        content: Omit<StageContent, "approvalSignature" | "rootHash" | "prevHash">,
        from: string
    ) {
        if (stage == 1) {
            throw new Error("Not allowed to submit root stage.");
        }

        const { lcContract: LCContract } = await this.getLCContract(documentId);
        const lcStatus = await this.getLCStatus(documentId, LCContract);
        const migrateStages = lcStatus.filter((item) => !(item.subStage == subStage && item.stage >= stage));

        return this.submitAmendment(documentId, stage, subStage, content, migrateStages, from);
    }

    /**
     *
     * @param rootHash a hash of `rootList` array
     * @param prevHash a hash of previous stage
     * @param contentHash hash of content of LC
     * @param url
     * @param signedTime sign time
     * @param acknowledgeSignature message confirm SWIFT is correct format
     * @param from executor
     * @returns approval signature after sign
     */
    private async approvalSignature(
        content: {
            rootHash: string;
            prevHash: string;
            contentHash: string[];
            url: string;
            signedTime: BN;
            acknowledgeSignature: string;
        },
        from: string
    ) {
        // Get approval message hash
        const approvalMessageHash = LC.generateApprovalMessageHash({
            rootHash: content.rootHash,
            prevHash: content.prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            acknowledgeSignature: content.acknowledgeSignature,
        });

        const approvalSignature = await this.web3.eth.personal.sign(approvalMessageHash, from, "");

        return approvalSignature;
    }

    /**
     *
     * @param typeOf type of LC contract
     * @param parties involved parties in LC contract
     * @param content content of LC contract
     * @param from executor
     * @returns
     */
    private async validateData(typeOf: LC_ENUM, parties: string[], content: StageContent, from: string) {
        let isValidOrg = false;

        if (typeOf === LC_ENUM.STANDARD_LC) {
            if (parties.length != LC.NUMOFPARTIES.STANDARD_LC_PARTIES) {
                throw new Error("The number of involved parties does not match. Expected 4.");
            }

            const orgs = await Promise.all(
                [LC.INDEXOFORG.NHPH, LC.INDEXOFORG.NHTB].map((i) => this.OrgManager.methods.checkOrgExists(parties[i].toString()).call())
            );

            if (!orgs.every((org) => org)) {
                throw new Error("Organization at index 0 or 1 does not exsist.");
            }

            isValidOrg = await this.validateAccountOrg(parties[0], from);
        } else if (typeOf === LC_ENUM.UPAS_LC) {
            if (parties.length != LC.NUMOFPARTIES.UPAS_LC_PARTIES) {
                throw new Error("The number of involved parties does not match. Expected 5.");
            }

            const orgs = await Promise.all(
                [LC.INDEXOFORG.NHPH, LC.INDEXOFORG.NHTB, LC.INDEXOFORG.NHTT].map((i) => this.OrgManager.methods.checkOrgExists(parties[i].toString()).call())
            );

            if (!orgs.every((org) => org)) {
                throw new Error("Organization at index 0 or 1 or 2 does not exsist.");
            }

            isValidOrg = await this.validateAccountOrg(parties[0], from);
        } else {
            const orgs = await Promise.all(parties.map((party) => this.OrgManager.methods.checkOrgExists(party.toString()).call()));

            if (!orgs.some((org) => org)) {
                throw new Error("Expected at least one organization exists.");
            }

            const validOrgs = await Promise.all(parties.map((party) => this.validateAccountOrg(party, from)));

            isValidOrg = validOrgs.some((validOrg) => validOrg);
        }

        if (!isValidOrg) {
            throw new Error("Organization not whitelist or Account not belong to organization");
        }

        if (content.numOfDocuments > content.contentHash.length) {
            throw new Error("The number of documents cannot greater than the length of content hash.");
        }

        if (content.prevHash != content.contentHash[0]) {
            throw new Error("Unlink to previous");
        }
    }

    /**
     *
     * @param org an org in involved parties
     * @param from executor
     * @returns
     */
    private async validateAccountOrg(org: string, from: string) {
        const [isWhitelist, isVerifyIdentity] = await Promise.all([
            this.LCManagement.methods.whitelistOrgs(org).call(),
            this.LCManagement.methods.verifyIdentity(from, org).call(),
        ]);

        return isWhitelist && isVerifyIdentity;
    }

    /**
     * @param lcContract type of LC contract
     * @param documentId hash of LC number
     * @param stage
     * @param subStage
     * @param typeOf type of LC contract
     * @returns content of stage
     */
    async getStageInfo(lcContract: LCContract, documentId: number | string | BN, stage: number, subStage: number, typeOf: string) {
        let prevStage = stage,
            prevSubStage = subStage;
        const rootSubStage = +(await lcContract.methods.numOfSubStage(1).call());
        //  Stage and Sub-stage must follow order constraints:
        //  - Row:
        //      Example: Stage 1.1 <- Stage 2.1 <- Stage 3.1
        //                         <- Stage 2.2 <- Stage 3.2
        //  - Column: only apply on Stage 2 (Stage 3 and other are exceptional)
        //      Example: Stage 1.1 <- Stage 2.1 <- Stage 3.1
        //                         <- Stage 2.2 <- Stage 3.2 <- Stage 4.2
        //      It means that Stage 2.1 must be submitted before Stage 2.2
        //      However, Stage 3.2 could be submitted before Stage 3.1 as long as the row constraint is qualified
        if (stage < LC.Stage.THONG_BAO_BCT_MH && +(await lcContract.methods.numOfSubStage(stage).call()) != subStage - 1) {
            throw new Error("Invalid sub stage");
        }
        if (stage != LC.Stage.PHAT_HANH_LC) {
            prevStage = prevStage - 1;
        }

        if (stage == LC.Stage.XUAT_TRINH_TCD_BCT) {
            prevSubStage = rootSubStage;
        }
        const stageInfo = await this.RouterService.methods.getStageContent(documentId, prevStage, prevSubStage).call();

        return stageInfo;
    }

    /**
     * @param lcContract type of LC contract
     * @param documentId hash of LC number
     * @param stage
     * @param subStage
     * @returns content of stage, number of stage and sub-stage amend
     */
    async getAmendInfo(lcContract: LCContract, documentId: number | string | BN, stage: number, subStage: number) {
        const amendStage = stage;
        let amendSubStage = subStage;
        let prevStage = amendStage,
            prevSubStage = amendSubStage;
        const rootSubStage = +(await lcContract.methods.numOfSubStage(1).call());

        if (amendStage == LC.Stage.PHAT_HANH_LC) {
            amendSubStage = rootSubStage + 1;
        } else {
            prevStage = prevStage - 1;
        }

        if (amendStage == LC.Stage.XUAT_TRINH_TCD_BCT) {
            prevSubStage = rootSubStage;
        }

        const stageInfo = await this.RouterService.methods.getStageContent(documentId, prevStage, prevSubStage).call();
        return { stageInfo, amendStage, amendSubStage };
    }

    /**
     * @param lcContract type of LC contract
     * @param migrateStages number of stage and sub-stage to migrate
     * @returns array content of each stage and sub-stage to migrate
     */
    private async calMigrateStages(documentId: string | number | BN, migrateStages: Stage[]) {
        const migrating_stages = await Promise.all(
            migrateStages.map(async (s) => {
                const content = await this.RouterService.methods.getStageContent(documentId, s.stage, s.subStage).call();
                return LC.generateStageHash({
                    rootHash: content[0],
                    signedTime: new BN(content[1]),
                    prevHash: content[2],
                    contentHash: content[4],
                    url: content[5],
                    acknowledgeSignature: content[6],
                    approvalSignature: content[7],
                });
            })
        );
        return migrating_stages;
    }

    /**
     * @param documentId hash of LC number
     * @param stageContent content of stage
     * @returns rootHash of LC and prevHash of stage
     */
    private async getRoothashAndPrevhash(documentId: string | number | BN, stageContent: [string, string, string, string, string[], string, string, string]) {
        const rootHash = await this.RouterService.methods.getRootHash(documentId).call();
        // get prevHash
        const prevHash = LC.generateStageHash({
            rootHash: stageContent[0],
            signedTime: new BN(stageContent[1]),
            prevHash: stageContent[2],
            contentHash: stageContent[4],
            url: stageContent[5],
            acknowledgeSignature: stageContent[6],
            approvalSignature: stageContent[7],
        });
        return { rootHash, prevHash };
    }

    /**
     * @param migrateStages array content of each stage and sub-stage to migrate
     * @param amendStage content of stage to amend
     * @returns a signature is signed by a requestor to be amend
     */
    private async amendSingature(
        migrateStages: string[],
        amendStage: {
            stage: number;
            subStage: number;
            content: {
                rootHash: string;
                prevHash: string;
                contentHash: string[];
                url: string;
                numOfDocuments: number;
                signedTime: BN;
                acknowledgeSignature: string;
                approvalSignature: string;
            };
        },
        from: string
    ) {
        // Get amend message hash
        const amendMessageHash = LC.generateAmendMessageHash(migrateStages, amendStage);
        // Get amend signature
        const amendSignature = await this.web3.eth.personal.sign(amendMessageHash, from, "");
        return amendSignature;
    }

    /**
     * @param lcContract LC contract
     * @param stage
     * @param typeOf type of LC contract
     * @returns an org in involved parties
     */
    private async checkOrgByStage(lcContract: LCContract, stage: number, lcType: LC_ENUM, from: string) {
        const parties = await lcContract.methods.getInvolvedParties().call();
        let orgs = [];

        if (lcType == LC_ENUM.STANDARD_LC) {
            if (stage == LC.Stage.XUAT_TRINH_TCD_BCT || stage == LC.Stage.UPAS_NHXT_BTH) {
                orgs = [parties[LC.INDEXOFORG.NHTB]];
            } else {
                orgs = [parties[LC.INDEXOFORG.NHPH]];
            }
        } else if (lcType == LC_ENUM.UPAS_LC) {
            if (stage == LC.Stage.XUAT_TRINH_TCD_BCT || stage == LC.Stage.UPAS_NHXT_BTH) {
                orgs = [parties[LC.INDEXOFORG.NHTB]];
            } else if (stage == LC.Stage.UPAS_NHTT_NHXT) {
                orgs = [parties[LC.INDEXOFORG.NHTT]];
            } else {
                orgs = [parties[LC.INDEXOFORG.NHPH]];
            }
        } else {
            orgs = parties;
        }

        const validOrgs = await Promise.all(orgs.map((org) => this.validateAccountOrg(org, from)));

        if (!validOrgs.some((validOrg) => validOrg)) {
            throw new Error("Organization not whitelist or Account not belong to organization");
        }
    }

    /**
     *
     * @param parties involved parties in LC contract
     * @param content content of LC contract
     * @returns data to create LC
     */
    private async generateDataForCreateLC(
        parties: string[],
        content: {
            rootHash: string;
            prevHash: string;
            contentHash: string[];
            url: string;
            signedTime: BN;
            numOfDocuments: number;
            acknowledgeSignature: string;
            approvalSignature: string;
        }
    ) {
        const _data: [
            string[],
            [
                string | number[],
                number | string | BN,
                string | number[],
                number | string | BN,
                (string | number[])[],
                string,
                string | number[],
                string | number[]
            ]
        ] = [
            parties,
            [
                content.rootHash,
                content.signedTime.toString(),
                content.prevHash,
                content.numOfDocuments,
                content.contentHash,
                content.url,
                content.acknowledgeSignature,
                content.approvalSignature,
            ],
        ];
        return _data;
    }
}
