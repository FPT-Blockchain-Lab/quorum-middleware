package com.fptblockchainlab.middleware;

import com.fptblockchainlab.exceptions.FailedTransactionException;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface IMiddleware {

    /**
     * get admin role from default roles
     * @return
     */
    Permission.Role getAdminRole();

    /**
     * Get member role from default role
     * @return
     */
    Permission.Role getMemberRole();

    /**
     * Give a level 1 org in MiddlewareSErvice check if account is active and under this level 1 org
     * or any sub or of this level 1 org
     * @param account
     * @return
     * @throws IOException
     */
    boolean isAccountOnchainUnderLevel1Org(String account) throws IOException;

    /**
     *
     * @param to
     * @param data
     * @throws IOException
     * @throws TransactionException
     * @throws FailedTransactionException
     */
    void signWithAdminAndSend(String to, String data) throws IOException, TransactionException, FailedTransactionException;

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
     * @throws FailedTransactionException
     * @throws IOException
     */
    void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, FailedTransactionException, IOException;

    /**
     *
     * @param subOrgFullId
     * @param adminWallet
     * @throws IOException
     * @throws FailedTransactionException
     */
    void addAdminForSubOrg(String subOrgFullId, String adminWallet) throws IOException, FailedTransactionException;

    /**
     * Allow org to use LC protocol
     * @param orgFullId
     * @throws FailedTransactionException
     * @throws IOException
     */
    void whiteListOrg(String orgFullId) throws FailedTransactionException, IOException;

    /**
     * Restrict org to use LC protocol
     * @param orgFullId
     * @throws FailedTransactionException
     * @throws IOException
     */
    void unwhiteListOrg(String orgFullId) throws FailedTransactionException, IOException;

    /**
     *
     * @param subOrgFullId
     * @param adminAddress
     * @throws FailedTransactionException
     * @throws IOException
     */
    void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws FailedTransactionException, IOException;

    /**
     *
     * @param parties
     * @param content
     * @param credentials
     * @return
     * @throws Exception
     */
    TransactionReceipt createLC(List<String> parties, LC.Content content, Credentials credentials, LC.LCTYPE lctype) throws Exception;

//    /**
//     *
//     * @param parties
//     * @param content
//     * @param credentials
//     * @return
//     * @throws Exception
//     */
//    TransactionReceipt createUPASLC(List<String> parties, LC.Content content, Credentials credentials) throws Exception;

    /**
     *
     * @param documentId
     * @param stage
     * @param content
     * @param credentials
     * @return
     * @throws Exception
     */
    TransactionReceipt approveLC(BigInteger documentId, LC.Stage stage, LC.Content content, Credentials credentials) throws Exception;

    /**
     *
     * @param documentId
     * @return
     * @throws Exception
     */
    TransactionReceipt closeLC(BigInteger documentId) throws Exception;

    /**
     *
     * @param documentId
     * @param content
     * @param credentials
     * @throws Exception
     */
    TransactionReceipt submitRootAmendment(BigInteger documentId, LC.Content content, Credentials credentials) throws Exception;

    /**
     *
     * @param documentId
     * @param stage
     * @param content
     * @param credentials
     * @return
     * @throws Exception
     */
    TransactionReceipt submitGeneralAmendment(BigInteger documentId, LC.Stage stage, LC.Content content, Credentials credentials) throws Exception;

    /**
     *
     * @param documentId
     * @param proposer
     * @param nonce
     * @param credentials
     * @return
     * @throws Exception
     */
    TransactionReceipt approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, Credentials credentials) throws Exception;

    /**
     *
     * @param documentId
     * @param proposer
     * @param nonce
     * @return
     * @throws Exception
     */
    TransactionReceipt fulfillAmendment(BigInteger documentId, String proposer, BigInteger nonce) throws Exception;
}