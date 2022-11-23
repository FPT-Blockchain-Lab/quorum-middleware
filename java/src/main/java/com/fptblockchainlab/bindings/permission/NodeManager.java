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
import org.web3j.abi.datatypes.generated.Uint16;
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
public class NodeManager extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_UPDATENODESTATUS = "updateNodeStatus";

    public static final String FUNC_GETNODEDETAILS = "getNodeDetails";

    public static final String FUNC_ADDADMINNODE = "addAdminNode";

    public static final String FUNC_CONNECTIONALLOWED = "connectionAllowed";

    public static final String FUNC_ADDORGNODE = "addOrgNode";

    public static final String FUNC_ADDNODE = "addNode";

    public static final String FUNC_GETNODEDETAILSFROMINDEX = "getNodeDetailsFromIndex";

    public static final String FUNC_GETNUMBEROFNODES = "getNumberOfNodes";

    public static final String FUNC_APPROVENODE = "approveNode";

    public static final Event NODEPROPOSED_EVENT = new Event("NodeProposed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event NODEAPPROVED_EVENT = new Event("NodeApproved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event NODEDEACTIVATED_EVENT = new Event("NodeDeactivated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event NODEACTIVATED_EVENT = new Event("NodeActivated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event NODEBLACKLISTED_EVENT = new Event("NodeBlacklisted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event NODERECOVERYINITIATED_EVENT = new Event("NodeRecoveryInitiated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event NODERECOVERYCOMPLETED_EVENT = new Event("NodeRecoveryCompleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected NodeManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NodeManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected NodeManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected NodeManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> updateNodeStatus(String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _orgId, BigInteger _action) {
        final Function function = new Function(
                FUNC_UPDATENODESTATUS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.generated.Uint256(_action)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>> getNodeDetails(String enodeId) {
        final Function function = new Function(FUNC_GETNODEDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(enodeId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> addAdminNode(String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _orgId) {
        final Function function = new Function(
                FUNC_ADDADMINNODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
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

    public RemoteFunctionCall<TransactionReceipt> addOrgNode(String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _orgId) {
        final Function function = new Function(
                FUNC_ADDORGNODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addNode(String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _orgId) {
        final Function function = new Function(
                FUNC_ADDNODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>> getNodeDetailsFromIndex(BigInteger _nodeIndex) {
        final Function function = new Function(FUNC_GETNODEDETAILSFROMINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_nodeIndex)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint16>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getNumberOfNodes() {
        final Function function = new Function(FUNC_GETNUMBEROFNODES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approveNode(String _enodeId, String _ip, BigInteger _port, BigInteger _raftport, String _orgId) {
        final Function function = new Function(
                FUNC_APPROVENODE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Utf8String(_ip), 
                new org.web3j.abi.datatypes.generated.Uint16(_port), 
                new org.web3j.abi.datatypes.generated.Uint16(_raftport), 
                new org.web3j.abi.datatypes.Utf8String(_orgId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<NodeProposedEventResponse> getNodeProposedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NODEPROPOSED_EVENT, transactionReceipt);
        ArrayList<NodeProposedEventResponse> responses = new ArrayList<NodeProposedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NodeProposedEventResponse typedResponse = new NodeProposedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NodeProposedEventResponse> nodeProposedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NodeProposedEventResponse>() {
            @Override
            public NodeProposedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NODEPROPOSED_EVENT, log);
                NodeProposedEventResponse typedResponse = new NodeProposedEventResponse();
                typedResponse.log = log;
                typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NodeProposedEventResponse> nodeProposedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NODEPROPOSED_EVENT));
        return nodeProposedEventFlowable(filter);
    }

    public static List<NodeApprovedEventResponse> getNodeApprovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NODEAPPROVED_EVENT, transactionReceipt);
        ArrayList<NodeApprovedEventResponse> responses = new ArrayList<NodeApprovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NodeApprovedEventResponse typedResponse = new NodeApprovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NodeApprovedEventResponse> nodeApprovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NodeApprovedEventResponse>() {
            @Override
            public NodeApprovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NODEAPPROVED_EVENT, log);
                NodeApprovedEventResponse typedResponse = new NodeApprovedEventResponse();
                typedResponse.log = log;
                typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NodeApprovedEventResponse> nodeApprovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NODEAPPROVED_EVENT));
        return nodeApprovedEventFlowable(filter);
    }

    public static List<NodeDeactivatedEventResponse> getNodeDeactivatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NODEDEACTIVATED_EVENT, transactionReceipt);
        ArrayList<NodeDeactivatedEventResponse> responses = new ArrayList<NodeDeactivatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NodeDeactivatedEventResponse typedResponse = new NodeDeactivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NodeDeactivatedEventResponse> nodeDeactivatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NodeDeactivatedEventResponse>() {
            @Override
            public NodeDeactivatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NODEDEACTIVATED_EVENT, log);
                NodeDeactivatedEventResponse typedResponse = new NodeDeactivatedEventResponse();
                typedResponse.log = log;
                typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NodeDeactivatedEventResponse> nodeDeactivatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NODEDEACTIVATED_EVENT));
        return nodeDeactivatedEventFlowable(filter);
    }

    public static List<NodeActivatedEventResponse> getNodeActivatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NODEACTIVATED_EVENT, transactionReceipt);
        ArrayList<NodeActivatedEventResponse> responses = new ArrayList<NodeActivatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NodeActivatedEventResponse typedResponse = new NodeActivatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NodeActivatedEventResponse> nodeActivatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NodeActivatedEventResponse>() {
            @Override
            public NodeActivatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NODEACTIVATED_EVENT, log);
                NodeActivatedEventResponse typedResponse = new NodeActivatedEventResponse();
                typedResponse.log = log;
                typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NodeActivatedEventResponse> nodeActivatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NODEACTIVATED_EVENT));
        return nodeActivatedEventFlowable(filter);
    }

    public static List<NodeBlacklistedEventResponse> getNodeBlacklistedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NODEBLACKLISTED_EVENT, transactionReceipt);
        ArrayList<NodeBlacklistedEventResponse> responses = new ArrayList<NodeBlacklistedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NodeBlacklistedEventResponse typedResponse = new NodeBlacklistedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NodeBlacklistedEventResponse> nodeBlacklistedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NodeBlacklistedEventResponse>() {
            @Override
            public NodeBlacklistedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NODEBLACKLISTED_EVENT, log);
                NodeBlacklistedEventResponse typedResponse = new NodeBlacklistedEventResponse();
                typedResponse.log = log;
                typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NodeBlacklistedEventResponse> nodeBlacklistedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NODEBLACKLISTED_EVENT));
        return nodeBlacklistedEventFlowable(filter);
    }

    public static List<NodeRecoveryInitiatedEventResponse> getNodeRecoveryInitiatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NODERECOVERYINITIATED_EVENT, transactionReceipt);
        ArrayList<NodeRecoveryInitiatedEventResponse> responses = new ArrayList<NodeRecoveryInitiatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NodeRecoveryInitiatedEventResponse typedResponse = new NodeRecoveryInitiatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NodeRecoveryInitiatedEventResponse> nodeRecoveryInitiatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NodeRecoveryInitiatedEventResponse>() {
            @Override
            public NodeRecoveryInitiatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NODERECOVERYINITIATED_EVENT, log);
                NodeRecoveryInitiatedEventResponse typedResponse = new NodeRecoveryInitiatedEventResponse();
                typedResponse.log = log;
                typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NodeRecoveryInitiatedEventResponse> nodeRecoveryInitiatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NODERECOVERYINITIATED_EVENT));
        return nodeRecoveryInitiatedEventFlowable(filter);
    }

    public static List<NodeRecoveryCompletedEventResponse> getNodeRecoveryCompletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NODERECOVERYCOMPLETED_EVENT, transactionReceipt);
        ArrayList<NodeRecoveryCompletedEventResponse> responses = new ArrayList<NodeRecoveryCompletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NodeRecoveryCompletedEventResponse typedResponse = new NodeRecoveryCompletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NodeRecoveryCompletedEventResponse> nodeRecoveryCompletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NodeRecoveryCompletedEventResponse>() {
            @Override
            public NodeRecoveryCompletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NODERECOVERYCOMPLETED_EVENT, log);
                NodeRecoveryCompletedEventResponse typedResponse = new NodeRecoveryCompletedEventResponse();
                typedResponse.log = log;
                typedResponse._enodeId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._ip = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._port = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._raftport = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NodeRecoveryCompletedEventResponse> nodeRecoveryCompletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NODERECOVERYCOMPLETED_EVENT));
        return nodeRecoveryCompletedEventFlowable(filter);
    }

    @Deprecated
    public static NodeManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NodeManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static NodeManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NodeManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static NodeManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new NodeManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static NodeManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new NodeManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class NodeProposedEventResponse extends BaseEventResponse {
        public String _enodeId;

        public String _ip;

        public BigInteger _port;

        public BigInteger _raftport;

        public String _orgId;
    }

    public static class NodeApprovedEventResponse extends BaseEventResponse {
        public String _enodeId;

        public String _ip;

        public BigInteger _port;

        public BigInteger _raftport;

        public String _orgId;
    }

    public static class NodeDeactivatedEventResponse extends BaseEventResponse {
        public String _enodeId;

        public String _ip;

        public BigInteger _port;

        public BigInteger _raftport;

        public String _orgId;
    }

    public static class NodeActivatedEventResponse extends BaseEventResponse {
        public String _enodeId;

        public String _ip;

        public BigInteger _port;

        public BigInteger _raftport;

        public String _orgId;
    }

    public static class NodeBlacklistedEventResponse extends BaseEventResponse {
        public String _enodeId;

        public String _ip;

        public BigInteger _port;

        public BigInteger _raftport;

        public String _orgId;
    }

    public static class NodeRecoveryInitiatedEventResponse extends BaseEventResponse {
        public String _enodeId;

        public String _ip;

        public BigInteger _port;

        public BigInteger _raftport;

        public String _orgId;
    }

    public static class NodeRecoveryCompletedEventResponse extends BaseEventResponse {
        public String _enodeId;

        public String _ip;

        public BigInteger _port;

        public BigInteger _raftport;

        public String _orgId;
    }
}
