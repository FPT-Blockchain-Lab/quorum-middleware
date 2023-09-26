package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.RouterService;
import com.fptblockchainlab.bindings.lc.StandardLC;
import com.fptblockchainlab.bindings.lc.StandardLCFactory;
import com.fptblockchainlab.bindings.lc.UPASLCFactory;
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
import org.web3j.tx.gas.ContractGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LCWrapper {
    private final ContractGasProvider contractGasProvider;
    private final Credentials credentials;
    private final Quorum quorum;

    private final StandardLCFactory standardLCFactory;

    private final UPASLCFactory upaslcFactory;

    private final RouterService routerService;

    private static LCWrapper instance;

    private LCWrapper(ContractGasProvider contractGasProvider,
                      Credentials credentials,
                      Quorum quorum,
                      StandardLCFactory standardLCFactory,
                      UPASLCFactory upaslcFactory,
                      RouterService routerService
    ) {
        this.routerService = routerService;
        this.quorum = quorum;
        this.contractGasProvider = contractGasProvider;
        this.credentials = credentials;
        this.upaslcFactory = upaslcFactory;
        this.standardLCFactory = standardLCFactory;
    }

    public static LCWrapper getInstance(
            ContractGasProvider contractGasProvider,
            Credentials credentials,
            Quorum quorum,
            StandardLCFactory standardLCFactory,
            UPASLCFactory upaslcFactory,
            RouterService routerService
    ) {
        if (instance == null) {
            instance = new LCWrapper(contractGasProvider, credentials, quorum, standardLCFactory, upaslcFactory, routerService);
        }
        return instance;
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
        String approvalSignature = approvalSignature(LCUtils.DEFAULT_ROOT_HASH, prevHash, contentHash, url, signedTime, acknowledge, privateKey);
        List<byte[]> bytesParties = new ArrayList<>();

        for (int i = 0; i < parties.size(); i++) {
            bytesParties.add(parties.get(i).getBytes());
        }

        StandardLCFactory.Content content = new StandardLCFactory.Content(
                LCUtils.DEFAULT_ROOT_HASH.getBytes(),
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
        String approvalSignature = approvalSignature(LCUtils.DEFAULT_ROOT_HASH, prevHash, contentHash, url, signedTime, acknowledge, privateKey);
        List<byte[]> bytesParties = new ArrayList<>();

        for (int i = 0; i < parties.size(); i++) {
            bytesParties.add(parties.get(i).getBytes());
        }

        UPASLCFactory.Content content = new UPASLCFactory.Content(
                LCUtils.DEFAULT_ROOT_HASH.getBytes(),
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
        String prevHash = Utils.generateStageHash(
                (String[]) content.contentHash.stream().map(Object::toString).toArray(),
                content.url,
                Arrays.toString(content.rootHash),
                Arrays.toString(content.prevHash),
                content.signedTime,
                Arrays.toString(content.acknowledge),
                Arrays.toString(content.signature)
        );
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
    public void submitAmendment(
            BigInteger documentId,
            int stage,
            int subStage,
            String[] contentHash,
            String url,
            BigInteger signedTime,
            int numOfDocuments,
            String acknowledge,
            com.fptblockchainlab.bindings.lc.LC.Stage[] migrateStages,
            String privateKey
    ) throws Exception {
        StandardLC lc = getLCContract(documentId);
        Tuple3<RouterService.Content, BigInteger, BigInteger> amendStageContent = getAmendInfo(lc, documentId, BigInteger.valueOf(stage), BigInteger.valueOf(subStage));
        RouterService.Content amendStageInfo = amendStageContent.component1();
        BigInteger amendStage = amendStageContent.component2();
        BigInteger amendSubStage = amendStageContent.component3();

        if (Arrays.toString(amendStageInfo.signature).equals(LCUtils.EMPTY_BYTES)) {
            throw new Exception("Stage Amend not found");
        }

        byte[] rootHash = this.routerService.getRootHash(documentId).send();
        String prevHash = Utils.generateStageHash(
                (String[]) amendStageInfo.contentHash.stream().map(Object::toString).toArray(),
                amendStageInfo.url,
                Arrays.toString(amendStageInfo.rootHash),
                Arrays.toString(amendStageInfo.prevHash),
                amendStageInfo.signedTime,
                Arrays.toString(amendStageInfo.acknowledge),
                Arrays.toString(amendStageInfo.signature)
        );
        List<byte[]> migrating_stages = calMigrateStages(documentId, migrateStages);

        if (amendStage.intValue() == LCUtils.Stage.PHAT_HANH_LC.ordinal() || amendStage.intValue() == LCUtils.Stage.CHAP_NHAN_THANH_TOAN.ordinal() || amendStage.intValue() == LCUtils.Stage.UPAS_NHTT_NHXT.ordinal()) {

        }

        String approvalSignature = this.approvalSignature(
                Arrays.toString(rootHash),
                prevHash,
                contentHash,
                url,
                signedTime,
                acknowledge,
                privateKey
        );
        String amendSignature = this.amendSignature(
                migrating_stages,
                amendStage,
                amendSubStage,
                Arrays.toString(rootHash),
                prevHash,
                contentHash,
                url,
                signedTime,
                acknowledge,
                approvalSignature,
                privateKey
        );

        TransactionReceipt transactionReceipt = this.routerService.submitAmendment(
                documentId,
                migrating_stages,
                new RouterService.AmendStage(
                        amendStage,
                        amendSubStage,
                        new RouterService.Content(
                                rootHash,
                                signedTime,
                                prevHash.getBytes(),
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
        BigInteger requestId = new BigInteger(Utils.generateRequestId(proposer, nonce), 16);
        RouterService.Request amendRequest = this.routerService.getAmendmentRequest(documentId, requestId).send();
        boolean isApproved = this.routerService.isAmendApproved(documentId, requestId).send();

        if (isApproved) throw new Exception("Amend request has been approved.");

        String amendSignature = this.amendSignature(
                amendRequest.migratingStages,
                amendRequest.amendStage.stage,
                amendRequest.amendStage.subStage,
                Arrays.toString(amendRequest.amendStage.content.rootHash),
                Arrays.toString(amendRequest.amendStage.content.prevHash),
                (String[]) amendRequest.amendStage.content.contentHash.stream().map(Object::toString).toArray(),
                amendRequest.amendStage.content.url,
                amendRequest.amendStage.content.signedTime,
                Arrays.toString(amendRequest.amendStage.content.acknowledge),
                Arrays.toString(amendRequest.amendStage.content.signature),
                privateKey
        );

        TransactionReceipt transactionReceipt = this.routerService.approveAmendment(documentId, requestId, amendSignature.getBytes()).send();

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
        BigInteger requestId = new BigInteger(Utils.generateRequestId(proposer, nonce), 16);
        TransactionReceipt transactionReceipt = this.routerService.fulfillAmendment(documentId, requestId).send();

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
        List<com.fptblockchainlab.bindings.lc.LC.Stage> stages = LCUtils.getLcStatus(lc);
        List<LCUtils.StageContent> stageContents = new ArrayList<>();

        for (com.fptblockchainlab.bindings.lc.LC.Stage stage: stages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, stage.stage, stage.subStage).send();
            stageContents.add(new LCUtils.StageContent(stage, Arrays.toString(content.rootHash)));
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

            List<LCUtils.StageContent> generalStages = stageContents.stream().filter(
                    stageContent -> Objects.equals(stageContent.rootHash, hash) && stageContent.stage.stage.intValue() != 1
            ).collect(Collectors.toList());
            migrateStages.addAll(generalStages.stream().map(stage -> stage.stage).collect(Collectors.toList()));

            if (i != rootList.size() - 1) {
                migrateStages.add(new com.fptblockchainlab.bindings.lc.LC.Stage(BigInteger.ONE, BigInteger.valueOf(i + 2)));
            }
        }

        return migrateStages;
    }

    private String amendSignature(
            List<byte[]> migrateStages,
            BigInteger stage,
            BigInteger subStage,
            String rootHash,
            String prevHash,
            String[] contentHash,
            String url,
            BigInteger signedTime,
            String acknowledge,
            String signature,
            String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        String[] migrate_stages = new String[migrateStages.size()];
        for (int i = 0; i < migrate_stages.length; i++) {
            migrate_stages[i] = Arrays.toString(migrateStages.get(i));
        }
        String amendMessageHash = Utils.generateAmendMessageHash(
                migrate_stages,
                stage,
                subStage,
                rootHash,
                prevHash,
                contentHash,
                url,
                signedTime,
                acknowledge,
                signature

        );

        return Utils.signMessage(amendMessageHash, credentials);
    }

    private List<byte[]> calMigrateStages(BigInteger documentId, com.fptblockchainlab.bindings.lc.LC.Stage[] migrateStages) throws Exception {
        List<byte[]> migrating_stages = new ArrayList<>();
        for (com.fptblockchainlab.bindings.lc.LC.Stage migrateStage : migrateStages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, migrateStage.stage, migrateStage.subStage).send();
            String prevHash = Utils.generateStageHash(
                    (String[]) content.contentHash.stream().map(Object::toString).toArray(),
                    content.url,
                    Arrays.toString(content.rootHash),
                    Arrays.toString(content.prevHash),
                    content.signedTime,
                    Arrays.toString(content.acknowledge),
                    Arrays.toString(content.signature)
            );

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

        if (amendStage.intValue() == LCUtils.Stage.PHAT_HANH_LC.ordinal()) {
            amendSubStage = rootSubStage.add(BigInteger.ONE);
        } else {
            prevStage = prevStage.add(BigInteger.ONE);
        }

        if (amendStage.intValue() == LCUtils.Stage.XUAT_TRINH_TCD_BCT.ordinal()) {
            prevSubStage = rootSubStage;
        }

        RouterService.Content stageInfo = this.routerService.getStageContent(documentId, prevStage, prevSubStage).send();

        return new Tuple3<>(stageInfo, amendStage, amendSubStage);
    }

    private RouterService.Content getStageInfo(StandardLC lc, BigInteger documentId, int stage, int subStage) throws Exception {
        int prevStage = stage, prevSubStage = subStage;
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.valueOf(1)).send();

        if (stage < LCUtils.Stage.THONG_BAO_BCT_MH.ordinal() && (lc.numOfSubStage(BigInteger.valueOf(stage)).send().equals(BigInteger.valueOf(subStage - 1)))) {
            throw new Error("Invalid sub stage");
        }
        if (stage != LCUtils.Stage.PHAT_HANH_LC.ordinal()) {
            prevStage = prevStage - 1;
        }

        if (stage == LCUtils.Stage.XUAT_TRINH_TCD_BCT.ordinal()) {
            prevSubStage = rootSubStage.intValue();
        }

        return this.routerService.getStageContent(documentId, BigInteger.valueOf(prevStage), BigInteger.valueOf(prevSubStage)).send();
    }

    private String approvalSignature(String rootHash, String prevHash, String[] contentHash, String url, BigInteger signedTime, String acknowledge, String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        String approvalMessageHash = Utils.generateApprovalMessageHash(
                contentHash,
                url,
                rootHash,
                prevHash,
                signedTime,
                acknowledge
        );

        return Utils.signMessage(approvalMessageHash, credentials);
    }
}
