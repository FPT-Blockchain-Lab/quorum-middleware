import BN from "bn.js";
import Web3 from "web3";
import { AbiItem, encodePacked, keccak256, Mixed } from "web3-utils";
import { LCContractABIs } from "../abi/lc";
import { LCManagement, RouterService, StandardLCFactory, UPASLCFactory, UPASLC, StandardLC } from "../bindings/lc";
import { OrgManager } from "../bindings/permission";
import { DEFAULT_CONFIG } from "../config";
import { StageContent, Stage } from "./interfaces";
import { LC } from "./lc";
import { Permission } from "./permission";
import { Utils } from "./utils";

export class LCWrapper {
    private readonly web3: Web3;
    private readonly LCManagement: LCManagement;
    private readonly RouterService: RouterService;
    private readonly StandardLCFactory: StandardLCFactory;
    private readonly UPASLCFactory: UPASLCFactory;
    private readonly OrgManager: OrgManager;

    constructor(web3: Web3, config = DEFAULT_CONFIG) {
        if (
            !config.lCContractAddresses.LCManagement ||
            !config.lCContractAddresses.RouterService ||
            !config.lCContractAddresses.StandardLCFactory ||
            !config.lCContractAddresses.UPASLCFactory ||
            !config.permissionContractAddresses.OrgManager
        ) {
            throw new Error(`required LCManagement RouterService StandardLCFactory UPASLCFactory OrgManager to be defined`);
        }

        const { LCManagement, RouterService, StandardLCFactory, UPASLCFactory } = LC.loadContract(web3, config);
        const { OrgManager } = Permission.loadContract(web3, config);

        this.web3 = web3;
        this.LCManagement = LCManagement;
        this.RouterService = RouterService;
        this.StandardLCFactory = StandardLCFactory;
        this.UPASLCFactory = UPASLCFactory;
        this.OrgManager = OrgManager;
    }

    /**
     *
     * @param parties an array of involved parties
     * @param content content of LC
     * @param from executor
     * @returns
     */
    async createStandardLC(parties: string[], content: Omit<StageContent, "approvalSignature" | "rootHash">, from: string) {
        // Check acknowledge signature
        if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
            throw new Error("Invalid acknowledge signature.");
        }

        // Get approval message hash
        const approvalMessageHash = LC.generateApprovalMessageHash({
            rootHash: Utils.DEFAULT_ROOT_HASH,
            prevHash: content.prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            acknowledgeSignature: content.acknowledgeSignature,
        });

        const approvalSignature = await this.web3.eth.personal.sign(approvalMessageHash, from, "");

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

        if (parties.length != 4) {
            throw new Error("The number of involved parties does not match. Expected 4.");
        }

        const orgs = await Promise.all([0, 1].map((i) => this.OrgManager.methods.checkOrgExists(parties[i].toString()).call()));

        if (!orgs.every((org) => org)) {
            throw new Error("Organization at index 0 or 1 does not exsist.");
        }

        const [isWhitelist, isVerifyIdentity] = await Promise.all([
            this.LCManagement.methods.whitelistOrgs(parties[0]).call(),
            this.LCManagement.methods.verifyIdentity(from, parties[0]).call(),
        ]);

        if (!isWhitelist) {
            throw new Error("Organization does not whitelist.");
        }

        if (!isVerifyIdentity) {
            throw new Error("Account not belong to organization.");
        }

        if (_content.numOfDocuments > _content.contentHash.length) {
            throw new Error("The number of documents cannot greater than the length of content hash.");
        }

        if (_content.prevHash != _content.contentHash[0]) {
            throw new Error("Unlink to previous");
        }

