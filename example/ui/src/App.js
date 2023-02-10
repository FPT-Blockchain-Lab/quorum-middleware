import React, { useEffect, useState } from "react";
import "./App.css";
import { Layout, Menu, Form, Input, Button } from "antd";
import Web3 from "web3";
import { CHAIN_ID, setupDefaultNetwork } from "./utils";
import BN from "bn.js";
import { LCWrapper, LC, Permission, Utils } from "quorum-middleware";
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
const addAccountToOrgList = ["accountId", "orgId", "roleId"];

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
    const routerService = LC.loadContract(
      new Web3("http://1.54.89.229:32278")
    ).RouterService;
    // Generate documentId
    const documentId = Utils.keccak256Utf8(values.documentId);
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
      const documentId = Utils.keccak256Utf8(values.documentId);
      const url = values.url;

      /**
       * example content hash
       */
      const contentHash = [
        documentId,
        Utils.keccak256Utf8("Hash of LC 1"),
        Utils.keccak256Utf8("Hash of LC 2"),
        Utils.keccak256Utf8("Hash of LC 3"),
        Utils.keccak256Utf8("Hash of LC information"),
        Utils.keccak256Utf8("Hash of LC information"),
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

      /**
       * Example for custom config
       * 
       * 
        const customConfig = {
          lCContractAddresses: {
            LCManagement: "0x795C2DeEf13e6D49dF73Ca194250c97511796255",
            Mode: "0x9c2ccBf987b9DC2144ae2c6767475017AeBF42b3",
            RouterService: "0x0B3b181264f97b005e2ceB1Acac082E4ec883d86",
            StandardLCFactory: "0x452EEfa90c9628Df5421e5E1A3fB8F044F2F364b",
            UPASLCFactory: "0x5692c93c784ef84711E6b460CF40577Fc95Bd039",
            AmendRequest: "0xD92cFf94741C78153190f9A060005653f9410d95",
          },
          permissionContractAddresses: {
            OrgManager: "0xf5B2c0829f9485EEB114B11a7C1cb3227B8749Ee",
            AccountManager: "0xc7123BEA05D26823f99bba87a83BADDD021C3c2d",
            PermissionsImplementation:
              "0x48272bB87cdA70aCd600B7B8883ada2e799983b8",
            PermissionsInterface: "0x638449F761362fB1041a75eE92e86a2e8fe92780",
            PermissionsUpgradable: "0x40686513a73B3e861810345B08Ca2d952CE8E4F4",
            NodeManager: "0xeE2EF0e68A72654C20c6f3dfa43e03816f362B4F",
            RoleManager: "0x09Ad12B57407dEd24ad2789043F8962fe8679DB3",
            VoterManager: "0xF5E507788E7eaa9Dbf8f24a5D24C2de1F9192416",
          },
          chainId: 6788,
          chainName: "FPT Quorum Testnet FIS Gateway",
          url: "https://lc-blockchain.dev.etradevn.com/",
        };

        const wrapperContract = new LCWrapper(web3, customConfig);
       */

      const wrapperContract = new LCWrapper(web3);
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
        Utils.keccak256Utf8("Hash of LC Document Number"),
        Utils.keccak256Utf8("Hash of LC 1"),
        Utils.keccak256Utf8("Hash of LC 2"),
        Utils.keccak256Utf8("Hash of LC 3"),
        Utils.keccak256Utf8("Hash of LC information"),
      ];
      // Generate documentId
      const documentId = Utils.keccak256Utf8(values.documentId);
      const signedTime = new BN(values.signedTime);
      const _stage = new BN(values.stage);
      const _subStage = new BN(values.subStage);
      const numOfDocument = new BN(values.numOfDocument);
      const url = values.url;
      const wrapperContract = new LCWrapper(web3);
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
      const documentId = Utils.keccak256Utf8(values.documentId);
      const wrapperContract = new LCWrapper(web3);
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
      const documentId = Utils.keccak256Utf8(values.documentId);
      const migrateStages = [{ stage: LC.Stage.PHAT_HANH_LC, subStage: 1 }];

      /**
       * example content hash
       */
      const contentHash = [
        Utils.keccak256Utf8("Hash of LC Document Number"),
        Utils.keccak256Utf8("Hash of LC 1"),
        Utils.keccak256Utf8("Hash of LC 2"),
        Utils.keccak256Utf8("Hash of LC 3"),
        Utils.keccak256Utf8("Hash of LC information"),
      ];
      // mock data
      const signedTime = Math.floor(Date.now() / 1000);
      const numOfDocuments = 3;
      const url = "https://fpt.com.vn/LCPlatform/standardLC/";

      const wrapperContract = new LCWrapper(web3);
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

      const documentId = Utils.keccak256Utf8(values.documentId);
      const wrapperContract = new LCWrapper(web3);
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

      const documentId = Utils.keccak256Utf8(values.documentId);
      const wrapperContract = new LCWrapper(web3);
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

  const handleAddAccountToOrg = async (values) => {
    try {
      if (!web3 || !account) {
        alert("Connect to Metamask!");
        return;
      }
      if (chainId !== CHAIN_ID) {
        await setupDefaultNetwork();
      }
      console.log(values);

      const { PermissionsInterface } = new Permission.loadContract(web3);

      await PermissionsInterface.methods
        .assignAccountRole(values.accountId, values.orgId, values.roleId)
        .estimateGas({ from: account });
      const tx = await PermissionsInterface.methods
        .assignAccountRole(values.accountId, values.orgId, values.roleId)
        .send({ from: account });

      console.log(tx);
      alert("Add account to org success!");
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

  const AddAccountToOrgForm = () => {
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
        onFinish={handleAddAccountToOrg}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        {addAccountToOrgList.map((idx, label) => FormItem(idx, label))}
        <Form.Item
          wrapperCol={{
            offset: 8,
            span: 16,
          }}
        >
          <Button type="primary" htmlType="submit">
            Add
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
            { key: "AddAccountToOrg", label: "Add account to org" },
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
            ) : keyMenu === "AddAccountToOrg" ? (
              AddAccountToOrgForm()
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
