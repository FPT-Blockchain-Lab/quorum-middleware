package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.permission.AccountManager;
import com.fptblockchainlab.bindings.permission.OrgManager;
import com.fptblockchainlab.bindings.permission.PermissionsInterface;
import com.fptblockchainlab.bindings.permission.RoleManager;
import com.fptblockchainlab.exceptions.FailedTransactionException;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class Permission {
    private final OrgManager orgManager;
    private final RoleManager roleManager;
    private final PermissionsInterface permissionInterface;
    private final AccountManager accountManager;
    private final String orgLevel1;

    @Getter
    @AllArgsConstructor
    public enum AccessType {
        READ_ONLY(0, "READ_ONLY"),
        VALUE_TRANSFER(1, "VALUE_TRANSFER"),
        CONTRACT_DEPLOY(2, "CONTRACT_DEPLOY"),
        FULL_ACCESS(3, "FULL_ACCESS"),
        CONTRACT_CALL(4, "CONTRACT_CALL"),
        VALUE_TRANSFER_AND_CONTRACT_CALL(5, "VALUE_TRANSFER_AND_CONTRACT_CALL"),
        VALUE_TRANSFER_AND_CONTRACT_DEPLOY(6, "VALUE_TRANSFER_AND_CONTRACT_DEPLOY"),
        CONTRACT_CALL_AND_DEPLOY(7, "CONTRACT_CALL_AND_DEPLOY");

        private final Integer baseAccess; // Onchain
        private final String name; // Offchain

        public static AccessType valueOf(int value) {
            AccessType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                AccessType responseCode = var1[var3];
                if (responseCode.getBaseAccess() == value) {
                    return responseCode; // code int
                }
            }
            return null;
        }

    }

    @Getter
    @AllArgsConstructor
    public enum Role {
        MEMBER(0, AccessType.VALUE_TRANSFER_AND_CONTRACT_CALL, "MEMBER", false, false),
        ORGADMIN(1, AccessType.VALUE_TRANSFER_AND_CONTRACT_CALL, "ORGADMIN", false, true);

        private final int value;
        private final Permission.AccessType accessType;
        private final String name;
        private final Boolean isVoter;
        private final Boolean isOrgAdmin;

        public static Role valueOf(int value) {
            Role[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                Role responseCode = var1[var3];
                if (responseCode.getValue() == value) {
                    return responseCode; // code int
                }
            }
            return null;
        }
    }

    public Permission(
            OrgManager orgManager,
            RoleManager roleManager,
            PermissionsInterface permissionInterface,
            AccountManager accountManager,
            String orgLevel1
    ) {
        if (orgManager.getContractAddress() != null && !orgManager.getContractAddress().isEmpty())
            this.orgManager = orgManager;
        else throw new RuntimeException("org manager is invalid");
        if (roleManager.getContractAddress() != null && !roleManager.getContractAddress().isEmpty())
            this.roleManager = roleManager;
        else throw new RuntimeException("org manager is invalid");
        if (permissionInterface.getContractAddress() != null && !permissionInterface.getContractAddress().isEmpty())
            this.permissionInterface = permissionInterface;
        else throw new RuntimeException("org manager is invalid");
        if (accountManager.getContractAddress() != null && !accountManager.getContractAddress().isEmpty())
            this.accountManager = accountManager;
        else throw new RuntimeException("org manager is invalid");
        this.orgLevel1 = orgLevel1;
    }

    public void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, FailedTransactionException, IOException {
        this.createSubOrgs(orgFullId);
        this.createDefaultRolesForOrg(orgFullId);
    }

    public void addAdminForSubOrg(String subOrgFullId, String adminAddress) throws IOException, FailedTransactionException {
        try {
            this.permissionInterface.assignAccountRole(adminAddress, subOrgFullId, Permission.Role.ORGADMIN.getName()).send();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws FailedTransactionException, IOException {
        // suspend requires account is in active status
        if (!isAccountOnchainUnderLevel1Org(adminAddress)) {
            throw new IOException(String.format("account %s is not active under %s", adminAddress, this.orgLevel1));
        }
        try {
            this.permissionInterface.updateAccountStatus(subOrgFullId, adminAddress, BigInteger.ONE).send();
        } catch (Exception e) {
            throw new IOException("failed to unwhitelist org", e);
        }
    }

    public boolean isAccountOnchainUnderLevel1Org(String account) throws IOException {
        Tuple5<String, String, String, BigInteger, Boolean> accountDetails;
        try {
            accountDetails = this.accountManager.getAccountDetails(account).send();
        } catch (Exception e) {
            throw new IOException("failed to get account details", e);
        }
        String[] fullOrgIdAsArr = accountDetails.component2().split("\\.");
        // Account is active state and ultimate parent org is FIS org
        return accountDetails.component4().compareTo(BigInteger.valueOf(2)) == 0 && this.orgLevel1.equals(fullOrgIdAsArr[0]);
    }

    private boolean isOrgExist(String fullOrgId) throws IOException {
        String[] orgHierarchyList = fullOrgId.split("\\.");
        String ultimateParentOrg = orgHierarchyList[0];
        String orgId = orgHierarchyList[orgHierarchyList.length - 1];
        String parentOrg = orgHierarchyList.length > 1 ? String.join(".", Arrays.copyOfRange(orgHierarchyList, 0, orgHierarchyList.length - 1)) : null;

        Tuple5<String, String, String, BigInteger, BigInteger> orgDetails = null;
        try {
            orgDetails = this.orgManager.getOrgDetails(fullOrgId).send();
        } catch (Exception e) {
            throw new IOException("failed to get org details", e);
        }
        return orgDetails.component1().equals(orgId) && orgDetails.component2().equals(parentOrg) && orgDetails.component3().equals(ultimateParentOrg);
    }

    private void createSubOrgs(String subOrgFullId) throws NotParentOrgException, IOException, FailedTransactionException {
        String[] orgHierachyList = subOrgFullId.split("\\.");
        if (!this.orgLevel1.equals(orgHierachyList[0])) {
            throw new NotParentOrgException(String.format("{} is not child org of {}", subOrgFullId, this.orgLevel1));
        }

        for (int i = 1; i < orgHierachyList.length; i++) {
            String parentOrg = String.join(".", Arrays.copyOfRange(orgHierachyList, 0, i));
            String currSubOrgFullId = parentOrg.concat(".").concat(orgHierachyList[i]);
            if (!this.isOrgExist(currSubOrgFullId)) {

                try {
                    this.permissionInterface.addSubOrg(parentOrg, orgHierachyList[i], "", "", BigInteger.valueOf(0), BigInteger.valueOf(0)).send();
                } catch (Exception e) {
                    throw new IOException("failed to add subOrg");
                }
            } else {
                throw new IOException(String.format("subOrg exists %s", currSubOrgFullId));
            }
        }
    }

    private void createDefaultRolesForOrg(String orgFullId) throws FailedTransactionException, IOException {
        // Can optimize with sendAsync -> CompletableFuture

        if (isRoleExist(Permission.Role.ORGADMIN.getName(), orgFullId)) {
            System.out.printf("%s already exists under %s%n", Permission.Role.ORGADMIN.getName(), orgFullId);
        } else {
            try {
                this.permissionInterface.addNewRole(
                        Permission.Role.ORGADMIN.getName(),
                        orgFullId, BigInteger.valueOf(Permission.Role.ORGADMIN.getAccessType().getBaseAccess()),
                        Permission.Role.ORGADMIN.getIsVoter(),
                        Permission.Role.ORGADMIN.getIsOrgAdmin()
                ).send();
            } catch (Exception e) {
                throw new IOException("failed to create ORGADMIN role", e);
            }
        }

        if (isRoleExist(Permission.Role.MEMBER.getName(), orgFullId)) {
            System.out.printf("%s already exists under %s%n", Permission.Role.MEMBER.getName(), orgFullId);
        } else {
            try {
                this.permissionInterface.addNewRole(
                        Permission.Role.MEMBER.getName(),
                        orgFullId, BigInteger.valueOf(Permission.Role.MEMBER.getAccessType().getBaseAccess()),
                        Permission.Role.MEMBER.getIsVoter(),
                        Permission.Role.MEMBER.getIsOrgAdmin()
                ).send();
            } catch (Exception e) {
                throw new IOException("failed to create MEMBER role", e);
            }
        }
    }

    private boolean isRoleExist(String roleName, String fullOrgId) throws IOException {
        Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean> roleDetails;
        try {
            roleDetails = this.roleManager.getRoleDetails(roleName, fullOrgId).send();
        } catch (Exception e) {
            throw new IOException("failed to get role details", e);
        }
        return roleDetails.component1().equals(roleName) && roleDetails.component2().equals(fullOrgId);
    }
}
