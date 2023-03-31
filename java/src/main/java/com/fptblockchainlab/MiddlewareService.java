package com.fptblockchainlab;

import com.fptblockchainlab.exceptions.FailedTransactionExeception;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import com.fptblockchainlab.middleware.Permission;
import org.springframework.stereotype.Service;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.quorum.methods.request.PrivateTransaction;

import java.io.IOException;

@Service
public interface MiddlewareService {

    /**
     * get admin role from default roles
     * @return
     */
    public Permission.Role getAdminRole();

    /**
     * Get member role from default role
     * @return
     */
    public Permission.Role getMemberRole();

    /**
     * Give a level 1 org in MiddlewareSErvice check if account is active and under this level 1 org
     * or any sub or of this level 1 org
     * @param account
     * @return
     * @throws IOException
     */
    public boolean isAccountOnchainUnderLevel1Org(String account) throws IOException;

    /**
     *
     * @param to
     * @param data
     * @throws IOException
     * @throws TransactionException
     * @throws FailedTransactionExeception
     */
    public void signWithFisAdminAndSend(String to, String data) throws IOException, TransactionException, FailedTransactionExeception;

    /**
     * sign message hash compatible with personal_sign
     * @param messageHash
     * @return
     */
    public String signWithFisAdmin(String messageHash);

    /**
     * create private transaction options for quorumPermission API
     * @return
     */
    public PrivateTransaction getQuorumPrivateTransactionOptionFromFisAdmin();

    /**
     * Create sub org for org level 1 with default roles
     * @param orgFullId
     * @throws NotParentOrgException
     * @throws IOException
     */
    public void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, FailedTransactionExeception, IOException;

    /**
     * Assign ORGADMIN role for sub-org
     * @param subOrgFullId
     * @param adminAddress
     * @throws IOException
     */
    public void addAdminForSubOrg(String subOrgFullId, String adminAddress) throws IOException, FailedTransactionExeception;

    /**
     * Allow org to use LC protocol
     * @param orgFullId
     * @throws IOException
     */
    public void whiteListOrg(String orgFullId) throws IOException;

    /**
     * Restrict org to use LC protocol
     * @param orgFullId
     * @throws IOException
     */
    public void unwhiteListOrg(String orgFullId) throws IOException;

    /**
     * Suspend admin of sub org
     * @param subOrgFullId
     * @param adminAddress
     * @throws IOException
     */
    public void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws IOException;

    public void createStandardLC() throws IOException;
    public void createUpasLC() throws IOException;
    public void approveLC() throws IOException;
    public void getRootHash(String docdumentId) throws IOException;
}
