package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.permission.AccountManager;
import com.fptblockchainlab.bindings.permission.PermissionsInterface;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.web3j.protocol.core.RemoteFunctionCall;

import java.io.IOException;
import java.math.BigInteger;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PermissionTest {
    @Mock
    PermissionsInterface mockPermissionInterface;

    @Mock
    AccountManager mockAccountManager;

    @Spy
    @InjectMocks
    Permission permission;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addAdminForSubOrg() {
        when(mockPermissionInterface.assignAccountRole("0x000000", "sub-org-full-id", Permission.Role.ORGADMIN.getName())).thenReturn(mock(RemoteFunctionCall.class));
        assertDoesNotThrow(() -> permission.addAdminForSubOrg("sub-org-full-id", "0x000000"));
    }

    @Test
    public void suspendAdminSubOrg() throws IOException {
        doReturn(true).when(permission).isAccountOnchainUnderLevel1Org("0x000000");
        when(mockPermissionInterface.updateAccountStatus("sub-org-full-id", "0x000000", BigInteger.ONE)).thenReturn(mock(RemoteFunctionCall.class));
        assertDoesNotThrow(() -> permission.suspendAdminSubOrg("sub-org-full-id", "0x000000"));
    }

    @Test
    public void suspendAdminSubOrgOnFailed() throws IOException {
        doReturn(false).when(permission).isAccountOnchainUnderLevel1Org("0x000000");
        assertThrows(IOException.class, () -> permission.suspendAdminSubOrg("sub-org-full-id", "0x000000"));
    }
}