        const data: [
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
                _content.rootHash,
                _content.signedTime.toString(),
                _content.prevHash,
                _content.numOfDocuments,
                _content.contentHash,
                _content.url,
                _content.acknowledgeSignature,
                _content.approvalSignature,
            ],
        ];

        const gas = await this.StandardLCFactory.methods.create(...data).estimateGas({ from });

        return this.StandardLCFactory.methods.create(...data).send({ from, gas });
    }

    /**
     *
     * @param parties an array of involved parties
     * @param content content of LC
     * @param from executor
     * @returns
     */
    async createUPASLC(parties: string[], content: Omit<StageContent, "approvalSignature" | "rootHash">, from: string) {
        // Check acknowledge signature
        if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
            throw new Error("Invalid acknowledge signature.");
        }

        // Get approval message hash
        const approvalMessageHash = LC.generateApprovalMessageHash({
            rootHash: Utils.DEFAULT_ROOT_HASH,
            prevHash: content.prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            acknowledgeSignature: content.acknowledgeSignature,
        });

        const approvalSignature = await this.web3.eth.personal.sign(approvalMessageHash, from, "");

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

        if (parties.length != 5) {
            throw new Error("The number of involved parties does not match. Expected 5.");
        }

        const orgs = await Promise.all([0, 1, 2].map((i) => this.OrgManager.methods.checkOrgExists(parties[i].toString()).call()));

        if (!orgs.every((org) => org)) {
            throw new Error("Organization at index 0 or 1 or 2 does not exsist.");
        }

        const [isWhitelist, isVerifyIdentity] = await Promise.all([
            this.LCManagement.methods.whitelistOrgs(parties[0]).call(),
            this.LCManagement.methods.verifyIdentity(from, parties[0]).call(),
        ]);

        if (!isWhitelist) {
            throw new Error("Organization does not whitelist.");
        }

        if (!isVerifyIdentity) {
            throw new Error("Account not belong to organization.");
        }

        if (_content.numOfDocuments > _content.contentHash.length) {
            throw new Error("The number of documents cannot greater than the length of content hash.");
        }

        if (_content.prevHash != _content.contentHash[0]) {
            throw new Error("Unlink to previous");
        }

        const data: [
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
                _content.rootHash,
                _content.signedTime.toString(),
                _content.prevHash,
                _content.numOfDocuments,
                _content.contentHash,
                _content.url,
                _content.acknowledgeSignature,
                _content.approvalSignature,
            ],
        ];
        const gas = await this.UPASLCFactory.methods.create(...data).estimateGas({ from });

        return this.UPASLCFactory.methods.create(...data).send({ from, gas });
    }

    /**
     *
     * @param documentId hash of LC number
     * @param stage
     * @param subStage
     * @param content
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
        let prevStage = stage,
            prevSubStage = subStage;
        const rootSubStage = +(await StandardLC.methods.numOfSubStage(1).call());

        //  Stage and Sub-stage must follow order constraints:
        //  - Row:
        //      Example: Stage 1.1 <- Stage 2.1 <- Stage 3.1
        //                         <- Stage 2.2 <- Stage 3.2
        //  - Column: only apply on Stage 2 (Stage 3 and other are exceptional)
        //      Example: Stage 1.1 <- Stage 2.1 <- Stage 3.1
        //                         <- Stage 2.2 <- Stage 3.2 <- Stage 4.2
        //      It means that Stage 2.1 must be submitted before Stage 2.2
        //      However, Stage 3.2 could be submitted before Stage 3.1 as long as the row constraint is qualified
        if (stage < 3 && +(await StandardLC.methods.numOfSubStage(stage).call()) != subStage - 1) {
            throw new Error("Invalid sub stage");
        }

        if (stage != 1) {
            prevStage = prevStage - 1;
        }

        if (stage == 2) {
            prevSubStage = rootSubStage;
        }

        const stageInfo = await this.RouterService.methods.getStageContent(documentId, prevStage, prevSubStage).call();

        // Get message hash
        const prevHash = LC.generateStageHash({
            rootHash: stageInfo[0],
            prevHash: stageInfo[2],
            contentHash: stageInfo[4],
            url: stageInfo[5],
            signedTime: new BN(stageInfo[1]),
            acknowledgeSignature: stageInfo[6],
            approvalSignature: stageInfo[7],
        });

        if (stage == 1 || stage == 4 || stage == 5) {
            if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
                throw new Error("Invalid acknowledge signature.");
            }
        }

        const ROOT_HASH = await this.RouterService.methods.getRootHash(documentId).call();

        // Get approval message hash
        const messageHash = LC.generateApprovalMessageHash({
            rootHash: ROOT_HASH,
            prevHash: prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: content.signedTime,
            acknowledgeSignature: content.acknowledgeSignature,
        });

        const approval_sig = await this.web3.eth.personal.sign(messageHash, from, "");
        const parties = await StandardLC.methods.getInvolvedParties().call();
        let org = "";

        if (_typeOf == "1") {
            if (stage == 2 || stage == 6) {
                org = parties[1];
            } else {
                org = parties[0];
            }
        } else if (_typeOf == "2") {
            if (stage == 2 || stage == 6) {
                org = parties[1];
            } else if (stage == 5) {
                org = parties[2];
            } else {
                org = parties[0];
            }
        }

        const [isWhitelist, isVerifyIdentity] = await Promise.all([
            this.LCManagement.methods.whitelistOrgs(org).call(),
            this.LCManagement.methods.verifyIdentity(from, org).call(),
        ]);

        if (!isWhitelist) {
            throw new Error("Organization does not whitelist.");
        }

        if (!isVerifyIdentity) {
            throw new Error("Account not belong to organization.");
        }

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
                approval_sig,
            ],
        ];

        const gas = await this.RouterService.methods.approve(...data).estimateGas({ from });

        return this.RouterService.methods.approve(...data).send({ from, gas });
    }

    /**
     *
     * @param documentId hash of LC number
     * @param from executor
     * @returns transaction result
     */
    async closeLC(documentId: number | string | BN, from: string) {
        const gas = await this.RouterService.methods.closeLC(documentId).estimateGas({ from });

        return this.RouterService.methods.closeLC(documentId).send({ from, gas });
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
        const amendStage = stage;
        let amendSubStage = subStage;
        let prevStage = amendStage,
            prevSubStage = amendSubStage;
        const rootSubStage = +(await StandardLC.methods.numOfSubStage(1).call());

        if (amendStage == 1) {
            amendSubStage = rootSubStage + 1;
        } else {
            prevStage = prevStage - 1;
        }

        if (amendStage == 2) {
            prevSubStage = rootSubStage;
        }

        const amendStageContent = await this.RouterService.methods.getStageContent(documentId, prevStage.toString(), prevSubStage.toString()).call();

        if (amendStageContent[7] === Utils.EMPTY_BYTES) throw new Error("Stage Amend not found");

        const rootHash = await this.RouterService.methods.getRootHash(documentId).call();

        // get prevHash
        const prevHash = LC.generateStageHash({
            rootHash: amendStageContent[0],
            prevHash: amendStageContent[2],
            contentHash: amendStageContent[4],
            url: amendStageContent[5],
            signedTime: new BN(amendStageContent[1]),
            acknowledgeSignature: amendStageContent[6],
            approvalSignature: amendStageContent[7],
        });

        const migrating_stages = await Promise.all(
            migrateStages.map(async (s) => {
                const content = await this.RouterService.methods.getStageContent(documentId, s.stage, s.subStage).call();
                return LC.generateStageHash({
                    rootHash: content[0],
                    prevHash: content[2],
                    url: content[5],
                    contentHash: content[4],
                    signedTime: new BN(content[1]),
                    acknowledgeSignature: content[6],
                    approvalSignature: content[7],
                });
            })
        );

        if (amendStage == 1 || amendStage == 4 || amendStage == 5) {
            if (!/^0x[0-9a-zA-Z]{130}$/.test(content.acknowledgeSignature)) {
                throw new Error("Invalid acknowledge signature.");
            }
        }

        // get approval signature
        const messageHash = LC.generateApprovalMessageHash({
            rootHash,
            prevHash,
            contentHash: content.contentHash,
            url: content.url,
            signedTime: new BN(content.signedTime),
            acknowledgeSignature: content.acknowledgeSignature,
        });
        const approval_sig = await this.web3.eth.personal.sign(messageHash, from, "");
        //get amend message hash
        const amendHash = LC.generateAmendMessageHash(migrating_stages, {
            stage: amendStage,
            subStage: amendSubStage,
            content: {
                rootHash,
                prevHash,
                contentHash: content.contentHash,
                url: content.url,
                signedTime: new BN(content.signedTime),
                numOfDocuments: content.numOfDocuments,
                acknowledgeSignature: content.acknowledgeSignature,
                approvalSignature: approval_sig,
            },
        });

        const amend_sig = await this.web3.eth.personal.sign(amendHash, from, "");

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
                    approval_sig,
                ],
            ],
            amend_sig,
        ];
        const gas = await this.RouterService.methods.submitAmendment(...data).estimateGas({ from });

        return this.RouterService.methods.submitAmendment(...data).send({ from, gas });
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
        const amendMessageHash = LC.generateAmendMessageHash(amendmentRequest[2], amendStage);

        const amendSig = await this.web3.eth.personal.sign(amendMessageHash, from, "");

        const gas = await this.RouterService.methods.approveAmendment(documentId, requestId, amendSig).estimateGas({ from });

        return this.RouterService.methods.approveAmendment(documentId, requestId, amendSig).send({ from, gas });
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

        return this.RouterService.methods.fulfillAmendment(documentId, requestId).send({ from, gas });
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
     * @param standardLC instance of LC contract
     * @returns LC stages
     */
    private async _getLCStatus(standardLC: StandardLC) {
        const rootSubStage = +(await standardLC.methods.numOfSubStage(1).call());
        const lcStatus = await standardLC.methods.getStatus().call();
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
        const { _contract, _typeOf } = await this.RouterService.methods.getAddress(documentId).call();
        if (/^0x0+$/.test(_contract)) throw new Error("DocumentId not found");

        return { lcContract: new this.web3.eth.Contract(LCContractABIs.StandardLC as any[] as AbiItem[], _contract) as any as StandardLC, type: _typeOf };
    }

    /**
     * get LC status sort by root hash
     * @param documentId hash of LC number
     * @param lcContract instance of LC contract
     * @returns array of LC status sort by root hash
     */
    async getLCStatus(documentId: number | string | BN, lcContract: StandardLC) {
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
}
