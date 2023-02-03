import BN from "bn.js";
import { LCManagement, RouterService, StandardLCFactory, UPASLCFactory, AmendRequest } from "../bindings/lc";
import {
    AccountManager,
    NodeManager,
    OrgManager,
    PermissionsImplementation,
    PermissionsInterface,
    PermissionsUpgradable,
    RoleManager,
    VoterManager,
} from "../bindings/permission";

/** Contracts of LC protocol */
export interface LCContracts {
    LCManagement: LCManagement;
    RouterService: RouterService;
    StandardLCFactory: StandardLCFactory;
    UPASLCFactory: UPASLCFactory;
    AmendRequest: AmendRequest;
}

/** Content of LC contract */
export interface StageContent {
    rootHash: string;

    /** Stage content hash of previous LC contract */
    prevHash: string;

    /** Hashes of LC content */
    contentHash: string[];

    /** */
    url: string;

    /** Signed time */
    signedTime: BN;

    /** Must be provided in the Stage 1, Stage 4, and Stage 5. Others can be 0 */
    numOfDocuments: BN;

    /** Acknowledge signature generated by Management Org for Stage 1, Stage 4, Stage 5 */
    acknowledgeSignature: string;

    /** Signature generated by Approver */
    approvalSignature: string;
}

export interface AmendStage {
    /**
     * From 1 to 6 for `Standard LC`, from 1 to 7 for `UPAS LC`
     */
    stage: BN;
    subStage: BN;
    content: StageContent;
}

export interface Stage {
    stage: BN;
    subStage: BN;
}

export interface PermissionContracts {
    AccountManager: AccountManager;
    PermissionsImplementation: PermissionsImplementation;
    PermissionsInterface: PermissionsInterface;
    NodeManager: NodeManager;
    OrgManager: OrgManager;
    PermissionsUpgradable: PermissionsUpgradable;
    RoleManager: RoleManager;
    VoterManager: VoterManager;
}
