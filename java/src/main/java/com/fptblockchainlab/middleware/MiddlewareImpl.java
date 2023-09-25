package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.*;
import com.fptblockchainlab.bindings.permission.AccountManager;
import com.fptblockchainlab.bindings.permission.OrgManager;
import com.fptblockchainlab.bindings.permission.PermissionsInterface;
import com.fptblockchainlab.bindings.permission.RoleManager;
import com.fptblockchainlab.exceptions.FailedTransactionException;
import com.fptblockchainlab.exceptions.NotParentOrgException;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.quorum.methods.request.PrivateTransaction;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class MiddlewareImpl implements IMiddleware {
    // Block time in milliseconds
    private static final long DEFAULT_POLLING_FREQUENCY = 6 * 1000;
    private static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 10;
    private static final long MAX_GAS_PER_BLOCK = 4_700_000;
    private static final int GAS_PRICE = 0;

    private static final String DEFAULT_ROOT_HASH = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";

    // Required account sent from one instance of client
    private final FastRawTransactionManager fastRawTransactionManager;
    private final ContractGasProvider contractGasProvider;
    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final Credentials credentials;
    private final Quorum quorum;
    private final AccountManager accountManager;
    private final OrgManager orgManager;
    private final RoleManager roleManager;
    private final LCManagement lcManagement;
    private final PermissionsInterface permissionInterface;

    private final StandardLCFactory standardLCFactory;

    private final UPASLCFactory upaslcFactory;

    private final RouterService routerService;

    private String orgLevel1;

    public MiddlewareImpl(String quorumUrl, String privateKey, long chainId, String accountMgrAddress, String orgMgrAddress, String roleMgrAddress, String lcManagementAddress, String interfaceAddress, String standardLCFactory, String upaslcFactoryAddress, String routerServiceAddress) {
        HttpService httpService = new HttpService(quorumUrl);

        this.quorum = Quorum.build(httpService);
        this.contractGasProvider = new StaticGasProvider(BigInteger.valueOf(GAS_PRICE), BigInteger.valueOf(MAX_GAS_PER_BLOCK));
        this.credentials = Credentials.create(privateKey);
        this.transactionReceiptProcessor = new PollingTransactionReceiptProcessor(
                quorum,
                DEFAULT_POLLING_FREQUENCY,
                DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );
        this.fastRawTransactionManager = new FastRawTransactionManager(quorum, this.credentials, chainId, this.transactionReceiptProcessor);
        this.accountManager = AccountManager.load(accountMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.orgManager = OrgManager.load(orgMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.roleManager = RoleManager.load(roleMgrAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.lcManagement = LCManagement.load(lcManagementAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.permissionInterface = PermissionsInterface.load(interfaceAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.standardLCFactory = StandardLCFactory.load(standardLCFactory, quorum, fastRawTransactionManager, contractGasProvider);
        this.upaslcFactory = UPASLCFactory.load(upaslcFactoryAddress, quorum, fastRawTransactionManager, contractGasProvider);
        this.routerService = RouterService.load(routerServiceAddress, quorum, fastRawTransactionManager, contractGasProvider);
    }

    @Override
    public Permission.Role getAdminRole() {
        return Permission.Role.ORGADMIN;
    }

    @Override
    public Permission.Role getMemberRole() {
        return Permission.Role.MEMBER;
    }

    public boolean isAccountOnchainUnderLevel1Org(String account) throws IOException {
        Tuple5<String, String, String, BigInteger, Boolean> accountDetails = null;
        try {
            accountDetails = this.accountManager.getAccountDetails(account).send();
        } catch (Exception e) {
            throw new IOException("failed to get account details", e);
        }
        String[] fullOrgIdAsArr = accountDetails.component2().split("\\.");
        // Account is active state and ultimate parent org is FIS org
        return accountDetails.component4().compareTo(BigInteger.valueOf(2)) == 0 && this.orgLevel1.equals(fullOrgIdAsArr[0]);
    }

    private boolean isOrgExist(String fullOrgId) throws IOException {
        String[] orgHierarchyList = fullOrgId.split("\\.");
        String ultimateParentOrg = orgHierarchyList[0];
        String orgId = orgHierarchyList[orgHierarchyList.length - 1];
        String parentOrg = orgHierarchyList.length > 1 ? String.join(".", Arrays.copyOfRange(orgHierarchyList, 0, orgHierarchyList.length - 1)) : null;

        Tuple5<String, String, String, BigInteger, BigInteger> orgDetails = null;
        try {
            orgDetails = this.orgManager.getOrgDetails(fullOrgId).send();
        } catch (Exception e) {
            throw new IOException("failed to get org details", e);
        }
        return orgDetails.component1().equals(orgId) && orgDetails.component2().equals(parentOrg) && orgDetails.component3().equals(ultimateParentOrg);
    }

    private boolean isRoleExist(String roleName, String fullOrgId) throws IOException {
        Tuple6<String, String, BigInteger, Boolean, Boolean, Boolean> roleDetails = null;
        try {
            roleDetails = this.roleManager.getRoleDetails(roleName, fullOrgId).send();
        } catch (Exception e) {
            throw new IOException("failed to get role details", e);
        }
        return roleDetails.component1().equals(roleName) && roleDetails.component2().equals(fullOrgId);
    }

    public void signWithAdminAndSend(String to, String data) throws IOException, TransactionException, FailedTransactionException {
        EthSendTransaction ethSendTransaction = this.fastRawTransactionManager.sendTransaction(
                BigInteger.valueOf(GAS_PRICE),
                BigInteger.valueOf(MAX_GAS_PER_BLOCK),
                to,
                data,
                // number of native token transfer
                BigInteger.valueOf(0)
        );

        TransactionReceipt transactionReceipt = this.transactionReceiptProcessor.waitForTransactionReceipt(ethSendTransaction.getTransactionHash());
        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    public String signWithUltimateParentAdmin(String messageHash) {
        return signMessage(messageHash, this.credentials);
    }

    private String signMessage(String messageHash, Credentials credentials) {
        byte[] messageBytes = Numeric.hexStringToByteArray(messageHash);
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(messageBytes, credentials.getEcKeyPair());
        byte[] signature = new byte[65];

        System.arraycopy(signatureData.getR(), 0, signature, 0, 32);
        System.arraycopy(signatureData.getS(), 0, signature, 32, 32);
        System.arraycopy(signatureData.getV(), 0, signature, 64, 1);

        return Numeric.toHexString(signature);
    }

    /**
     * create private transaction options for quorumPermission API
     * @return
     */
    public PrivateTransaction getQuorumPrivateTransactionOptionFromFisAdmin() {
        BigInteger nonce = this.fastRawTransactionManager.getCurrentNonce();
        return new PrivateTransaction(this.credentials.getAddress(), nonce, BigInteger.valueOf(4700000), null,
                BigInteger.ZERO, null, null, null);
    }

    /**
     * Create by hierachical order subOrg
     * @param subOrgFullId
     * @throws Exception
     */
    private void createSubOrgs(String subOrgFullId) throws NotParentOrgException, IOException, FailedTransactionException {
        String[] orgHierachyList = subOrgFullId.split("\\.");
        if (!this.orgLevel1.equals(orgHierachyList[0])) {
            throw new NotParentOrgException(String.format("{} is not child org of {}", subOrgFullId, this.orgLevel1));
        }

        for (int i = 1; i < orgHierachyList.length ; i++) {
            String parentOrg = String.join(".", Arrays.copyOfRange(orgHierachyList, 0, i));
            String currSubOrgFullId = parentOrg.concat (".").concat(orgHierachyList[i]);
            if (!this.isOrgExist(currSubOrgFullId)) {

                try {
                    this.permissionInterface.addSubOrg(parentOrg, orgHierachyList[i], "", "", BigInteger.valueOf(0), BigInteger.valueOf(0)).send();
                } catch (Exception e) {
                    throw new IOException("failed to add subOrg");
                }
            } else {
//                log.debug("subOrg exists {}", currSubOrgFullId);
            }
        }
    }

    private void createDefaultRolesForOrg(String orgFullId) throws FailedTransactionException, IOException {
        // Can optimize with sendAsync -> CompletableFuture

        if (isRoleExist(Permission.Role.ORGADMIN.getName(), orgFullId)) {
            System.out.printf("%s already exists under %s%n",Permission.Role.ORGADMIN.getName(), orgFullId);
        } else {
            try {
                this.permissionInterface.addNewRole(
                        Permission.Role.ORGADMIN.getName(),
                        orgFullId, BigInteger.valueOf(Permission.Role.ORGADMIN.getAccessType().getBaseAccess()),
                        Permission.Role.ORGADMIN.getIsVoter(),
                        Permission.Role.ORGADMIN.getIsOrgAdmin()
                ).send();
            } catch (Exception e) {
                throw new IOException("failed to create ORGADMIN role", e);
            }
        }

        if (isRoleExist(Permission.Role.MEMBER.getName(), orgFullId)) {
            System.out.printf("%s already exists under %s%n", Permission.Role.MEMBER.getName(), orgFullId);;
        } else {
            try {
                this.permissionInterface.addNewRole(
                        Permission.Role.MEMBER.getName(),
                        orgFullId, BigInteger.valueOf(Permission.Role.MEMBER.getAccessType().getBaseAccess()),
                        Permission.Role.MEMBER.getIsVoter(),
                        Permission.Role.MEMBER.getIsOrgAdmin()
                ).send();
            } catch (Exception e) {
                throw new IOException("failed to create MEMBER role", e);
            }
        }
    }

    public void createSubOrgWithDefaultRoles(String orgFullId) throws NotParentOrgException, FailedTransactionException, IOException {
        this.createSubOrgs(orgFullId);
        this.createDefaultRolesForOrg(orgFullId);
    }

    @Override
    public void addAdminForSubOrg(String subOrgFullId, String adminAddress) throws IOException, FailedTransactionException {
        TransactionReceipt transactionReceipt;
        try {
            this.permissionInterface.assignAccountRole(adminAddress, subOrgFullId, Permission.Role.ORGADMIN.getName()).send();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * Allow org to use LC protocol
     * @param orgFullId
     * @throws FailedTransactionException
     * @throws IOException
     */
    public void whiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = this.lcManagement.whitelist(Arrays.asList(orgFullId)).send();
        } catch (Exception e) {
            throw new IOException("failed to whitelist org", e);
        }

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     * Restrict org to use LC protocol
     * @param orgFullId
     * @throws FailedTransactionException
     * @throws IOException
     */
    public void unwhiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = this.lcManagement.unwhitelist(Arrays.asList(orgFullId)).send();
        } catch (Exception e) {
            throw new IOException("failed to unwhitelist org", e);
        }

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    @Override
    public void suspendAdminSubOrg(String subOrgFullId, String adminAddress) throws FailedTransactionException, IOException {
        // suspend requires account is in active status
        if (!isAccountOnchainUnderLevel1Org(adminAddress)) {
            throw new IOException(String.format("account %s is not active under %s", adminAddress, this.orgLevel1));
        }
        try{
            this.permissionInterface.updateAccountStatus(subOrgFullId, adminAddress, BigInteger.ONE).send();
        } catch (Exception e) {
            throw new IOException("failed to unwhitelist org", e);
        }
    }

    /**
     *
     * @param parties
     * @param prevHash
     * @param contentHash
     * @param url
     * @param signedTime
     * @param numOfDocuments
     * @param acknowledge
     * @param privateKey
     * @throws FailedTransactionException
     * @throws IOException
     */
    public void createStandardLC(List<String> parties, String prevHash, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws FailedTransactionException, IOException {
        String approvalSignature = approvalSignature(DEFAULT_ROOT_HASH, prevHash, contentHash, url, signedTime, acknowledge, privateKey);
        List<byte[]> bytesParties = new ArrayList<>();

        for (int i = 0; i < parties.size(); i++) {
            bytesParties.add(parties.get(i).getBytes());
        }

        StandardLCFactory.Content content = new StandardLCFactory.Content(
                DEFAULT_ROOT_HASH.getBytes(),
                signedTime,
                prevHash.getBytes(),
                BigInteger.valueOf(numOfDocuments),
                bytesParties,
                url,
                acknowledge.getBytes(),
                approvalSignature.getBytes()
        );

        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = this.standardLCFactory.create(parties, content).send();
        } catch (Exception e) {
            throw new IOException("failed to create standard LC", e);
        }

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     *
     * @param parties
     * @param prevHash
     * @param contentHash
     * @param url
     * @param signedTime
     * @param numOfDocuments
     * @param acknowledge
     * @param privateKey
     * @throws FailedTransactionException
     * @throws IOException
     */
    public void createUPASLC(List<String> parties, String prevHash, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws FailedTransactionException, IOException {
        String approvalSignature = approvalSignature(DEFAULT_ROOT_HASH, prevHash, contentHash, url, signedTime, acknowledge, privateKey);
        List<byte[]> bytesParties = new ArrayList<>();

        for (int i = 0; i < parties.size(); i++) {
            bytesParties.add(parties.get(i).getBytes());
        }

        UPASLCFactory.Content content = new UPASLCFactory.Content(
                DEFAULT_ROOT_HASH.getBytes(),
                signedTime,
                prevHash.getBytes(),
                BigInteger.valueOf(numOfDocuments),
                bytesParties,
                url,
                acknowledge.getBytes(),
                approvalSignature.getBytes()
        );

        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = this.upaslcFactory.create(parties, content).send();
        } catch (Exception e) {
            throw new IOException("failed to create upas LC", e);
        }

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     *
     * @param documentId
     * @param stage
     * @param subStage
     * @param contentHash
     * @param url
     * @param signedTime
     * @param numOfDocuments
     * @param acknowledge
     * @param privateKey
     * @throws Exception
     */
    public void approveLC(BigInteger documentId, int stage, int subStage, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws Exception {

            StandardLC lc = getLCContract(documentId);
            RouterService.Content content = getStageInfo(lc, documentId, stage, subStage);
            String prevHash = LC.generateStageHash(new LC.Content(
                    Arrays.toString(content.rootHash),
                    content.signedTime,
                    Arrays.toString(content.prevHash),
                    content.numOfDocuments.intValue(),
                    (String[]) content.contentHash.stream().map(Object::toString).toArray(),
                    content.url,
                    Arrays.toString(content.acknowledge),
                    Arrays.toString(content.signature)
            ));
            String rootHash = Arrays.toString(this.routerService.getRootHash(documentId).send());
            String approvalSignature = approvalSignature(Arrays.toString(content.rootHash), prevHash, (String[]) content.contentHash.stream().map(Object::toString).toArray(), content.url, content.signedTime, content.acknowledge.toString(), privateKey);
            TransactionReceipt transactionReceipt = this.routerService.approve(documentId, BigInteger.valueOf(stage), BigInteger.valueOf(subStage),
                    new RouterService.Content(
                            rootHash.getBytes(),
                            content.signedTime,
                            prevHash.getBytes(),
                            content.numOfDocuments,
                            content.contentHash,
                            content.url,
                            content.acknowledge,
                            approvalSignature.getBytes())
            ).send();

            if (!transactionReceipt.isStatusOK()) {
                throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
            }
    }

    /**
     *
     * @param documentId
     * @throws FailedTransactionException
     * @throws IOException
     */
    public void closeLC(BigInteger documentId) throws FailedTransactionException, IOException {
        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = this.routerService.closeLC(documentId).send();
        } catch (Exception e) {
            throw new IOException("failed to close LC", e);
        }

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     *
     * @param documentId
     * @param stage
     * @param subStage
     * @param contentHash
     * @param url
     * @param signedTime
     * @param numOfDocuments
     * @param acknowledge
     * @param migrateStages
     * @param privateKey
     * @throws Exception
     */
    public void submitAmendment(BigInteger documentId, int stage, int subStage, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, com.fptblockchainlab.bindings.lc.LC.Stage[] migrateStages, String privateKey) throws Exception {
            StandardLC lc = getLCContract(documentId);
            Tuple3<RouterService.Content, BigInteger, BigInteger> amendStageContent = getAmendInfo(lc, documentId, BigInteger.valueOf(stage), BigInteger.valueOf(subStage));
            RouterService.Content amendStageInfo = amendStageContent.component1();
            BigInteger amendStage = amendStageContent.component2();
            BigInteger amendSubStage = amendStageContent.component3();

            if (Arrays.toString(amendStageInfo.signature).equals(Utils.EMPTY_BYTES)) {
                throw new Exception("Stage Amend not found");
            }

            Tuple2<String, String> rootHashAndPreHash = getRoothashAndPrevhash(documentId, amendStageInfo);
            List<byte[]> migrating_stages = calMigrateStages(documentId, migrateStages);

            if (amendStage.intValue() == LC.EnumStage.PHAT_HANH_LC.ordinal() || amendStage.intValue() == LC.EnumStage.CHAP_NHAN_THANH_TOAN.ordinal() || amendStage.intValue() == LC.EnumStage.UPAS_NHTT_NHXT.ordinal()) {

            }

            String approvalSignature = this.approvalSignature(
                    rootHashAndPreHash.component1(),
                    rootHashAndPreHash.component2(),
                    contentHash,
                    url,
                    signedTime,
                    acknowledge,
                    privateKey
            );
            String amendSignature = this.amendSignature(
                    migrating_stages,
                    new LC.AmendStage(amendStage, amendSubStage, new LC.Content(rootHashAndPreHash.component1(), signedTime, rootHashAndPreHash.component2(),numOfDocuments, contentHash, url, acknowledge, approvalSignature)),
                    privateKey
            );

            TransactionReceipt transactionReceipt = this.routerService.submitAmendment(
                    documentId,
                    migrating_stages,
                    new RouterService.AmendStage(
                            amendStage,
                            amendSubStage,
                            new RouterService.Content(
                                    rootHashAndPreHash.component1().getBytes(),
                                    signedTime,
                                    rootHashAndPreHash.component2().getBytes(),
                                    BigInteger.valueOf(numOfDocuments),
                                    Arrays.stream(contentHash).map(String::getBytes).collect(Collectors.toList()),
                                    url,
                                    acknowledge.getBytes(),
                                    approvalSignature.getBytes()
                            )
                    ),
                    amendSignature.getBytes()
            ).send();

            if (!transactionReceipt.isStatusOK()) {
                throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
            }
    }

    /**
     *
     * @param documentId
     * @param proposer
     * @param nonce
     * @param privateKey
     * @throws Exception
     */
    public void approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, String privateKey) throws Exception {
        String requestId = LC.generateRequestId(proposer, nonce);
        RouterService.Request amendRequest = this.routerService.getAmendmentRequest(documentId, new BigInteger(requestId, 16)).send();
        boolean isApproved = this.routerService.isAmendApproved(documentId, new BigInteger(requestId, 16)).send();

//        if (amendRequest) throw new Exception("Amend request not found.");
        if (isApproved) throw new Exception("Amend request has been approved.");

        String amendSignature = this.amendSignature(
                amendRequest.migratingStages,
                new LC.AmendStage(
                        amendRequest.amendStage.stage,
                        amendRequest.amendStage.subStage,
                        new LC.Content(
                                Arrays.toString(amendRequest.amendStage.content.rootHash),
                                amendRequest.amendStage.content.signedTime,
                                Arrays.toString(amendRequest.amendStage.content.prevHash),
                                amendRequest.amendStage.content.numOfDocuments.intValue(),
                                (String[]) amendRequest.amendStage.content.contentHash.stream().map(Object::toString).toArray(),
                                amendRequest.amendStage.content.url,
                                Arrays.toString(amendRequest.amendStage.content.acknowledge),
                                Arrays.toString(amendRequest.amendStage.content.signature)
                        )
                ),
                privateKey
        );

        TransactionReceipt transactionReceipt = this.routerService.approveAmendment(documentId, new BigInteger(requestId, 16), amendSignature.getBytes()).send();

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     *
     * @param documentId
     * @param proposer
     * @param nonce
     * @param privateKey
     * @throws Exception
     */
    public void fulfillAmendment(BigInteger documentId, String proposer, BigInteger nonce, String privateKey) throws Exception {
        String requestId = LC.generateRequestId(proposer, nonce);

        TransactionReceipt transactionReceipt = this.routerService.fulfillAmendment(documentId, new BigInteger(requestId, 16)).send();
        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     *
     * @param documentId
     * @param contentHash
     * @param url
     * @param signedTime
     * @param numOfDocuments
     * @param acknowledge
     * @param privateKey
     * @throws Exception
     */
    @Override
    public void submitRootAmendment(BigInteger documentId, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws Exception {
        StandardLC lc = getLCContract(documentId);
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.ONE).send();
        List<com.fptblockchainlab.bindings.lc.LC.Stage> migrateStages = this.getLCStatus(documentId, lc);

        this.submitAmendment(documentId, 1, rootSubStage.intValue(), contentHash, url, signedTime, numOfDocuments, acknowledge, (com.fptblockchainlab.bindings.lc.LC.Stage[]) migrateStages.toArray(), privateKey);
    }

    /**
     *
     * @param documentId
     * @param stage
     * @param subStage
     * @param contentHash
     * @param url
     * @param signedTime
     * @param numOfDocuments
     * @param acknowledge
     * @param privateKey
     * @throws Exception
     */
    @Override
    public void submitGeneralAmendment(BigInteger documentId, int stage, int subStage, String[] contentHash, String url, BigInteger signedTime, int numOfDocuments, String acknowledge, String privateKey) throws Exception {
        if (stage == 1) {
            throw new Exception("Not allowed to submit root stage.");
        }
        StandardLC lc = getLCContract(documentId);
        List<com.fptblockchainlab.bindings.lc.LC.Stage> lcStatus = this.getLCStatus(documentId, lc);
        List<com.fptblockchainlab.bindings.lc.LC.Stage> migrateStages = lcStatus.stream().filter(
                item -> !(item.subStage.intValue() == subStage && item.stage.intValue() == stage
                )).collect(Collectors.toList());

        this.submitAmendment(documentId, stage, subStage, contentHash, url, signedTime, numOfDocuments, acknowledge, (com.fptblockchainlab.bindings.lc.LC.Stage[]) migrateStages.toArray(), privateKey);
    }

    /**
     *
     * @param documentId
     * @param lc
     * @return
     * @throws Exception
     */
    private List<com.fptblockchainlab.bindings.lc.LC.Stage> getLCStatus(BigInteger documentId, StandardLC lc) throws Exception {
        List<com.fptblockchainlab.bindings.lc.LC.Stage> stages = this._getLcStatus(lc);
        List<StageContent> stageContents = new ArrayList<>();

        for (com.fptblockchainlab.bindings.lc.LC.Stage stage: stages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, stage.stage, stage.subStage).send();
            stageContents.add(new StageContent(stage, Arrays.toString(content.rootHash)));
        }

        List<byte[]> rootList = lc.getRootList().send();
        List<com.fptblockchainlab.bindings.lc.LC.Stage> migrateStages = new ArrayList<>();
        List<Bytes32> roots = new ArrayList<>();

        migrateStages.add(new com.fptblockchainlab.bindings.lc.LC.Stage(BigInteger.ONE, BigInteger.ONE));

        for (int i = 0; i < rootList.size(); i++) {
            roots.add(new Bytes32(rootList.get(i)));

            String hash = Hash.sha3(
                    TypeEncoder.encodePacked(
                            new DynamicArray(roots)
                    )
            );

            List<StageContent> generalStages = stageContents.stream().filter(
                    stageContent -> Objects.equals(stageContent.rootHash, hash) && stageContent.stage.stage.intValue() != 1
            ).collect(Collectors.toList());
            migrateStages.addAll(generalStages.stream().map(stage -> stage.stage).collect(Collectors.toList()));

            if (i != rootList.size() - 1) {
                migrateStages.add(new com.fptblockchainlab.bindings.lc.LC.Stage(BigInteger.ONE, BigInteger.valueOf(i + 2)));
            }
        }

        return migrateStages;
    }

    /**
     *
     * @param lc
     * @return
     * @throws Exception
     */
    private List<com.fptblockchainlab.bindings.lc.LC.Stage> _getLcStatus(StandardLC lc) throws Exception {
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.ONE).send();
        List<BigInteger> lcStatus = lc.getStatus().send();
        List<com.fptblockchainlab.bindings.lc.LC.Stage> rootStages = new ArrayList<>();

        for (int i = 0; i < rootSubStage.intValue(); i++) {
            rootStages.add(new com.fptblockchainlab.bindings.lc.LC.Stage(BigInteger.ONE, BigInteger.valueOf(i)));
        }

        List<com.fptblockchainlab.bindings.lc.LC.Stage> lcStages = this.calculateStages(lcStatus);
        List<com.fptblockchainlab.bindings.lc.LC.Stage> allStatus = new ArrayList<>();
        allStatus.addAll(rootStages);
        allStatus.addAll(lcStages);

        return allStatus;
    }

    /**
     *
     * @param lastestStages
     * @return
     */
    private List<com.fptblockchainlab.bindings.lc.LC.Stage> calculateStages(List<BigInteger> lastestStages) {
        List<com.fptblockchainlab.bindings.lc.LC.Stage> res = new ArrayList<>();

        for (int i = 0; i < lastestStages.size(); i++) {
            for (int j = 0; j < lastestStages.get(i).intValue(); j++) {
                res.add(new com.fptblockchainlab.bindings.lc.LC.Stage(BigInteger.valueOf(j + 1), BigInteger.valueOf(i + 1)));
            }
        }

        return res;
    }

    private String amendSignature(List<byte[]> migrateStages, LC.AmendStage amendStage, String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        String[] migrate_stages = new String[migrateStages.size()];
        for (int i = 0; i < migrate_stages.length; i++) {
            migrate_stages[i] = Arrays.toString(migrateStages.get(i));
        }
        String amendMessageHash = LC.generateAmendMessageHash(migrate_stages, amendStage);

        return signMessage(amendMessageHash, credentials);
    }

    private Tuple2<String, String> getRoothashAndPrevhash(BigInteger documentId, RouterService.Content stageContent) throws Exception {
        byte[] rootHash = this.routerService.getRootHash(documentId).send();
        String prevHash = LC.generateStageHash(new LC.Content(
                Arrays.toString(stageContent.rootHash),
                stageContent.signedTime,
                Arrays.toString(stageContent.prevHash),
                0,
                (String[]) stageContent.contentHash.stream().map(Object::toString).toArray(),
                stageContent.url,
                Arrays.toString(stageContent.acknowledge),
                Arrays.toString(stageContent.signature)
        ));

        return new Tuple2<>(Arrays.toString(rootHash), prevHash);
    }

    private List<byte[]> calMigrateStages(BigInteger documentId, com.fptblockchainlab.bindings.lc.LC.Stage[] migrateStages) throws Exception {
        List<byte[]> migrating_stages = new ArrayList<>();
        for (com.fptblockchainlab.bindings.lc.LC.Stage migrateStage : migrateStages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, migrateStage.stage, migrateStage.subStage).send();
            String prevHash = LC.generateStageHash(new LC.Content(
                    Arrays.toString(content.rootHash),
                    content.signedTime,
                    Arrays.toString(content.prevHash),
                    0,
                    (String[]) content.contentHash.stream().map(Object::toString).toArray(),
                    content.url,
                    Arrays.toString(content.acknowledge),
                    Arrays.toString(content.signature)
            ));

            migrating_stages.add(prevHash.getBytes());
        }

        return migrating_stages;
    }

    private StandardLC getLCContract(BigInteger documentId) throws Exception {
        Tuple2<String, BigInteger> result = this.routerService.getAddress(documentId).send();
        return StandardLC.load(result.component1(), this.quorum, this.credentials, this.contractGasProvider);
    }

    private Tuple3<RouterService.Content, BigInteger, BigInteger> getAmendInfo(StandardLC lc, BigInteger documentId, BigInteger stage, BigInteger subStage) throws Exception {
        BigInteger amendStage = stage, amendSubStage = subStage, prevStage = amendStage, prevSubStage = amendSubStage;
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.ONE).send();

        if (amendStage.intValue() == LC.EnumStage.PHAT_HANH_LC.ordinal()) {
            amendSubStage = rootSubStage.add(BigInteger.ONE);
        } else {
            prevStage = prevStage.add(BigInteger.ONE);
        }

        if (amendStage.intValue() == LC.EnumStage.XUAT_TRINH_TCD_BCT.ordinal()) {
            prevSubStage = rootSubStage;
        }

        RouterService.Content stageInfo = this.routerService.getStageContent(documentId, prevStage, prevSubStage).send();

        return new Tuple3<>(stageInfo, amendStage, amendSubStage);
    }

    private RouterService.Content getStageInfo(StandardLC lc, BigInteger documentId, int stage, int subStage) throws Exception {
        int prevStage = stage, prevSubStage = subStage;
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.valueOf(1)).send();

        if (stage < LC.EnumStage.THONG_BAO_BCT_MH.ordinal() && (lc.numOfSubStage(BigInteger.valueOf(stage)).send().equals(BigInteger.valueOf(subStage - 1)))) {
            throw new Error("Invalid sub stage");
        }
        if (stage != LC.EnumStage.PHAT_HANH_LC.ordinal()) {
            prevStage = prevStage - 1;
        }

        if (stage == LC.EnumStage.XUAT_TRINH_TCD_BCT.ordinal()) {
            prevSubStage = rootSubStage.intValue();
        }

        return this.routerService.getStageContent(documentId, BigInteger.valueOf(prevStage), BigInteger.valueOf(prevSubStage)).send();
    }

    private String approvalSignature(String rootHash, String prevHash, String[] contentHash, String url, BigInteger signedTime, String acknowledge, String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        LC.Content content = new LC.Content(rootHash, signedTime ,prevHash, 0, contentHash, url, acknowledge, "");
        String approvalMessageHash = LC.generateApprovalMessageHash(content);

        return signMessage(approvalMessageHash, credentials);
    }

    private static class StageContent {
        public com.fptblockchainlab.bindings.lc.LC.Stage stage;

        public String rootHash;

        public StageContent(com.fptblockchainlab.bindings.lc.LC.Stage stage, String rootHash) {
            this.stage = stage;
            this.rootHash = rootHash;
        }
    }

}