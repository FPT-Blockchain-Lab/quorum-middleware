package com.fptblockchainlab.bindings.permission;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class PermissionsUpgradable extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_GETPERMIMPL = "getPermImpl";

    public static final String FUNC_CONFIRMIMPLCHANGE = "confirmImplChange";

    public static final String FUNC_GETGUARDIAN = "getGuardian";

    public static final String FUNC_GETPERMINTERFACE = "getPermInterface";

    public static final String FUNC_INIT = "init";

    @Deprecated
    protected PermissionsUpgradable(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PermissionsUpgradable(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PermissionsUpgradable(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PermissionsUpgradable(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<String> getPermImpl() {
        final Function function = new Function(FUNC_GETPERMIMPL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> confirmImplChange(String _proposedImpl) {
        final Function function = new Function(
                FUNC_CONFIRMIMPLCHANGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _proposedImpl)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getGuardian() {
        final Function function = new Function(FUNC_GETGUARDIAN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getPermInterface() {
        final Function function = new Function(FUNC_GETPERMINTERFACE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> init(String _permInterface, String _permImpl) {
        final Function function = new Function(
                FUNC_INIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _permInterface), 
                new org.web3j.abi.datatypes.Address(160, _permImpl)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static PermissionsUpgradable load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PermissionsUpgradable(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PermissionsUpgradable load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PermissionsUpgradable(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PermissionsUpgradable load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PermissionsUpgradable(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PermissionsUpgradable load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PermissionsUpgradable(contractAddress, web3j, transactionManager, contractGasProvider);
    }
}
