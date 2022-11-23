package com.fptblockchainlab.bindings.permission;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
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
public class RoleManager extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_GETROLEDETAILS = "getRoleDetails";

    public static final String FUNC_ADDROLE = "addRole";

    public static final String FUNC_GETNUMBEROFROLES = "getNumberOfRoles";

    public static final String FUNC_GETROLEDETAILSFROMINDEX = "getRoleDetailsFromIndex";

    public static final String FUNC_REMOVEROLE = "removeRole";

    public static final String FUNC_ROLEEXISTS = "roleExists";

    public static final String FUNC_ISADMINROLE = "isAdminRole";

    public static final String FUNC_ROLEACCESS = "roleAccess";

    public static final String FUNC_TRANSACTIONALLOWED = "transactionAllowed";

    public static final String FUNC_ISVOTERROLE = "isVoterRole";

    public static final Event ROLECREATED_EVENT = new Event("RoleCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event ROLEREVOKED_EVENT = new Event("RoleRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected RoleManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected RoleManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected RoleManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected RoleManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>> getRoleDetails(String _roleId, String _orgId) {
        final Function function = new Function(FUNC_GETROLEDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>>(function,
                new Callable<Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>>() {
                    @Override
                    public Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> addRole(String _roleId, String _orgId, BigInteger _baseAccess, Boolean _isVoter, Boolean _isAdmin) {
        final Function function = new Function(
                FUNC_ADDROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_baseAccess), 
                new org.web3j.abi.datatypes.Bool(_isVoter), 
                new org.web3j.abi.datatypes.Bool(_isAdmin)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getNumberOfRoles() {
        final Function function = new Function(FUNC_GETNUMBEROFROLES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>> getRoleDetailsFromIndex(BigInteger _rIndex) {
        final Function function = new Function(FUNC_GETROLEDETAILSFROMINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_rIndex)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>>(function,
                new Callable<Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>>() {
                    @Override
                    public Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> removeRole(String _roleId, String _orgId) {
        final Function function = new Function(
                FUNC_REMOVEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> roleExists(String _roleId, String _orgId, String _ultParent) {
        final Function function = new Function(FUNC_ROLEEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_ultParent)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isAdminRole(String _roleId, String _orgId, String _ultParent) {
        final Function function = new Function(FUNC_ISADMINROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_ultParent)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> roleAccess(String _roleId, String _orgId, String _ultParent) {
        final Function function = new Function(FUNC_ROLEACCESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_ultParent)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> transactionAllowed(String _roleId, String _orgId, String _ultParent, BigInteger _typeOfTxn) {
        final Function function = new Function(FUNC_TRANSACTIONALLOWED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_ultParent), 
                new org.web3j.abi.datatypes.generated.Uint256(_typeOfTxn)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isVoterRole(String _roleId, String _orgId, String _ultParent) {
        final Function function = new Function(FUNC_ISVOTERROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_ultParent)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static List<RoleCreatedEventResponse> getRoleCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLECREATED_EVENT, transactionReceipt);
        ArrayList<RoleCreatedEventResponse> responses = new ArrayList<RoleCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleCreatedEventResponse typedResponse = new RoleCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._baseAccess = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._isVoter = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._isAdmin = (Boolean) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleCreatedEventResponse> roleCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, RoleCreatedEventResponse>() {
            @Override
            public RoleCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLECREATED_EVENT, log);
                RoleCreatedEventResponse typedResponse = new RoleCreatedEventResponse();
                typedResponse.log = log;
                typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._baseAccess = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._isVoter = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._isAdmin = (Boolean) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleCreatedEventResponse> roleCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLECREATED_EVENT));
        return roleCreatedEventFlowable(filter);
    }

    public static List<RoleRevokedEventResponse> getRoleRevokedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ROLEREVOKED_EVENT, transactionReceipt);
        ArrayList<RoleRevokedEventResponse> responses = new ArrayList<RoleRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, RoleRevokedEventResponse>() {
            @Override
            public RoleRevokedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEREVOKED_EVENT, log);
                RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
                typedResponse.log = log;
                typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEREVOKED_EVENT));
        return roleRevokedEventFlowable(filter);
    }

    @Deprecated
    public static RoleManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new RoleManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static RoleManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new RoleManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RoleManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new RoleManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static RoleManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new RoleManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class RoleCreatedEventResponse extends BaseEventResponse {
        public String _roleId;

        public String _orgId;

        public BigInteger _baseAccess;

        public Boolean _isVoter;

        public Boolean _isAdmin;
    }

    public static class RoleRevokedEventResponse extends BaseEventResponse {
        public String _roleId;

        public String _orgId;
    }
}
