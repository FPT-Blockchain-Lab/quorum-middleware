import Web3 from "web3";
import BN from "bn.js";
import { OrgManager, PermissionsInterface } from "../bindings/permission";
import { Permission } from "./permission";
import { DEFAULT_CONFIG } from "../config";

export class PermissionWrapper {
    private readonly PermissionsInterface: PermissionsInterface;
    private readonly OrgManager: OrgManager;

    constructor(web3: Web3, config = DEFAULT_CONFIG) {
        if (!config.permissionContractAddresses.PermissionsInterface || !config.permissionContractAddresses.OrgManager) {
            throw new Error(`required PermissionsInterface OrgManager to be defined`);
        }

        const { PermissionsInterface, OrgManager } = Permission.loadContract(web3, config);

        this.PermissionsInterface = PermissionsInterface;
        this.OrgManager = OrgManager;
    }

    async assignAccountRole(account: string, orgId: string, roleId: string, from: string) {
        const isOrgExists = await this.OrgManager.methods.checkOrgExists(orgId).call();

        if (!isOrgExists) {
            throw new Error("org does not exist.");
        }

        const gas = await this.PermissionsInterface.methods.assignAccountRole(account, orgId, roleId).estimateGas({ from });

        return this.PermissionsInterface.methods.assignAccountRole(account, orgId, roleId).send({ from, gas });
    }

    async updateAccountStatus(orgId: string, account: string, action: number | string | BN, from: string) {
        const isOrgExists = await this.OrgManager.methods.checkOrgExists(orgId).call();

        if (!isOrgExists) {
            throw new Error("org does not exist.");
        }

        const gas = await this.PermissionsInterface.methods.updateAccountStatus(account, orgId, action).estimateGas({ from });

        return this.PermissionsInterface.methods.updateAccountStatus(account, orgId, action).send({ from, gas });
    }
}
