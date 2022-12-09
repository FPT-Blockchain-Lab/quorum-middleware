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

/**
 * LC protocol
 */
public class LC {

    /**
     * The content of LC contract
     */
    public static class Content {
        public String rootHash;

        /** LC signed time */
        public BigInteger signedTime;

        /** Stage content hash of previous LC contract */
        public String prevHash;

        /** Must be provided in the Stage 1, Stage 4, and Stage 5. Others can be 0 */
        public int numOfDocuments;

        /** The hashes of LC content */
        public String[] contentHash;

        public String url;

        /** Acknowledge signature generated by Management Org for Stage 1, Stage 4, Stage 5 */
        public String acknowledge;

        /** Signature generated by Approver */
        public String signature;

        /**
         * Class constructor
         * @param rootHash
         * @param signedTime LC signed time
         * @param prevHash Stage content hash of previous LC contract
         * @param numOfDocuments Must be provided in the Stage 1, Stage 4, and Stage 5. Others can be 0
         * @param contentHash The hashes of LC content
         * @param url
         * @param acknowledge Acknowledge signature generated by Management Org for Stage 1, Stage 4, Stage 5
         * @param signature Signature generated by Approver
         */
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
        /** From 1 to 6 for Standard LC, from 1 to 7 for UPAS LC */
        public BigInteger stage;

        public BigInteger subStage;

        public Content content;

        public AmendStage(BigInteger stage, BigInteger subStage, Content content) {
            this.stage = stage;
            this.subStage = subStage;
            this.content = content;
        }
    }

    /** The hash of empty bytes */
    public static String DEFAULT_ROOT_HASH = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";

    public static String checkStage(BigInteger stage) {
        String _stage = stage.toString();
        switch (_stage) {
            case "1":
                return "Letter Of Credit";
            case "2":
                return "Presentation Document";
            case "3":
                return "Documentation Result Notification";
            case "4":
                return "LC Payment Acceptance";
            case "5":
                return "LC Issuance Bank To Advising Bank Payment";
            case "6":
                return "LC Advising Bank To Beneficiary Payment";
            case "7":
                return "UpasLC Payment Acceptance";
            default:
                return "";
        }
    }

    /**
     * Compute the hash of content array
     * @param contentHash the hashes of content
     * @param numOfDocuments
     */
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

    /**
     * Compute the hash of content array
     * @param contentHash the hashes of content
     * @param numOfDocuments
     * @return the string represent of bytes32 in heximal
     */
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

    /**
     * Compute the hash of content array
     * @param contentHash the hashes of content
     * @param numOfDocuments
     * @return the string represent of bytes32 in heximal
     */
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

    private static String _encodeSignature(String signature) {
        return signature.equals("0x") ?
            "0000000000000000000000000000000000000000000000000000000000000000" : // signature (0 byte -> 32 bytes)
                "0000000000000000000000000000000000000000000000000000000000000041" + // length of acknowledge signature byte
                    signature.trim().substring(2, signature.length()).trim() + "00000000000000000000000000000000000000000000000000000000000000"; // signature (65 bytes -> 96 bytes)
    }

    /**
     * Compute the hash of LC content without `numOfDocuments` and `approvalSignature`
     * @param stageContent LC content
     * @return the string represent of bytes32 in heximal
     */
    public static String generateApprovalMessageHash(LC.Content stageContent) {
        int contentHashLength = stageContent.contentHash.length;
        List urlList = new ArrayList<Utf8String>();
        urlList.add(new Utf8String(stageContent.url));
        String urlEncoded = FunctionEncoder.encodeConstructor(urlList);
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
                    _encodeSignature(stageContent.acknowledge) // signature (65 bytes -> 96 bytes, 0 byte - 32 bytes)
            );
    }

    /**
     * Compute the hash of LC content without `numOfDocuments`
     * @param stageContent LC content
     * @return the string represent of bytes32 in heximal
     */
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

    /**
     * @param proposer address of proposer
     * @param nonce
     * @return the string represent of bytes32 in heximal
     */
    public static String generateRequestId(String proposer, BigInteger nonce) {
        return
            Hash.sha3(
            Numeric.toHexStringNoPrefix(Numeric.hexStringToByteArray(proposer)) +
                TypeEncoder.encodePacked(
                    new Uint256(nonce)
                )
            );
    }

    /**
     * Compute the hash of migrating stages and amend stage
     * @param migratingStages the hashes of migrating stages after amend (order by approved time)
     * @param amendStage
     * @return the string represent of bytes32 in heximal
     */
    public static String generateAmendMessageHash(String[] migratingStages, AmendStage amendStage) {
        Content stageContent = amendStage.content;
        int contentHashLength = stageContent.contentHash.length;
        List urlList = new ArrayList<Utf8String>();
        urlList.add(new Utf8String(stageContent.url));
        String urlEncoded = FunctionEncoder.encodeConstructor(urlList);
        String encodeAcknowledge = _encodeSignature(stageContent.acknowledge);
        int offsetAcknowledge = 9 + contentHashLength + urlEncoded.length() / 64;
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
                    Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((offsetAcknowledge) * 32L), 64) + // offset in bytes to the start of acknowledge signature
                    Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((offsetAcknowledge + encodeAcknowledge.length() / 64 ) * 32L), 64) + // offset in bytes to the start of approval signature
                    Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf(contentHashLength)), 64) +
                    FunctionEncoder.encodeConstructor(
                        Arrays.asList(
                            Arrays.stream(stageContent.contentHash)
                                .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                                .toArray(Bytes32[]::new)
                        )
                    ) +
                    urlEncoded.substring(64, urlEncoded.length()) + // remove offset
                    _encodeSignature(stageContent.acknowledge) + // signature (65 bytes -> 96 bytes, 0 byte - 32 bytes)
                    _encodeSignature(stageContent.signature) // approval signature (65 bytes -> 96 bytes, 0 byte - 32 bytes)
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