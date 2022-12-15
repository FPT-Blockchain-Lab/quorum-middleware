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
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
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
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class StandardLCFactory extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_STANDARD_WRAPPER = "STANDARD_WRAPPER";

    public static final String FUNC_AMEND = "amend";

    public static final String FUNC_CREATE = "create";

    public static final String FUNC_GETLCADDRESS = "getLCAddress";

    public static final String FUNC_MANAGEMENT = "management";

    public static final String FUNC_SETLCMANAGEMENT = "setLCManagement";

    public static final Event NEWSTANDARDLC_EVENT = new Event("NewStandardLC", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected StandardLCFactory(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected StandardLCFactory(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected StandardLCFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected StandardLCFactory(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<NewStandardLCEventResponse> getNewStandardLCEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(NEWSTANDARDLC_EVENT, transactionReceipt);
        ArrayList<NewStandardLCEventResponse> responses = new ArrayList<NewStandardLCEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewStandardLCEventResponse typedResponse = new NewStandardLCEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.documentID = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.creator = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.lcContractAddr = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewStandardLCEventResponse> newStandardLCEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewStandardLCEventResponse>() {
            @Override
            public NewStandardLCEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWSTANDARDLC_EVENT, log);
                NewStandardLCEventResponse typedResponse = new NewStandardLCEventResponse();
                typedResponse.log = log;
                typedResponse.documentID = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.creator = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.lcContractAddr = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewStandardLCEventResponse> newStandardLCEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWSTANDARDLC_EVENT));
        return newStandardLCEventFlowable(filter);
    }

    public RemoteFunctionCall<String> STANDARD_WRAPPER() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_STANDARD_WRAPPER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> amend(String _executor, BigInteger _documentId, List<String> _parties) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_AMEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _executor), 
                new org.web3j.abi.datatypes.generated.Uint256(_documentId), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_parties, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> create(List<String> _parties, Content _content) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_parties, org.web3j.abi.datatypes.Utf8String.class)), 
                _content), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getLCAddress(BigInteger _documentId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETLCADDRESS, 
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
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MANAGEMENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setLCManagement(String _management) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETLCMANAGEMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _management)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static StandardLCFactory load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new StandardLCFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static StandardLCFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new StandardLCFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static StandardLCFactory load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new StandardLCFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static StandardLCFactory load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new StandardLCFactory(contractAddress, web3j, transactionManager, contractGasProvider);
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

    public static class NewStandardLCEventResponse extends BaseEventResponse {
        public BigInteger documentID;

        public String creator;

        public String lcContractAddr;
    }
}
