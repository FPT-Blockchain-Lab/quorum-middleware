import BN from "bn.js";
import Web3 from "web3";
import ethAbiEncoder from "web3-eth-abi";
import { AbiItem, encodePacked, keccak256, Mixed } from "web3-utils";
import { StageContent, AmendStage, LCContracts, SpecialRole } from "./interfaces";
import { DEFAULT_CONFIG } from "../config";
import { LCContractABIs } from "../abi/lc";
import { LCManagement, RouterService, LCFactory, AmendRequest } from "../bindings/lc";

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
        const LCFactory = new web3.eth.Contract(LCContractABIs.LCFactory as any as AbiItem[], config.lCContractAddresses.LCFactory) as any as LCFactory;
        const AmendRequest = new web3.eth.Contract(
            LCContractABIs.AmendRequest as any as AbiItem[],
            config.lCContractAddresses.AmendRequest
        ) as any as AmendRequest;

        return {
            LCManagement,
            RouterService,
            LCFactory,
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
        signedTime,
        prevHash,
        contentHash,
        url,
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

    // Enum type of LC contract
    // export enum LCTYPE {
    //     STANDARD_LC = 1, // lc thường
    //     UPAS_LC = 2, // lc upas
    // }

    // Enum the number of involved parties of each LC type
    export enum NUMOFPARTIES {
        STANDARD_LC_PARTIES = 4, // [0] = _issuingBank, [1] = _advisingBank, [2] = _applicantOrg, [3] = _beneficiaryOrg;
        UPAS_LC_PARTIES = 5, // [0] = _issuingBank, [1] = _advisingBank, [2] = _reimbursingBank, [3] = _applicantOrg, [4] = _beneficiaryOrg;
    }

    // Enum index of org in involved parties
    export enum INDEXOFORG {
        NHPH, // ngân hàng xuất trình
        NHTB, // ngân hàng thông báo
        NHTT, // ngân hàng tài trợ
    }

    // Special role has authority to execute LC Protocol's operations
    // - DEFAULT_ADMIN_ROLE (Admin of LC Protocol):
    //     - Grant/Revoke other special roles (including Admin)
    //     - Has authority to manage special settings/operations in the LC Protocol
    // - VERIFIER_ROLE:
    //     - Has authority to sign acknowledge message
    // - OPERATOR_ROLE:
    //     - Has authority to close LC contracts
    const DEFAULT_ADMIN_ROLE: SpecialRole = {
        role: "0x0000000000000000000000000000000000000000000000000000000000000000",
    };
    const VERIFIER_ROLE: SpecialRole = {
        role: "0x0ce23c3e399818cfee81a7ab0880f714e53d7672b08df0fa62f2843416e1ea09",
    };
    const OPERATOR_ROLE: SpecialRole = {
        role: "0x97667070c54ef182b0f5858b034beac1b6f3089aa2d3188bb1e8929f4fa9b929",
    };

    // LC Protocol's special role list
    export const SpecialRoleEnum = {
        DEFAULT_ADMIN_ROLE: DEFAULT_ADMIN_ROLE,
        VERIFIER_ROLE: VERIFIER_ROLE,
        OPERATOR_ROLE: OPERATOR_ROLE,
    };
}
