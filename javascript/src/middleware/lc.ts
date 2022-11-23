import { keccak256 } from "web3-utils";
import ethAbiEncoder from "web3-eth-abi";
import * as BN from "bn.js";
import { LCManagement, Mode, RouterService, StandardLCFactory, UPASLCFactory } from "../bindings/lc";

export interface LCContracts {
    LCManagement?: LCManagement
    Mode?: Mode
    RouterService?: RouterService
    StandardLCFactory?: StandardLCFactory
    UPASLCFactory?: UPASLCFactory
}

/**TODO write definitions */
export interface StageContent {
    rootHash: string
    prevHash: string
    contentHash: string
    URL: string
    signedTime: string
    acknowledgeSignature: string
}

export class LC {
    static loadContract(): LCContracts {
        throw Error("not implemented yet");
    }

    static generateAcknowledgeMessageHash(contentHash: string[]): string {
        return keccak256(ethAbiEncoder.encodeParameters(["bytes32[]"], [contentHash]));
    }

    /**TODO use StageContent in here */
    static generateApprovalMessageHash({
        rootHash,
        prevHash,
        contentHash,
        URL,
        signedTime,
        acknowledgeSignature,
    }: StageContent): string {
        return keccak256(
            ethAbiEncoder.encodeParameters(
                ["bytes32", "bytes32", "bytes32[]", "string", "uint256", "bytes"],
                [rootHash, prevHash, contentHash, URL, signedTime, acknowledgeSignature]
            )
        );
    }
}