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
import org.web3j.abi.datatypes.Address;
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
import org.web3j.tuples.generated.Tuple4;
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
public class PermissionsImplementation extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_UPDATEACCOUNTSTATUS = "updateAccountStatus";

    public static final String FUNC_ADDNEWROLE = "addNewRole";

    public static final String FUNC_SETPOLICY = "setPolicy";

    public static final String FUNC_STARTBLACKLISTEDACCOUNTRECOVERY = "startBlacklistedAccountRecovery";

    public static final String FUNC_UPDATEORGSTATUS = "updateOrgStatus";

    public static final String FUNC_ASSIGNADMINROLE = "assignAdminRole";

    public static final String FUNC_UPDATENETWORKBOOTSTATUS = "updateNetworkBootStatus";

    public static final String FUNC_CONNECTIONALLOWED = "connectionAllowed";

    public static final String FUNC_APPROVEBLACKLISTEDACCOUNTRECOVERY = "approveBlacklistedAccountRecovery";

    public static final String FUNC_GETNETWORKBOOTSTATUS = "getNetworkBootStatus";

    public static final String FUNC_ADDADMINACCOUNT = "addAdminAccount";

    public static final String FUNC_REMOVEROLE = "removeRole";

    public static final String FUNC_ADDSUBORG = "addSubOrg";

    public static final String FUNC_VALIDATEACCOUNT = "validateAccount";

    public static final String FUNC_ADDADMINNODE = "addAdminNode";

    public static final String FUNC_APPROVEADMINROLE = "approveAdminRole";

    public static final String FUNC_ASSIGNACCOUNTROLE = "assignAccountRole";

    public static final String FUNC_TRANSACTIONALLOWED = "transactionAllowed";

    public static final String FUNC_ISORGADMIN = "isOrgAdmin";

    public static final String FUNC_APPROVEBLACKLISTEDNODERECOVERY = "approveBlacklistedNodeRecovery";

    public static final String FUNC_INIT = "init";

    public static final String FUNC_APPROVEORGSTATUS = "approveOrgStatus";

    public static final String FUNC_UPDATENODESTATUS = "updateNodeStatus";

    public static final String FUNC_GETPOLICYDETAILS = "getPolicyDetails";

    public static final String FUNC_ISNETWORKADMIN = "isNetworkAdmin";

    public static final String FUNC_STARTBLACKLISTEDNODERECOVERY = "startBlacklistedNodeRecovery";

    public static final String FUNC_ADDORG = "addOrg";

    public static final String FUNC_ADDNODE = "addNode";

    public static final String FUNC_GETPENDINGOP = "getPendingOp";

    public static final String FUNC_SETMIGRATIONPOLICY = "setMigrationPolicy";

    public static final String FUNC_APPROVEORG = "approveOrg";

    public static final Event PERMISSIONSINITIALIZED_EVENT = new Event("PermissionsInitialized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected PermissionsImplementation(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PermissionsImplementation(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected PermissionsImplementation(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected PermissionsImplementation(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> updateAccountStatus(String _orgId, String _account, BigInteger _action, String _caller) {
        final Function function = new Function(
                FUNC_UPDATEACCOUNTSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.generated.Uint256(_action), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addNewRole(String _roleId, String _orgId, BigInteger _access, Boolean _voter, Boolean _admin, String _caller) {
        final Function function = new Function(
                FUNC_ADDNEWROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_access), 
                new org.web3j.abi.datatypes.Bool(_voter), 
                new org.web3j.abi.datatypes.Bool(_admin), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPolicy(String _nwAdminOrg, String _nwAdminRole, String _oAdminRole) {
        final Function function = new Function(
                FUNC_SETPOLICY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_nwAdminOrg), 
                new org.web3j.abi.datatypes.Utf8String(_nwAdminRole), 
                new org.web3j.abi.datatypes.Utf8String(_oAdminRole)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> startBlacklistedAccountRecovery(String _orgId, String _account, String _caller) {
        final Function function = new Function(
                FUNC_STARTBLACKLISTEDACCOUNTRECOVERY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateOrgStatus(String _orgId, BigInteger _action, String _caller) {
        final Function function = new Function(
                FUNC_UPDATEORGSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_action), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> assignAdminRole(String _orgId, String _account, String _roleId, String _caller) {
        final Function function = new Function(
                FUNC_ASSIGNADMINROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateNetworkBootStatus() {
        final Function function = new Function(
                FUNC_UPDATENETWORKBOOTSTATUS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> connectionAllowed(String _enodeId, String _ip, BigInteger _port) {
        final Function function = new Function(FUNC_CONNECTIONALLOWED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approveBlacklistedAccountRecovery(String _orgId, String _account, String _caller) {
        final Function function = new Function(
                FUNC_APPROVEBLACKLISTEDACCOUNTRECOVERY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> getNetworkBootStatus() {
        final Function function = new Function(FUNC_GETNETWORKBOOTSTATUS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addAdminAccount(String _account) {
        final Function function = new Function(
                FUNC_ADDADMINACCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeRole(String _roleId, String _orgId, String _caller) {
        final Function function = new Function(
                FUNC_REMOVEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addSubOrg(String _pOrgId, String _orgId, String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _caller) {
        final Function function = new Function(
                FUNC_ADDSUBORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_pOrgId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> validateAccount(String _account, String _orgId) {
        final Function function = new Function(FUNC_VALIDATEACCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addAdminNode(String _enodeId, String _ip, BigInteger _port, BigInteger _raftport) {
        final Function function = new Function(
                FUNC_ADDADMINNODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approveAdminRole(String _orgId, String _account, String _caller) {
        final Function function = new Function(
                FUNC_APPROVEADMINROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> assignAccountRole(String _account, String _orgId, String _roleId, String _caller) {
        final Function function = new Function(
                FUNC_ASSIGNACCOUNTROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> transactionAllowed(String _sender, String _target, BigInteger _value, BigInteger _gasPrice, BigInteger _gasLimit, byte[] _payload) {
        final Function function = new Function(FUNC_TRANSACTIONALLOWED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _sender), 
                new org.web3j.abi.datatypes.Address(160, _target), 
                new org.web3j.abi.datatypes.generated.Uint256(_value), 
                new org.web3j.abi.datatypes.generated.Uint256(_gasPrice), 
                new org.web3j.abi.datatypes.generated.Uint256(_gasLimit), 
                new org.web3j.abi.datatypes.DynamicBytes(_payload)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isOrgAdmin(String _account, String _orgId) {
        final Function function = new Function(FUNC_ISORGADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approveBlacklistedNodeRecovery(String _orgId, String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _caller) {
        final Function function = new Function(
                FUNC_APPROVEBLACKLISTEDNODERECOVERY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> init(BigInteger _breadth, BigInteger _depth) {
        final Function function = new Function(
                FUNC_INIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_breadth), 
                new org.web3j.abi.datatypes.generated.Uint256(_depth)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approveOrgStatus(String _orgId, BigInteger _action, String _caller) {
        final Function function = new Function(
                FUNC_APPROVEORGSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_action), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> updateNodeStatus(String _orgId, String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, BigInteger _action, String _caller) {
        final Function function = new Function(
                FUNC_UPDATENODESTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.generated.Uint256(_action), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple4<String, String, String, Boolean>> getPolicyDetails() {
        final Function function = new Function(FUNC_GETPOLICYDETAILS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple4<String, String, String, Boolean>>(function,
                new Callable<Tuple4<String, String, String, Boolean>>() {
                    @Override
                    public Tuple4<String, String, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, String, Boolean>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (Boolean) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> isNetworkAdmin(String _account) {
        final Function function = new Function(FUNC_ISNETWORKADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> startBlacklistedNodeRecovery(String _orgId, String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _caller) {
        final Function function = new Function(
                FUNC_STARTBLACKLISTEDNODERECOVERY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addOrg(String _orgId, String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _account, String _caller) {
        final Function function = new Function(
                FUNC_ADDORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addNode(String _orgId, String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _caller) {
        final Function function = new Function(
                FUNC_ADDNODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple4<String, String, String, BigInteger>> getPendingOp(String _orgId) {
        final Function function = new Function(FUNC_GETPENDINGOP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<String, String, String, BigInteger>>(function,
                new Callable<Tuple4<String, String, String, BigInteger>>() {
                    @Override
                    public Tuple4<String, String, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> setMigrationPolicy(String _nwAdminOrg, String _nwAdminRole, String _oAdminRole, Boolean _networkBootStatus) {
        final Function function = new Function(
                FUNC_SETMIGRATIONPOLICY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_nwAdminOrg), 
                new org.web3j.abi.datatypes.Utf8String(_nwAdminRole), 
                new org.web3j.abi.datatypes.Utf8String(_oAdminRole), 
                new org.web3j.abi.datatypes.Bool(_networkBootStatus)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approveOrg(String _orgId, String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _account, String _caller) {
        final Function function = new Function(
                FUNC_APPROVEORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Address(160, _caller)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<PermissionsInitializedEventResponse> getPermissionsInitializedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PERMISSIONSINITIALIZED_EVENT, transactionReceipt);
        ArrayList<PermissionsInitializedEventResponse> responses = new ArrayList<PermissionsInitializedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            PermissionsInitializedEventResponse typedResponse = new PermissionsInitializedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._networkBootStatus = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<PermissionsInitializedEventResponse> permissionsInitializedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, PermissionsInitializedEventResponse>() {
            @Override
            public PermissionsInitializedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(PERMISSIONSINITIALIZED_EVENT, log);
                PermissionsInitializedEventResponse typedResponse = new PermissionsInitializedEventResponse();
                typedResponse.log = log;
                typedResponse._networkBootStatus = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<PermissionsInitializedEventResponse> permissionsInitializedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PERMISSIONSINITIALIZED_EVENT));
        return permissionsInitializedEventFlowable(filter);
    }

    @Deprecated
    public static PermissionsImplementation load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PermissionsImplementation(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static PermissionsImplementation load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PermissionsImplementation(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static PermissionsImplementation load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new PermissionsImplementation(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static PermissionsImplementation load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new PermissionsImplementation(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class PermissionsInitializedEventResponse extends BaseEventResponse {
        public Boolean _networkBootStatus;
    }
}
