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
const labelApproveAmendLCList = ["documentId", "nonce"];
const labelFullfillAmendLCList = ["documentId", "nonce"];
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

      const signedTime = new BN(values.signedTime);

      const numOfDocument = new BN(values.numOfDocument);
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));
      const url = values.url;

      /**
       * example content hash
       */
      const contentHash = [
        documentId,
        keccak256(asciiToHex("Hash of LC 1")),
        keccak256(asciiToHex("Hash of LC 2")),
        keccak256(asciiToHex("Hash of LC 3")),
        keccak256(asciiToHex("Hash of LC information")),
        keccak256(asciiToHex("Hash of LC information")),
      ];

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

      const content = {
        prevHash: documentId,
        contentHash: contentHash,
        url: url,
        signedTime: signedTime,
        numOfDocuments: numOfDocument,
      };

      const wrapperContract = new Middleware.LCWrapper(web3);
      const tx = await wrapperContract.createStandardLC(
        parties,
        content,
        account
      );

      console.log(tx);
      alert("Create LC success");
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
      const signedTime = new BN(values.signedTime);
      const _stage = new BN(values.stage);
      const _subStage = new BN(values.subStage);
      const numOfDocument = new BN(values.numOfDocument);
      const url = values.url;
      const wrapperContract = new Middleware.LCWrapper(web3);
      const tx = await wrapperContract.approveLC(
        documentId,
        _stage,
        _subStage,
        {
          signedTime,
          numOfDocuments: numOfDocument,
          contentHash,
          url,
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

      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));
      const wrapperContract = new Middleware.LCWrapper(web3);
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
      // Generate documentId
      const documentId = keccak256(asciiToHex(values.documentId));
      const migrateStages = [{ stage: 1, subStage: 1 }];

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
      const numOfDocuments = 3;
      const url = "https://fpt.com.vn/LCPlatform/standardLC/";

      const wrapperContract = new Middleware.LCWrapper(web3);
      // MUST STORE nonce TO APPROVE AND FULLFILL AMEND LC
      const tx = await wrapperContract.submitAmendment(
        documentId,
        +values.stage,
        +values.subStage,
        {
          signedTime,
          numOfDocuments,
          url,
          contentHash,
        },
        migrateStages,
        account
      );
      console.log(tx);
      // MUST STORE nonce TO APPROVE AND FULLFILL AMEND LC
      console.log(tx.events[0].raw.topics[3]); // MUST convert nonce in heximal number to decimal number
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

      const documentId = keccak256(asciiToHex(values.documentId));
      const wrapperContract = new Middleware.LCWrapper(web3);
      const tx = await wrapperContract.approveAmendment(
        documentId,
        new BN(values.nonce),
        account
      );

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

      const documentId = keccak256(asciiToHex(values.documentId));
      const wrapperContract = new Middleware.LCWrapper(web3);
      const tx = await wrapperContract.fulfillAmendment(
        documentId,
        new BN(values.nonce),
        account
      );

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
