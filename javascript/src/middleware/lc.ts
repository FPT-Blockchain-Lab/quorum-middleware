import Web3 from "web3";
import { keccak256, encodePacked, Mixed, AbiItem } from "web3-utils";
import ethAbiEncoder from "web3-eth-abi";
import { LCContractABIs } from "../abi";
import BN from "bn.js";
import { LCManagement, Mode, RouterService, StandardLCFactory, UPASLCFactory, AmendRequest } from "../bindings/lc";
import { LCContractAddresses } from "../config";

export namespace Middleware {
    export interface LCContracts {
        LCManagement?: LCManagement;
        Mode?: Mode;
        RouterService?: RouterService;
        StandardLCFactory?: StandardLCFactory;
        UPASLCFactory?: UPASLCFactory;
        AmendRequest?: AmendRequest;
    }

    export interface StageContent {
        rootHash: string;
        prevHash: string;
        contentHash: string[];
        url: string;
        signedTime: BN;
        numOfDocuments: BN;
        acknowledgeSignature: string;
        approvalSignature: string;
    }

    export interface AmendStage {
        stage: BN;
        subStage: BN;
        content: StageContent;
    }

    export class LC {
        static readonly DEFAULT_ROOT_HASH = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";

        static loadContract(web3: Web3): LCContracts {
            const LCManagement = new web3.eth.Contract(LCContractABIs.LCManagement as any as AbiItem[], LCContractAddresses.LCManagement) as any as LCManagement;
            const Mode = new web3.eth.Contract(LCContractABIs.Mode as any as AbiItem[], LCContractAddresses.Mode) as any as Mode;
            const RouterService = new web3.eth.Contract(LCContractABIs.RouterService as any as AbiItem[], LCContractAddresses.RouterService) as any as RouterService;
            const StandardLCFactory = new web3.eth.Contract(LCContractABIs.StandardLCFactory as any as AbiItem[], LCContractAddresses.StandardLCFactory) as any as StandardLCFactory;
            const UPASLCFactory = new web3.eth.Contract(LCContractABIs.UPASLCFactory as any as AbiItem[], LCContractAddresses.UPASLCFactory) as any as UPASLCFactory;
            const AmendRequest = new web3.eth.Contract(LCContractABIs.AmendRequest as any as AbiItem[], LCContractAddresses.AmendRequest) as any as AmendRequest;

            return {
                LCManagement,
                Mode,
                RouterService,
                StandardLCFactory,
                UPASLCFactory,
                AmendRequest,
            };
        }

        static generateAcknowledgeMessageHash(contentHash: string[]): string {
            return keccak256(ethAbiEncoder.encodeParameters(["bytes32[]"], [contentHash]));
        }

        static generateApprovalMessageHash({
            rootHash,
            prevHash,
            contentHash,
            url,
            signedTime,
            acknowledgeSignature,
        }: Omit<StageContent, "approvalSignature" | "numOfDocuments">): string {
            return keccak256(
                ethAbiEncoder.encodeParameters(
                    ["bytes32", "bytes32", "bytes32[]", "string", "uint256", "bytes"],
                    [rootHash, prevHash, contentHash, url, signedTime, acknowledgeSignature]
                )
            );
        }

        static generateAmendMessageHash(migratingStages: string[], { stage, subStage, content }: AmendStage): string {
            let message = keccak256(
                ethAbiEncoder.encodeParameters(
                    ["uint256", "uint256", "bytes32", "bytes32", "bytes32[]", "string", "uint256", "bytes", "bytes"],
                    [
                        stage,
                        subStage,
                        content.rootHash,
                        content.prevHash,
                        content.contentHash,
                        content.url,
                        content.signedTime,
                        content.acknowledgeSignature,
                        content.approvalSignature,
                    ]
                )
            );

            return keccak256(ethAbiEncoder.encodeParameters(["bytes32[]", "bytes32"], [migratingStages, message]));
        }

        static generateStageHash({
            rootHash,
            prevHash,
            contentHash,
            url,
            signedTime,
            acknowledgeSignature,
            approvalSignature,
        }: Omit<StageContent, "numOfDocuments">): string {
            let content: Mixed[] = [
                { v: rootHash, t: "bytes32" },
                { v: prevHash, t: "bytes32" },
                ...contentHash.map((hash) => ({ v: hash, t: "bytes32" })),
                { v: url, t: "string" },
                { v: signedTime, t: "uint256" },
                { v: approvalSignature, t: "bytes" },
                { v: acknowledgeSignature, t: "bytes" },
            ];

            return keccak256(encodePacked(...content) ?? "");
        }

        static generateRequestId(proposer: string, nonce: BN): string {
            let content: Mixed[] = [
                { v: proposer, t: "address" },
                { v: nonce, t: "uint256" },
            ];

            return keccak256(encodePacked(...content) ?? "");
        }

        static checkStage(stage: BN): string {
            switch (stage.toString()) {
                case "1":
                    return "Letter Of Credit";
                case "2":
                    return "Presentation Document";
                case "3":
                    return "Documentation Result Notification";
                case "4":
                    return "LC Payment Acceptance";
                case "5":
                    return "LC Issuance Bank To Advising Bank Payment";
                case "6":
                    return "LC Advising Bank To Beneficiary Payment";
                case "7":
                    return "UpasLC Payment Acceptance";
                default:
                    return "";
            }
        }
    }
}
