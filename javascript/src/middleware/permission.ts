import Web3 from "web3";
import { AbiItem } from "web3-utils";
import { PermissionContractABIs } from "../abi/permission";
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
import { DEFAULT_CONFIG } from "../config";
import { PermissionContracts, Role } from "./interfaces";

export class Permission {
    static loadContract(web3: Web3, config = DEFAULT_CONFIG): PermissionContracts {
        console.log("🚀 ~ Permission ~ loadContract ~ web3:", web3);
        const AccountManager = new web3.eth.Contract(
            PermissionContractABIs.AccountManager as any[] as AbiItem[],
            config.permissionContractAddresses.AccountManager
        ) as any as AccountManager;
        const PermissionsImplementation = new web3.eth.Contract(
            PermissionContractABIs.PermissionsImplementation as any[] as AbiItem[],
            config.permissionContractAddresses.PermissionsImplementation
        ) as any as PermissionsImplementation;
        const PermissionsInterface = new web3.eth.Contract(
            PermissionContractABIs.PermissionsInterface as any[] as AbiItem[],
            config.permissionContractAddresses.PermissionsInterface
        ) as any as PermissionsInterface;
        const NodeManager = new web3.eth.Contract(
            PermissionContractABIs.NodeManager as any[] as AbiItem[],
            config.permissionContractAddresses.NodeManager
        ) as any as NodeManager;
        const OrgManager = new web3.eth.Contract(
            PermissionContractABIs.OrgManager as any[] as AbiItem[],
            config.permissionContractAddresses.OrgManager
        ) as any as OrgManager;
        const PermissionsUpgradable = new web3.eth.Contract(
            PermissionContractABIs.PermissionsUpgradable as any[] as AbiItem[],
            config.permissionContractAddresses.PermissionsUpgradable
        ) as any as PermissionsUpgradable;
        const RoleManager = new web3.eth.Contract(
            PermissionContractABIs.RoleManager as any[] as AbiItem[],
            config.permissionContractAddresses.RoleManager
        ) as any as RoleManager;
        const VoterManager = new web3.eth.Contract(
            PermissionContractABIs.VoterManager as any[] as AbiItem[],
            config.permissionContractAddresses.VoterManager
        ) as any as VoterManager;

        return {
            AccountManager,
            PermissionsImplementation,
            PermissionsInterface,
            NodeManager,
            OrgManager,
            PermissionsUpgradable,
            RoleManager,
            VoterManager,
        };
    }
}

export namespace Permission {
    export enum BASE_ACCESS {
        READ_ONLY,
        VALUE_TRANSFER,
        CONTRACT_DEPLOY,
        FULL_ACCESS,
        CONTRACT_CALL,
        VALUE_TRANSFER_AND_CONTRACT_CALL,
        VALUE_TRANSFER_AND_CONTRACT_DEPLOY,
        CONTRACT_CALL_AND_DEPLOY,
    }

    const MEMBER: Role = {
        accessType: Permission.BASE_ACCESS.VALUE_TRANSFER_AND_CONTRACT_CALL,
        name: "MEMBER",
        isVoter: false,
        isOrgAdmin: false,
    };

    const ORGADMIN: Role = {
        accessType: Permission.BASE_ACCESS.VALUE_TRANSFER_AND_CONTRACT_CALL,
        name: "ORGADMIN",
        isVoter: false,
        isOrgAdmin: true,
    };

    // Org permission role list
    export const RoleEnum = {
        MEMBER: MEMBER,
        ORGADMIN: ORGADMIN,
    };
}
