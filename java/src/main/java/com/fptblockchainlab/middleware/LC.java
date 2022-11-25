package com.fptblockchainlab.middleware;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

public class LC {
    public static class Content {
        public String rootHash;

        public BigInteger signedTime;

        public String prevHash;

        public BigInteger numOfDocuments;

        public String[] contentHash;

        public String url;

        public String acknowledge;

        public String signature;

        public Content(String rootHash, BigInteger signedTime, String prevHash, BigInteger numOfDocuments, String[] contentHash, String url, String acknowledge, String signature) {
            this.rootHash = rootHash;
            this.signedTime = signedTime;
            this.prevHash = prevHash;
            this.numOfDocuments = numOfDocuments;
            this.contentHash = contentHash;
            this.url = url;
            this.acknowledge = acknowledge;
            this.signature = signature;
        }
    }

    public static Bytes32 generateAcknowledgeMessageHashBytes(Bytes32[] contentHash, BigInteger numOfDocuments) {
        int length = numOfDocuments.intValue() + 1;
        return new Bytes32(
            Numeric.hexStringToByteArray(
                Hash.sha3(
                    "0000000000000000000000000000000000000000000000000000000000000020" + // offset in bytes to the start of encoded data
                        Numeric.toHexStringNoPrefixZeroPadded(numOfDocuments, 64) + // length of encoded array
                        FunctionEncoder.encodeConstructor(
                            Arrays.asList(
                                Arrays.copyOfRange(contentHash, 1, length)
                            )
                        )
                )
            )
        );
    }

    public static String generateAcknowledgeMessageHash(Bytes32[] contentHash, BigInteger numOfDocuments) {
        int length = numOfDocuments.intValue() + 1;
        return Hash.sha3(
            "0000000000000000000000000000000000000000000000000000000000000020" + // offset in bytes to the start of encoded data
                Numeric.toHexStringNoPrefixZeroPadded(numOfDocuments, 64) + // length of encoded array
                FunctionEncoder.encodeConstructor(
                    Arrays.asList(
                            Arrays.copyOfRange(contentHash, 1, length)
                        )
                    )
            );
    }

    public static String generateAcknowledgeMessageHash(String[] contentHash, BigInteger numOfDocuments) {
        int length = numOfDocuments.intValue() + 1;
        return Hash.sha3(
            "0000000000000000000000000000000000000000000000000000000000000020" + // offset in bytes to the start of encoded data
                Numeric.toHexStringNoPrefixZeroPadded(numOfDocuments, 64) + // length of encoded array
                FunctionEncoder.encodeConstructor(
                    Arrays.asList(
                        Arrays.stream(
                                Arrays.copyOfRange(contentHash, 1, length)
                            )
                            .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                            .toArray(Bytes32[]::new)
                    )
                )
        );
    }

    public static String generateApprovalMessageHash(LC.Content stageContent) {
        int contentHashLength = stageContent.contentHash.length;
        List urlList = new ArrayList<Utf8String>();
        urlList.add(new Utf8String(stageContent.url));
        String urlEncoded = FunctionEncoder.encodeConstructor(urlList);
        List acknowledgeList = new ArrayList<String>();
        acknowledgeList.add(Numeric.toHexStringNoPrefix(Numeric.hexStringToByteArray(stageContent.acknowledge)));
        String acknowledgeNoPrefix = stageContent.acknowledge.substring(2, stageContent.acknowledge.length());
        return
            Hash.sha3(
                FunctionEncoder.encodeConstructor(Arrays.asList(
                    new Bytes32(Numeric.hexStringToByteArray(stageContent.rootHash)),
                    new Bytes32(Numeric.hexStringToByteArray(stageContent.prevHash))
                )) +
                    "00000000000000000000000000000000000000000000000000000000000000c0" + // offset in bytes to the start of contentHash
                    "0000000000000000000000000000000000000000000000000000000000000180" + // offset in bytes to the start of url
                    Numeric.toHexStringNoPrefixZeroPadded(stageContent.signedTime, 64) +
                    "0000000000000000000000000000000000000000000000000000000000000220" + // offset in bytes to the start of acknowledge
                    Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf(contentHashLength)), 64) +
                    FunctionEncoder.encodeConstructor(
                        Arrays.asList(
                            Arrays.stream(stageContent.contentHash)
                                .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                                .toArray(Bytes32[]::new)
                        )
                    ) +
                    urlEncoded.substring(64, urlEncoded.length()) + // remove offset
                    "0000000000000000000000000000000000000000000000000000000000000041" + // length of acknowledge byte
                    acknowledgeNoPrefix + "00000000000000000000000000000000000000000000000000000000000000" // signature 65 bytes -> 96 bytes
            );
    }
}