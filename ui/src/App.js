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
import {
  CHAIN_ID,
  checkStage,
  postMethod,
  setupDefaultNetwork,
  EMPTY_BYTES,
  STANDARD_FACTORY_ADDRESS,
  ROUTER_SERVICE_ADDRESS,
  AMEND_REQUEST_ADDRESS,
} from "./utils";
import BN from "bn.js";
import { LC } from "@tuannm106/quorum-middleware";
import lcABI from "./abis/LC.json";
const { Header, Sider, Content } = Layout;
const { Link } = Anchor;

const standardColumns = [
  {
    title: "TransactionHash",
    dataIndex: "id",
    key: "id",
  },
  {
    title: "Document ID",
    dataIndex: "documentId",
    key: "documentId",
  },
  {
    title: "Creator",
    dataIndex: "creator",
    key: "creator",
  },
];

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

const labelAmendLCList = ["documentId", "stage", "subStage"];
const labelApproveAmendLCList = ["documentId", "requestID"];

function App() {
  const [lc, setLC] = useState(undefined);
  const [subStage, setSubStage] = useState(undefined);
  const [keyMenu, setKeyMenu] = useState("");
  const [web3, setWeb3] = useState(new Web3());
  const [account, setAccount] = useState(undefined);
  const [chainId, setChainId] = useState(undefined);
  const [lcInput, setLcInput] = useState({});
  const [isModalVisible, setIsModalVisible] = useState(false);

  const [migrateStages, setMigrateStages] = useState([]);
  const [amendLCSelected, setAmendLCSelected] = useState("");

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
    const routerService = LC.loadContract(
      new Web3("http://1.54.89.229:32278")
    ).RouterService;
    console.log("Values:", values);
    const tx = await routerService.methods
      .getStageContent(values.documentId, values.stage, values.subStage)
      .call();
    console.log(tx);
    setLC(tx);
  };

  const handleUpdateLC = async () => {
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      const standardFactory = LC.loadContract(
        new Web3("http://1.54.89.229:32278")
      ).StandardLCFactory;

      const contentHash = [
        keccak256(asciiToHex("Hash of LC Document Number")),
        keccak256(asciiToHex("Hash of LC 1")),
        keccak256(asciiToHex("Hash of LC 2")),
        keccak256(asciiToHex("Hash of LC 3")),
        keccak256(asciiToHex("Hash of LC information")),
      ];

      const routerService = LC.loadContract(
        new Web3("http://1.54.89.229:32278")
      ).RouterService;

      const stageInfo = routerService.methods.getStageContent();
      const lcContract = new web3.eth.Contract(lcABI, lcAddress);
      const ROOT_HASH = lcContract.methods.getRootHash().call();
      const prevContent = await lcContract.methods
        .getContent(subStage.stage, subStage.subStage)
        .call();
      const prevHash = LC.generateStageHash(
        prevContent.rootHash,
        prevContent.prevHash,
        prevContent.contentHash,
        prevContent.url,
        prevContent.signedTime,
        prevContent.signature,
        prevContent.acknowledge
      );
      const signedTime = new BN("1669261586");
      const _stage = +subStage.stage + 1;
      const _subStage = subStage.subStage;
      const numOfDocument = 3;
      let acknowledge_sig = EMPTY_BYTES; //  Acknowledge signature only for Stage 1, Stage 4 and Stage 5
      const url = "https://fpt.com.vn/LCPlatform/standardLC/";

      if (_stage == 4 || _stage == 5) {
        const ackMessageHash = LC.generateAcknowledgeMessageHash(
          contentHash.slice(1, numOfDocument + 1)
        );

        acknowledge_sig = await web3.eth.personal.sign(
          ackMessageHash,
          account,
          ""
        );
      }
      const messageHash = LC.generateApprovalMessageHash({
        rootHash: ROOT_HASH,
        prevHash: prevHash,
        contentHash,
        URL: url,
        signedTime: new BN("1669261586"),
        acknowledge_sig,
      });
      const approval_sig = await web3.eth.personal.sign(
        messageHash,
        account,
        ""
      );
      const content = [
        ROOT_HASH,
        signedTime,
        prevHash,
        numOfDocument,
        contentHash,
        url,
        acknowledge_sig,
        approval_sig,
      ];

      const tx = await lcContract.methods
        .approve(account, _stage, _subStage, content)
        .send({ from: account });
      console.log(tx);

      // refetch();
      alert("Update LC success");
      setIsModalVisible(false);
    } catch (error) {
      console.error(error);
    }
  };

  const handleCreateLC = async (values) => {
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }

      const contentHash = [
        keccak256(asciiToHex("Hash of LC Document Number")),
        keccak256(asciiToHex("Hash of LC 1")),
        keccak256(asciiToHex("Hash of LC 2")),
        keccak256(asciiToHex("Hash of LC 3")),
        keccak256(asciiToHex("Hash of LC information")),
      ];

      if (isNaN(values.signedTime))
        return alert("Input signedTime is a number");
      const signedTime = new BN("1669261586");

      if (isNaN(values.numOfDocument))
        return alert("Input numOfDocument is a number (demo min 1, max 4)");
      const numOfDocument = 3;
      const ROOT_HASH =
        "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
      const documentId = keccak256(asciiToHex(values.documentId));
      const url = "https://fpt.com.vn/LCPlatform/";

      const ackMessageHash = LC.generateAcknowledgeMessageHash(
        contentHash.slice(1, numOfDocument + 1)
      );

      const acknowledgeSignature = await web3.eth.personal.sign(
        ackMessageHash,
        account,
        ""
      );
      console.log(acknowledgeSignature);
      // const acknowledgeSignature =
      //   "0x0690111fffba5193a9c41459021c6526b2c286f5968c24b07754d5e2839aba06552a6014bc5e0a356ce4c681bbb42bdf3732deb38fe76278b5aa0b0447d219bd1b";
      const messageHash = LC.generateApprovalMessageHash({
        rootHash: ROOT_HASH,
        prevHash: documentId,
        contentHash,
        URL: url,
        signedTime: "1669261586",
        acknowledgeSignature,
      });
      console.log("messageHash", messageHash);
      const approval_sig = await web3.eth.personal.sign(
        messageHash,
        account,
        ""
      );
      console.log("approval_sig", approval_sig);
      const ORGS = [
        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
        "0x5bac6b7287fd56f459d23e62797ff954588ef68cc8dabdce6a0e319f2883ac1a",
      ];

      web3.eth.handleRevert = true;
      const standardFactory = LC.loadContract(
        new Web3("http://1.54.89.229:32278")
      ).StandardLCFactory;

      const parties = [
        "0xfefdd636afe429a7946a6697df05997abe83567ba8429ce977c8fa1527f0993a",
        "0xe53d3c89983f70ac67ba8a0b8a0b247de580637a6b50e00008dcfca1f98fed23",
        "0xa55b8e896143eddcabb730c30946030c365349e6ec290b2263d2813ececb1e76",
        "0x92a3dcb78917ce90e97f6c558e33b74f611f69f539c7588ff2b5d3dd98c94873",
      ];

      console.log("Parties: ", parties);
      const content = [
        ROOT_HASH,
        "1669261586",
        documentId,
        numOfDocument,
        contentHash,
        url,
        acknowledgeSignature,
        approval_sig,
      ];
      console.log(content);

      const tx = await standardFactory.methods
        .create(parties, content)
        .estimateGas({ from: account });
      console.log(tx);
      alert("Create LC success");
    } catch (error) {
      console.log(error);
      error.message && alert(error.message);
    }
  };

  // const handleAmendLC = async (values) => {
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
  //     const { _contract: lcAddress } = await routerService.methods
  //       .getAddress(values.documentId)
  //       .call();
  //     if (/^0x0+$/.test(lcAddress)) return alert("DocumentId not found");
  //     setAmendLCSelected(lcAddress);

  //     const amendStage = +values.stage;
  //     const amendSubStage = +values.subStage;

  //     const lcContract = new web3.eth.Contract(lcABI, lcAddress);

  //     const amendStageContent = await lcContract.methods
  //       .getContent(amendStage, amendSubStage)
  //       .call();

  //     if (amendStageContent.signature === "0x")
  //       return alert("Stage Amend not found");

  //     const rootHash = await lcContract.methods.getRootHash().call();

  //     const migrating_stages = await Promise.all(
  //       migrateStages.map(async (s) => {
  //         const content = await lcContract.methods
  //           .getContent(s.stage, s.subStage)
  //           .call();
  //         return toStageHash(
  //           content.rootHash,
  //           content.prevHash,
  //           content.contentHash,
  //           content.url,
  //           content.signedTime,
  //           content.signature,
  //           content.acknowledge
  //         );
  //       })
  //     );

  //     const prevContent = await lcContract.methods
  //       .getContent(amendStage, amendSubStage)
  //       .call();
  //     const prevHash = toStageHash(
  //       prevContent.rootHash,
  //       prevContent.prevHash,
  //       prevContent.contentHash,
  //       prevContent.url,
  //       prevContent.signedTime,
  //       prevContent.signature,
  //       prevContent.acknowledge
  //     );

  //     const contentHash = [
  //       keccak256(
  //         encodePacked({ v: "Hash of LC Document Number", t: "string" })
  //       ),
  //       keccak256(encodePacked({ v: "Hash of LC 1", t: "string" })),
  //       keccak256(encodePacked({ v: "Hash of LC 2", t: "string" })),
  //       keccak256(encodePacked({ v: "Hash of LC 3", t: "string" })),
  //       keccak256(encodePacked({ v: "Hash of LC information", t: "string" })),
  //     ];
  //     const signedTime = Math.floor(Date.now() / 1000);
  //     const numOfDocument = 3;
  //     let acknowledge_sig = EMPTY_BYTES; //  Acknowledge signature only for Stage 1, Stage 4 and Stage 5
  //     const url = "https://fpt.com.vn/LCPlatform/standardLC/";

  //     if (amendStage == 4 || amendStage == 5) {
  //       const ackMessageHash = hashMessageSign(
  //         toAcknowledgeMessageSigns(contentHash.slice(1, 4))
  //       );

  //       acknowledge_sig = await web3.eth.personal.sign(
  //         ackMessageHash,
  //         account,
  //         ""
  //       );
  //     }
  //     const messageHash = hashMessageSign(
  //       toLCMessageSigns({
  //         rootHash,
  //         prevHash,
  //         contentHash,
  //         url,
  //         signedTime,
  //         acknowledgeSign: acknowledge_sig,
  //       })
  //     );
  //     const approval_sig = await web3.eth.personal.sign(
  //       messageHash,
  //       account,
  //       ""
  //     );
  //     const content = [
  //       rootHash,
  //       signedTime,
  //       prevHash,
  //       numOfDocument,
  //       contentHash,
  //       url,
  //       acknowledge_sig,
  //       approval_sig,
  //     ];

  //     const amend_stage = [amendStage, amendSubStage, content];

  //     const amendRequest = new web3.eth.Contract(
  //       AmendABI,
  //       AMEND_REQUEST_ADDRESS
  //     );

  //     const nonce = await amendRequest.methods.nonces(account).call();

  //     // requestId = gen_request_id(admin.address, nonce);
  //     const amendHash = gen_amend_sig(migrating_stages, amend_stage);

  //     const amend_sig = await web3.eth.personal.sign(amendHash, account, "");

  //     await routerService.methods
  //       .submitAmendment(
  //         values.documentId,
  //         migrating_stages,
  //         amend_stage,
  //         amend_sig
  //       )
  //       .send({ from: account });
  //     console.log("submit amendment success");
  //   } catch (error) {
  //     console.log(error);
  //     error.message && alert(error.message);
  //   }
  // };

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
      </>
    );
  };
  // const AmendLCForm = () => {
  //   return (
  //     <Form
  //       name="basic"
  //       labelCol={{
  //         span: 4,
  //       }}
  //       wrapperCol={{
  //         span: 16,
  //       }}
  //       initialValues={{
  //         remember: true,
  //       }}
  //       onFinish={handleAmendLC}
  //       onFinishFailed={onFinishFailed}
  //       autoComplete="off"
  //     >
  //       {labelAmendLCList.map((idx, label) => FormItem(idx, label))}
  //       <div>Migrate stages</div>
  //       {lcData?.lc?.subStageChangeApproves.map((stage, idx) => (
  //         <div style={{ display: "flex" }}>
  //           <input
  //             type="checkbox"
  //             onChange={(e) => {
  //               let existIdx = migrateStages.findIndex(
  //                 (s) => s.stage == stage.stage && s.subStage == stage.subStage
  //               );
  //               if (existIdx !== -1) {
  //                 // remove
  //                 setMigrateStages((pre) => [
  //                   ...pre.slice(0, existIdx),
  //                   ...pre.slice(existIdx + 1, pre.length),
  //                 ]);
  //               } else {
  //                 setMigrateStages((pre) => [
  //                   ...pre,
  //                   { stage: stage.stage, subStage: stage.subStage },
  //                 ]);
  //               }
  //             }}
  //           />
  //           <div>
  //             Stage: {stage.stage}; substage: {stage.subStage}
  //           </div>
  //         </div>
  //       ))}
  //       <Form.Item
  //         wrapperCol={{
  //           offset: 8,
  //           span: 16,
  //         }}
  //       >
  //         <Button type="primary" htmlType="submit">
  //           Amend
  //         </Button>
  //       </Form.Item>
  //     </Form>
  //   );
  // };

  // const ApproveAmendForm = () => {
  //   return (
  //     <Form
  //       name="basic"
  //       labelCol={{
  //         span: 4,
  //       }}
  //       wrapperCol={{
  //         span: 16,
  //       }}
  //       initialValues={{
  //         remember: true,
  //       }}
  //       onFinish={
  //         keyMenu === "ApproveAmend" ? handleApproveAmend : handleFullfillAmend
  //       }
  //       onFinishFailed={onFinishFailed}
  //       autoComplete="off"
  //     >
  //       {labelApproveAmendLCList.map((idx, label) => FormItem(idx, label))}
  //       {lcData?.lc?.submittedAmendments.map((submittedAmend, idx) => (
  //         <div style={{ display: "flex", flexDirection: "column" }}>
  //           <div>Submitted Amendment</div>
  //           <pre>
  //             Proposer: {submittedAmend.proposer}
  //             <br />
  //             ID: {submittedAmend.id.replace("-", "\nRequest ID: ")}
  //             <br />
  //             Nonce: {submittedAmend.nonce}
  //           </pre>
  //           {/* <div>Approved Amendment</div>
  //           <pre>
  //             Approver: {submittedAmend.approvedAmendments[0].approver ?? " "}
  //             <br />
  //             ID: {submittedAmend.approvedAmendments[0].id ?? " "}
  //           </pre> */}
  //         </div>
  //       ))}
  //       <Form.Item
  //         wrapperCol={{
  //           offset: 8,
  //           span: 16,
  //         }}
  //       >
  //         <Button type="primary" htmlType="submit">
  //           {keyMenu === "ApproveAmend" ? "Approve Amend" : "Fullfill Amend"}
  //         </Button>
  //       </Form.Item>
  //     </Form>
  //   );
  // };

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
      </Layout>
    </Layout>
  );
}

export default App;
