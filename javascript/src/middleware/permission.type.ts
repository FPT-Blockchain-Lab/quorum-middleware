import Web3 from "web3";
import { AccessType, Role, SpecialRole } from "./interfaces";
export namespace PermissionType {
    // Base access type of account list
    const READ_ONLY: AccessType = {
        baseAccess: 0,
        name: "READ_ONLY",
    };
    const VALUE_TRANSFER: AccessType = {
        baseAccess: 1,
        name: "VALUE_TRANSFER",
    };
    const CONTRACT_DEPLOY: AccessType = {
        baseAccess: 2,
        name: "CONTRACT_DEPLOY",
    };
    const FULL_ACCESS: AccessType = {
        baseAccess: 3,
        name: "FULL_ACCESS",
    };
    const CONTRACT_CALL: AccessType = {
        baseAccess: 4,
        name: "CONTRACT_CALL",
    };
    const VALUE_TRANSFER_AND_CONTRACT_CALL: AccessType = {
        baseAccess: 5,
        name: "VALUE_TRANSFER_AND_CONTRACT_CALL",
    };
    const VALUE_TRANSFER_AND_CONTRACT_DEPLOY: AccessType = {
        baseAccess: 6,
        name: "VALUE_TRANSFER_AND_CONTRACT_DEPLOY",
    };
    const CONTRACT_CALL_AND_DEPLOY: AccessType = {
        baseAccess: 7,
        name: "CONTRACT_CALL_AND_DEPLOY",
    };

    export const AccessTypeEnum = {
        READ_ONLY: READ_ONLY,
        VALUE_TRANSFER: VALUE_TRANSFER,
        CONTRACT_DEPLOY: CONTRACT_DEPLOY,
        FULL_ACCESS: FULL_ACCESS,
        CONTRACT_CALL: CONTRACT_CALL,
        VALUE_TRANSFER_AND_CONTRACT_CALL: VALUE_TRANSFER_AND_CONTRACT_CALL,
        VALUE_TRANSFER_AND_CONTRACT_DEPLOY: VALUE_TRANSFER_AND_CONTRACT_DEPLOY,
        CONTRACT_CALL_AND_DEPLOY: CONTRACT_CALL_AND_DEPLOY,
    };

    // LC Protocol org role list
    const MEMBER: Role = {
        value: 0,
        accessType: VALUE_TRANSFER_AND_CONTRACT_CALL.baseAccess,
        name: "MEMBER",
        isVoter: false,
        isOrgAdmin: false,
    };
    const ORGADMIN: Role = {
        value: 0,
        accessType: VALUE_TRANSFER_AND_CONTRACT_CALL.baseAccess,
        name: "ORGADMIN",
        isVoter: false,
        isOrgAdmin: true,
    };

    export const RoleEnum = {
        MEMBER: MEMBER,
        ORGADMIN: ORGADMIN,
    };

    // LC Protocol's special role list
    const DEFAULT_ADMIN_ROLE: SpecialRole = {
        role: "0x0000000000000000000000000000000000000000000000000000000000000000",
    };
    const VERIFIER_ROLE: SpecialRole = {
        role: "0x0ce23c3e399818cfee81a7ab0880f714e53d7672b08df0fa62f2843416e1ea09",
    };
    const OPERATOR_ROLE: SpecialRole = {
        role: "0x97667070c54ef182b0f5858b034beac1b6f3089aa2d3188bb1e8929f4fa9b929",
    };

    export const SpecialRoleEnum = {
        DEFAULT_ADMIN_ROLE: DEFAULT_ADMIN_ROLE,
        VERIFIER_ROLE: VERIFIER_ROLE,
        OPERATOR_ROLE: OPERATOR_ROLE,
    };
}
