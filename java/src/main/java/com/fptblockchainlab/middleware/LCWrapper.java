package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.*;
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

    public LCWrapper(ContractGasProvider contractGasProvider,
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

    /**
     *
     * @param parties
     * @param content
     * @param privateKey
     * @throws FailedTransactionException
     * @throws IOException
     */
    public void createStandardLC(List<String> parties, LC.Content content, String privateKey) throws FailedTransactionException, IOException {
        String approvalSignature = approvalSignature(LC.DEFAULT_ROOT_HASH, content.prevHash, content.contentHash, content.url, content.signedTime, content.acknowledge, privateKey);
        StandardLCFactory.Content lcContent = new StandardLCFactory.Content(
                LC.DEFAULT_ROOT_HASH.getBytes(),
                content.signedTime,
                content.prevHash.getBytes(),
                BigInteger.valueOf(content.numOfDocuments),
                parties.stream().map(String::getBytes).collect(Collectors.toList()),
                content.url,
                content.acknowledge.getBytes(),
                approvalSignature.getBytes()
        );

        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = this.standardLCFactory.create(parties, lcContent).send();
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
     * @param content
     * @param privateKey
     * @throws FailedTransactionException
     * @throws IOException
     */
    public void createUPASLC(List<String> parties, LC.Content content, String privateKey) throws FailedTransactionException, IOException {
        String approvalSignature = approvalSignature(LC.DEFAULT_ROOT_HASH, content.prevHash, content.contentHash, content.url, content.signedTime, content.acknowledge, privateKey);
        UPASLCFactory.Content lcContent = new UPASLCFactory.Content(
                LC.DEFAULT_ROOT_HASH.getBytes(),
                content.signedTime,
                content.prevHash.getBytes(),
                BigInteger.valueOf(content.numOfDocuments),
                parties.stream().map(String::getBytes).collect(Collectors.toList()),
                content.url,
                content.acknowledge.getBytes(),
                approvalSignature.getBytes()
        );

        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = this.upaslcFactory.create(parties, lcContent).send();
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
     * @param content
     * @param privateKey
     * @throws Exception
     */
    public void approveLC(BigInteger documentId, LC.Stage stage, LC.Content content, String privateKey) throws Exception {

        StandardLC lc = getLCContract(documentId);
        RouterService.Content stageInfo = getStageInfo(lc, documentId, stage);
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
        String approvalSignature = approvalSignature(Arrays.toString(stageInfo.rootHash), prevHash, (String[]) stageInfo.contentHash.stream().map(Object::toString).toArray(), stageInfo.url, stageInfo.signedTime, stageInfo.acknowledge.toString(), privateKey);
        TransactionReceipt transactionReceipt = this.routerService.approve(documentId, stage.stage, stage.subStage,
                new RouterService.Content(
                        rootHash.getBytes(),
                        content.signedTime,
                        prevHash.getBytes(),
                        BigInteger.valueOf(content.numOfDocuments),
                        Arrays.stream(content.contentHash).map(String::getBytes).collect(Collectors.toList()),
                        content.url,
                        content.acknowledge.getBytes(),
                        approvalSignature.getBytes())
        ).send();

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
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
     * @param content
     * @param migrateStages
     * @param privateKey
     * @throws Exception
     */
    public void submitAmendment(
            BigInteger documentId,
            LC.Stage stage,
            LC.Content content,
            List<com.fptblockchainlab.bindings.lc.LC.Stage> migrateStages,
            String privateKey
    ) throws Exception {
        StandardLC lc = getLCContract(documentId);
        Tuple3<RouterService.Content, BigInteger, BigInteger> amendStageContent = getAmendInfo(lc, documentId, stage);
        RouterService.Content amendStageInfo = amendStageContent.component1();
        BigInteger amendStage = amendStageContent.component2();
        BigInteger amendSubStage = amendStageContent.component3();

        if (Arrays.toString(amendStageInfo.signature).equals(LC.EMPTY_BYTES)) {
            throw new Exception("EnumStage Amend not found");
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
        List<byte[]> migrating_stages = calMigrateStages(documentId, migrateStages);

        if (amendStage.intValue() == LC.EnumStage.PHAT_HANH_LC.getValue() || amendStage.intValue() == LC.EnumStage.CHAP_NHAN_THANH_TOAN.getValue() || amendStage.intValue() == LC.EnumStage.UPAS_NHTT_NHXT.getValue()) {

        }

        String approvalSignature = this.approvalSignature(
                Arrays.toString(rootHash),
                prevHash,
                content.contentHash,
                content.url,
                content.signedTime,
                content.acknowledge,
                privateKey
        );
        String amendSignature = this.amendSignature(
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
                                content.signedTime,
                                prevHash.getBytes(),
                                BigInteger.valueOf(content.numOfDocuments),
                                Arrays.stream(content.contentHash).map(String::getBytes).collect(Collectors.toList()),
                                content.url,
                                content.acknowledge.getBytes(),
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
     * @param documentId
     * @param proposer
     * @param nonce
     * @param privateKey
     * @throws Exception
     */
    public void approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, String privateKey) throws Exception {
        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce), 16);
        RouterService.Request amendRequest = this.routerService.getAmendmentRequest(documentId, requestId).send();
        boolean isApproved = this.routerService.isAmendApproved(documentId, requestId).send();

        if (isApproved) throw new Exception("Amend request has been approved.");

        String amendSignature = this.amendSignature(
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
                privateKey
        );

        TransactionReceipt transactionReceipt = this.routerService.approveAmendment(documentId, requestId, amendSignature.getBytes()).send();

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     * @param documentId
     * @param proposer
     * @param nonce
     * @throws Exception
     */
    public void fulfillAmendment(BigInteger documentId, String proposer, BigInteger nonce) throws Exception {
        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce), 16);
        TransactionReceipt transactionReceipt = this.routerService.fulfillAmendment(documentId, requestId).send();

        if (!transactionReceipt.isStatusOK()) {
            throw new FailedTransactionException(String.format("transaction %s failed with %s", transactionReceipt.getTransactionHash(), transactionReceipt.getRevertReason()));
        }
    }

    /**
     *
     * @param documentId
     * @param content
     * @param privateKey
     * @throws Exception
     */
    public void submitRootAmendment(BigInteger documentId, LC.Content content, String privateKey) throws Exception {
        StandardLC lc = getLCContract(documentId);
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.ONE).send();
        List<com.fptblockchainlab.bindings.lc.LC.Stage> migrateStages = this.getLCStatus(documentId, lc);

        this.submitAmendment(documentId, new LC.Stage(BigInteger.ONE, rootSubStage), content, migrateStages, privateKey);
    }

    /**
     *
     * @param documentId
     * @param stage
     * @param content
     * @param privateKey
     * @throws Exception
     */
    public void submitGeneralAmendment(BigInteger documentId, LC.Stage stage, LC.Content content, String privateKey) throws Exception {
        if (stage.stage.intValue() == LC.EnumStage.PHAT_HANH_LC.getValue()) {
            throw new Exception("Not allowed to submit root stage.");
        }
        StandardLC lc = getLCContract(documentId);
        List<com.fptblockchainlab.bindings.lc.LC.Stage> lcStatus = this.getLCStatus(documentId, lc);
        List<com.fptblockchainlab.bindings.lc.LC.Stage> migrateStages = lcStatus.stream().filter(
                item -> !(item.subStage.equals(stage.subStage) && item.stage.equals(stage.stage)
                )).collect(Collectors.toList());

        this.submitAmendment(documentId, stage, content, migrateStages, privateKey);
    }

    /**
     *
     * @param documentId
     * @param lc
     * @return
     * @throws Exception
     */
    private List<com.fptblockchainlab.bindings.lc.LC.Stage> getLCStatus(BigInteger documentId, StandardLC lc) throws Exception {
        List<com.fptblockchainlab.bindings.lc.LC.Stage> stages = LC.getLcStatus(lc);
        List<LC.StageContent> stageContents = new ArrayList<>();

        for (com.fptblockchainlab.bindings.lc.LC.Stage stage : stages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, stage.stage, stage.subStage).send();
            stageContents.add(new LC.StageContent(stage, Arrays.toString(content.rootHash)));
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

            List<LC.StageContent> generalStages = stageContents.stream().filter(
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
            LC.Stage stage,
            LC.Content content,
            String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        String[] migrate_stages = new String[migrateStages.size()];
        for (int i = 0; i < migrate_stages.length; i++) {
            migrate_stages[i] = Arrays.toString(migrateStages.get(i));
        }
        String amendMessageHash = LC.generateAmendMessageHash(
                migrate_stages,
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

    private List<byte[]> calMigrateStages(BigInteger documentId, List<com.fptblockchainlab.bindings.lc.LC.Stage> migrateStages) throws Exception {
        List<byte[]> migrating_stages = new ArrayList<>();
        for (com.fptblockchainlab.bindings.lc.LC.Stage migrateStage : migrateStages) {
            RouterService.Content content = this.routerService.getStageContent(documentId, migrateStage.stage, migrateStage.subStage).send();
            String prevHash = LC.generateStageHash(
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

            migrating_stages.add(prevHash.getBytes());
        }

        return migrating_stages;
    }

    private StandardLC getLCContract(BigInteger documentId) throws Exception {
        Tuple2<String, BigInteger> result = this.routerService.getAddress(documentId).send();
        return StandardLC.load(result.component1(), this.quorum, this.credentials, this.contractGasProvider);
    }

    private Tuple3<RouterService.Content, BigInteger, BigInteger> getAmendInfo(StandardLC lc, BigInteger documentId, LC.Stage stage) throws Exception {
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

    private RouterService.Content getStageInfo(StandardLC lc, BigInteger documentId, LC.Stage stage) throws Exception {
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

    private String approvalSignature(String rootHash, String prevHash, String[] contentHash, String url, BigInteger signedTime, String acknowledge, String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        String approvalMessageHash = LC.generateApprovalMessageHash(
                new LC.Content(
                        rootHash,
                        signedTime,
                        prevHash,
                        0,
                        contentHash,
                        url,
                        acknowledge,
                        ""
                        )
        );

        return LC.signMessage(approvalMessageHash, credentials);
    }
}
