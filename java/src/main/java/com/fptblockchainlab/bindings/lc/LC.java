package com.fptblockchainlab.bindings.lc;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
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
public class LC extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC__HASHTOSTAGE = "_hashToStage";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC_AMEND = "amend";

    public static final String FUNC_AMENDED = "amended";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_CHECKPROPOSER = "checkProposer";

    public static final String FUNC_CLOSE = "close";

    public static final String FUNC_FACTORY = "factory";

    public static final String FUNC_GETCONTENT = "getContent";

    public static final String FUNC_GETCOUNTER = "getCounter";

    public static final String FUNC_GETINVOLVEDPARTIES = "getInvolvedParties";

    public static final String FUNC_GETMIGRATEINFO = "getMigrateInfo";

    public static final String FUNC_GETROOTHASH = "getRootHash";

    public static final String FUNC_GETROOTLIST = "getRootList";

    public static final String FUNC_GETSTATUS = "getStatus";

    public static final String FUNC_HASHTOSTAGE = "hashToStage";

    public static final String FUNC_ISCLOSED = "isClosed";

    public static final String FUNC_NUMOFSUBSTAGE = "numOfSubStage";

    public static final String FUNC_SETCOUNTER = "setCounter";

    public static final Event APPROVED_EVENT = new Event("Approved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected LC(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LC(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LC(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LC(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovedEventResponse> getApprovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVED_EVENT, transactionReceipt);
        ArrayList<ApprovedEventResponse> responses = new ArrayList<ApprovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovedEventResponse typedResponse = new ApprovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.caller = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.documentID = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.stage = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.subStage = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.approvedTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.organization = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovedEventResponse> approvedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovedEventResponse>() {
            @Override
            public ApprovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVED_EVENT, log);
                ApprovedEventResponse typedResponse = new ApprovedEventResponse();
                typedResponse.log = log;
                typedResponse.caller = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.documentID = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.stage = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.subStage = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.approvedTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.organization = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovedEventResponse> approvedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVED_EVENT));
        return approvedEventFlowable(filter);
    }

    public RemoteFunctionCall<Tuple2<BigInteger, BigInteger>> _hashToStage(byte[] param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__HASHTOSTAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<BigInteger, BigInteger>>(function,
                new Callable<Tuple2<BigInteger, BigInteger>>() {
                    @Override
                    public Tuple2<BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> _owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC__OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> amend() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_AMEND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> amended() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_AMENDED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String _caller, BigInteger _stage, BigInteger _subStage, Content _content) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _caller), 
                new org.web3j.abi.datatypes.generated.Uint256(_stage), 
                new org.web3j.abi.datatypes.generated.Uint256(_subStage), 
                _content), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> checkProposer(String _proposer, BigInteger _stage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CHECKPROPOSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _proposer), 
                new org.web3j.abi.datatypes.generated.Uint256(_stage)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> close() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CLOSE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> factory() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FACTORY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Content> getContent(BigInteger _stage, BigInteger _subStage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCONTENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_stage), 
                new org.web3j.abi.datatypes.generated.Uint256(_subStage)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Content>() {}));
        return executeRemoteCallSingleValueReturn(function, Content.class);
    }

    public RemoteFunctionCall<BigInteger> getCounter() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCOUNTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> getInvolvedParties() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETINVOLVEDPARTIES, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
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

    public RemoteFunctionCall<Tuple2<List<Stage>, List<Pack>>> getMigrateInfo(List<byte[]> _hashes) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETMIGRATEINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.datatypes.generated.Bytes32.class,
                        org.web3j.abi.Utils.typeMap(_hashes, org.web3j.abi.datatypes.generated.Bytes32.class))), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Stage>>() {}, new TypeReference<DynamicArray<Pack>>() {}));
        return new RemoteFunctionCall<Tuple2<List<Stage>, List<Pack>>>(function,
                new Callable<Tuple2<List<Stage>, List<Pack>>>() {
                    @Override
                    public Tuple2<List<Stage>, List<Pack>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<List<Stage>, List<Pack>>(
                                convertToNative((List<Stage>) results.get(0).getValue()), 
                                convertToNative((List<Pack>) results.get(1).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<byte[]> getRootHash() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROOTHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<List> getRootList() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROOTLIST, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Bytes32>>() {}));
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

    public RemoteFunctionCall<List> getStatus() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTATUS, 
                Arrays.<Type>asList(), 
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

    public RemoteFunctionCall<Stage> hashToStage(byte[] _hash) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_HASHTOSTAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Stage>() {}));
        return executeRemoteCallSingleValueReturn(function, Stage.class);
    }

    public RemoteFunctionCall<Boolean> isClosed() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISCLOSED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> numOfSubStage(BigInteger _stage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NUMOFSUBSTAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_stage)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setCounter(BigInteger _newValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCOUNTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_newValue)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static LC load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LC(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LC load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LC(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LC load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LC(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LC load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LC(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class Content extends DynamicStruct {
        public byte[] rootHash;

        public BigInteger signedTime;

        public byte[] prevHash;

        public BigInteger numOfDocuments;

        public List<byte[]> contentHash;

        public String url;

        public byte[] acknowledge;

        public byte[] signature;

        public Content(byte[] rootHash, BigInteger signedTime, byte[] prevHash, BigInteger numOfDocuments, List<byte[]> contentHash, String url, byte[] acknowledge, byte[] signature) {
            super(new org.web3j.abi.datatypes.generated.Bytes32(rootHash), 
                    new org.web3j.abi.datatypes.generated.Uint256(signedTime), 
                    new org.web3j.abi.datatypes.generated.Bytes32(prevHash), 
                    new org.web3j.abi.datatypes.generated.Uint256(numOfDocuments), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                            org.web3j.abi.datatypes.generated.Bytes32.class,
                            org.web3j.abi.Utils.typeMap(contentHash, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                    new org.web3j.abi.datatypes.Utf8String(url), 
                    new org.web3j.abi.datatypes.DynamicBytes(acknowledge), 
                    new org.web3j.abi.datatypes.DynamicBytes(signature));
            this.rootHash = rootHash;
            this.signedTime = signedTime;
            this.prevHash = prevHash;
            this.numOfDocuments = numOfDocuments;
            this.contentHash = contentHash;
            this.url = url;
            this.acknowledge = acknowledge;
            this.signature = signature;
        }

        public Content(Bytes32 rootHash, Uint256 signedTime, Bytes32 prevHash, Uint256 numOfDocuments, DynamicArray<Bytes32> contentHash, Utf8String url, DynamicBytes acknowledge, DynamicBytes signature) {
            super(rootHash, signedTime, prevHash, numOfDocuments, contentHash, url, acknowledge, signature);
            this.rootHash = rootHash.getValue();
            this.signedTime = signedTime.getValue();
            this.prevHash = prevHash.getValue();
            this.numOfDocuments = numOfDocuments.getValue();
            this.contentHash = contentHash.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.url = url.getValue();
            this.acknowledge = acknowledge.getValue();
            this.signature = signature.getValue();
        }
    }

    public static class Stage extends StaticStruct {
        public BigInteger stage;

        public BigInteger subStage;

        public Stage(BigInteger stage, BigInteger subStage) {
            super(new org.web3j.abi.datatypes.generated.Uint256(stage), 
                    new org.web3j.abi.datatypes.generated.Uint256(subStage));
            this.stage = stage;
            this.subStage = subStage;
        }

        public Stage(Uint256 stage, Uint256 subStage) {
            super(stage, subStage);
            this.stage = stage.getValue();
            this.subStage = subStage.getValue();
        }
    }

    public static class Pack extends DynamicStruct {
        public String sender;

        public Content content;

        public Pack(String sender, Content content) {
            super(new org.web3j.abi.datatypes.Address(160, sender), 
                    content);
            this.sender = sender;
            this.content = content;
        }

        public Pack(Address sender, Content content) {
            super(sender, content);
            this.sender = sender.getValue();
            this.content = content;
        }
    }

    public static class ApprovedEventResponse extends BaseEventResponse {
        public String caller;

        public BigInteger documentID;

        public BigInteger stage;

        public BigInteger subStage;

        public BigInteger approvedTime;

        public String organization;
    }
}
