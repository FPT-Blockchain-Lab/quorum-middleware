package com.fptblockchainlab.bindings.lc;

import io.reactivex.Flowable;
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
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
public class LCFactory extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_WRAPPER = "WRAPPER";

    public static final String FUNC_AMEND = "amend";

    public static final String FUNC_CREATE = "create";

    public static final String FUNC_GETLCADDRESS = "getLCAddress";

    public static final String FUNC_MANAGEMENT = "management";

    public static final String FUNC_SETLCMANAGEMENT = "setLCManagement";

    public static final Event NEWLC_EVENT = new Event("NewLC", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected LCFactory(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected LCFactory(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected LCFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected LCFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<NewLCEventResponse> getNewLCEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NEWLC_EVENT, transactionReceipt);
        ArrayList<NewLCEventResponse> responses = new ArrayList<NewLCEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewLCEventResponse typedResponse = new NewLCEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.lcType = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.documentID = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.creator = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.lcContractAddr = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static NewLCEventResponse getNewLCEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(NEWLC_EVENT, log);
        NewLCEventResponse typedResponse = new NewLCEventResponse();
        typedResponse.log = log;
        typedResponse.lcType = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.documentID = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.creator = (String) eventValues.getIndexedValues().get(2).getValue();
        typedResponse.lcContractAddr = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<NewLCEventResponse> newLCEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getNewLCEventFromLog(log));
    }

    public Flowable<NewLCEventResponse> newLCEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWLC_EVENT));
        return newLCEventFlowable(filter);
    }

    public RemoteFunctionCall<String> WRAPPER() {
        final Function function = new Function(FUNC_WRAPPER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> amend(String _executor, BigInteger _documentId, List<String> _parties, BigInteger _lcType) {
        final Function function = new Function(
                FUNC_AMEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _executor), 
                new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_parties, org.web3j.abi.datatypes.Utf8String.class)), 
                new org.web3j.abi.datatypes.generated.Uint8(_lcType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> create(List<String> _parties, Content _content, BigInteger _lcType) {
        final Function function = new Function(
                FUNC_CREATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_parties, org.web3j.abi.datatypes.Utf8String.class)), 
                _content, 
                new org.web3j.abi.datatypes.generated.Uint8(_lcType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getLCAddress(BigInteger _documentId) {
        final Function function = new Function(FUNC_GETLCADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_documentId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
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

    public RemoteFunctionCall<String> management() {
        final Function function = new Function(FUNC_MANAGEMENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setLCManagement(String _management) {
        final Function function = new Function(
                FUNC_SETLCMANAGEMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _management)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static LCFactory load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new LCFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static LCFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new LCFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static LCFactory load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new LCFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static LCFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new LCFactory(contractAddress, web3j, transactionManager, contractGasProvider);
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

    public static class NewLCEventResponse extends BaseEventResponse {
        public BigInteger lcType;

        public BigInteger documentID;

        public String creator;

        public String lcContractAddr;
    }
}
