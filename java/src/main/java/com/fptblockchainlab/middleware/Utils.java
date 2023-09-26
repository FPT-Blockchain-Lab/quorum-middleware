package com.fptblockchainlab.middleware;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static String signMessage(String messageHash, Credentials credentials) {
        byte[] messageBytes = Numeric.hexStringToByteArray(messageHash);
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(messageBytes, credentials.getEcKeyPair());
        byte[] signature = new byte[65];

        System.arraycopy(signatureData.getR(), 0, signature, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signature, 32, 32);
        System.arraycopy(signatureData.getV(), 0, signature, 64, 1);

        return Numeric.toHexString(signature);
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

    private static String _encodeSignature(String signature) {
        return signature.equals("0x") ?
                "0000000000000000000000000000000000000000000000000000000000000000" : // signature (0 byte -> 32 bytes)
                "0000000000000000000000000000000000000000000000000000000000000041" + // length of acknowledge signature byte
                        signature.trim().substring(2, signature.length()).trim() + "00000000000000000000000000000000000000000000000000000000000000"; // signature (65 bytes -> 96 bytes)
    }

    public static String generateApprovalMessageHash(
            String[] contentHash,
            String url,
            String rootHash,
            String prevHash,
            BigInteger signedTime,
            String acknowledge
    ) {
        int contentHashLength = contentHash.length;
        List urlList = new ArrayList<Utf8String>();
        urlList.add(new Utf8String(url));
        String urlEncoded = FunctionEncoder.encodeConstructor(urlList);
        return
                Hash.sha3(
                        FunctionEncoder.encodeConstructor(Arrays.asList(
                                new Bytes32(Numeric.hexStringToByteArray(rootHash)),
                                new Bytes32(Numeric.hexStringToByteArray(prevHash))
                        )) +
                                "00000000000000000000000000000000000000000000000000000000000000c0" + // offset in bytes to the start of contentHash
                                Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf((contentHashLength + 7) * 32)), 64) + // offset in bytes to the start of url
                                Numeric.toHexStringNoPrefixZeroPadded(signedTime, 64) +
                                Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((6 + contentHashLength + urlEncoded.length() / 64) * 32L), 64) + // offset in bytes to the start of acknowledge
                                Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf(contentHashLength)), 64) +
                                FunctionEncoder.encodeConstructor(
                                        Arrays.asList(
                                                Arrays.stream(contentHash)
                                                        .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                                                        .toArray(Bytes32[]::new)
                                        )
                                ) +
                                urlEncoded.substring(64, urlEncoded.length()) + // remove offset
                                _encodeSignature(acknowledge) // signature (65 bytes -> 96 bytes, 0 byte - 32 bytes)
                );
    }

    public static String generateStageHash(
            String[] contentHash,
            String url,
            String rootHash,
            String prevHash,
            BigInteger signedTime,
            String acknowledge,
            String signature
    ) {
        return
                Hash.sha3(
                        TypeEncoder.encodePacked(
                                new DynamicArray(
                                        new Bytes32(Numeric.hexStringToByteArray(rootHash)),
                                        new Bytes32(Numeric.hexStringToByteArray(prevHash))
                                )
                        ) +
                                TypeEncoder.encodePacked(
                                        new DynamicArray(
                                                Arrays.asList(
                                                        Arrays.stream(contentHash)
                                                                .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                                                                .toArray(Bytes32[]::new)
                                                )
                                        )
                                ) +
                                TypeEncoder.encodePacked(
                                        new Utf8String(url)
                                ) +
                                TypeEncoder.encodePacked(
                                        new Uint256(signedTime)
                                ) +  signature.trim().substring(2, signature.length()).trim()+
                                acknowledge.trim().substring(2, acknowledge.length()).trim()
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

    public static String generateAmendMessageHash(
            String[] migratingStages,
            BigInteger stage,
            BigInteger subStage,
            String rootHash,
            String prevHash,
            String[] contentHash,
            String url,
            BigInteger signedTime,
            String acknowledge,
            String signature
    ) {
        int contentHashLength = contentHash.length;
        List urlList = new ArrayList<Utf8String>();
        urlList.add(new Utf8String(url));
        String urlEncoded = FunctionEncoder.encodeConstructor(urlList);
        String encodeAcknowledge = _encodeSignature(acknowledge);
        int offsetAcknowledge = 9 + contentHashLength + urlEncoded.length() / 64;
        String message = Hash.sha3(
                FunctionEncoder.encodeConstructor(Arrays.asList(
                        new Uint256(stage),
                        new Uint256(subStage),
                        new Bytes32(Numeric.hexStringToByteArray(rootHash)),
                        new Bytes32(Numeric.hexStringToByteArray(prevHash))
                )) +
                        "0000000000000000000000000000000000000000000000000000000000000120" + // offset in bytes to the start of contentHash
                        Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf((contentHashLength + 10) * 32)), 64) + // offset in bytes to the start of url
                        Numeric.toHexStringNoPrefixZeroPadded(signedTime, 64) +
                        Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((offsetAcknowledge) * 32L), 64) + // offset in bytes to the start of acknowledge signature
                        Numeric.toHexStringNoPrefixZeroPadded(BigInteger.valueOf((offsetAcknowledge + encodeAcknowledge.length() / 64 ) * 32L), 64) + // offset in bytes to the start of approval signature
                        Numeric.toHexStringNoPrefixZeroPadded((BigInteger.valueOf(contentHashLength)), 64) +
                        FunctionEncoder.encodeConstructor(
                                Arrays.asList(
                                        Arrays.stream(contentHash)
                                                .map(c -> new Bytes32(Numeric.hexStringToByteArray(c)))
                                                .toArray(Bytes32[]::new)
                                )
                        ) +
                        urlEncoded.substring(64, urlEncoded.length()) + // remove offset
                        _encodeSignature(acknowledge) + // signature (65 bytes -> 96 bytes, 0 byte - 32 bytes)
                        _encodeSignature(signature) // approval signature (65 bytes -> 96 bytes, 0 byte - 32 bytes)
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
