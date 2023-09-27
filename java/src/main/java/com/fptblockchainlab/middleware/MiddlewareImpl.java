package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.LCManagement;
import com.fptblockchainlab.bindings.lc.RouterService;
import com.fptblockchainlab.bindings.lc.StandardLCFactory;
import com.fptblockchainlab.bindings.lc.UPASLCFactory;
import com.fptblockchainlab.bindings.permission.AccountManager;
import com.fptblockchainlab.bindings.permission.OrgManager;
import com.fptblockchainlab.bindings.permission.PermissionsInterface;
import com.fptblockchainlab.bindings.permission.RoleManager;
import com.fptblockchainlab.exceptions.FailedTransactionException;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static com.fptblockchainlab.middleware.LC.signMessage;

public class MiddlewareImpl implements IMiddleware {
    // Block time in milliseconds
    private static final long DEFAULT_POLLING_FREQUENCY = 6 * 1000;
    private static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 10;
    private static final long MAX_GAS_PER_BLOCK = 4_700_000;
    private static final int GAS_PRICE = 0;

    private final Credentials credentials;

    private final FastRawTransactionManager fastRawTransactionManager;

    private final TransactionReceiptProcessor transactionReceiptProcessor;

    @Getter
    private final Permission permission;

    @Getter
    private final LCWrapper lcWrapper;

    public MiddlewareImpl(
            String quorumUrl,
            String privateKey,
            long chainId,
            String accountMgrAddress,
            String orgMgrAddress,
            String roleMgrAddress,
            String lcManagementAddress,
            String interfaceAddress,
            String standardLCFactoryAddress,
            String upaslcFactoryAddress,
            String routerServiceAddress,
            String orgLevel1) {
        HttpService httpService = new HttpService(quorumUrl);

        Quorum quorum = Quorum.build(httpService);
        StaticGasProvider contractGasProvider = new StaticGasProvider(BigInteger.valueOf(GAS_PRICE), BigInteger.valueOf(MAX_GAS_PER_BLOCK));
        this.credentials = Credentials.create(privateKey);
        this.transactionReceiptProcessor = new PollingTransactionReceiptProcessor(
                quorum,
                DEFAULT_POLLING_FREQUENCY,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );
        this.fastRawTransactionManager = new FastRawTransactionManager(quorum, credentials, chainId, transactionReceiptProcessor);
        AccountManager accountManager = AccountManager.load(accountMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        OrgManager orgManager = OrgManager.load(orgMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        RoleManager roleManager = RoleManager.load(roleMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        LCManagement lcManagement = LCManagement.load(lcManagementAddress, quorum, fastRawTransactionManager, contractGasProvider);
        PermissionsInterface permissionInterface = PermissionsInterface.load(interfaceAddress, quorum, fastRawTransactionManager, contractGasProvider);
        StandardLCFactory standardLCFactory = StandardLCFactory.load(standardLCFactoryAddress, quorum, fastRawTransactionManager, contractGasProvider);
        UPASLCFactory upaslcFactory = UPASLCFactory.load(upaslcFactoryAddress, quorum, fastRawTransactionManager, contractGasProvider);
        RouterService routerService = RouterService.load(routerServiceAddress, quorum, fastRawTransactionManager, contractGasProvider);

        this.permission = new Permission(orgManager, roleManager, lcManagement, permissionInterface, accountManager, orgLevel1);
        this.lcWrapper = new LCWrapper(
                contractGasProvider,
                credentials,
                quorum,
                standardLCFactory,
                upaslcFactory,
                routerService
        );
    }

    /**
     * get admin role from default roles
     *
     * @return
     */
    public Permission.Role getAdminRole() {
        return Permission.Role.ORGADMIN;
    }

    /**
     * Get member role from default role
     * @return
     */
    public Permission.Role getMemberRole() {
        return Permission.Role.MEMBER;
    }

    /**
     * Give a level 1 org in MiddlewareSErvice check if account is active and under this level 1 org
     * or any sub or of this level 1 org
     *
     * @param account
     * @return
     * @throws IOException
     */
    @Override
    public boolean isAccountOnchainUnderLevel1Org(String account) throws IOException {
        return this.permission.isAccountOnchainUnderLevel1Org(account);
    }

    /**
     * @param to
     * @param data
     * @throws IOException
     * @throws TransactionException
     * @throws FailedTransactionException
     */
    @Override
    public void signWithAdminAndSend(String to, String data) throws IOException, TransactionException, FailedTransactionException {
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
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     * sign message hash compatible with personal_sign
     *
     * @param messageHash
     * @return
     */
    @Override
    public String signWithUltimateParentAdmin(String messageHash) {
        return signMessage(messageHash, this.credentials);
    }

    /**
     * Create sub org for org level 1
     *
     * @param orgFullId
     * @throws NotParentOrgException
     * @throws FailedTransactionException
     * @throws IOException
     */
    @Override
    public void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, FailedTransactionException, IOException {
        this.permission.createSubOrgWithDefaultRoles(orgFullId);
    }

    /**
     * Add
     *
     * @param subOrgFullId
     * @param adminWallet
     */
    @Override
    public void addAdminForSubOrg(String subOrgFullId, String adminWallet) throws IOException, FailedTransactionException {
        this.permission.addAdminForSubOrg(subOrgFullId, adminWallet);
    }

    /**
     * Allow org to use LC protocol
     *
     * @param orgFullId
     * @throws FailedTransactionException
     * @throws IOException
     */
    @Override
    public void whiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        this.permission.whiteListOrg(orgFullId);
    }

    /**
     * Restrict org to use LC protocol
     *
     * @param orgFullId
     * @throws FailedTransactionException
     * @throws IOException
     */
    @Override
    public void unwhiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        this.permission.unwhiteListOrg(orgFullId);
    }

    /**
     * Suspend admin of sub org
     *
     * @param subOrgFullId
     * @param adminAddress
     */
    @Override
    public void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws FailedTransactionException, IOException {
        this.permission.suspendAdminSubOrg(subOrgFullId, adminAddress);
    }

    @Override
    public void createStandardLC(List<String> parties, String prevHash, LC.Content content, String privateKey) throws FailedTransactionException, IOException {
        this.lcWrapper.createStandardLC(parties, content, privateKey);
    }

    @Override
    public void createUPASLC(List<String> parties, String prevHash, LC.Content content, String privateKey) throws FailedTransactionException, IOException {
        this.lcWrapper.createUPASLC(parties, content, privateKey);
    }

    @Override
    public void approveLC(BigInteger documentId, LC.Stage stage, LC.Content content, String privateKey) throws Exception {
        this.lcWrapper.approveLC(documentId, stage, content, privateKey);
    }

    @Override
    public void closeLC(BigInteger documentId) throws FailedTransactionException, IOException {
        this.lcWrapper.closeLC(documentId);
    }

    @Override
    public void submitRootAmendment(BigInteger documentId, LC.Content content, String privateKey) throws Exception {
        this.lcWrapper.submitRootAmendment(documentId, content, privateKey);
    }

    @Override
    public void submitGeneralAmendment(BigInteger documentId, LC.Stage stage, LC.Content content, String privateKey) throws Exception {
        this.lcWrapper.submitGeneralAmendment(documentId, stage, content, privateKey);
    }

    @Override
    public void approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, String privateKey) throws Exception {
        this.lcWrapper.approveAmendment(documentId, proposer, nonce, privateKey);
    }

    @Override
    public void fulfillAmendment(BigInteger documentId, String proposer, BigInteger nonce) throws Exception {
        this.lcWrapper.fulfillAmendment(documentId, proposer, nonce);
    }
}