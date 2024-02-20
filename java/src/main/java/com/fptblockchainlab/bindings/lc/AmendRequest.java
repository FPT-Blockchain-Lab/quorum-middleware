package com.fptblockchainlab.bindings.lc;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
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
 * <p>Generated with web3j version 1.5.0.
 */
@SuppressWarnings("rawtypes")
public class AmendRequest extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_FULFILLED = "fulfilled";

    public static final String FUNC_GETAMENDREQUEST = "getAmendRequest";

    public static final String FUNC_ISAPPROVED = "isApproved";

    public static final String FUNC_ISFULFILLED = "isFulfilled";

    public static final String FUNC_ISPROPOSER = "isProposer";

    public static final String FUNC_MANAGEMENT = "management";

    public static final String FUNC_NONCES = "nonces";

    public static final String FUNC_SETLCMANAGEMENT = "setLCManagement";

    public static final String FUNC_SUBMIT = "submit";

    public static final Event APPROVEDAMENDMENT_EVENT = new Event("ApprovedAmendment", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event SUBMITTEDAMENDMENT_EVENT = new Event("SubmittedAmendment", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected AmendRequest(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AmendRequest(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AmendRequest(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AmendRequest(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ApprovedAmendmentEventResponse> getApprovedAmendmentEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(APPROVEDAMENDMENT_EVENT, transactionReceipt);
        ArrayList<ApprovedAmendmentEventResponse> responses = new ArrayList<ApprovedAmendmentEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovedAmendmentEventResponse typedResponse = new ApprovedAmendmentEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.documentId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.requestId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.approver = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ApprovedAmendmentEventResponse getApprovedAmendmentEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(APPROVEDAMENDMENT_EVENT, log);
        ApprovedAmendmentEventResponse typedResponse = new ApprovedAmendmentEventResponse();
        typedResponse.log = log;
        typedResponse.documentId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.requestId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.approver = (String) eventValues.getIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<ApprovedAmendmentEventResponse> approvedAmendmentEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getApprovedAmendmentEventFromLog(log));
    }

    public Flowable<ApprovedAmendmentEventResponse> approvedAmendmentEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVEDAMENDMENT_EVENT));
        return approvedAmendmentEventFlowable(filter);
    }

    public static List<SubmittedAmendmentEventResponse> getSubmittedAmendmentEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SUBMITTEDAMENDMENT_EVENT, transactionReceipt);
        ArrayList<SubmittedAmendmentEventResponse> responses = new ArrayList<SubmittedAmendmentEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SubmittedAmendmentEventResponse typedResponse = new SubmittedAmendmentEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proposer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.documentId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.nonce = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.requestId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SubmittedAmendmentEventResponse getSubmittedAmendmentEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SUBMITTEDAMENDMENT_EVENT, log);
        SubmittedAmendmentEventResponse typedResponse = new SubmittedAmendmentEventResponse();
        typedResponse.log = log;
        typedResponse.proposer = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.documentId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.nonce = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.requestId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<SubmittedAmendmentEventResponse> submittedAmendmentEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getSubmittedAmendmentEventFromLog(log));
    }

    public Flowable<SubmittedAmendmentEventResponse> submittedAmendmentEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SUBMITTEDAMENDMENT_EVENT));
        return submittedAmendmentEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(BigInteger _documentId, BigInteger _requestId, String _approver, byte[] _signature) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_requestId), 
                new org.web3j.abi.datatypes.Address(160, _approver), 
                new org.web3j.abi.datatypes.DynamicBytes(_signature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> fulfilled(BigInteger _documentId, BigInteger _requestId) {
        final Function function = new Function(
                FUNC_FULFILLED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_requestId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Request> getAmendRequest(BigInteger _documentId, BigInteger _requestId) {
        final Function function = new Function(FUNC_GETAMENDREQUEST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_requestId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Request>() {}));
        return executeRemoteCallSingleValueReturn(function, Request.class);
    }

    public RemoteFunctionCall<Boolean> isApproved(BigInteger _documentId, BigInteger _requestId) {
        final Function function = new Function(FUNC_ISAPPROVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_requestId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isFulfilled(BigInteger _documentId, BigInteger _requestId) {
        final Function function = new Function(FUNC_ISFULFILLED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_requestId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isProposer(BigInteger _documentId, BigInteger _requestId, String _executor) {
        final Function function = new Function(FUNC_ISPROPOSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_requestId), 
                new org.web3j.abi.datatypes.Address(160, _executor)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> management() {
        final Function function = new Function(FUNC_MANAGEMENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> nonces(String param0) {
        final Function function = new Function(FUNC_NONCES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setLCManagement(String _management) {
        final Function function = new Function(
                FUNC_SETLCMANAGEMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _management)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> submit(BigInteger _documentId, String _proposer, List<byte[]> _migratingStages, AmendStage _amendStage, byte[] _signature) {
        final Function function = new Function(
                FUNC_SUBMIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.Address(160, _proposer), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.datatypes.generated.Bytes32.class,
                        org.web3j.abi.Utils.typeMap(_migratingStages, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                _amendStage, 
                new org.web3j.abi.datatypes.DynamicBytes(_signature)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static AmendRequest load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AmendRequest(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AmendRequest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AmendRequest(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AmendRequest load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AmendRequest(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AmendRequest load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AmendRequest(contractAddress, web3j, transactionManager, contractGasProvider);
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

    public static class Confirmation extends DynamicStruct {
        public String issuingBank;

        public String advisingBank;

        public String reimbursingBank;

        public byte[] issuingBankSig;

        public byte[] advisingBankSig;

        public byte[] reimbursingBankSig;

        public Confirmation(String issuingBank, String advisingBank, String reimbursingBank, byte[] issuingBankSig, byte[] advisingBankSig, byte[] reimbursingBankSig) {
            super(new org.web3j.abi.datatypes.Utf8String(issuingBank), 
                    new org.web3j.abi.datatypes.Utf8String(advisingBank), 
                    new org.web3j.abi.datatypes.Utf8String(reimbursingBank), 
                    new org.web3j.abi.datatypes.DynamicBytes(issuingBankSig), 
                    new org.web3j.abi.datatypes.DynamicBytes(advisingBankSig), 
                    new org.web3j.abi.datatypes.DynamicBytes(reimbursingBankSig));
            this.issuingBank = issuingBank;
            this.advisingBank = advisingBank;
            this.reimbursingBank = reimbursingBank;
            this.issuingBankSig = issuingBankSig;
            this.advisingBankSig = advisingBankSig;
            this.reimbursingBankSig = reimbursingBankSig;
        }

        public Confirmation(Utf8String issuingBank, Utf8String advisingBank, Utf8String reimbursingBank, DynamicBytes issuingBankSig, DynamicBytes advisingBankSig, DynamicBytes reimbursingBankSig) {
            super(issuingBank, advisingBank, reimbursingBank, issuingBankSig, advisingBankSig, reimbursingBankSig);
            this.issuingBank = issuingBank.getValue();
            this.advisingBank = advisingBank.getValue();
            this.reimbursingBank = reimbursingBank.getValue();
            this.issuingBankSig = issuingBankSig.getValue();
            this.advisingBankSig = advisingBankSig.getValue();
            this.reimbursingBankSig = reimbursingBankSig.getValue();
        }
    }

    public static class AmendStage extends DynamicStruct {
        public BigInteger stage;

        public BigInteger subStage;

        public Content content;

        public AmendStage(BigInteger stage, BigInteger subStage, Content content) {
            super(new org.web3j.abi.datatypes.generated.Uint256(stage), 
                    new org.web3j.abi.datatypes.generated.Uint256(subStage), 
                    content);
            this.stage = stage;
            this.subStage = subStage;
            this.content = content;
        }

        public AmendStage(Uint256 stage, Uint256 subStage, Content content) {
            super(stage, subStage, content);
            this.stage = stage.getValue();
            this.subStage = subStage.getValue();
            this.content = content;
        }
    }

    public static class Request extends DynamicStruct {
        public BigInteger typeOf;

        public String proposer;

        public List<byte[]> migratingStages;

        public AmendStage amendStage;

        public Confirmation confirmed;

        public Boolean isFulfilled;

        public Request(BigInteger typeOf, String proposer, List<byte[]> migratingStages, AmendStage amendStage, Confirmation confirmed, Boolean isFulfilled) {
            super(new org.web3j.abi.datatypes.generated.Uint256(typeOf), 
                    new org.web3j.abi.datatypes.Address(160, proposer), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                            org.web3j.abi.datatypes.generated.Bytes32.class,
                            org.web3j.abi.Utils.typeMap(migratingStages, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                    amendStage, 
                    confirmed, 
                    new org.web3j.abi.datatypes.Bool(isFulfilled));
            this.typeOf = typeOf;
            this.proposer = proposer;
            this.migratingStages = migratingStages;
            this.amendStage = amendStage;
            this.confirmed = confirmed;
            this.isFulfilled = isFulfilled;
        }

        public Request(Uint256 typeOf, Address proposer, DynamicArray<Bytes32> migratingStages, AmendStage amendStage, Confirmation confirmed, Bool isFulfilled) {
            super(typeOf, proposer, migratingStages, amendStage, confirmed, isFulfilled);
            this.typeOf = typeOf.getValue();
            this.proposer = proposer.getValue();
            this.migratingStages = migratingStages.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.amendStage = amendStage;
            this.confirmed = confirmed;
            this.isFulfilled = isFulfilled.getValue();
        }
    }

    public static class ApprovedAmendmentEventResponse extends BaseEventResponse {
        public BigInteger documentId;

        public BigInteger requestId;

        public String approver;
    }

    public static class SubmittedAmendmentEventResponse extends BaseEventResponse {
        public String proposer;

        public BigInteger documentId;

        public BigInteger nonce;

        public BigInteger requestId;
    }
}
