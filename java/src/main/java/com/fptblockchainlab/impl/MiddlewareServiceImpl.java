package com.fptblockchainlab.impl;

import com.fptblockchainlab.MiddlewareService;
import com.fptblockchainlab.exceptions.FailedTransactionExeception;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import com.fptblockchainlab.middleware.LC;
import com.fptblockchainlab.middleware.Permission;
import com.fptblockchainlab.bindings.permission.AccountManager;
import com.fptblockchainlab.bindings.permission.RoleManager;
import com.fptblockchainlab.bindings.permission.OrgManager;
import com.fptblockchainlab.bindings.permission.PermissionsInterface;
import com.fptblockchainlab.bindings.lc.LCManagement;
import com.fptblockchainlab.model.enumerate.BlcWalletStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.quorum.methods.request.PrivateTransaction;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import jakarta.annotation.PostConstruct;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class MiddlewareServiceImpl implements MiddlewareService {
    // Blocktime in milliseconds
    private static final long DEFAULT_POLLING_FREQUENCY = 6 * 1000;
    private static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 10;
    private static final long MAX_GAS_PER_BLOCK = 4_700_000;
    private static final int GAS_PRICE = 0;

    // Required account sent from one instance of client
    private final FastRawTransactionManager fastRawTransactionManager;
    private final ContractGasProvider contractGasProvider;
    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final Credentials credentials;
    private final Quorum quorum;
    private final AccountManager accountManager;
    private final OrgManager orgManager;
    private final RoleManager roleManager;
    private LCManagement lcManagement;
    private final PermissionsInterface permissionInterface;

    @Value("${client.quorum.permission.admin.org}")
    private String orgLevel1;
    public MiddlewareServiceImpl(
            @Value("${client.quorum.permission.url}") String quorumUrl,
            @Value("${client.quorum.permission.admin.privateKey}") String privateKey,
            @Value("${client.quorum.chainId}") long chainId,
            @Value("${client.quorum.permission.accountMgrAddress}") String accountMgrAddress,
            @Value("${client.quorum.permission.orgMgrAddress}") String orgMgrAddress,
            @Value("${client.quorum.permission.roleMgrAddress}") String roleMgrAddress,
            @Value("${client.quorum.permission.interfaceAddress}") String interfaceAddress,
            @Value("${client.quorum.lc.lcManagmentAddress}") String lcManagmentAddress
    ) {
        HttpService httpService = new HttpService(quorumUrl);
        // Not testable
        this.quorum = Quorum.build(httpService);
        this.credentials = Credentials.create(privateKey);
        this.transactionReceiptProcessor = new PollingTransactionReceiptProcessor(
                quorum,
                MiddlewareServiceImpl.DEFAULT_POLLING_FREQUENCY,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );
        this.fastRawTransactionManager = new FastRawTransactionManager(quorum, this.credentials, chainId, this.transactionReceiptProcessor);
        this.contractGasProvider = new StaticGasProvider(BigInteger.valueOf(GAS_PRICE), BigInteger.valueOf(MAX_GAS_PER_BLOCK));
        this.accountManager = AccountManager.load(accountMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.orgManager = OrgManager.load(orgMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.roleManager = RoleManager.load(roleMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.lcManagement = LCManagement.load(lcManagmentAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.permissionInterface = PermissionsInterface.load(interfaceAddress, quorum, fastRawTransactionManager, contractGasProvider);
        // Not testable
    }

    @Autowired
    private ConfigurableApplicationContext ctx;

    @PostConstruct()
    private void init() throws RuntimeException {
        String admin = this.credentials.getAddress();
        Boolean isLcAdmin = null;
        Boolean isOrgLevel1Admin = null;

        try {
            isLcAdmin = this.lcManagement.isAdmin(admin).send();
            isOrgLevel1Admin = this.permissionInterface.isOrgAdmin(admin, this.orgLevel1).send();
        } catch (Exception e) {
            log.error("Ops!", e);
            throw new RuntimeException(e);
        }

        if (!isLcAdmin|| !isOrgLevel1Admin) {
            log.error("{} is not lc admin or org level 1 {} admin", admin, this.orgLevel1);
            this.ctx.close();
        }
    }

    @Override
    public Permission.Role getAdminRole() {
        return Permission.Role.ORGADMIN;
    }

    @Override
    public Permission.Role getMemberRole() {
        return Permission.Role.MEMBER;
    }

    public boolean isAccountOnchainUnderLevel1Org(String account) throws IOException {
        Tuple5<String, String, String, BigInteger, Boolean> accountDetails = null;
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
        String[] orgHierachyList = fullOrgId.split("\\.");
        String ultimateParentOrg = orgHierachyList[0];
        String orgId = orgHierachyList[orgHierachyList.length - 1];
        String parentOrg = orgHierachyList.length > 1 ? String.join(".", Arrays.copyOfRange(orgHierachyList, 0, orgHierachyList.length - 1)) : null;

        Tuple5<String, String, String, BigInteger, BigInteger> orgDetails = null;
        try {
            orgDetails = this.orgManager.getOrgDetails(fullOrgId).send();
        } catch (Exception e) {
            throw new IOException("failed to get org details", e);
        }
        return orgDetails.component1().equals(orgId) && orgDetails.component2().equals(parentOrg) && orgDetails.component3().equals(ultimateParentOrg);
    }

    private boolean isRoleExist(String roleName, String fullOrgId) throws IOException {
        Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean> roleDetails = null;
        try {
            roleDetails = this.roleManager.getRoleDetails(roleName, fullOrgId).send();
        } catch (Exception e) {
            throw new IOException("failed to get role details", e);
        }
        return roleDetails.component1().equals(roleName) && roleDetails.component2().equals(fullOrgId);
    }

    public void signWithFisAdminAndSend(String to, String data) throws IOException, TransactionException, FailedTransactionExeception {
        EthSendTransaction ethSendTransaction = this.fastRawTransactionManager.sendTransaction(
                BigInteger.valueOf(GAS_PRICE),
                BigInteger.valueOf(MAX_GAS_PER_BLOCK),
                to,
                data,
                // number of native token transfer
                BigInteger.valueOf(0)
        );

        TransactionReceipt transactionReceipt = this.transactionReceiptProcessor.waitForTransactionReceipt(ethSendTransaction.getTransactionHash());
        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionExeception(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    public String signWithFisAdmin(String messageHash) {
        byte[] messageBytes = Numeric.hexStringToByteArray(messageHash);
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(messageBytes, credentials.getEcKeyPair());
        byte[] signature = new byte[65];

        System.arraycopy(signatureData.getR(), 0, signature, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signature, 32, 32);
        System.arraycopy(signatureData.getV(), 0, signature, 64, 1);

        return Numeric.toHexString(signature);
    }

    /**
     * create private transaction options for quorumPermission API
     * @return
     */
    public PrivateTransaction getQuorumPrivateTransactionOptionFromFisAdmin() {
        BigInteger nonce = this.fastRawTransactionManager.getCurrentNonce();
        return new PrivateTransaction(this.credentials.getAddress(), nonce, BigInteger.valueOf(4700000), null,
                BigInteger.ZERO, null, null, null);
    }

    /**
     * Create by hierachical order subOrg
     * @param subOrgFullId
     * @throws Exception
     */
    private void createSubOrgs(String subOrgFullId) throws NotParentOrgException, IOException {
        String[] orgHierachyList = subOrgFullId.split("\\.");
        if (!this.orgLevel1.equals(orgHierachyList[0])) {
            throw new NotParentOrgException(String.format("{} is not child org of {}", subOrgFullId, this.orgLevel1));
        }

        for (int i = 1; i < orgHierachyList.length ; i++) {
            String parentOrg = String.join(".", Arrays.copyOfRange(orgHierachyList, 0, i));
            String currSubOrgFullId = parentOrg.concat (".").concat(orgHierachyList[i]);
            if (!this.isOrgExist(currSubOrgFullId)) {

                TransactionReceipt transactionReceipt = null;
                try {
                    transactionReceipt = this.permissionInterface.addSubOrg(parentOrg, orgHierachyList[i], "", "", BigInteger.valueOf(0), BigInteger.valueOf(0)).send();
                } catch (Exception e) {
                    throw new IOException("failed to add subOrg");
                }

            } else {
                log.debug("subOrg exists {}", currSubOrgFullId);
            }
        }

        return;
    }

    private void createDefaultRolesForOrg(String orgFullId) throws IOException {
        // Can optimize with sendAsync -> CompletableFuture

        if (isRoleExist(Permission.Role.ORGADMIN.getName(), orgFullId)) {
            log.info("{} already exists under {}", Permission.Role.ORGADMIN.getName(), orgFullId);
        } else {
            try {
                TransactionReceipt transactionReceipt1 = this.permissionInterface.addNewRole(
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
            log.info("{} already exists under {}", Permission.Role.MEMBER.getName(), orgFullId);
        } else {
            try {
                TransactionReceipt transactionReceipt2 = this.permissionInterface.addNewRole(
                        Permission.Role.MEMBER.getName(),
                        orgFullId, BigInteger.valueOf(Permission.Role.MEMBER.getAccessType().getBaseAccess()),
                        Permission.Role.MEMBER.getIsVoter(),
                        Permission.Role.MEMBER.getIsOrgAdmin()
                ).send();
            } catch (Exception e) {
                throw new IOException("failed to create MEMBER role", e);
            }
        }

        return;
    }

    /**
     * Create sub org for org level 1 with default roles
     * @param orgFullId
     * @throws NotParentOrgException
     * @throws IOException
     */
    public void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, IOException {
        this.createSubOrgs(orgFullId);
        this.createDefaultRolesForOrg(orgFullId);
        return;
    }

    /**
     * Assign ORGADMIN role for sub-org
     * @param subOrgFullId
     * @param adminAddress
     * @throws IOException
     */
    @Override
    public void addAdminForSubOrg(String subOrgFullId, String adminAddress) throws IOException {
        try {
            this.permissionInterface.assignAccountRole(adminAddress, subOrgFullId, Permission.Role.ORGADMIN.getName()).send();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * Allow org to use LC protocol
     * @param orgFullId
     * @throws IOException
     */
    public void whiteListOrg(String orgFullId) throws IOException {
        try {
            this.lcManagement.whitelist(Arrays.asList(orgFullId)).send();
        } catch (Exception e) {
            throw new IOException("failed to whitelist org", e);
        }

        return;
    }

    /**
     * Restrict org to use LC protocol
     * @param orgFullId
     * @throws IOException
     */
    public void unwhiteListOrg(String orgFullId) throws IOException {
        try {
            this.lcManagement.unwhitelist(Arrays.asList(orgFullId)).send();
        } catch (Exception e) {
            throw new IOException("failed to unwhitelist org", e);
        }

        return;
    }

    /**
     * Suspend admin of sub org
     * @param subOrgFullId
     * @param adminAddress
     * @throws IOException
     */
    @Override
    public void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws IOException {
        // suspend requires account is in active status
        if (!isAccountOnchainUnderLevel1Org(adminAddress)) {
            log.info("account {} is not active under {}", adminAddress, this.orgLevel1);
            return;
        }
        try{
            this.permissionInterface.updateAccountStatus(subOrgFullId, adminAddress, BigInteger.valueOf(BlcWalletStatus.SUSPENDING.getValue())).send();
        } catch (Exception e) {
            throw new IOException("failed to unwhitelist org", e);
        }
    }

    public void createStandardLC(List<byte[]> parties, LC.Content contentHashStr) throws IOException {
        String DEFAULT_ROOT_HASH = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
        if (!contentHashStr.acknowledge.matches("/^0x[0-9a-zA-Z]{130}$/")) throw new IOException("Invalid acknowledge signature.");
        String approvalMessageHash = LC.generateApprovalMessageHash(contentHashStr);
        String approvalSignature = signWithFisAdmin(approvalMessageHash);
        LC.Content _content = new LC.Content(DEFAULT_ROOT_HASH, contentHashStr.signedTime, contentHashStr.prevHash, contentHashStr.numOfDocuments, contentHashStr.contentHash, contentHashStr.url, contentHashStr.acknowledge, approvalSignature);

    }

    private void validateData(String typeOf, String[] parties, LC.Content content, String from) throws IOException {
        if (typeOf.equals("1")) {
            if (parties.length != 4) {
                throw new IOException("The number of involved parties does not match. Expected 4.");
            }

            boolean[] orgs = parties.
        }
    }

//    private void approvalSignature(LC.Content content, String from) {
//        String approvalMessageHash = LC.generateApprovalMessageHash(content);
//        String approvalSignature = signWithFisAdmin(approvalMessageHash);
//        return approvalSignature;
//    }

    public void createUpasLC(List<byte[]> parties, LC.Content contentHashStr) throws IOException {

    }
    public void getRootHash(String documentId) throws IOException {
    }
}
