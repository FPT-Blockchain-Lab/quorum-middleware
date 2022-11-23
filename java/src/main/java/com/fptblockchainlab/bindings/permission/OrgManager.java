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
import org.web3j.abi.datatypes.DynamicArray;
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
public class OrgManager extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_UPDATEORG = "updateOrg";

    public static final String FUNC_APPROVEORGSTATUSUPDATE = "approveOrgStatusUpdate";

    public static final String FUNC_GETULTIMATEPARENT = "getUltimateParent";

    public static final String FUNC_ADDSUBORG = "addSubOrg";

    public static final String FUNC_CHECKORGACTIVE = "checkOrgActive";

    public static final String FUNC_GETORGINFO = "getOrgInfo";

    public static final String FUNC_GETSUBORGINDEXES = "getSubOrgIndexes";

    public static final String FUNC_GETNUMBEROFORGS = "getNumberOfOrgs";

    public static final String FUNC_CHECKORGSTATUS = "checkOrgStatus";

    public static final String FUNC_SETUPORG = "setUpOrg";

    public static final String FUNC_APPROVEORG = "approveOrg";

    public static final String FUNC_GETORGDETAILS = "getOrgDetails";

    public static final String FUNC_ADDORG = "addOrg";

    public static final String FUNC_CHECKORGEXISTS = "checkOrgExists";

    public static final Event ORGAPPROVED_EVENT = new Event("OrgApproved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ORGPENDINGAPPROVAL_EVENT = new Event("OrgPendingApproval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ORGSUSPENDED_EVENT = new Event("OrgSuspended", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ORGSUSPENSIONREVOKED_EVENT = new Event("OrgSuspensionRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected OrgManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected OrgManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected OrgManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected OrgManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> updateOrg(String _orgId, BigInteger _action) {
        final Function function = new Function(
                FUNC_UPDATEORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_action)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approveOrgStatusUpdate(String _orgId, BigInteger _action) {
        final Function function = new Function(
                FUNC_APPROVEORGSTATUSUPDATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_action)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> getUltimateParent(String _orgId) {
        final Function function = new Function(FUNC_GETULTIMATEPARENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> addSubOrg(String _pOrgId, String _orgId) {
        final Function function = new Function(
                FUNC_ADDSUBORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_pOrgId), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> checkOrgActive(String _orgId) {
        final Function function = new Function(FUNC_CHECKORGACTIVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Tuple5<String, String, String, BigInteger, BigInteger>> getOrgInfo(BigInteger _orgIndex) {
        final Function function = new Function(FUNC_GETORGINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_orgIndex)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<String, String, String, BigInteger, BigInteger>>(function,
                new Callable<Tuple5<String, String, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<String, String, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<List> getSubOrgIndexes(String _orgId) {
        final Function function = new Function(FUNC_GETSUBORGINDEXES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getNumberOfOrgs() {
        final Function function = new Function(FUNC_GETNUMBEROFORGS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> checkOrgStatus(String _orgId, BigInteger _orgStatus) {
        final Function function = new Function(FUNC_CHECKORGSTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_orgStatus)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setUpOrg(String _orgId, BigInteger _breadth, BigInteger _depth) {
        final Function function = new Function(
                FUNC_SETUPORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_breadth), 
                new org.web3j.abi.datatypes.generated.Uint256(_depth)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> approveOrg(String _orgId) {
        final Function function = new Function(
                FUNC_APPROVEORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple5<String, String, String, BigInteger, BigInteger>> getOrgDetails(String _orgId) {
        final Function function = new Function(FUNC_GETORGDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<String, String, String, BigInteger, BigInteger>>(function,
                new Callable<Tuple5<String, String, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<String, String, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> addOrg(String _orgId) {
        final Function function = new Function(
                FUNC_ADDORG, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> checkOrgExists(String _orgId) {
        final Function function = new Function(FUNC_CHECKORGEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static List<OrgApprovedEventResponse> getOrgApprovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ORGAPPROVED_EVENT, transactionReceipt);
        ArrayList<OrgApprovedEventResponse> responses = new ArrayList<OrgApprovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OrgApprovedEventResponse typedResponse = new OrgApprovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OrgApprovedEventResponse> orgApprovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OrgApprovedEventResponse>() {
            @Override
            public OrgApprovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ORGAPPROVED_EVENT, log);
                OrgApprovedEventResponse typedResponse = new OrgApprovedEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OrgApprovedEventResponse> orgApprovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ORGAPPROVED_EVENT));
        return orgApprovedEventFlowable(filter);
    }

    public static List<OrgPendingApprovalEventResponse> getOrgPendingApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ORGPENDINGAPPROVAL_EVENT, transactionReceipt);
        ArrayList<OrgPendingApprovalEventResponse> responses = new ArrayList<OrgPendingApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OrgPendingApprovalEventResponse typedResponse = new OrgPendingApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OrgPendingApprovalEventResponse> orgPendingApprovalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OrgPendingApprovalEventResponse>() {
            @Override
            public OrgPendingApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ORGPENDINGAPPROVAL_EVENT, log);
                OrgPendingApprovalEventResponse typedResponse = new OrgPendingApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._status = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OrgPendingApprovalEventResponse> orgPendingApprovalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ORGPENDINGAPPROVAL_EVENT));
        return orgPendingApprovalEventFlowable(filter);
    }

    public static List<OrgSuspendedEventResponse> getOrgSuspendedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ORGSUSPENDED_EVENT, transactionReceipt);
        ArrayList<OrgSuspendedEventResponse> responses = new ArrayList<OrgSuspendedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OrgSuspendedEventResponse typedResponse = new OrgSuspendedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OrgSuspendedEventResponse> orgSuspendedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OrgSuspendedEventResponse>() {
            @Override
            public OrgSuspendedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ORGSUSPENDED_EVENT, log);
                OrgSuspendedEventResponse typedResponse = new OrgSuspendedEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OrgSuspendedEventResponse> orgSuspendedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ORGSUSPENDED_EVENT));
        return orgSuspendedEventFlowable(filter);
    }

    public static List<OrgSuspensionRevokedEventResponse> getOrgSuspensionRevokedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ORGSUSPENSIONREVOKED_EVENT, transactionReceipt);
        ArrayList<OrgSuspensionRevokedEventResponse> responses = new ArrayList<OrgSuspensionRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OrgSuspensionRevokedEventResponse typedResponse = new OrgSuspensionRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OrgSuspensionRevokedEventResponse> orgSuspensionRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OrgSuspensionRevokedEventResponse>() {
            @Override
            public OrgSuspensionRevokedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ORGSUSPENSIONREVOKED_EVENT, log);
                OrgSuspensionRevokedEventResponse typedResponse = new OrgSuspensionRevokedEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._porgId = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._ultParent = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._level = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OrgSuspensionRevokedEventResponse> orgSuspensionRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ORGSUSPENSIONREVOKED_EVENT));
        return orgSuspensionRevokedEventFlowable(filter);
    }

    @Deprecated
    public static OrgManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new OrgManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static OrgManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OrgManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static OrgManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new OrgManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static OrgManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new OrgManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class OrgApprovedEventResponse extends BaseEventResponse {
        public String _orgId;

        public String _porgId;

        public String _ultParent;

        public BigInteger _level;

        public BigInteger _status;
    }

    public static class OrgPendingApprovalEventResponse extends BaseEventResponse {
        public String _orgId;

        public String _porgId;

        public String _ultParent;

        public BigInteger _level;

        public BigInteger _status;
    }

    public static class OrgSuspendedEventResponse extends BaseEventResponse {
        public String _orgId;

        public String _porgId;

        public String _ultParent;

        public BigInteger _level;
    }

    public static class OrgSuspensionRevokedEventResponse extends BaseEventResponse {
        public String _orgId;

        public String _porgId;

        public String _ultParent;

        public BigInteger _level;
    }
}
