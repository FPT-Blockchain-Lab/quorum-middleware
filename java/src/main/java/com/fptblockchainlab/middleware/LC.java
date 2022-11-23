package middleware;

import java.math.BigInteger;
import java.lang.RuntimeException;
import com.fptblockchainlab.bindings.lc.RouterService;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;

public class LC {
    public static class Content {
        public String rootHash;

        public BigInteger signedTime;

        public String prevHash;

        public BigInteger numOfDocuments;

        public String[] contentHash;

        public String url;

        public String[] acknowledge;

        public String[] signature;

        public Content(String rootHash, BigInteger signedTime, String prevHash, BigInteger numOfDocuments, DynamicArray<String> contentHash, String url, String acknowledge, String signature) {
            super(rootHash, signedTime, prevHash, numOfDocuments, contentHash, url, acknowledge, signature);
            this.rootHash = rootHash;
            this.signedTime = signedTime;
            this.prevHash = prevHash;
            this.numOfDocuments = numOfDocuments;
            this.contentHash = contentHash.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.url = url;
            this.acknowledge = acknowledge;
            this.signature = signature;
        }
    }

    public static Bytes32 generateAcknowledgeMessageHash(Bytes32[] contentHash) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static String generateAcknowledgeMessageHashAsHexString(String[] contentHash) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static Bytes32 generateApprovalMessageHash(RouterService.Content stageContent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static String generateApprovalMessageHash(LC.Content stageContent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}