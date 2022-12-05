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

    @Test
    public void shouldGenerateStageHash()
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
            "0x081c43fe85b06fecb533874c06932c74dade99f20daec0e82c63af544aea9001458b6053f94804915b284b0a319fb654394cee548de4d7cc427d6a7caa79024c1b"
        );
        assertTrue(LC.generateStageHash(stageContent).equals("0xeec6ef53733206035312a6544cee5bf2fd11de68b0b3e53f22fea9d55e00d4d6"));
    }

    @Test
    public void shouldGenerateRequestId()
    {
        String proposer = "0xeC67442F28B5B85A37405240c31e339DF3ccb111";
        BigInteger nonce = new BigInteger("0");

        assertTrue(
            LC.generateRequestId(proposer, nonce)
                .equals("0x0d26a54e0a1dcc053a0c5153f72823106bade73882b5c0e5a6af38d18e4ebcc9")
        );
    }

    @Test
    public void shouldGenerateAmendMessageHash()
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

        LC.Content content = new LC.Content(
            rootHash,
            signedTime,
            prevHash,
            numOfDocuments,
            contentHashStr,
            url,
            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
            "0x081c43fe85b06fecb533874c06932c74dade99f20daec0e82c63af544aea9001458b6053f94804915b284b0a319fb654394cee548de4d7cc427d6a7caa79024c1b"
        );
        LC.AmendStage amendStage = new LC.AmendStage(
            new BigInteger("1"),
            new BigInteger("2"),
            content
        );

        String[] migratingStages = {
            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
        };
        System.out.println(

        );
        assertTrue(
            LC.generateAmendMessageHash(
                migratingStages,
                amendStage
            ).equals("0x213e09b025d6c7e3c69283ec1e339aeb2551268b30ba96a0cc75f3c528cb891d")
        );
    }
}

