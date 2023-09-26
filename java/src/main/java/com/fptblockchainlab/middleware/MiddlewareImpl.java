package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.LCManagement;
import com.fptblockchainlab.bindings.lc.RouterService;
import com.fptblockchainlab.bindings.lc.StandardLCFactory;
import com.fptblockchainlab.bindings.lc.UPASLCFactory;
import com.fptblockchainlab.bindings.permission.AccountManager;
import com.fptblockchainlab.bindings.permission.OrgManager;
import com.fptblockchainlab.bindings.permission.PermissionsInterface;
import com.fptblockchainlab.bindings.permission.RoleManager;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.math.BigInteger;

public class MiddlewareImpl {
    // Block time in milliseconds
    private static final long DEFAULT_POLLING_FREQUENCY = 6 * 1000;
    private static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 10;
    private static final long MAX_GAS_PER_BLOCK = 4_700_000;
    private static final int GAS_PRICE = 0;

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
        Credentials credentials = Credentials.create(privateKey);

        TransactionReceiptProcessor transactionReceiptProcessor = new PollingTransactionReceiptProcessor(
                quorum,
                DEFAULT_POLLING_FREQUENCY,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );
        FastRawTransactionManager fastRawTransactionManager = new FastRawTransactionManager(quorum, credentials, chainId, transactionReceiptProcessor);
        AccountManager accountManager = AccountManager.load(accountMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        OrgManager orgManager = OrgManager.load(orgMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        RoleManager roleManager = RoleManager.load(roleMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        LCManagement lcManagement = LCManagement.load(lcManagementAddress, quorum, fastRawTransactionManager, contractGasProvider);
        PermissionsInterface permissionInterface = PermissionsInterface.load(interfaceAddress, quorum, fastRawTransactionManager, contractGasProvider);
        StandardLCFactory standardLCFactory = StandardLCFactory.load(standardLCFactoryAddress, quorum, fastRawTransactionManager, contractGasProvider);
        UPASLCFactory upaslcFactory = UPASLCFactory.load(upaslcFactoryAddress, quorum, fastRawTransactionManager, contractGasProvider);
        RouterService routerService = RouterService.load(routerServiceAddress, quorum, fastRawTransactionManager, contractGasProvider);

        this.permission = Permission.getInstance(orgManager, roleManager, lcManagement, permissionInterface, accountManager, orgLevel1);
        this.lcWrapper = LCWrapper.getInstance(
                contractGasProvider,
                credentials,
                quorum,
                standardLCFactory,
                upaslcFactory,
                routerService
        );
    }
}