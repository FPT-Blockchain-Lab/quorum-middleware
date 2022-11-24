import Web3 from "web3";
import { keccak256 } from "web3-utils";
import ethAbiEncoder from "web3-eth-abi";
import { LCContractABIs } from "../abi";
import { LCManagement, Mode, RouterService, StandardLCFactory, UPASLCFactory } from "../bindings/lc";

const Contract = require("web3-eth-contract");

export interface LCContracts {
    LCManagement?: LCManagement;
    Mode?: Mode;
    RouterService?: RouterService;
    StandardLCFactory?: StandardLCFactory;
    UPASLCFactory?: UPASLCFactory;
}

/**TODO write definitions */
export interface StageContent {
    rootHash: string;
    prevHash: string;
    contentHash: string[];
    URL: string;
    signedTime: string;
    acknowledgeSignature: string;
}

export const LCContractAddresses = {
    LCManagement: "0x3179C0dDF36a95A9BaEe917fF2D4dbFa08B51462",
    Mode: "0xAF3b2ebdcc22513339DBa236AEd1660e917B699c",
    RouterService: "0xEdD3C07B254995c2520C28F0D4a0f6082006eFDe",
    StandardLCFactory: "0xfB1691a1BF872d4cc823563cC3d78bf740225C36",
    UPASLCFactory: "0xDf8c56d1c4ff6C2f34835513Cd700D4D6A258036",
};

export class LC {
    static loadContract(web3: Web3): LCContracts {
        Contract.setProvider(web3);

        const LCManagement = new Contract(LCContractABIs.LCManagement, LCContractAddresses.LCManagement) as any as LCManagement;
        const Mode = new Contract(LCContractABIs.Mode, LCContractAddresses.Mode) as any as Mode;
        const RouterService = new Contract(LCContractABIs.RouterService, LCContractAddresses.RouterService) as any as RouterService;
        const StandardLCFactory = new Contract(LCContractABIs.StandardLCFactory, LCContractAddresses.StandardLCFactory) as any as StandardLCFactory;
        const UPASLCFactory = new Contract(LCContractABIs.UPASLCFactory, LCContractAddresses.UPASLCFactory) as any as UPASLCFactory;

        return {
            LCManagement,
            Mode,
            RouterService,
            StandardLCFactory,
            UPASLCFactory,
        };
    }

    static generateAcknowledgeMessageHash(contentHash: string[]): string {
        return keccak256(ethAbiEncoder.encodeParameters(["bytes32[]"], [contentHash]));
    }

    /**TODO use StageContent in here */
    static generateApprovalMessageHash({ rootHash, prevHash, contentHash, URL, signedTime, acknowledgeSignature }: StageContent): string {
        return keccak256(
            ethAbiEncoder.encodeParameters(
                ["bytes32", "bytes32", "bytes32[]", "string", "uint256", "bytes"],
                [rootHash, prevHash, contentHash, URL, signedTime, acknowledgeSignature]
            )
        );
    }
}
