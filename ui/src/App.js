import React, { useEffect, useState } from "react";
import "./App.css";
import {
  Layout,
  Menu,
  Descriptions,
  Anchor,
  Form,
  Input,
  Button,
  Modal,
  message,
} from "antd";
import Web3 from "web3";
import { asciiToHex, keccak256 } from "web3-utils";
import { CHAIN_ID, setupDefaultNetwork, EMPTY_BYTES } from "./utils";
import BN from "bn.js";
import { LCMiddleware } from "@tuannm106/quorum-middleware";
const { Header, Sider, Content } = Layout;
const { Link } = Anchor;

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
const labelApproveAmendLCList = ["documentId", "requestID"];

function App() {
  const [lc, setLC] = useState(undefined);
  const [keyMenu, setKeyMenu] = useState("");
  const [web3, setWeb3] = useState(new Web3());
  const [account, setAccount] = useState(undefined);
  const [chainId, setChainId] = useState(undefined);
  const [isModalVisible, setIsModalVisible] = useState(false);

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

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

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
    const routerService = LCMiddleware.loadContract(
      new Web3("http://1.54.89.229:32278")
    ).RouterService;
    const documentId = keccak256(asciiToHex(values.documentId));
    const tx = await routerService.methods
      .getStageContent(documentId, values.stage, values.subStage)
      .call();
    console.log(tx);
    setLC(tx);
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

      const routerService = LCMiddleware.loadContract(
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

      const documentId = keccak256(asciiToHex(values.documentId));
      const stageInfo = await routerService.methods
        .getStageContent(documentId, values.stage - 1, values.subStage)
        .call();
      console.log("Stage info: ", stageInfo);
      const prevHash = LCMiddleware.generateStageHash({
        rootHash: stageInfo.rootHash,
        prevHash: stageInfo.prevHash,
        contentHash: stageInfo.contentHash,
        URL: stageInfo.url,
        signedTime: new BN(stageInfo.signedTime),
        acknowledgeSignature: stageInfo.signature,
        approvalSignature: stageInfo.acknowledge,
      });
      // const prevHash =
      //   "0x1ff52a878fe0bc00b2a5c058cab7d32c1d1a96e1adfa149782c09a4d4b3d82cd";
      console.log("Stage Message Hash: ", prevHash);

      const signedTime = new BN(values.signedTime);
      const _stage = new BN(values.stage);
      // const _subStage = new BN(+values.subStage + 1);
      const _subStage = new BN(values.subStage);
      const numOfDocument = new BN(values.numOfDocument);
      const url = values.url;

      let acknowledgeSignature = EMPTY_BYTES; //  Acknowledge signature only for Stage 1, Stage 4 and Stage 5

      if (_stage == 1 || _stage == 4 || _stage == 5) {
        const ackMessageHash = LCMiddleware.generateAcknowledgeMessageHash(
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

      const messageHash = LCMiddleware.generateApprovalMessageHash({
        rootHash: ROOT_HASH,
        prevHash: prevHash,
        contentHash,
        URL: url,
        signedTime,
        acknowledgeSignature,
      });
      console.log("Approval Message Hash: ", messageHash);

      const approval_sig = await web3.eth.personal.sign(
        messageHash,
        account,
        ""
      );
      console.log("Approval sig: ", approval_sig);

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

      const tx = await routerService.methods
        .approve(documentId, _stage, _subStage, content)
        .send({ from: account });
      console.log(tx);

      alert("Update LC success");
      setIsModalVisible(false);
    } catch (error) {
      console.error(error);
    }
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
      const contentHash = [
        keccak256(asciiToHex("Hash of LC Document Number")),
        keccak256(asciiToHex("Hash of LC 1")),
        keccak256(asciiToHex("Hash of LC 2")),
        keccak256(asciiToHex("Hash of LC 3")),
        keccak256(asciiToHex("Hash of LC information")),
      ];

      const signedTime = new BN(values.signedTime);

      const numOfDocument = new BN(values.numOfDocument);
      const ROOT_HASH =
        "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
      const documentId = keccak256(asciiToHex(values.documentId));
      const url = values.url;

      const ackMessageHash = LCMiddleware.generateAcknowledgeMessageHash(
        contentHash.slice(1, numOfDocument.add(new BN("1")).toNumber())
      );

      const acknowledgeSignature = await web3.eth.personal.sign(
        ackMessageHash,
        account,
        ""
      );
      const approvalMessageHash = LCMiddleware.generateApprovalMessageHash({
        rootHash: ROOT_HASH,
        prevHash: documentId,
        contentHash,
        URL: url,
        signedTime,
        acknowledgeSignature,
      });

      const approvalSignature = await web3.eth.personal.sign(
        approvalMessageHash,
        account,
        ""
      );

      web3.eth.handleRevert = true;
      const standardFactory = LCMiddleware.loadContract(
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

      const tx = await standardFactory.methods
        .create(parties, content)
        .send({ from: account });
      console.log(tx);
      alert("Create LC success");
    } catch (error) {
      console.log(error);
      error.message && alert(error.message);
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
        LCMiddleware.loadContract(new Web3("http://1.54.89.229:32278"));
      const { _contract: lcAddress } = await routerService.methods
        .getAddress(values.documentId)
        .call();
      console.log(lcAddress);
      if (/^0x0+$/.test(lcAddress)) return alert("DocumentId not found");
      // setAmendLCSelected(lcAddress);

      const amendStage = +values.stage;
      const amendSubStage = +values.subStage;

      const amendStageContent = await routerService.methods
        .getStageContent(values.documentId, values.stage, values.subStage)
        .call();

      if (amendStageContent.signature === "0x")
        return alert("Stage Amend not found");

      const rootHash = await await routerService.methods
        .getRootHash(values.documentId)
        .call();

      // get prevHash
      const prevHash = LCMiddleware.generateStageHash({
        rootHash: amendStageContent.rootHash,
        prevHash: amendStageContent.prevHash,
        URL: amendStageContent.url,
        contentHash: amendStageContent.contentHash,
        signedTime: amendStageContent.signedTime,
        approvalSignature: amendStageContent.signature,
        acknowledgeSignature: amendStageContent.acknowledge,
      });

      const migrateStages = [{ stage: 1, subStage: 1 }];
      const migrating_stages = await Promise.all(
        migrateStages.map(async (s) => {
          const content = await routerService.methods
            .getStageContent(values.documentId, s.stage, s.subStage)
            .call();
          return LCMiddleware.generateStageHash({
            rootHash: content.rootHash,
            prevHash: content.prevHash,
            URL: content.url,
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
      if (amendStage == 4 || amendStage == 5) {
        const ackMessageHash = LCMiddleware.generateAcknowledgeMessageHash(
          contentHash.slice(1, numOfDocument + 1)
        );

        acknowledge_sig = await web3.eth.personal.sign(
          ackMessageHash,
          account,
          ""
        );
      }

      // get approval signature
      const messageHash = LCMiddleware.generateApprovalMessageHash({
        rootHash,
        prevHash,
        contentHash,
        URL: url,
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

      const amendHash = LCMiddleware.generateAmendMessageHash(
        migrating_stages,
        {
          stage: amendStage,
          subStage: amendSubStage,
          content: {
            rootHash,
            prevHash,
            contentHash,
            URL: url,
            signedTime,
            numOfDocuments: numOfDocument,
            acknowledgeSignature: acknowledge_sig,
            approvalSignature: approval_sig,
          },
        }
      );

      const amend_sig = await web3.eth.personal.sign(amendHash, account, "");

      const tx = await routerService.methods
        .submitAmendment(
          values.documentId,
          migrating_stages,
          amend_stage,
          amend_sig
        )
        .send({ from: account });
      console.log(tx);
      alert("submit amendment success");
    } catch (error) {
      console.log(error);
      error.message && alert(error.message);
    }
  };

  // const handleFullfillAmend = async (values) => {
  //   try {
  //     if (!web3 || !account) {
  //       alert("Connect to Metamask!");
  //       return;
  //     }
  //     if (chainId !== CHAIN_ID) {
  //       await setupDefaultNetwork();
  //     }

  //     const routerService = new web3.eth.Contract(
  //       RouterABI,
  //       ROUTER_SERVICE_ADDRESS
  //     );

  //     const request = await routerService.methods
  //       .getAmendmentRequest(values.documentId, values.requestID)
  //       .call();
  //     if (!request) alert("Amend request not found!");
  //     console.log(request);
  //     await routerService.methods
  //       .fulfillAmendment(values.documentId, values.requestID)
  //       .call()
  //       .then((res) => alert("Fullfill success!"))
  //       .catch((err) => console.log(err));
  //   } catch (error) {
  //     console.log(error);
  //     error.message && alert(error.message);
  //   }
  // };

  // const handleApproveAmend = async (values) => {
  //   try {
  //     if (!web3 || !account) {
  //       alert("Connect to Metamask!");
  //       return;
  //     }
  //     if (chainId !== CHAIN_ID) {
  //       await setupDefaultNetwork();
  //     }

  //     console.log(values);

  //     const routerService = new web3.eth.Contract(
  //       RouterABI,
  //       ROUTER_SERVICE_ADDRESS
  //     );

  //     const request = await routerService.methods
  //       .getAmendmentRequest(values.documentId, values.requestID)
  //       .call();
  //     console.log(request);
  //     const isApproved = await routerService.methods
  //       .isAmendApproved(values.documentId, values.requestID)
  //       .call();
  //     if (isApproved) return alert("Amend request has been approved!");
  //     const approval_sig = await gen_amend_sig(
  //       request.migratingStages,
  //       request.amendStage
  //     );

  //     const amend_sig = await web3.eth.personal.sign(approval_sig, account, "");

  //     await routerService.methods
  //       .approveAmendment(values.documentId, values.requestID, amend_sig)
  //       .send({ from: account });
  //     console.log("Approve amendment success");
  //   } catch (error) {
  //     console.log(error);
  //     error.message && alert(error.message);
  //   }
  // };

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
              Create
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
        // onFinish={
        // 	keyMenu === "ApproveAmend" ? handleApproveAmend : handleFullfillAmend
        // }
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {/* {labelApproveAmendLCList.map((idx, label) => FormItem(idx, label))}
				{lcData?.lc?.submittedAmendments.map((submittedAmend, idx) => (
					<div style={{ display: "flex", flexDirection: "column" }}>
						<div>Submitted Amendment</div>
						<pre>
							Proposer: {submittedAmend.proposer}
							<br />
							ID: {submittedAmend.id.replace("-", "\nRequest ID: ")}
							<br />
							Nonce: {submittedAmend.nonce}
						</pre>
						<div>Approved Amendment</div>
	          <pre>
	            Approver: {submittedAmend.approvedAmendments[0].approver ?? " "}
	            <br />
	            ID: {submittedAmend.approvedAmendments[0].id ?? " "}
	          </pre>
					</div>
				))} */}
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
            {keyMenu === "CreateLC" ? (
              CreateLCForm()
            ) : keyMenu === "GetStageContent" ? (
              GetStageContentForm()
            ) : keyMenu === "Amend" ? (
              AmendLCForm()
            ) : keyMenu === "ApproveAmend" ? (
              ApproveAmendForm()
            ) : keyMenu === "ApproveLC" ? (
              UpdateLCForm()
            ) : (
              <>Nothing to show</>
            )}
            {/* {(keyMenu === "StandardLC" ||
              keyMenu === "UPASLC" ||
              keyMenu === "ConnectMetamask") &&
            lc ? (
              <Descriptions
                title={`Standard LC ${lc.documentId}`}
                layout="vertical"
                column={4}
                bordered={true}
              >
                <Descriptions.Item label="Document ID">{`${lc.documentId}`}</Descriptions.Item>
                <Descriptions.Item label="Latest Stage">{`${lc.documentId}`}</Descriptions.Item>
                <Descriptions.Item label="Approve LC">
                  <Button onClick={showModal}>Approve</Button>
                </Descriptions.Item>
                <Descriptions.Item label="Creation Transaction">
                  <Anchor>
                    <Link
                      href={`http://1.54.89.231:31000/tx/${
                        lc.subStageChangeApproves[0].id.split("-")[0]
                      }/logs`}
                      title={lc.id}
                    />
                  </Anchor>
                </Descriptions.Item>
              </Descriptions>
            ) : keyMenu === "CreateLC" 
            ? (
              CreateLCForm()
            ) : keyMenu === "ApproveLC" && lc ? (
              <>Approve</>
            ) : keyMenu === "Amend" ? (
              AmendLCForm()
            ) : keyMenu === "ApproveAmend" ? (
              ApproveAmendForm()
            ) : keyMenu === "FullfillAmend" ? (
              ApproveAmendForm()
            ) : (
              <>Nothing to show</>
            )} */}
          </Content>
          {/* {ApproveModal()} */}
        </Layout>
        {/* {lc ?? <Content>{lc.map((data) => console.log(data))}</Content>} */}
      </Layout>
    </Layout>
  );
}

export default App;
