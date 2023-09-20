package com.fptblockchainlab.middleware;

import com.fptblockchainlab.exceptions.FailedTransactionExeception;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;

public interface IMiddleware {

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
    public void signWithAdminAndSend(String to, String data) throws IOException, TransactionException, FailedTransactionExeception;

    /**
     * sign message hash compatible with personal_sign
     * @param messageHash
     * @return
     */
    public String signWithUltimateParentAdmin(String messageHash);

    /**
     * Create sub org for org level 1
     * @param orgFullId
     * @throws NotParentOrgException
     * @throws FailedTransactionExeception
     * @throws IOException
     */
    public void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, FailedTransactionExeception, IOException;

    /**
     * Add
     */
    public void addAdminForSubOrg(String subOrgFullId, String adminWallet) throws IOException, FailedTransactionExeception;

    /**
     * Allow org to use LC protocol
     * @param orgFullId
     * @throws FailedTransactionExeception
     * @throws IOException
     */
    public void whiteListOrg(String orgFullId) throws FailedTransactionExeception, IOException;

    /**
     * Restrict org to use LC protocol
     * @param orgFullId
     * @throws FailedTransactionExeception
     * @throws IOException
     */
    public void unwhiteListOrg(String orgFullId) throws FailedTransactionExeception, IOException;

    /**
     * Suspend admin of sub org
     */
    public void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws FailedTransactionExeception, IOException;
}