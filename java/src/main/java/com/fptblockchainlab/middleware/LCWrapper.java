package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.*;
import com.fptblockchainlab.bindings.permission.OrgManager;
import com.fptblockchainlab.exceptions.FailedTransactionException;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.quorum.Quorum;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LCWrapper {
    private final Quorum quorum;
    private final ReadonlyTransactionManager readonlyTransactionManager;
    private final ContractGasProvider contractGasProvider;
    private final LCFactory lcFactory;
    private final RouterService routerService;
    private final LCManagement lcManagement;
    private final OrgManager orgManager;

    public LCWrapper(Quorum quorum,
                     LCFactory lcFactory,
                     RouterService routerService,
                     LCManagement lcManagement,
                     OrgManager orgManager
    ) {
        this.quorum = quorum;
        this.readonlyTransactionManager = new ReadonlyTransactionManager(quorum, "0x0000000000000000000000000000000000000000");
        this.contractGasProvider = new DefaultGasProvider();
        this.routerService = routerService;
        this.lcFactory = lcFactory;
        this.lcManagement = lcManagement;
        this.orgManager = orgManager;
    }

    public TransactionReceipt createLC(List<String> parties, LC.Content content, Credentials credentials, LC.LCTYPE lctype) throws Exception {
        if (!isValidPrevHash(content.prevHash)) throw new Exception("Invalid previous hash.");
        if (!isValidContentHash(content.contentHash)) throw new Exception("Invalid content hash.");
        if (!isValid("^0x[0-9a-zA-Z]{130}", content.acknowledge)) throw new Exception("Invalid acknowledge signature.");

        content.signature = getApprovalSignature(
                new LC.Content(
                        LC.DEFAULT_ROOT_HASH,
                        content.signedTime,
                        content.prevHash,
                        0,
                        content.contentHash,
                        content.url,
                        content.acknowledge,
                        ""
                ),
                credentials
        );

        validateData(lctype, parties, content, credentials);

        LCFactory.Content lcContent = new LCFactory.Content(
            Numeric.hexStringToByteArray(LC.DEFAULT_ROOT_HASH),
            content.signedTime,
            Numeric.hexStringToByteArray(content.prevHash),
            BigInteger.valueOf(content.numOfDocuments),
            Arrays.stream(content.contentHash).map(Numeric::hexStringToByteArray).collect(Collectors.toList()),
            content.url,
            content.acknowledge.getBytes(),
            content.signature.getBytes()
        );

        return this.lcFactory.create(parties, lcContent, BigInteger.valueOf(lctype.getValue())).send();
    }

//    public TransactionReceipt createUPASLC(List<String> parties, LC.Content content, Credentials credentials) throws Exception {
//        if (!isValidPrevHash(content.prevHash)) throw new Exception("Invalid previous hash.");
//        if (!isValidContentHash(content.contentHash)) throw new Exception("Invalid content hash.");
//        if (!isValid("^0x[0-9a-zA-Z]{130}", content.acknowledge)) throw new Exception("Invalid acknowledge signature.");
//
//        content.signature = getApprovalSignature(
//                new LC.Content(
//                        LC.DEFAULT_ROOT_HASH,
//                        content.signedTime,
//                        content.prevHash,
//                        0,
//                        content.contentHash,
//                        content.url,
//                        content.acknowledge,
//                        ""
//                ),
//                credentials
//        );
//
//        validateData(LC.LCTYPE.UPAS_LC, parties, content, credentials);
//
//        UPASLCFactory.Content lcContent = new UPASLCFactory.Content(
//                Numeric.hexStringToByteArray(LC.DEFAULT_ROOT_HASH),
//                content.signedTime,
//                Numeric.hexStringToByteArray(content.prevHash),
//                BigInteger.valueOf(content.numOfDocuments),
//                Arrays.stream(content.contentHash).map(Numeric::hexStringToByteArray).collect(Collectors.toList()),
//                content.url,
//                content.acknowledge.getBytes(),
//                content.signature.getBytes()
//        );
//
//        return this.upaslcFactory.create(parties, lcContent).send();
//    }

    public TransactionReceipt approveLC(BigInteger documentId, LC.Stage stage, LC.Content content, Credentials credentials) throws Exception {
        if (!isValidContentHash(content.contentHash)) throw new Exception("Invalid content hash.");
        if (stage.stage.intValue() == LC.EnumStage.PHAT_HANH_LC.getValue() ||
            stage.stage.intValue() == LC.EnumStage.CHAP_NHAN_THANH_TOAN.getValue() ||
            stage.stage.intValue() == LC.EnumStage.UPAS_NHTT_NHXT.getValue()) {

            if (!isValid("^0x[0-9a-zA-Z]{130}", content.acknowledge)) {
                throw new Exception("Invalid acknowledge signature.");
            }
        }

        Tuple2<com.fptblockchainlab.bindings.lc.LC, BigInteger> lc = getLCContract(documentId);
        RouterService.Content stageInfo = getStageInfo(lc.component1(), documentId, stage);
        String prevHash = LC.generateStageHash(
                new LC.Content(
                        Arrays.toString(stageInfo.rootHash),
                        stageInfo.signedTime,
                        Arrays.toString(stageInfo.prevHash),
                        0,
                        (String[]) stageInfo.contentHash.stream().map(Object::toString).toArray(),
                        stageInfo.url,
                        Arrays.toString(stageInfo.acknowledge),
                        Arrays.toString(stageInfo.signature)
                )
        );

        String rootHash = Arrays.toString(this.routerService.getRootHash(documentId).send());
        String approvalSignature = getApprovalSignature(
                new LC.Content(
                        Arrays.toString(stageInfo.rootHash),
                        stageInfo.signedTime,
                        prevHash,
                        0,
                        (String[]) stageInfo.contentHash.stream().map(Object::toString).toArray(),
                        stageInfo.url,
                        Arrays.toString(stageInfo.acknowledge),
                        ""
                ),
                credentials
        );

        String org = getOrgByStage(lc.component1(), stage.stage.intValue(), lc.component2().intValue());

        validateAccountOrg(org, credentials.getAddress());

        return this.routerService.approve(documentId, stage.stage, stage.subStage,
                new RouterService.Content(
                        Numeric.hexStringToByteArray(rootHash),
                        content.signedTime,
                        Numeric.hexStringToByteArray(prevHash),
                        BigInteger.valueOf(content.numOfDocuments),
                        Arrays.stream(content.contentHash).map(Numeric::hexStringToByteArray).collect(Collectors.toList()),
                        content.url,
                        content.acknowledge.getBytes(),
                        approvalSignature.getBytes())
        ).send();
    }

    public TransactionReceipt closeLC(BigInteger documentId) throws Exception {
        return this.routerService.closeLC(documentId).send();
    }

    public TransactionReceipt submitAmendment(BigInteger documentId, LC.Stage stage, LC.Content content, List<LC.Stage> migrateStages, Credentials credentials) throws Exception {
        Tuple2<com.fptblockchainlab.bindings.lc.LC, BigInteger> lc = getLCContract(documentId);
        Tuple3<RouterService.Content, BigInteger, BigInteger> amendStageContent = getAmendInfo(lc.component1(), documentId, stage);
        RouterService.Content amendStageInfo = amendStageContent.component1();
        BigInteger amendStage = amendStageContent.component2();
        BigInteger amendSubStage = amendStageContent.component3();

        if (Arrays.toString(amendStageInfo.signature).equals(LC.EMPTY_BYTES)) {
            throw new Exception("Stage Amend not found");
        }

        byte[] rootHash = this.routerService.getRootHash(documentId).send();
        String prevHash = LC.generateStageHash(
                new LC.Content(
                        Arrays.toString(amendStageInfo.rootHash),
                        amendStageInfo.signedTime,
                        Arrays.toString(amendStageInfo.prevHash),
                        0,
                        (String[]) amendStageInfo.contentHash.stream().map(Object::toString).toArray(),
                        amendStageInfo.url,
                        Arrays.toString(amendStageInfo.acknowledge),
                        Arrays.toString(amendStageInfo.signature)
                )
        );
        List<byte[]> migrating_stages = getMigrateStageHashes(documentId, migrateStages);

        if (amendStage.intValue() == LC.EnumStage.PHAT_HANH_LC.getValue() || amendStage.intValue() == LC.EnumStage.CHAP_NHAN_THANH_TOAN.getValue() || amendStage.intValue() == LC.EnumStage.UPAS_NHTT_NHXT.getValue()) {
            if (!isValid("^0x[0-9a-zA-Z]{130}", content.acknowledge)) {
                throw new Exception("Invalid acknowledge signature.");
            }
        }

        String approvalSignature = this.getApprovalSignature(
                new LC.Content(
                        Arrays.toString(rootHash),
                        content.signedTime,
                        prevHash,
                        0,
                        content.contentHash,
                        content.url,
                        content.acknowledge,
                        ""
                ),
                credentials
        );
        String amendSignature = this.getAmendSignature(
                migrating_stages,
                new LC.Stage(amendStage, amendSubStage),
                new LC.Content(
                        Arrays.toString(rootHash),
                        content.signedTime,
                        prevHash,
                        0,
                        content.contentHash,
                        content.url,
                        content.acknowledge,
                        approvalSignature
                ),
                credentials
        );

        return this.routerService.submitAmendment(
                documentId,
                migrating_stages,
                new RouterService.AmendStage(
                        amendStage,
                        amendSubStage,
                        new RouterService.Content(
                                rootHash,
                                content.signedTime,
                                Numeric.hexStringToByteArray(prevHash),
                                BigInteger.valueOf(content.numOfDocuments),
                                Arrays.stream(content.contentHash).map(Numeric::hexStringToByteArray).collect(Collectors.toList()),
                                content.url,
                                content.acknowledge.getBytes(),
                                approvalSignature.getBytes()
                        )
                ),
                amendSignature.getBytes()
        ).send();
    }

    public TransactionReceipt approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, Credentials credentials) throws Exception {
        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce).substring(2), 16);
        RouterService.Request amendRequest = this.routerService.getAmendmentRequest(documentId, requestId).send();

        if (amendRequest == null) throw new Exception("Amend request not found.");
        if (this.routerService.isAmendApproved(documentId, requestId).send()) throw new Exception("Amend request has been approved.");

        String amendSignature = this.getAmendSignature(
                amendRequest.migratingStages,
                new LC.Stage(amendRequest.amendStage.stage, amendRequest.amendStage.subStage),
                new LC.Content(
                        Arrays.toString(amendRequest.amendStage.content.rootHash),
                        amendRequest.amendStage.content.signedTime,
                        Arrays.toString(amendRequest.amendStage.content.prevHash),
                        0,
                        (String[]) amendRequest.amendStage.content.contentHash.stream().map(Object::toString).toArray(),
                        amendRequest.amendStage.content.url,
                        Arrays.toString(amendRequest.amendStage.content.acknowledge),
                        Arrays.toString(amendRequest.amendStage.content.signature)
                ),
                credentials
        );

        return this.routerService.approveAmendment(documentId, requestId, amendSignature.getBytes()).send();
    }

    /**
     * @param documentId
     * @param proposer
     * @param nonce
     * @throws Exception
     */
    public TransactionReceipt fulfillAmendment(BigInteger documentId, String proposer, BigInteger nonce) throws Exception {
        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce).substring(2), 16);
        RouterService.Request request = this.routerService.getAmendmentRequest(documentId, requestId).send();
        if (request == null) {
            throw new Exception("Amend request not found.");
        }

        return this.routerService.fulfillAmendment(documentId, requestId).send();
    }

    /**
     * @param documentId
     * @param content
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt submitRootAmendment(BigInteger documentId, LC.Content content, Credentials credentials) throws Exception {
        Tuple2<com.fptblockchainlab.bindings.lc.LC, BigInteger> lc = getLCContract(documentId);
        BigInteger rootSubStage = lc.component1().numOfSubStage(BigInteger.ONE).send();
        List<LC.Stage> migrateStages = this.getMigrateStages(documentId, lc.component1());

        return this.submitAmendment(documentId, new LC.Stage(BigInteger.ONE, rootSubStage), content, migrateStages, credentials);
    }

    /**
     * @param documentId
     * @param stage
     * @param content
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt submitGeneralAmendment(BigInteger documentId, LC.Stage stage, LC.Content content, Credentials credentials) throws Exception {
        if (stage.stage.intValue() == LC.EnumStage.PHAT_HANH_LC.getValue()) {
            throw new Exception("Not allowed to submit root stage.");
        }
        Tuple2<com.fptblockchainlab.bindings.lc.LC, BigInteger> lc = getLCContract(documentId);
        List<LC.Stage> lcStatus = this.getMigrateStages(documentId, lc.component1());
        List<LC.Stage> migrateStages = lcStatus.stream().filter(
                item -> !(item.subStage.equals(stage.subStage) && item.stage.equals(stage.stage)
                )).collect(Collectors.toList());

        return this.submitAmendment(documentId, stage, content, migrateStages, credentials);
    }

    /**
     * @param documentId
     * @param lc instance of LC contract
     * @return
     * @throws Exception
     */
    private List<LC.Stage> getMigrateStages(BigInteger documentId, com.fptblockchainlab.bindings.lc.LC lc) throws Exception {
        List<LC.Stage> stages = LC.getLcStatus(lc);
        Map<LC.Stage, byte[]> generalStageContents = new HashMap<>();

        for (LC.Stage stage : stages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, stage.stage, stage.subStage).send();
            generalStageContents.put(stage, content.rootHash);
        }

        List<byte[]> rootList = lc.getRootList().send();
        List<LC.Stage> migrateStages = new ArrayList<>();
        List<Bytes32> roots = new ArrayList<>();

        migrateStages.add(new LC.Stage(BigInteger.ONE, BigInteger.ONE));

        for (int i = 0; i < rootList.size(); i++) {
            roots.add(new Bytes32(rootList.get(i)));

            String hash = Hash.sha3(
                    TypeEncoder.encodePacked(
                            new DynamicArray(roots)
                    )
            );
            Map<LC.Stage, byte[]> filteredGeneralStageContents = generalStageContents.entrySet().stream()
                    .filter(stageContent -> (!stageContent.getKey().stage.equals(BigInteger.ONE)) && Arrays.equals(stageContent.getValue(), hash.getBytes()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            List<LC.Stage> generalStages = filteredGeneralStageContents.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());

            migrateStages.addAll(generalStages);

            if (i != rootList.size() - 1) {
                migrateStages.add(new LC.Stage(BigInteger.ONE, BigInteger.valueOf(i + 2)));
            }
        }

        return migrateStages;
    }

    /**
     * @param migrateStageHashes
     * @param stage
     * @param content
     * @param credentials
     * @return
     */
    private String getAmendSignature(List<byte[]> migrateStageHashes, LC.Stage stage, LC.Content content, Credentials credentials) {
        String[] migrateStages = new String[migrateStageHashes.size()];
        for (int i = 0; i < migrateStages.length; i++) {
            migrateStages[i] = Arrays.toString(migrateStageHashes.get(i));
        }
        String amendMessageHash = LC.generateAmendMessageHash(
                migrateStages,
                new LC.AmendStage(
                        stage.stage,
                        stage.subStage,
                        new LC.Content(
                                content.rootHash,
                                content.signedTime,
                                content.prevHash,
                                content.numOfDocuments,
                                content.contentHash,
                                content.url,
                                content.acknowledge,
                                content.signature
                        )
                )
        );

        return LC.signMessage(amendMessageHash, credentials);
    }

    /**
     * @param documentId
     * @param migrateStages
     * @return
     * @throws Exception
     */
    private List<byte[]> getMigrateStageHashes(BigInteger documentId, List<LC.Stage> migrateStages) throws Exception {
        List<byte[]> hashes = new ArrayList<>();
        for (LC.Stage migrateStage : migrateStages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, migrateStage.stage, migrateStage.subStage).send();
            String stageHash = LC.generateStageHash(
                    new LC.Content(
                            Arrays.toString(content.rootHash),
                            content.signedTime,
                            Arrays.toString(content.prevHash),
                            0,
                            (String[]) content.contentHash.stream().map(Object::toString).toArray(),
                            content.url,
                            Arrays.toString(content.acknowledge),
                            Arrays.toString(content.signature)
                    )
            );

            hashes.add(stageHash.getBytes());
        }

        return hashes;
    }

    /**
     * Get instance of LC contract by documentId
     *
     * @param documentId
     * @return instance of LC contract
     * @throws Exception
     */
    private Tuple2<com.fptblockchainlab.bindings.lc.LC, BigInteger> getLCContract(BigInteger documentId) throws Exception {
        Tuple2<String, BigInteger> result = this.routerService.getAddress(documentId).send();
        return new Tuple2<>(com.fptblockchainlab.bindings.lc.LC.load(result.component1(), this.quorum, this.readonlyTransactionManager, contractGasProvider), result.component2());
    }

    private Tuple3<RouterService.Content, BigInteger, BigInteger> getAmendInfo(com.fptblockchainlab.bindings.lc.LC lc, BigInteger documentId, LC.Stage stage) throws Exception {
        BigInteger amendStage = stage.stage, amendSubStage = stage.subStage, prevStage = amendStage, prevSubStage = amendSubStage;
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.ONE).send();

        if (amendStage.intValue() == LC.EnumStage.PHAT_HANH_LC.getValue()) {
            amendSubStage = rootSubStage.add(BigInteger.ONE);
        } else {
            prevStage = prevStage.add(BigInteger.ONE);
        }

        if (amendStage.intValue() == LC.EnumStage.XUAT_TRINH_TCD_BCT.getValue()) {
            prevSubStage = rootSubStage;
        }

        RouterService.Content stageInfo = this.routerService.getStageContent(documentId, prevStage, prevSubStage).send();

        return new Tuple3<>(stageInfo, amendStage, amendSubStage);
    }

    /**
     * @param lc instance of LC contract
     * @param documentId
     * @param stage
     * @return LC content
     * @throws Exception
     */
    private RouterService.Content getStageInfo(com.fptblockchainlab.bindings.lc.LC lc, BigInteger documentId, LC.Stage stage) throws Exception {
        BigInteger prevStage = stage.stage, prevSubStage = stage.subStage;
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.valueOf(1)).send();

        if (stage.stage.intValue() < LC.EnumStage.THONG_BAO_BCT_MH.getValue() && (lc.numOfSubStage(stage.stage).send().equals(stage.subStage))) {
            throw new Error("Invalid sub stage");
        }
        if (stage.stage.intValue() != LC.EnumStage.PHAT_HANH_LC.getValue()) {
            prevStage = prevStage.subtract(BigInteger.ONE);
        }

        if (stage.stage.intValue() == LC.EnumStage.XUAT_TRINH_TCD_BCT.getValue()) {
            prevSubStage = rootSubStage;
        }

        return this.routerService.getStageContent(documentId, prevStage, prevSubStage).send();
    }

    /**
     * @param content
     * @param credentials
     * @return
     */
    private String getApprovalSignature(LC.Content content, Credentials credentials) {
        String approvalMessageHash = LC.generateApprovalMessageHash(content);

        return LC.signMessage(approvalMessageHash, credentials);
    }

    public void whiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        try {
            this.lcManagement.whitelist(Arrays.asList(orgFullId)).send();
        } catch (Exception e) {
            throw new IOException("failed to whitelist org", e);
        }
    }

    public void unwhiteListOrg(String orgFullId) throws FailedTransactionException, IOException {
        try {
            this.lcManagement.unwhitelist(Arrays.asList(orgFullId)).send();
        } catch (Exception e) {
            throw new IOException("failed to unwhitelist org", e);
        }
    }

    /**
     * Check character sequence to be matched expression
     * @param regex The expression to be compiled
     * @param input The character sequence to be matched
     * @return true if, and only if, a subsequence of the input sequence matches this matcher's pattern
     */
    private boolean isValid(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }

    private void validateData(LC.LCTYPE typeOf, List<String> parties, LC.Content content, Credentials credentials) throws Exception {
        if (typeOf == LC.LCTYPE.STANDARD_LC) {
            if (parties.size() != LC.NUMOFPARTIES.STANDARD_LC_PARTIES.getValue()) {
                throw new Exception("The number of involved parties does not match. Expected 4.");
            }

            if (
                !this.orgManager.checkOrgExists(parties.get(LC.INDEXOFORG.NHPH.ordinal())).send() &&
                    !this.orgManager.checkOrgExists(parties.get(LC.INDEXOFORG.NHTB.ordinal())).send()
            ) {
                throw new Exception("Organization at index 0 or 1 does not exist.");
            }
        } else if (typeOf == LC.LCTYPE.UPAS_LC) {
            if (parties.size() != LC.NUMOFPARTIES.UPAS_LC_PARTIES.getValue()) {
                throw new Exception("The number of involved parties does not match. Expected 5.");
            }

            if (
                !this.orgManager.checkOrgExists(parties.get(LC.INDEXOFORG.NHPH.ordinal())).send() &&
                    !this.orgManager.checkOrgExists(parties.get(LC.INDEXOFORG.NHTB.ordinal())).send() &&
                    !this.orgManager.checkOrgExists(parties.get(LC.INDEXOFORG.NHTT.ordinal())).send()
            ) {
                throw new Exception("Organization at index 0 or 1 or 2 does not exist.");
            }
        } else {

        }

        validateAccountOrg(parties.get(0), credentials.getAddress());

        if (content.numOfDocuments > content.contentHash.length) {
            throw new Exception("The number of documents cannot greater than the length of content hash.");
        }

        if (content.prevHash != content.contentHash[0]) {
            throw new Exception("Unlink to previous.");
        }
    }

    private void validateAccountOrg(String org, String publicKey) throws Exception {
        if (!this.lcManagement.whitelistOrgs(org).send()) {
            throw new Exception("Organization does not whitelist.");
        }

        if (!this.lcManagement.verifyIdentity(publicKey, org).send()) {
            throw new Exception("Account not belong to organization.");
        }
    }

    private String getOrgByStage(com.fptblockchainlab.bindings.lc.LC lc, int stage, int lctype) throws Exception {
        List<String> parties = lc.getInvolvedParties().send();
        String org = null;

        if (lctype == LC.LCTYPE.STANDARD_LC.getValue()) {
            if (stage == LC.EnumStage.XUAT_TRINH_TCD_BCT.getValue() || stage == LC.EnumStage.UPAS_NHXT_BTH.getValue()) {
                org = parties.get(LC.INDEXOFORG.NHTB.ordinal());
            } else {
                org = parties.get(LC.INDEXOFORG.NHPH.ordinal());
            }
        } else if (lctype == LC.LCTYPE.UPAS_LC.getValue()) {
            if (stage == LC.EnumStage.XUAT_TRINH_TCD_BCT.getValue() || stage == LC.EnumStage.UPAS_NHXT_BTH.getValue()) {
                org = parties.get(LC.INDEXOFORG.NHTB.ordinal());
            } else if (stage == LC.EnumStage.UPAS_NHTT_NHXT.getValue()) {
                org = parties.get(LC.INDEXOFORG.NHTT.ordinal());
            } else {
                org = parties.get(LC.INDEXOFORG.NHPH.ordinal());
            }
        }

        return org;
    }

    private boolean isValidPrevHash(String prevHash) {
        return prevHash.length() == 66;
    }

    private boolean isValidContentHash(String[] contentHash) {
        for (String hash: contentHash) {
            if (hash.length() != 66) return false;
        }

        return true;
    }
}
