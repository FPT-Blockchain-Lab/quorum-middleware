package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.LC;
import com.fptblockchainlab.exceptions.FailedTransactionException;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface Middleware {

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
     * Add
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
     * Suspend admin of sub org
     */
    void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws FailedTransactionException, IOException;

    void createStandardLC(List<String> parties, String prevHash, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws FailedTransactionException, IOException;

    void createUPASLC(List<String> parties, String prevHash, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws FailedTransactionException, IOException;

    public void approveLC(BigInteger documentId, int stage, int subStage, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws Exception;

    void closeLC(BigInteger documentId) throws FailedTransactionException, IOException;

//    void submitAmendment(BigInteger documentId, int stage, int subStage, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, LC.Stage[] migrateStages, String privateKey) throws Exception;

    void submitRootAmendment(BigInteger documentId, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws Exception;

    void submitGeneralAmendment(BigInteger documentId, int stage, int subStage, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws Exception;

    void approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, String privateKey) throws Exception;

    void fulfillAmendment(BigInteger documentId, String proposer, BigInteger nonce) throws Exception;
}