import BN from "bn.js";
import Web3 from "web3";
import ethAbiEncoder from "web3-eth-abi";
import { AbiItem, encodePacked, keccak256, Mixed } from "web3-utils";
import { StageContent, AmendStage, LCContracts } from "./interfaces";
import { DEFAULT_CONFIG } from "../config";
import { LCContractABIs } from "../abi/lc";
import { LCManagement, RouterService, StandardLCFactory, UPASLCFactory, AmendRequest } from "../bindings/lc";

/** LC protocol */
export class LC {
    static loadContract(web3: Web3, config = DEFAULT_CONFIG): LCContracts {
        const LCManagement = new web3.eth.Contract(
            LCContractABIs.LCManagement as any as AbiItem[],
            config.lCContractAddresses.LCManagement
        ) as any as LCManagement;
        const RouterService = new web3.eth.Contract(
            LCContractABIs.RouterService as any as AbiItem[],
            config.lCContractAddresses.RouterService
        ) as any as RouterService;
        const StandardLCFactory = new web3.eth.Contract(
            LCContractABIs.StandardLCFactory as any as AbiItem[],
            config.lCContractAddresses.StandardLCFactory
        ) as any as StandardLCFactory;
        const UPASLCFactory = new web3.eth.Contract(
            LCContractABIs.UPASLCFactory as any as AbiItem[],
            config.lCContractAddresses.UPASLCFactory
        ) as any as UPASLCFactory;
        const AmendRequest = new web3.eth.Contract(
            LCContractABIs.AmendRequest as any as AbiItem[],
            config.lCContractAddresses.AmendRequest
        ) as any as AmendRequest;

        return {
            LCManagement,
            RouterService,
            StandardLCFactory,
            UPASLCFactory,
            AmendRequest,
        };
    }

    /**
     * Compute the hash of content array
     * @param contentHash the hashes of content
     */
    static generateAcknowledgeMessageHash(contentHash: string[]): string {
        return keccak256(ethAbiEncoder.encodeParameters(["bytes32[]"], [contentHash]));
    }

    /**
     * Compute the hash of LC content without `numOfDocuments` and `approvalSignature`
     */
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

    /**
     * @param migratingStages the hashes of migrating stages after amend (order by approved time)
     */
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

    /**
     * Compute the hash of LC content without `numOfDocuments`
     */
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

    /**
     * @param proposer address of proposer
     * @param nonce
     */
    static generateRequestId(proposer: string, nonce: BN): string {
        let content: Mixed[] = [
            { v: proposer, t: "address" },
            { v: nonce, t: "uint256" },
        ];

        return keccak256(encodePacked(...content) ?? "");
    }
}

export namespace LC {
    export enum Stage {
        // BLC_03
        PHAT_HANH_LC = 1, // phát hành LC
        // BLC_04
        XUAT_TRINH_TCD_BCT = 2, // xuất trình thư chỉ dẫn bộ chứng từ
        // BLC_05
        THONG_BAO_BCT_MH = 3, // thông báo bộ chứng từ mua hàng
        // BLC_06
        CHAP_NHAN_THANH_TOAN = 4, // chấp nhận thanh toán
        //BLC_07_08
        LC_NHPH_NHXT = 5, // lc thường: ngân hàng phát hành - ngân hàng xuất trình
        LC_NHXT_BTH = 6, // lc thường: ngân hàng xuất trình - bên thụ hưởng
        UPAS_NHTT_NHXT = 5, // lc upas: ngân hàng tài trợ - ngân hàng xuất trình
        UPAS_NHXT_BTH = 6, // lc upas: ngân hàng xuất trình - bên thụ hưởng
        UPAS_NHPH_NHTT = 7, // lc upas: ngân hàng phát hành - ngân hàng tài trợ
    }
}
