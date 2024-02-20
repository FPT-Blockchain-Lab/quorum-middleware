package com.fptblockchainlab.middleware;


import com.fptblockchainlab.bindings.lc.LCManagement;
import com.fptblockchainlab.bindings.lc.RouterService;
import com.fptblockchainlab.bindings.permission.OrgManager;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LCWrapperTest {
//    @Mock
//    StandardLCFactory mockStandardLCFactory;
//
//    @Mock
//    UPASLCFactory mockUPASLCFactory;
//
//    @Mock
//    RouterService mockRouterService;
//
//    @Mock
//    LCManagement mockLcManagement;
//
//    @Mock
//    OrgManager mockOrgManager;
//
//    @InjectMocks
//    LCWrapper lcWrapper;
//
//    Credentials credentials = Credentials.create("5c22fc40004ffced42437b477b2a3a54825845085151238e4e5abf9742d33c40");
//    String[] parties = {
//        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
//        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
//        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
//        "0x7286bc0ec3e95fdd8e7dcdd39efa6ef38828d7534212edd7b9e0261d71873bcb",
//        "0x7286bc0ec3e95fdd8e7dcdd39efa6ef38828d7534212edd7b9e0261d71873bcb",
//    };
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void createStandardLC() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e12",
//            "");
//        StandardLCFactory.Content lcContent = new StandardLCFactory.Content(
//            Numeric.hexStringToByteArray(LC.DEFAULT_ROOT_HASH),
//            content.signedTime,
//            Numeric.hexStringToByteArray(content.prevHash),
//            BigInteger.valueOf(content.numOfDocuments),
//            Arrays.stream(content.contentHash).map(Numeric::hexStringToByteArray).collect(Collectors.toList()),
//            content.url,
//            content.acknowledge.getBytes(),
//            "0xd64760d704fb371d54702bcc1b841c16dccbe158901f1afac748c4dc7c1103b85e175055d6022946bfbcbfecf524f7339f12433f86bc73748bb68e82a5b703a41b".getBytes()
//        );
//
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.whitelistOrgs(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.whitelistOrgs(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0]).send()).thenReturn(true);
//        when(mockStandardLCFactory.create(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), lcContent)).thenReturn(mock(RemoteFunctionCall.class));
//        assertDoesNotThrow(() -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//    }
//
//    @Test
//    public void createStandardLCOnInvalidPrevHash() {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e",
//            "");
//
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("Invalid previous hash.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnInvalidContentHash() {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e",
//            "");
//
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("Invalid content hash.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnInvalidAcknowledgeSignature() {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e",
//            "");
//
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("Invalid acknowledge signature.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnNumOfInvolvedNotMatch() {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
//            "");
//
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(parties), content, credentials));
//        assertEquals("The number of involved parties does not match. Expected 4.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnOrgNotExist() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
//            "");
//
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(false);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("Organization at index 0 or 1 does not exist.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnOrgNotWhitelist() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
//            "");
//
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.whitelistOrgs(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.whitelistOrgs(parties[0]).send()).thenReturn(false);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("Organization does not whitelist.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnAccountNotBelong() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
//            "");
//
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.whitelistOrgs(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.whitelistOrgs(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0]).send()).thenReturn(false);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("Account not belong to organization.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnNumOfDocumentNotMatch() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            6,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e12",
//            "");
//
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.whitelistOrgs(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.whitelistOrgs(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0]).send()).thenReturn(true);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("The number of documents cannot greater than the length of content hash.", exception.getMessage());
//    }
//
//    @Test
//    public void createStandardLCOnPrevHashNotMatch() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e12",
//            "");
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.whitelistOrgs(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.whitelistOrgs(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0]).send()).thenReturn(true);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createStandardLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("Unlink to previous.", exception.getMessage());
//    }
//
//    @Test
//    public void createUPASLC() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e12",
//            "");
//        UPASLCFactory.Content lcContent = new UPASLCFactory.Content(
//            Numeric.hexStringToByteArray(LC.DEFAULT_ROOT_HASH),
//            content.signedTime,
//            Numeric.hexStringToByteArray(content.prevHash),
//            BigInteger.valueOf(content.numOfDocuments),
//            Arrays.stream(content.contentHash).map(Numeric::hexStringToByteArray).collect(Collectors.toList()),
//            content.url,
//            content.acknowledge.getBytes(),
//            "0xd64760d704fb371d54702bcc1b841c16dccbe158901f1afac748c4dc7c1103b85e175055d6022946bfbcbfecf524f7339f12433f86bc73748bb68e82a5b703a41b".getBytes()
//        );
//
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.whitelistOrgs(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.whitelistOrgs(parties[0]).send()).thenReturn(true);
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockLcManagement.verifyIdentity(credentials.getAddress(), parties[0]).send()).thenReturn(true);
//        when(mockUPASLCFactory.create(Arrays.asList(parties), lcContent)).thenReturn(mock(RemoteFunctionCall.class));
//        assertDoesNotThrow(() -> lcWrapper.createUPASLC(Arrays.asList(parties), content, credentials));
//    }
//
//    @Test
//    public void createUPASLCOnNumOfInvolvedNotMatch() {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
//            "");
//
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createUPASLC(Arrays.asList(Arrays.copyOfRange(parties, 0,4)), content, credentials));
//        assertEquals("The number of involved parties does not match. Expected 5.", exception.getMessage());
//    }
//
//    @Test
//    public void createUPASLCOnOrgNotExist() throws Exception {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
//            "");
//
//        when(mockOrgManager.checkOrgExists(parties[0])).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockOrgManager.checkOrgExists(parties[0]).send()).thenReturn(false);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.createUPASLC(Arrays.asList(parties), content, credentials));
//        assertEquals("Organization at index 0 or 1 or 2 does not exist.", exception.getMessage());
//    }
//
//    @Test
//    public void approveLCOnInvalidContentHash() {
//        String[] contentHash = {
//            "0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
//            "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
//            "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21",
//        };
//        LC.Content content = new LC.Content(
//            "",
//            BigInteger.valueOf(1668072205),
//            "",
//            3,
//            contentHash,
//            "https://fpt.com.vn/LCPlatform/standardLC/0xe1838364f6cf77352589929efdd58f32e7cb2422bab08fcfe74da159a012f7d7",
//            "0xc14b7777a44bdc43f6b6420e7486135e3a400f2edd272654539fd4f8247ad1550f922a2162ac94d2322dc194c89b883d44a97345ca3eb1bae0fb32d0747ec48e1b",
//            "");
//
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.approveLC(BigInteger.ONE, new LC.Stage(1, 1), content, credentials));
//        assertEquals("Invalid content hash.", exception.getMessage());
//    }
//
//    @Test
//    public void approveAmendmentOnApproved() throws Exception {
//        String proposer = "0x9606fc0b45d0caee1857311284D4F9FD25674396";
//        BigInteger nonce = BigInteger.ONE;
//        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce).substring(2), 16);
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId)).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId).send()).thenReturn(mock(RouterService.Request.class));
//        when(mockRouterService.isAmendApproved(BigInteger.ONE, requestId)).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockRouterService.isAmendApproved(BigInteger.ONE, requestId).send()).thenReturn(true);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.approveAmendment(BigInteger.ONE, proposer, nonce, credentials));
//        assertEquals("Amend request has been approved.", exception.getMessage());
//    }
//
//    @Test
//    public void approveAmendmentOnRequestNotFound() throws Exception {
//        String proposer = "0x9606fc0b45d0caee1857311284D4F9FD25674396";
//        BigInteger nonce = BigInteger.ONE;
//        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce).substring(2), 16);
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId)).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId).send()).thenReturn(null);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.approveAmendment(BigInteger.ONE, proposer, nonce, credentials));
//        assertEquals("Amend request not found.", exception.getMessage());
//    }
//
//    @Test
//    public void fulfillAmendment() throws Exception {
//        String proposer = "0x9606fc0b45d0caee1857311284D4F9FD25674396";
//        BigInteger nonce = BigInteger.ONE;
//        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce).substring(2), 16);
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId)).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId).send()).thenReturn(mock(RouterService.Request.class));
//        when(mockRouterService.fulfillAmendment(BigInteger.ONE, requestId)).thenReturn(mock(RemoteFunctionCall.class));
//        assertDoesNotThrow(() -> lcWrapper.fulfillAmendment(BigInteger.ONE, proposer, nonce));
//    }
//
//    @Test
//    public void fulfillAmendmentOnRequestNotFound() throws Exception {
//        String proposer = "0x9606fc0b45d0caee1857311284D4F9FD25674396";
//        BigInteger nonce = BigInteger.ONE;
//        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce).substring(2), 16);
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId)).thenReturn(mock(RemoteFunctionCall.class));
//        when(mockRouterService.getAmendmentRequest(BigInteger.ONE, requestId).send()).thenReturn(null);
//        Exception exception = assertThrows(Exception.class, () -> lcWrapper.fulfillAmendment(BigInteger.ONE, proposer, nonce));
//        assertEquals("Amend request not found.", exception.getMessage());
//    }
//
//    @Test
//    public void whiteListOrg() {
//        when(mockLcManagement.whitelist(Arrays.asList("sub-org-full-id"))).thenReturn(mock(RemoteFunctionCall.class));
//        assertDoesNotThrow(() -> lcWrapper.whiteListOrg("sub-org-full-id"));
//    }
//
//    @Test
//    public void unwhiteListOrg() {
//        when(mockLcManagement.unwhitelist(Arrays.asList("sub-org-full-id"))).thenReturn(mock(RemoteFunctionCall.class));
//        assertDoesNotThrow(() -> lcWrapper.unwhiteListOrg("sub-org-full-id"));
//    }
}
