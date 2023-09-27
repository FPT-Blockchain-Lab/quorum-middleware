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
import java.util.*;
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
     * @param parties
     * @param content
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt createStandardLC(List<String> parties, LC.Content content, Credentials credentials) throws Exception {
        String approvalSignature = getApprovalSignature(
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

        return this.standardLCFactory.create(parties, lcContent).send();
    }

    /**
     * @param parties
     * @param content
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt createUPASLC(List<String> parties, LC.Content content, Credentials credentials) throws Exception {
        String approvalSignature = getApprovalSignature(
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

        return this.upaslcFactory.create(parties, lcContent).send();
    }

    /**
     * @param documentId
     * @param stage
     * @param content
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt approveLC(BigInteger documentId, LC.Stage stage, LC.Content content, Credentials credentials) throws Exception {

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
        String approvalSignature = getApprovalSignature(
                new LC.Content(
                        Arrays.toString(stageInfo.rootHash),
                        stageInfo.signedTime,
                        prevHash,
                        0,
                        (String[]) stageInfo.contentHash.stream().map(Object::toString).toArray(),
                        stageInfo.url,
                        stageInfo.acknowledge.toString(),
                        ""
                ),
                credentials
        );

        return this.routerService.approve(documentId, stage.stage, stage.subStage,
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
    }

    /**
     * @param documentId
     * @throws FailedTransactionException
     * @throws IOException
     */
    public TransactionReceipt closeLC(BigInteger documentId) throws Exception {
        return this.routerService.closeLC(documentId).send();
    }

    /**
     * @param documentId
     * @param stage
     * @param content
     * @param migrateStages
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt submitAmendment(BigInteger documentId, LC.Stage stage, LC.Content content, List<LC.Stage> migrateStages, Credentials credentials) throws Exception {
        StandardLC lc = getLCContract(documentId);
        Tuple3<RouterService.Content, BigInteger, BigInteger> amendStageContent = getAmendInfo(lc, documentId, stage);
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
    }

    /**
     * @param documentId
     * @param proposer
     * @param nonce
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt approveAmendment(BigInteger documentId, String proposer, BigInteger nonce, Credentials credentials) throws Exception {
        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce), 16);
        RouterService.Request amendRequest = this.routerService.getAmendmentRequest(documentId, requestId).send();
        boolean isApproved = this.routerService.isAmendApproved(documentId, requestId).send();

        if (isApproved) throw new Exception("Amend request has been approved.");

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
        BigInteger requestId = new BigInteger(LC.generateRequestId(proposer, nonce), 16);
        return this.routerService.fulfillAmendment(documentId, requestId).send();
    }

    /**
     * @param documentId
     * @param content
     * @param credentials
     * @throws Exception
     */
    public TransactionReceipt submitRootAmendment(BigInteger documentId, LC.Content content, Credentials credentials) throws Exception {
        StandardLC lc = getLCContract(documentId);
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.ONE).send();
        List<LC.Stage> migrateStages = this.getMigrateStages(documentId, lc);

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
        StandardLC lc = getLCContract(documentId);
        List<LC.Stage> lcStatus = this.getMigrateStages(documentId, lc);
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
    private List<LC.Stage> getMigrateStages(BigInteger documentId, StandardLC lc) throws Exception {
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
            List<LC.Stage> generalStages = filteredGeneralStageContents.entrySet().stream().map(m -> m.getKey()).collect(Collectors.toList());

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
    private StandardLC getLCContract(BigInteger documentId) throws Exception {
        Tuple2<String, BigInteger> result = this.routerService.getAddress(documentId).send();
        return StandardLC.load(result.component1(), this.quorum, this.credentials, this.contractGasProvider);
    }

    /**
     * @param lc instance of LC contract
     * @param documentId
     * @param stage
     * @return LC content, stage and substage
     * @throws Exception
     */
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

    /**
     * @param lc instance of LC contract
     * @param documentId
     * @param stage
     * @return LC content
     * @throws Exception
     */
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

    /**
     * @param content
     * @param credentials
     * @return
     */
    private String getApprovalSignature(LC.Content content, Credentials credentials) {
        String approvalMessageHash = LC.generateApprovalMessageHash(content);

        return LC.signMessage(approvalMessageHash, credentials);
    }
}
