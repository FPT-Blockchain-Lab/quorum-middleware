package com.fptblockchainlab.middleware;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

public class LC {
    public static class Content {
        public String rootHash;

        public BigInteger signedTime;

        public String prevHash;

        public int numOfDocuments;

        public String[] contentHash;

        public String url;

        public String acknowledge;

        public String signature;

        public Content(String rootHash, BigInteger signedTime, String prevHash, int numOfDocuments, String[] contentHash, String url, String acknowledge, String signature) {
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

    public static class AmendStage {
        public BigInteger stage;

        public BigInteger subStage;

        public Content content;

        public AmendStage(BigInteger stage, BigInteger subStage, Content content) {
            this.stage = stage;
            this.subStage = subStage;
            this.content = content;
        }
    }

    public static Bytes32 generateAcknowledgeMessageHashBytes(Bytes32[] contentHash, int numOfDocuments) {
        int lastIndex = numOfDocuments + 1;
        return new Bytes32(
            Numeric.hexStringToByteArray(
                Hash.sha3(
                    "0000000000000000000000000000000000000000000000000000000000000020" + // offset in bytes to the start of encoded data
                        Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(numOfDocuments), 64) + // length of encoded array
                        FunctionEncoder.encodeConstructor(
                            Arrays.asList(
                                Arrays.copyOfRange(contentHash, 1, lastIndex)
                            )
                        )
                )
            )
        );
    }

    public static String generateAcknowledgeMessageHash(Bytes32[] contentHash, int numOfDocuments) {
        int lastIndex = numOfDocuments + 1;
        return Hash.sha3(
            "0000000000000000000000000000000000000000000000000000000000000020" + // offset in bytes to the start of encoded data
                Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(numOfDocuments), 64) + // length of encoded array
                FunctionEncoder.encodeConstructor(
                    Arrays.asList(
                        Arrays.copyOfRange(contentHash, 1, lastIndex)
                    )
                )
        );
    }

    public static String generateAcknowledgeMessageHash(String[] contentHash, int numOfDocuments) {
        int lastIndex = numOfDocuments + 1;
        return Hash.sha3(
            "0000000000000000000000000000000000000000000000000000000000000020" + // offset in bytes to the start of encoded data
                Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf(numOfDocuments), 64) + // length of encoded array
                FunctionEncoder.encodeConstructor(
                    Arrays.asList(
                        Arrays.stream(
                                Arrays.copyOfRange(contentHash, 1, lastIndex)
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
        String acknowledgeNoPrefix = stageContent.acknowledge.substring(2, stageContent.acknowledge.length()).trim();
        return
            Hash.sha3(
                FunctionEncoder.encodeConstructor(Arrays.asList(
                    new Bytes32(Numeric.hexStringToByteArray(stageContent.rootHash)),
                    new Bytes32(Numeric.hexStringToByteArray(stageContent.prevHash))
                )) +
                    "00000000000000000000000000000000000000000000000000000000000000c0" + // offset in bytes to the start of contentHash
                    Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf((contentHashLength + 7) * 32)), 64) + // offset in bytes to the start of url
                    Numeric.toHexStringNoPrefixZeroPadded(stageContent.signedTime, 64) +
                    Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((6 + contentHashLength + urlEncoded.length() / 64) * 32L), 64) + // offset in bytes to the start of acknowledge
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
                    acknowledgeNoPrefix + "00000000000000000000000000000000000000000000000000000000000000" // signature 65 bytes -> 96 bytes);
            );
    }

    public static String generateStageHash(LC.Content stageContent) {
        return
            Hash.sha3(
                TypeEncoder.encodePacked(
                    new DynamicArray(
                        new Bytes32(Numeric.hexStringToByteArray(stageContent.rootHash)),
                        new Bytes32(Numeric.hexStringToByteArray(stageContent.prevHash))
                    )
                ) +
                TypeEncoder.encodePacked(
                    new DynamicArray(
                        Arrays.asList(
                            Arrays.stream(stageContent.contentHash)
                                .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                                .toArray(Bytes32[]::new)
                        )
                    )
                ) +
                TypeEncoder.encodePacked(
                    new Utf8String(stageContent.url)
                ) +
                TypeEncoder.encodePacked(
                    new Uint256(stageContent.signedTime)
                ) +  stageContent.signature.trim().substring(2, stageContent.signature.length()).trim()+
                stageContent.acknowledge.trim().substring(2, stageContent.acknowledge.length()).trim()
            );
    }

    public static String generateRequestId(String proposer, BigInteger nonce) {
        return
            Hash.sha3(
            Numeric.toHexStringNoPrefix(Numeric.hexStringToByteArray(proposer)) +
                TypeEncoder.encodePacked(
                    new Uint256(nonce)
                )
            );
    }

    public static String generateAmendMessageHash(String[] migratingStages, AmendStage amendStage) {
        Content stageContent = amendStage.content;
        int contentHashLength = stageContent.contentHash.length;
        List urlList = new ArrayList<Utf8String>();
        urlList.add(new Utf8String(stageContent.url));
        String urlEncoded = FunctionEncoder.encodeConstructor(urlList);
        String acknowledgeNoPrefix = Numeric.toHexStringNoPrefix(Numeric.hexStringToByteArray(stageContent.acknowledge));
        String signatureNoPrefix = Numeric.toHexStringNoPrefix(Numeric.hexStringToByteArray(stageContent.signature));
        String message = Hash.sha3(
                FunctionEncoder.encodeConstructor(Arrays.asList(
                    new Uint256(amendStage.stage),
                    new Uint256(amendStage.subStage),
                    new Bytes32(Numeric.hexStringToByteArray(stageContent.rootHash)),
                    new Bytes32(Numeric.hexStringToByteArray(stageContent.prevHash))
                )) +
                    "0000000000000000000000000000000000000000000000000000000000000120" + // offset in bytes to the start of contentHash
                    Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf((contentHashLength + 10) * 32)), 64) + // offset in bytes to the start of url
                    Numeric.toHexStringNoPrefixZeroPadded(stageContent.signedTime, 64) +
                    Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((9 + contentHashLength + urlEncoded.length() / 64) * 32L), 64) + // offset in bytes to the start of acknowledge signature
                    Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((13 + contentHashLength + urlEncoded.length() / 64) * 32L), 64) + // offset in bytes to the start of acknowledge signature
                    Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf(contentHashLength)), 64) +
                    FunctionEncoder.encodeConstructor(
                        Arrays.asList(
                            Arrays.stream(stageContent.contentHash)
                                .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                                .toArray(Bytes32[]::new)
                        )
                    ) +
                    urlEncoded.substring(64, urlEncoded.length()) + // remove offset
                    "0000000000000000000000000000000000000000000000000000000000000041" + // length of acknowledge signature byte
                    acknowledgeNoPrefix + "00000000000000000000000000000000000000000000000000000000000000" + // acknowledge signature 65 bytes -> 96 bytes);
                    "0000000000000000000000000000000000000000000000000000000000000041" + // length of approval signature byte
                    signatureNoPrefix + "00000000000000000000000000000000000000000000000000000000000000" // approval signature 65 bytes -> 96 bytes);
            );
        
        return Hash.sha3(
        "0000000000000000000000000000000000000000000000000000000000000040" +
            Numeric.toHexStringNoPrefix(Numeric.hexStringToByteArray(message)) +
            Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf(migratingStages.length)), 64) +
            FunctionEncoder.encodeConstructor(
                Arrays.asList(
                    Arrays.stream(migratingStages)
                        .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                        .toArray(Bytes32[]::new)
                )
            )
        );
    }
}