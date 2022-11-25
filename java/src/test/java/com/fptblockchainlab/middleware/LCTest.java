package com.fptblockchainlab.middleware;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.utils.Numeric;
import java.math.BigInteger;

/**
 * Unit test for LC Middleware
 */
public class LCTest 
{
    /**
     * 
     */
    @Test
    public void shouldGenerateAcknowledgeByte32WithByte32ContentHash()
    {
        String rootHash = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
        String prevHash = "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";
        BigInteger signedTime = new BigInteger("1668072205");
        int numOfDocuments = 3;

        String[] contentHashStr = {
            "0xb4d0ada81a05d1b9d1647929f59495053ce478189fc2af55d72e74a1f06b94a9",
            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
        };
        String url = "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";

        assertTrue(
            LC.generateAcknowledgeMessageHash(contentHashStr, numOfDocuments)
                .equals("0x3807c6810bcde59b6fc1f8c63a71a7b6a634ad604ab8a7be0f160d7248bd92ba")
        );
    }

    @Test
    public void shouldGenerateAcknowledgeHexWithHexContentHash()
    {
        String rootHash = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
        String prevHash = "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";
        BigInteger signedTime = new BigInteger("1668072205");
        int numOfDocuments = 3;

        String[] contentHashStr = {
            "0xb4d0ada81a05d1b9d1647929f59495053ce478189fc2af55d72e74a1f06b94a9",
            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
        };
        String url = "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";

        assertTrue(
            LC.generateAcknowledgeMessageHash(contentHashStr, numOfDocuments)
                .equals("0x3807c6810bcde59b6fc1f8c63a71a7b6a634ad604ab8a7be0f160d7248bd92ba")
        );
    }

    @Test
    public void shouldGenerateAcknowledgeHexWithByte32ContentHash()
    {
        String rootHash = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
        String prevHash = "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";
        BigInteger signedTime = new BigInteger("1668072205");
        int numOfDocuments = 3;
        Bytes32[] contentHash = {
            new Bytes32(Numeric.hexStringToByteArray("0xb4d0ada81a05d1b9d1647929f59495053ce478189fc2af55d72e74a1f06b94a9")),
            new Bytes32(Numeric.hexStringToByteArray("0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208")),
            new Bytes32(Numeric.hexStringToByteArray("0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208")),
            new Bytes32(Numeric.hexStringToByteArray("0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1")),
            new Bytes32(Numeric.hexStringToByteArray("0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3")),
        };

        String url = "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";

        assertTrue(
            LC.generateAcknowledgeMessageHash(contentHash, numOfDocuments)
                .equals("0x3807c6810bcde59b6fc1f8c63a71a7b6a634ad604ab8a7be0f160d7248bd92ba")
        );
    }

    @Test
    public void shouldGenerateApprovalHex()
    {
        String rootHash = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
        String prevHash = "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";
        BigInteger signedTime = new BigInteger("1668072205");
        int numOfDocuments = 3;

        String[] contentHashStr = {
            "0xb4d0ada81a05d1b9d1647929f59495053ce478189fc2af55d72e74a1f06b94a9",
            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
        };
        String url = "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7";

        LC.Content stageContent = new LC.Content(
            rootHash,
            signedTime,
            prevHash,
            numOfDocuments,
            contentHashStr,
            url,
            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
            ""
        );
        assertTrue(
            LC.generateApprovalMessageHash(stageContent)
                .equals("0xa64586b16e61c2b55c8082a503a3246e61cbb373be6ac35342aedae3665e8820")
        );
    }
}
