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
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static com.fptblockchainlab.middleware.LC.signMessage;

public class MiddlewareImpl implements IMiddleware {
    private static final long DEFAULT_POLLING_FREQUENCY = 1_000;
    private static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 20;
    private static final long MAX_GAS_PER_BLOCK = 4_700_000;
    private static final int GAS_PRICE = 0;

    private final Credentials credentials;
    private final TransactionManager transactionManager;
    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final ContractGasProvider contractGasProvider;
    private final Quorum quorum;
    private LCWrapper lcWrapper;
    private Permission permission;

    @Getter
    public boolean isReadOnly;

    public MiddlewareImpl(
            HttpService httpService,
            Config config
    ) {
        this.quorum = Quorum.build(httpService);
        this.contractGasProvider = new StaticGasProvider(BigInteger.valueOf(GAS_PRICE), BigInteger.valueOf(MAX_GAS_PER_BLOCK));
        this.credentials = Credentials.create(config.getPrivateKey());
        this.transactionReceiptProcessor = new PollingTransactionReceiptProcessor(
                quorum,
                DEFAULT_POLLING_FREQUENCY,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );
        this.transactionManager = new FastRawTransactionManager(quorum, credentials, config.getChainId(), transactionReceiptProcessor);
        if (config.getLcManagmentAddress().isPresent() && config.getStandardLCFactoryAddress().isPresent() && config.getUpaslcFactoryAddress().isPresent() && config.getRouterServiceAddress().isPresent()) {
            LCManagement lcManagement = LCManagement.load(config.getLcManagmentAddress().get(), quorum, this.transactionManager , contractGasProvider);
            StandardLCFactory standardLCFactory = StandardLCFactory.load(config.getStandardLCFactoryAddress().get(), quorum, this.transactionManager , contractGasProvider);
            UPASLCFactory upaslcFactory = UPASLCFactory.load(config.getUpaslcFactoryAddress().get(), quorum, this.transactionManager , contractGasProvider);
            RouterService routerService = RouterService.load(config.getRouterServiceAddress().get(), quorum, this.transactionManager , contractGasProvider);
            this.lcWrapper = new LCWrapper(quorum, standardLCFactory, upaslcFactory, routerService, lcManagement);
        }

        if (config.getAccountMgrAddress().isPresent() && config.getOrgMgrAddress().isPresent() && config.getRoleMgrAddress().isPresent() && config.getInterfaceAddress().isPresent() && config.getUltimateParentOrg().isPresent()) {
            AccountManager accountManager = AccountManager.load(config.getAccountMgrAddress().get(), quorum, this.transactionManager, contractGasProvider);
            OrgManager orgManager = OrgManager.load(config.getOrgMgrAddress().get(), quorum, this.transactionManager, contractGasProvider);
            RoleManager roleManager = RoleManager.load(config.getRoleMgrAddress().get(), quorum, this.transactionManager, contractGasProvider);
            PermissionsInterface permissionInterface = PermissionsInterface.load(config.getInterfaceAddress().get(), quorum, this.transactionManager, contractGasProvider);
            this.permission = new Permission(orgManager, roleManager, permissionInterface, accountManager, config.getUltimateParentOrg().get());
        }
    }

    public Permission.Role getAdminRole() {
        return Permission.Role.ORGADMIN;
    }

    public Permission.Role getMemberRole() {
        return Permission.Role.MEMBER;
    }

    @Override
    public boolean isAccountOnchainUnderLevel1Org(String account) throws IOException {
        return this.permission.isAccountOnchainUnderLevel1Org(account);
    }

    @Override
    public void signWithAdminAndSend(String to, String data) throws IOException, TransactionException, FailedTransactionException {
        EthSendTransaction ethSendTransaction = this.transactionManager.sendTransaction(
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

    @Override
    public String signWithUltimateParentAdmin(String messageHash) {
        return signMessage(messageHash, this.credentials);
    }

    @Override
    public void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, FailedTransactionException, IOException {
        this.permission.createSubOrgWithDefaultRoles(orgFullId);
    }

    @Override
    public void addAdminForSubOrg(String subOrgFullId, String adminWallet) throws IOException, FailedTransactionException {
        this.permission.addAdminForSubOrg(subOrgFullId, adminWallet);
    }

    @Override
    public void whiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        this.lcWrapper.whiteListOrg(orgFullId);
    }

    @Override
    public void unwhiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        this.lcWrapper.unwhiteListOrg(orgFullId);
    }

    @Override
    public void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws FailedTransactionException, IOException {
        this.permission.suspendAdminSubOrg(subOrgFullId, adminAddress);
    }

    @Override
    public TransactionReceipt createStandardLC(List<String> parties, LC.Content content, Credentials credentials) throws Exception {
        return this.lcWrapper.createStandardLC(parties, content, credentials);
    }

    @Override
    public TransactionReceipt createUPASLC(List<String> parties, LC.Content content, Credentials credentials) throws Exception {
        return this.lcWrapper.createUPASLC(parties, content, credentials);
    }

    @Override
    public TransactionReceipt approveLC(BigInteger documentId, LC.Stage stage, LC.Content content, Credentials credentials) throws Exception {
        return this.lcWrapper.approveLC(documentId, stage, content, credentials);
    }

    @Override
    public TransactionReceipt closeLC(BigInteger documentId) throws Exception {
        return this.lcWrapper.closeLC(documentId);
    }

    @Override
    public TransactionReceipt submitRootAmendment(BigInteger documentId, LC.Content content, Credentials credentials) throws Exception {
        return this.lcWrapper.submitRootAmendment(documentId, content, credentials);
    }

    @Override
    public TransactionReceipt submitGeneralAmendment(BigInteger documentId, LC.Stage stage, LC.Content content, Credentials credentials) throws Exception {
        return this.lcWrapper.submitGeneralAmendment(documentId, stage, content, credentials);
    }

    @Override
    public TransactionReceipt approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, Credentials credentials) throws Exception {
        return this.lcWrapper.approveAmendment(documentId, proposer, nonce, credentials);
    }

    @Override
    public TransactionReceipt fulfillAmendment(BigInteger documentId, String proposer, BigInteger nonce) throws Exception {
        return this.lcWrapper.fulfillAmendment(documentId, proposer, nonce);
    }
}