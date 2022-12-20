import React, { useEffect, useState } from "react";
import "./App.css";
import { Layout, Menu, Form, Input, Button } from "antd";
import Web3 from "web3";
import { asciiToHex, keccak256 } from "web3-utils";
import { CHAIN_ID, setupDefaultNetwork, EMPTY_BYTES } from "./utils";
import BN from "bn.js";
import { Middleware } from "quorum-middleware";
const { Header, Content } = Layout;

const labelCreateLCList = [
  "documentId",
  "signedTime",
  "numOfDocument",
  "applicant",
  "beneficiary",
  "issuanceBank",
  "advisingBank",
  "url",
];

const labelUpdateLCList = [
  "documentId",
  "stage",
  "subStage",
  "signedTime",
  "numOfDocument",
  "applicant",
  "beneficiary",
  "issuanceBank",
  "advisingBank",
  "url",
];

const labelAmendLCList = ["documentId", "stage", "subStage"];
const labelApproveAmendLCList = ["documentId"];
const labelFullfillAmendLCList = ["documentId"];
const labelCloseLCList = ["documentId"];

function App() {
  const [lc, setLC] = useState(undefined);
  const [keyMenu, setKeyMenu] = useState("");
  const [web3, setWeb3] = useState(new Web3());
  const [account, setAccount] = useState(undefined);
  const [chainId, setChainId] = useState(undefined);

  const handleConnect = async () => {
    try {
      if (!window.ethereum) alert("Metamask is not installed!");
      await window.ethereum.enable();
      const web3 = new Web3(window.ethereum);
      let chainId = await web3.eth.getChainId();
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
        return handleConnect();
      }
      const account = await web3.eth.getAccounts();
      setWeb3(web3);
      setChainId(chainId);
      setAccount(account[0]);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    handleConnect();
  }, []);

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const currentKey = (e) => {
    if (e.key === "ConnectMetamask") {
      handleConnect();
    }
    setKeyMenu(e.key);
  };

  const getStageInfo = async (values) => {
    const routerService = Middleware.LC.loadContract(
      new Web3("http://1.54.89.229:32278")
    ).RouterService;
    // Generate documentId
    const documentId = keccak256(asciiToHex(values.documentId));
    const tx = await routerService.methods
      .getStageContent(documentId, values.stage, values.subStage)
      .call();
    console.log(tx);
    setLC(tx);
  };

  const handleCreateLC = async (values) => {
    console.log(values);
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      /**
       * example content hash
       */
      // const contentHash = [
      //   keccak256(asciiToHex("Hash of LC Document Number")),
      //   keccak256(asciiToHex("Hash of LC 1")),
      //   keccak256(asciiToHex("Hash of LC 2")),
      //   keccak256(asciiToHex("Hash of LC 3")),
      //   keccak256(asciiToHex("Hash of LC information")),
      //   keccak256(asciiToHex("Hash of LC information")),
      // ];

      const signedTime = new BN(values.signedTime);

      const numOfDocument = new BN(values.numOfDocument);
      const ROOT_HASH = Middleware.LC.DEFAULT_ROOT_HASH;
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));
      const url = values.url;

      const contentHash = [
        documentId,
        keccak256(asciiToHex("Hash of LC 1")),
        keccak256(asciiToHex("Hash of LC 2")),
        keccak256(asciiToHex("Hash of LC 3")),
        keccak256(asciiToHex("Hash of LC information")),
        keccak256(asciiToHex("Hash of LC information")),
      ];
      // Get acknowledge message hash
      const ackMessageHash = Middleware.LC.generateAcknowledgeMessageHash(
        contentHash.slice(1, numOfDocument.add(new BN("1")).toNumber())
      );

      const acknowledgeSignature = await web3.eth.personal.sign(
        ackMessageHash,
        account,
        ""
      );

      // Get approval message hash
      const approvalMessageHash = Middleware.LC.generateApprovalMessageHash({
        rootHash: ROOT_HASH,
        prevHash: documentId,
        contentHash,
        url: url,
        signedTime,
        acknowledgeSignature,
      });

      const approvalSignature = await web3.eth.personal.sign(
        approvalMessageHash,
        account,
        ""
      );

      web3.eth.handleRevert = true;
      const standardFactory = Middleware.LC.loadContract(
        new Web3("http://1.54.89.229:32278")
      ).StandardLCFactory;

      /**
       * example paries
        const parties = [
          "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
          "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
          "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
          "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
        ];
       */
      const parties = [
        values.issuanceBank,
        values.advisingBank,
        values.applicant,
        values.beneficiary,
      ];

      const content = [
        ROOT_HASH,
        signedTime.toString(),
        documentId,
        numOfDocument.toString(),
        contentHash,
        url,
        acknowledgeSignature,
        approvalSignature,
      ];

      const objContent = {
        rootHash: ROOT_HASH,
        prevHash: documentId,
        contentHash: contentHash,
        url: url,
        signedTime: signedTime,
        numOfDocuments: numOfDocument,
        acknowledgeSignature: acknowledgeSignature,
        approvalSignature: approvalSignature,
      };

      // const gas = await standardFactory.methods
      //   .create(parties, content)
      //   .estimateGas({ from: account });
      // console.log(gas);
      // const tx = await standardFactory.methods
      //   .create(parties, content)
      //   .estimateGas({ from: account, gas });
      // console.log(tx);

      const wrapperContract = new Middleware.LCContractWrapper(web3);

      console.log(content);

      try {
        const tx = await wrapperContract.createStandardLC(
          parties,
          objContent,
          account
        );
        console.log(tx);

        alert("Create LC success");
      } catch (error) {
        console.log(error);
      }
    } catch (error) {
      console.log(error);
      error.message && alert(error.message);
    }
  };

  const handleUpdateLC = async (values) => {
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      const routerService = Middleware.LC.loadContract(
        new Web3("http://1.54.89.229:32278")
      ).RouterService;

      /**
       * example content hash
       */
      const contentHash = [
        keccak256(asciiToHex("Hash of LC Document Number")),
        keccak256(asciiToHex("Hash of LC 1")),
        keccak256(asciiToHex("Hash of LC 2")),
        keccak256(asciiToHex("Hash of LC 3")),
        keccak256(asciiToHex("Hash of LC information")),
      ];
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));
      const stageInfo = await routerService.methods
        .getStageContent(documentId, values.stage - 1, values.subStage)
        .call();

      // Get message hash
      const prevHash = Middleware.LC.generateStageHash({
        rootHash: stageInfo.rootHash,
        prevHash: stageInfo.prevHash,
        contentHash: stageInfo.contentHash,
        url: stageInfo.url,
        signedTime: new BN(stageInfo.signedTime),
        acknowledgeSignature: stageInfo.acknowledge,
        approvalSignature: stageInfo.signature,
      });

      const signedTime = new BN(values.signedTime);
      const _stage = new BN(values.stage);
      const _subStage = new BN(values.subStage);
      const numOfDocument = new BN(values.numOfDocument);
      const url = values.url;

      let acknowledgeSignature = EMPTY_BYTES; //  Acknowledge signature only for Stage 1, Stage 4 and Stage 5

      if (_stage == 1 || _stage == 4 || _stage == 5) {
        const ackMessageHash = Middleware.LC.generateAcknowledgeMessageHash(
          contentHash.slice(1, numOfDocument.add(new BN("1")).toNumber())
        );

        acknowledgeSignature = await web3.eth.personal.sign(
          ackMessageHash,
          account,
          ""
        );
      }

      const ROOT_HASH = await routerService.methods
        .getRootHash(documentId)
        .call();
      console.log(ROOT_HASH);

      // Get approval message hash
      const messageHash = Middleware.LC.generateApprovalMessageHash({
        rootHash: ROOT_HASH,
        prevHash: prevHash,
        contentHash,
        url: url,
        signedTime,
        acknowledgeSignature,
      });

      const approval_sig = await web3.eth.personal.sign(
        messageHash,
        account,
        ""
      );

      const content = [
        ROOT_HASH,
        signedTime.toString(),
        prevHash,
        numOfDocument.toString(),
        contentHash,
        url,
        acknowledgeSignature,
        approval_sig,
      ];

      // const tx = await routerService.methods
      //   .approve(documentId, _stage, _subStage, content)
      //   .send({ from: account });
      // console.log(tx);

      const wrapperContract = new Middleware.LCContractWrapper(web3);

      const tx = await wrapperContract.approveLC(
        documentId,
        _stage,
        _subStage,
        {
          rootHash: ROOT_HASH,
          signedTime,
          prevHash,
          numOfDocuments: numOfDocument,
          contentHash,
          url,
          acknowledgeSignature,
          approvalSignature: approval_sig,
        },
        account
      );
      console.log(tx);
      alert("Update LC success");
    } catch (error) {
      console.error(error);
    }
  };

  const handleCloseLC = async (values) => {
    try {
      if (!web3 || !account) {
        alert("Connect to the metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      const routerService = Middleware.LC.loadContract(
        new Web3("http://1.54.89.229:32278")
      ).RouterService;
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));
      const wrapperContract = new Middleware.LCContractWrapper(web3);
      const tx = await wrapperContract.closeLC(documentId, account);

      console.log(tx);
      alert("Close LC success!");
    } catch (err) {
      console.log(err);
    }
  };

  const handleAmendLC = async (values) => {
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      const { RouterService: routerService, AmendRequest: amendRequest } =
        Middleware.LC.loadContract(new Web3("http://1.54.89.229:32278"));
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));
      const { _contract: lcAddress } = await routerService.methods
        .getAddress(documentId)
        .call();
      if (/^0x0+$/.test(lcAddress)) return alert("DocumentId not found");

      const amendStage = +values.stage;
      const amendSubStage = +values.subStage + 1;
      const amendStageContent = await routerService.methods
        .getStageContent(documentId, values.stage, values.subStage)
        .call();

      if (amendStageContent.signature === "0x")
        return alert("Stage Amend not found");

      const rootHash = await routerService.methods
        .getRootHash(documentId)
        .call();

      // get prevHash
      const prevHash = Middleware.LC.generateStageHash({
        rootHash: amendStageContent.rootHash,
        prevHash: amendStageContent.prevHash,
        contentHash: amendStageContent.contentHash,
        url: amendStageContent.url,
        signedTime: amendStageContent.signedTime,
        acknowledgeSignature: amendStageContent.acknowledge,
        approvalSignature: amendStageContent.signature,
      });
      console.log("Prev hash: ", prevHash);
      const migrateStages = [{ stage: 1, subStage: 1 }];
      const migrating_stages = await Promise.all(
        migrateStages.map(async (s) => {
          const content = await routerService.methods
            .getStageContent(documentId, s.stage, s.subStage)
            .call();
          return Middleware.LC.generateStageHash({
            rootHash: content.rootHash,
            prevHash: content.prevHash,
            url: content.url,
            contentHash: content.contentHash,
            signedTime: content.signedTime,
            approvalSignature: content.signature,
            acknowledgeSignature: content.acknowledge,
          });
        })
      );

      /**
       * example content hash
       */
      const contentHash = [
        keccak256(asciiToHex("Hash of LC Document Number")),
        keccak256(asciiToHex("Hash of LC 1")),
        keccak256(asciiToHex("Hash of LC 2")),
        keccak256(asciiToHex("Hash of LC 3")),
        keccak256(asciiToHex("Hash of LC information")),
      ];
      // mock data
      const signedTime = Math.floor(Date.now() / 1000);
      const numOfDocument = 3;
      const url = "https://fpt.com.vn/LCPlatform/standardLC/";

      // get acknowledge signature
      let acknowledge_sig = EMPTY_BYTES; //  Acknowledge signature only for Stage 1, Stage 4 and Stage 5
      if (amendStage == 1 || amendStage == 4 || amendStage == 5) {
        const ackMessageHash = Middleware.LC.generateAcknowledgeMessageHash(
          contentHash.slice(1, numOfDocument + 1)
        );

        acknowledge_sig = await web3.eth.personal.sign(
          ackMessageHash,
          account,
          ""
        );
      }

      // get approval signature
      const messageHash = Middleware.LC.generateApprovalMessageHash({
        rootHash,
        prevHash,
        contentHash,
        url: url,
        signedTime,
        acknowledgeSignature: acknowledge_sig,
      });
      const approval_sig = await web3.eth.personal.sign(
        messageHash,
        account,
        ""
      );
      const content = [
        rootHash,
        signedTime,
        prevHash,
        numOfDocument,
        contentHash,
        url,
        acknowledge_sig,
        approval_sig,
      ];

      const amend_stage = [amendStage, amendSubStage, content];

      //get amend message hash
      const amendHash = Middleware.LC.generateAmendMessageHash(
        migrating_stages,
        {
          stage: amendStage,
          subStage: amendSubStage,
          content: {
            rootHash,
            prevHash,
            contentHash,
            url,
            signedTime,
            numOfDocuments: numOfDocument,
            acknowledgeSignature: acknowledge_sig,
            approvalSignature: approval_sig,
          },
        }
      );

      const amend_sig = await web3.eth.personal.sign(amendHash, account, "");

      // const tx = await routerService.methods
      //   .submitAmendment(documentId, migrating_stages, amend_stage, amend_sig)
      //   .send({ from: account });
      const wrapperContract = new Middleware.LCContractWrapper(web3);
      const tx = await wrapperContract.submitAmendment(
        documentId,
        migrating_stages,
        {
          stage: amendStage,
          subStage: amendSubStage,
          content: {
            rootHash,
            prevHash,
            contentHash,
            url,
            signedTime,
            numOfDocuments: numOfDocument,
            acknowledgeSignature: acknowledge_sig,
            approvalSignature: approval_sig,
          },
        },
        amend_sig,
        account
      );
      console.log(tx);
      alert("Submit Amend Success");
    } catch (error) {
      console.log(error);
      error.message && alert(error.message);
    }
  };

  const handleApproveAmendLC = async (values) => {
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      // const { RouterService: routerService, AmendRequest: amendRequest } =
      //   Middleware.LC.loadContract(new Web3("http://1.54.89.229:32278"));
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));

      //  Account 1 has been submitted amendment request twice -> current_nonce = 2
      //  - `nonce = 0` -> Standard LC amendment request
      //  - `nonce = 1` -> UPAS LC amendment request
      // Get current nonces of proposer
      // const nonces = await amendRequest.methods.nonces(account).call();
      // // Generate requestId
      // const requestId = Middleware.LC.generateRequestId(
      //   account,
      //   new BN(nonces - 1)
      // );
      // const amendmentRequest = await routerService.methods
      //   .getAmendmentRequest(documentId, requestId)
      //   .call();
      // const isApproved = await routerService.methods
      //   .isAmendApproved(documentId, requestId)
      //   .call();
      // if (isApproved) return alert("Amend request has been approved!");

      // const content = Object.assign({}, amendmentRequest.amendStage.content);
      // // Format amendmentRequest
      // const amendStage = {
      //   stage: amendmentRequest.amendStage.stage,
      //   subStage: amendmentRequest.amendStage.subStage,
      //   content: {
      //     rootHash: content.rootHash,
      //     prevHash: content.prevHash,
      //     contentHash: content.contentHash,
      //     url: content.url,
      //     signedTime: content.signedTime,
      //     acknowledgeSignature: content.acknowledge,
      //     approvalSignature: content.signature,
      //   },
      // };
      // const amendMessageHash = Middleware.LC.generateAmendMessageHash(
      //   amendmentRequest.migratingStages,
      //   amendStage
      // );

      // const amend_sig = await web3.eth.personal.sign(
      //   amendMessageHash,
      //   account,
      //   ""
      // );

      // const tx = await routerService.methods
      //   .approveAmendment(documentId, requestId, amend_sig)
      //   .send({ from: account });
      const wrapperContract = new Middleware.LCContractWrapper(web3);
      const tx = await wrapperContract.approveAmendment(documentId, account);
      console.log(tx);
      alert("Approve Amendment Success!");
    } catch (error) {
      console.log(error);
      error.message && alert(error.message);
    }
  };

  const handleFullfillAmend = async (values) => {
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      // const { RouterService: routerService, AmendRequest: amendRequest } =
      //   Middleware.LC.loadContract(new Web3("http://1.54.89.229:32278"));
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));

      //  Account 1 has been submitted amendment request twice -> current_nonce = 2
      //  - `nonce = 0` -> Standard LC amendment request
      //  - `nonce = 1` -> UPAS LC amendment request
      // Get current nonce of proposer
      // const nonces = await amendRequest.methods.nonces(account).call();
      // // Generate requestId using current nonce
      // const requestId = Middleware.LC.generateRequestId(
      //   account,
      //   new BN(nonces - 1)
      // );

      // const request = await routerService.methods
      //   .getAmendmentRequest(documentId, requestId)
      //   .call();
      // if (!request) alert("Amend request not found!");
      // console.log(request);
      // const tx = await routerService.methods
      //   .fulfillAmendment(documentId, requestId)
      //   .call();
      const wrapperContract = new Middleware.LCContractWrapper(web3);
      const tx = await wrapperContract.fulfillAmendment(documentId, account);
      console.log(tx);
      alert("Fullfill success!");
    } catch (error) {
      console.log(error);
      error.message && alert(error.message);
    }
  };

  const FormItem = (label) => {
    return (
      <Form.Item
        label={label}
        name={label}
        rules={[
          {
            required: true,
            message: `Please input your ${label}!`,
          },
        ]}
      >
        <Input
          onChange={async (e) => {
            // try {
            //   const routerService = new web3.eth.Contract(
            //     RouterABI,
            //     ROUTER_SERVICE_ADDRESS
            //   );
            //   const { _contract: lcAddress } = await routerService.methods
            //     .getAddress(e.target.value)
            //     .call();
            //   if (!/^0x0+$/.test(lcAddress)) setAmendLCSelected(lcAddress);
            // } catch (error) {}
          }}
        />
      </Form.Item>
    );
  };

  const CreateLCForm = () => {
    return (
      <Form
        name="basic"
        labelCol={{
          span: 4,
        }}
        wrapperCol={{
          span: 16,
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={handleCreateLC}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {labelCreateLCList.map((idx, label) => FormItem(idx, label))}
        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit">
            Create
          </Button>
        </Form.Item>
      </Form>
    );
  };

  const UpdateLCForm = () => {
    return (
      <Form
        name="basic"
        labelCol={{
          span: 4,
        }}
        wrapperCol={{
          span: 16,
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={handleUpdateLC}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {labelUpdateLCList.map((idx, label) => FormItem(idx, label))}
        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit">
            Update
          </Button>
        </Form.Item>
      </Form>
    );
  };

  const GetStageContentForm = () => {
    return (
      <>
        <Form
          name="basic"
          labelCol={{
            span: 4,
          }}
          wrapperCol={{
            span: 16,
          }}
          initialValues={{
            remember: true,
          }}
          onFinish={getStageInfo}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          {labelAmendLCList.map((idx, label) => FormItem(idx, label))}
          <Form.Item
            wrapperCol={{
              offset: 8,
              span: 16,
            }}
          >
            <Button type="primary" htmlType="submit">
              Get
            </Button>
          </Form.Item>
        </Form>
        {lc && (
          <div>
            <div>Root hash: {lc.rootHash}</div>
            <div>Prev hash: {lc.prevHash}</div>
            <div>
              Content hash:
              <ul>
                {lc.contentHash.map((l, i) => (
                  <li key={i}>{l}</li>
                ))}
              </ul>
            </div>
            <div>Number of documents: {lc.numOfDocuments}</div>
            <div>Acknowledge: {lc.acknowledge}</div>
            <div>Signature: {lc.signature}</div>
          </div>
        )}
      </>
    );
  };
  const AmendLCForm = () => {
    return (
      <Form
        name="basic"
        labelCol={{
          span: 4,
        }}
        wrapperCol={{
          span: 16,
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={handleAmendLC}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {labelAmendLCList.map((idx, label) => FormItem(idx, label))}
        <div>Migrate stages</div>
        {/* {lcData?.lc?.subStageChangeApproves.map((stage, idx) => (
					<div style={{ display: "flex" }}>
						<input
							type="checkbox"
							onChange={(e) => {
								let existIdx = migrateStages.findIndex(
									(s) => s.stage == stage.stage && s.subStage == stage.subStage
								);
								if (existIdx !== -1) {
									// remove
									setMigrateStages((pre) => [
										...pre.slice(0, existIdx),
										...pre.slice(existIdx + 1, pre.length),
									]);
								} else {
									setMigrateStages((pre) => [
										...pre,
										{ stage: stage.stage, subStage: stage.subStage },
									]);
								}
							}}
						/>
						<div>
							Stage: {stage.stage}; substage: {stage.subStage}
						</div>
					</div>
				))} */}
        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit">
            Amend
          </Button>
        </Form.Item>
      </Form>
    );
  };

  const ApproveAmendForm = () => {
    return (
      <Form
        name="basic"
        labelCol={{
          span: 4,
        }}
        wrapperCol={{
          span: 16,
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={handleApproveAmendLC}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {labelApproveAmendLCList.map((idx, label) => FormItem(idx, label))}
        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit">
            {keyMenu === "ApproveAmend" ? "Approve Amend" : "Fullfill Amend"}
          </Button>
        </Form.Item>
      </Form>
    );
  };

  const FullfillAmendForm = () => {
    return (
      <Form
        name="basic"
        labelCol={{
          span: 4,
        }}
        wrapperCol={{
          span: 16,
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={handleFullfillAmend}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {labelFullfillAmendLCList.map((idx, label) => FormItem(idx, label))}
        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit">
            {keyMenu === "ApproveAmend" ? "Approve Amend" : "Fullfill Amend"}
          </Button>
        </Form.Item>
      </Form>
    );
  };

  const CloseLCForm = () => {
    return (
      <Form
        name="basic"
        labelCol={{
          span: 4,
        }}
        wrapperCol={{
          span: 16,
        }}
        initialValues={{
          remember: true,
        }}
        onFinish={handleCloseLC}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {labelCloseLCList.map((idx, label) => FormItem(idx, label))}
        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit">
            Close
          </Button>
        </Form.Item>
      </Form>
    );
  };

  // const ApproveModal = () => {
  //   return (
  //     <>
  //       <Modal
  //         title="Basic Modal"
  //         visible={isModalVisible}
  //         onOk={handleUpdateLC}
  //         onCancel={handleCancel}
  //       >
  //         {/* {labelCreateLCList.map((idx, label) => FormItem(idx, label))} */}
  //         Update Next Step LC
  //       </Modal>
  //     </>
  //   );
  // };
  return (
    <Layout>
      <Header className="header">
        <div className="logo" />
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={["2"]}
          items={[
            { key: "GetStageContent", label: "Get Stage Content" },
            { key: "CreateLC", label: "Create LC" },
            { key: "ApproveLC", label: "Approve LC" },
            { key: "CloseLC", label: "Close LC" },
            { key: "Amend", label: "Amend LC" },
            { key: "ApproveAmend", label: "Approve Amend" },
            { key: "FullfillAmend", label: "Fullfill Amend" },
            {
              key: "ConnectMetamask",
              label: !account ? "ConnectMetamask" : account,
            },
          ]}
          onClick={currentKey}
        />
      </Header>
      <Layout>
        <Layout
          style={{
            padding: "0 24px 24px",
          }}
        >
          <Content
            className="site-layout-background"
            style={{
              padding: 24,
              margin: 0,
              minHeight: 280,
            }}
          >
            {keyMenu === "GetStageContent" ? (
              GetStageContentForm()
            ) : keyMenu === "CreateLC" ? (
              CreateLCForm()
            ) : keyMenu === "ApproveLC" ? (
              UpdateLCForm()
            ) : keyMenu === "CloseLC" ? (
              CloseLCForm()
            ) : keyMenu === "Amend" ? (
              AmendLCForm()
            ) : keyMenu === "ApproveAmend" ? (
              ApproveAmendForm()
            ) : keyMenu === "FullfillAmend" ? (
              FullfillAmendForm()
            ) : (
              <>Nothing to show</>
            )}
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
}

export default App;
