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
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple5;
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
public class AccountManager extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_ASSIGNACCOUNTROLE = "assignAccountRole";

    public static final String FUNC_REMOVEEXISTINGADMIN = "removeExistingAdmin";

    public static final String FUNC_GETACCOUNTDETAILS = "getAccountDetails";

    public static final String FUNC_GETNUMBEROFACCOUNTS = "getNumberOfAccounts";

    public static final String FUNC_GETACCOUNTORGROLE = "getAccountOrgRole";

    public static final String FUNC_VALIDATEACCOUNT = "validateAccount";

    public static final String FUNC_GETACCOUNTROLE = "getAccountRole";

    public static final String FUNC_UPDATEACCOUNTSTATUS = "updateAccountStatus";

    public static final String FUNC_ORGADMINEXISTS = "orgAdminExists";

    public static final String FUNC_GETACCOUNTDETAILSFROMINDEX = "getAccountDetailsFromIndex";

    public static final String FUNC_ADDNEWADMIN = "addNewAdmin";

    public static final String FUNC_SETDEFAULTS = "setDefaults";

    public static final String FUNC_ASSIGNADMINROLE = "assignAdminRole";

    public static final String FUNC_CHECKORGADMIN = "checkOrgAdmin";

    public static final String FUNC_GETACCOUNTSTATUS = "getAccountStatus";

    public static final Event ACCOUNTACCESSMODIFIED_EVENT = new Event("AccountAccessModified", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ACCOUNTACCESSREVOKED_EVENT = new Event("AccountAccessRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
    ;

    public static final Event ACCOUNTSTATUSCHANGED_EVENT = new Event("AccountStatusChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected AccountManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AccountManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AccountManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AccountManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> assignAccountRole(String _account, String _orgId, String _roleId, Boolean _adminRole) {
        final Function function = new Function(
                FUNC_ASSIGNACCOUNTROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.Bool(_adminRole)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> removeExistingAdmin(String _orgId) {
        final Function function = new Function(
                FUNC_REMOVEEXISTINGADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple5<String, String, String, BigInteger, Boolean>> getAccountDetails(String _account) {
        final Function function = new Function(FUNC_GETACCOUNTDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple5<String, String, String, BigInteger, Boolean>>(function,
                new Callable<Tuple5<String, String, String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple5<String, String, String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, String, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getNumberOfAccounts() {
        final Function function = new Function(FUNC_GETNUMBEROFACCOUNTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<String, String>> getAccountOrgRole(String _account) {
        final Function function = new Function(FUNC_GETACCOUNTORGROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteFunctionCall<Tuple2<String, String>>(function,
                new Callable<Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> validateAccount(String _account, String _orgId) {
        final Function function = new Function(FUNC_VALIDATEACCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> getAccountRole(String _account) {
        final Function function = new Function(FUNC_GETACCOUNTROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> updateAccountStatus(String _orgId, String _account, BigInteger _action) {
        final Function function = new Function(
                FUNC_UPDATEACCOUNTSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.generated.Uint256(_action)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> orgAdminExists(String _orgId) {
        final Function function = new Function(FUNC_ORGADMINEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Tuple5<String, String, String, BigInteger, Boolean>> getAccountDetailsFromIndex(BigInteger _aIndex) {
        final Function function = new Function(FUNC_GETACCOUNTDETAILSFROMINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_aIndex)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple5<String, String, String, BigInteger, Boolean>>(function,
                new Callable<Tuple5<String, String, String, BigInteger, Boolean>>() {
                    @Override
                    public Tuple5<String, String, String, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, String, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> addNewAdmin(String _orgId, String _account) {
        final Function function = new Function(
                FUNC_ADDNEWADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setDefaults(String _nwAdminRole, String _oAdminRole) {
        final Function function = new Function(
                FUNC_SETDEFAULTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_nwAdminRole), 
                new org.web3j.abi.datatypes.Utf8String(_oAdminRole)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> assignAdminRole(String _account, String _orgId, String _roleId, BigInteger _status) {
        final Function function = new Function(
                FUNC_ASSIGNADMINROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_roleId), 
                new org.web3j.abi.datatypes.generated.Uint256(_status)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> checkOrgAdmin(String _account, String _orgId, String _ultParent) {
        final Function function = new Function(FUNC_CHECKORGADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_ultParent)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> getAccountStatus(String _account) {
        final Function function = new Function(FUNC_GETACCOUNTSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static List<AccountAccessModifiedEventResponse> getAccountAccessModifiedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCOUNTACCESSMODIFIED_EVENT, transactionReceipt);
        ArrayList<AccountAccessModifiedEventResponse> responses = new ArrayList<AccountAccessModifiedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccountAccessModifiedEventResponse typedResponse = new AccountAccessModifiedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._orgAdmin = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AccountAccessModifiedEventResponse> accountAccessModifiedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AccountAccessModifiedEventResponse>() {
            @Override
            public AccountAccessModifiedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ACCOUNTACCESSMODIFIED_EVENT, log);
                AccountAccessModifiedEventResponse typedResponse = new AccountAccessModifiedEventResponse();
                typedResponse.log = log;
                typedResponse._account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._orgAdmin = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AccountAccessModifiedEventResponse> accountAccessModifiedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCOUNTACCESSMODIFIED_EVENT));
        return accountAccessModifiedEventFlowable(filter);
    }

    public static List<AccountAccessRevokedEventResponse> getAccountAccessRevokedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCOUNTACCESSREVOKED_EVENT, transactionReceipt);
        ArrayList<AccountAccessRevokedEventResponse> responses = new ArrayList<AccountAccessRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccountAccessRevokedEventResponse typedResponse = new AccountAccessRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._orgAdmin = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AccountAccessRevokedEventResponse> accountAccessRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AccountAccessRevokedEventResponse>() {
            @Override
            public AccountAccessRevokedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ACCOUNTACCESSREVOKED_EVENT, log);
                AccountAccessRevokedEventResponse typedResponse = new AccountAccessRevokedEventResponse();
                typedResponse.log = log;
                typedResponse._account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._roleId = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._orgAdmin = (Boolean) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AccountAccessRevokedEventResponse> accountAccessRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCOUNTACCESSREVOKED_EVENT));
        return accountAccessRevokedEventFlowable(filter);
    }

    public static List<AccountStatusChangedEventResponse> getAccountStatusChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ACCOUNTSTATUSCHANGED_EVENT, transactionReceipt);
        ArrayList<AccountStatusChangedEventResponse> responses = new ArrayList<AccountStatusChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccountStatusChangedEventResponse typedResponse = new AccountStatusChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AccountStatusChangedEventResponse> accountStatusChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AccountStatusChangedEventResponse>() {
            @Override
            public AccountStatusChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ACCOUNTSTATUSCHANGED_EVENT, log);
                AccountStatusChangedEventResponse typedResponse = new AccountStatusChangedEventResponse();
                typedResponse.log = log;
                typedResponse._account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AccountStatusChangedEventResponse> accountStatusChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCOUNTSTATUSCHANGED_EVENT));
        return accountStatusChangedEventFlowable(filter);
    }

    @Deprecated
    public static AccountManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccountManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AccountManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccountManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AccountManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AccountManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AccountManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AccountManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class AccountAccessModifiedEventResponse extends BaseEventResponse {
        public String _account;

        public String _orgId;

        public String _roleId;

        public Boolean _orgAdmin;

        public BigInteger _status;
    }

    public static class AccountAccessRevokedEventResponse extends BaseEventResponse {
        public String _account;

        public String _orgId;

        public String _roleId;

        public Boolean _orgAdmin;
    }

    public static class AccountStatusChangedEventResponse extends BaseEventResponse {
        public String _account;

        public String _orgId;

        public BigInteger _status;
    }
}
