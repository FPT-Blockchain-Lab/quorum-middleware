package com.fptblockchainlab.bindings.lc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
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
public class Mode extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_CHECKAUTHORIZATION = "checkAuthorization";

    public static final String FUNC_DAO = "dao";

    public static final String FUNC_INIT = "init";

    public static final String FUNC_INITIALIZED = "initialized";

    public static final String FUNC_MANAGEMENT = "management";

    public static final String FUNC_SETDAO = "setDAO";

    public static final String FUNC_SETMANAGEMENT = "setManagement";

    public static final String FUNC_SWITCHMODE = "switchMode";

    public static final String FUNC_SWITCHEDTODAO = "switchedToDAO";

    @Deprecated
    protected Mode(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Mode(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Mode(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Mode(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Boolean> checkAuthorization(String _operator) {
        final Function function = new Function(FUNC_CHECKAUTHORIZATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _operator)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> dao() {
        final Function function = new Function(FUNC_DAO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> init(String _management) {
        final Function function = new Function(
                FUNC_INIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _management)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> initialized() {
        final Function function = new Function(FUNC_INITIALIZED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> management() {
        final Function function = new Function(FUNC_MANAGEMENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setDAO(String _dao) {
        final Function function = new Function(
                FUNC_SETDAO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _dao)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setManagement(String _management) {
        final Function function = new Function(
                FUNC_SETMANAGEMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _management)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> switchMode() {
        final Function function = new Function(
                FUNC_SWITCHMODE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> switchedToDAO() {
        final Function function = new Function(FUNC_SWITCHEDTODAO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static Mode load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Mode(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Mode load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Mode(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Mode load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Mode(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Mode load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Mode(contractAddress, web3j, transactionManager, contractGasProvider);
    }
}
