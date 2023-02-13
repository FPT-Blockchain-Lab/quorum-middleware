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

    async approveLC(
        documentId: number | string | BN,
        stage: number,
        subStage: number,
        content: Omit<StageContent, "approvalSignature" | "rootHash" | "prevHash">,
        from: string
    ) {
        const { _contract, _typeOf } = await this.RouterService.methods.getAddress(documentId).call();
        if (/^0x0+$/.test(_contract)) throw new Error("DocumentId not found");

        const StandardLC = new this.web3.eth.Contract(LCContractABIs.StandardLC as any[] as AbiItem[], _contract) as any as StandardLC;
        let prevStage = stage,
            prevSubStage = subStage;
        const counter = +(await StandardLC.methods.getCounter().call());

        if (stage == 1) {
            prevStage = prevStage - 1;
        }

        if (stage == 2) {
            prevSubStage = counter + 1;
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
        let org = "";

        if (_typeOf == "1") {
            const StandardLC = new this.web3.eth.Contract(LCContractABIs.StandardLC as any[] as AbiItem[], _contract) as any as StandardLC;
            const parties = await StandardLC.methods.getInvolvedParties().call();

            if (stage == 2 || stage == 6) {
                org = parties[1];
            } else {
                org = parties[0];
            }
        } else if (_typeOf == "2") {
            const UPASLC = new this.web3.eth.Contract(LCContractABIs.UPASLC as any[] as AbiItem[], _contract) as any as UPASLC;
            const parties = await UPASLC.methods.getInvolvedParties().call();

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

    async closeLC(documentId: number | string | BN, from: string) {
        const gas = await this.RouterService.methods.closeLC(documentId).estimateGas({ from });

        return this.RouterService.methods.closeLC(documentId).send({ from, gas });
    }

    async submitAmendment(
        documentId: number | string | BN,
        stage: number,
        subStage: number,
        content: Omit<StageContent, "approvalSignature" | "rootHash" | "prevHash">,
        migrateStages: Stage[],
        from: string
    ) {
        const { _contract } = await this.RouterService.methods.getAddress(documentId).call();
        if (/^0x0+$/.test(_contract)) throw new Error("DocumentId not found");

        const StandardLC = new this.web3.eth.Contract(LCContractABIs.StandardLC as any[] as AbiItem[], _contract) as any as StandardLC;
        const amendStage = stage;
        let amendSubStage = subStage;
        let prevStage = amendStage,
            prevSubStage = amendSubStage;
        const counter = await StandardLC.methods.getCounter().call();

        if (amendStage == 1) {
            amendSubStage = parseInt(counter, 10) + 2;
        } else {
            prevStage = prevStage - 1;
        }

        if (amendStage == 2) {
            prevSubStage = parseInt(counter, 10) + 1;
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

    async approveAmendment(documentId: number | string | BN, proposer: string, nonce: BN, from: string) {
        // Generate requestId
        const requestId = LC.generateRequestId(proposer, nonce);

        const [amendmentRequest, isApproved] = await Promise.all([
            this.RouterService.methods.getAmendmentRequest(documentId, requestId).call(),
            this.RouterService.methods.isAmendApproved(documentId, requestId).call(),
        ]);

        if (isApproved) return alert("Amend request has been approved!");

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

    async fulfillAmendment(documentId: number | string | BN, proposer: string, nonce: BN, from: string) {
        // Generate requestId
        const requestId = LC.generateRequestId(proposer, nonce);

        const request = await this.RouterService.methods.getAmendmentRequest(documentId, requestId).call();
        if (!request) throw new Error("Amend request not found.");

        const gas = await this.RouterService.methods.fulfillAmendment(documentId, requestId).estimateGas({ from });

        return this.RouterService.methods.fulfillAmendment(documentId, requestId).send({ from, gas });
    }

    // async getLCStatus(documentId: number | string | BN) {
    //     const { _contract } = await this.RouterService.methods.getAddress(documentId).call();
    //     if (/^0x0+$/.test(_contract)) throw new Error("DocumentId not found");

    //     const StandardLC = new this.web3.eth.Contract(LCContractABIs.StandardLC as any[] as AbiItem[], _contract) as any as StandardLC;
    //     return this._getLCStatus(StandardLC)
    // }

    private _calculateStages(lastestStages: number[]): Stage[] {
        let res: Stage[] = [];

        for (let i = 0; i < lastestStages.length; i++) {
            for (let j = 1; j < lastestStages[i]; j++) {
                res = [...res, { stage: j + 1, subStage: i + 1 }];
            }
        }

        return res;
    }

    private async _getLCStatus(standardLC: StandardLC) {
        const counter = +(await standardLC.methods.getCounter().call());
        const lcStatus = await standardLC.methods.getStatus().call();
        let rootStages: Stage[] = [];
        for (let i = 1; i <= counter + 1; i++) {
            rootStages = [...rootStages, { stage: 1, subStage: i }];
        }
        const lcStages = this._calculateStages(lcStatus.map((stage) => parseInt(stage, 10)));

        return [...rootStages, ...lcStages];
    }

    private async _getLCContract(documentId: number | string | BN) {
        const { _contract } = await this.RouterService.methods.getAddress(documentId).call();
        if (/^0x0+$/.test(_contract)) throw new Error("DocumentId not found");

        return new this.web3.eth.Contract(LCContractABIs.StandardLC as any[] as AbiItem[], _contract) as any as StandardLC;
    }

    async getLCStatus(documentId: number | string | BN) {
        const LCContract = await this._getLCContract(documentId);
        const stages = await this._getLCStatus(LCContract);
        const stageContents = await Promise.all(
            stages.map(async (stage) => {
                const content = await this.RouterService.methods.getStageContent(documentId, stage.stage, stage.subStage).call();
                return { stage, rootHash: content[0] };
            })
        );
        const rootList = await LCContract.methods.getRootList().call();
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
}
