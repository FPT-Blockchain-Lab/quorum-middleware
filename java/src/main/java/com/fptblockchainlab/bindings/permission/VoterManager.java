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
public class VoterManager extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_GETPENDINGOPDETAILS = "getPendingOpDetails";

    public static final String FUNC_ADDVOTER = "addVoter";

    public static final String FUNC_DELETEVOTER = "deleteVoter";

    public static final String FUNC_PROCESSVOTE = "processVote";

    public static final String FUNC_ADDVOTINGITEM = "addVotingItem";

    public static final Event VOTERADDED_EVENT = new Event("VoterAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event VOTERDELETED_EVENT = new Event("VoterDeleted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event VOTINGITEMADDED_EVENT = new Event("VotingItemAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event VOTEPROCESSED_EVENT = new Event("VoteProcessed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected VoterManager(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VoterManager(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VoterManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VoterManager(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Tuple4<String, String, String, BigInteger>> getPendingOpDetails(String _orgId) {
        final Function function = new Function(FUNC_GETPENDINGOPDETAILS, 
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

    public RemoteFunctionCall<TransactionReceipt> addVoter(String _orgId, String _vAccount) {
        final Function function = new Function(
                FUNC_ADDVOTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _vAccount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteVoter(String _orgId, String _vAccount) {
        final Function function = new Function(
                FUNC_DELETEVOTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Address(160, _vAccount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> processVote(String _authOrg, String _vAccount, BigInteger _pendingOp) {
        final Function function = new Function(
                FUNC_PROCESSVOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_authOrg), 
                new org.web3j.abi.datatypes.Address(160, _vAccount), 
                new org.web3j.abi.datatypes.generated.Uint256(_pendingOp)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addVotingItem(String _authOrg, String _orgId, String _enodeId, String _account, BigInteger _pendingOp) {
        final Function function = new Function(
                FUNC_ADDVOTINGITEM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_authOrg), 
                new org.web3j.abi.datatypes.Utf8String(_orgId), 
                new org.web3j.abi.datatypes.Utf8String(_enodeId), 
                new org.web3j.abi.datatypes.Address(160, _account), 
                new org.web3j.abi.datatypes.generated.Uint256(_pendingOp)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static List<VoterAddedEventResponse> getVoterAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTERADDED_EVENT, transactionReceipt);
        ArrayList<VoterAddedEventResponse> responses = new ArrayList<VoterAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoterAddedEventResponse typedResponse = new VoterAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._vAccount = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoterAddedEventResponse> voterAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, VoterAddedEventResponse>() {
            @Override
            public VoterAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTERADDED_EVENT, log);
                VoterAddedEventResponse typedResponse = new VoterAddedEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._vAccount = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoterAddedEventResponse> voterAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTERADDED_EVENT));
        return voterAddedEventFlowable(filter);
    }

    public static List<VoterDeletedEventResponse> getVoterDeletedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTERDELETED_EVENT, transactionReceipt);
        ArrayList<VoterDeletedEventResponse> responses = new ArrayList<VoterDeletedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoterDeletedEventResponse typedResponse = new VoterDeletedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._vAccount = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoterDeletedEventResponse> voterDeletedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, VoterDeletedEventResponse>() {
            @Override
            public VoterDeletedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTERDELETED_EVENT, log);
                VoterDeletedEventResponse typedResponse = new VoterDeletedEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._vAccount = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoterDeletedEventResponse> voterDeletedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTERDELETED_EVENT));
        return voterDeletedEventFlowable(filter);
    }

    public static List<VotingItemAddedEventResponse> getVotingItemAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTINGITEMADDED_EVENT, transactionReceipt);
        ArrayList<VotingItemAddedEventResponse> responses = new ArrayList<VotingItemAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotingItemAddedEventResponse typedResponse = new VotingItemAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VotingItemAddedEventResponse> votingItemAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, VotingItemAddedEventResponse>() {
            @Override
            public VotingItemAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTINGITEMADDED_EVENT, log);
                VotingItemAddedEventResponse typedResponse = new VotingItemAddedEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VotingItemAddedEventResponse> votingItemAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTINGITEMADDED_EVENT));
        return votingItemAddedEventFlowable(filter);
    }

    public static List<VoteProcessedEventResponse> getVoteProcessedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTEPROCESSED_EVENT, transactionReceipt);
        ArrayList<VoteProcessedEventResponse> responses = new ArrayList<VoteProcessedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteProcessedEventResponse typedResponse = new VoteProcessedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteProcessedEventResponse> voteProcessedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, VoteProcessedEventResponse>() {
            @Override
            public VoteProcessedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTEPROCESSED_EVENT, log);
                VoteProcessedEventResponse typedResponse = new VoteProcessedEventResponse();
                typedResponse.log = log;
                typedResponse._orgId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<VoteProcessedEventResponse> voteProcessedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTEPROCESSED_EVENT));
        return voteProcessedEventFlowable(filter);
    }

    @Deprecated
    public static VoterManager load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new VoterManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VoterManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VoterManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VoterManager load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new VoterManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VoterManager load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VoterManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class VoterAddedEventResponse extends BaseEventResponse {
        public String _orgId;

        public String _vAccount;
    }

    public static class VoterDeletedEventResponse extends BaseEventResponse {
        public String _orgId;

        public String _vAccount;
    }

    public static class VotingItemAddedEventResponse extends BaseEventResponse {
        public String _orgId;
    }

    public static class VoteProcessedEventResponse extends BaseEventResponse {
        public String _orgId;
    }
}
