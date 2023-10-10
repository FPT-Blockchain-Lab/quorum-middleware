package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.permission.PermissionsInterface;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.web3j.protocol.core.RemoteCall;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PermissionTest {
    @Mock
    PermissionsInterface mockPermissionInterface;

    @InjectMocks
    Permission mockPermission;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addAdminForSubOrg() {
        when(mockPermissionInterface.assignAccountRole("0x000000", "sub-org-full-id", Permission.Role.ORGADMIN.getName())).thenReturn(any(RemoteCall.class));
        assertDoesNotThrow(() -> mockPermission.addAdminForSubOrg(eq("sub-org-full-id"), eq("0x000000")));
    }
}
