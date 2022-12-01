import { asciiToHex, utf8ToHex, keccak256 } from "web3-utils";
import { expect } from "chai";
import Web3 from "web3";
import BN from "bn.js";
import { Middleware } from "../src/main";

const ROOT_HASH = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";
const documentId = keccak256(asciiToHex("Document 1"));
const numOfDocument = 3;
const contentHash = [
    keccak256(asciiToHex("Hash of LC Document Number")),
    keccak256(asciiToHex("Hash of LC 1")),
    keccak256(asciiToHex("Hash of LC 2")),
    keccak256(asciiToHex("Hash of LC 3")),
    keccak256(asciiToHex("Hash of LC information")),
];
const acknowledgeSignature =
    "0x0690111fffba5193a9c41459021c6526b2c286f5968c24b07754d5e2839aba06552a6014bc5e0a356ce4c681bbb42bdf3732deb38fe76278b5aa0b0447d219bd1b";

describe("Hash message testing", () => {
    it("Generate acknowledge message hash", () => {
        const messageHash = Middleware.LC.generateAcknowledgeMessageHash(contentHash.slice(1, numOfDocument + 1));

        expect(messageHash).to.deep.eq("0xb4bfd37a323afdf767e777452a6befaeb3823cd934b9d208388865b127faa770");
    });

    it("Generate approval message hash", () => {
        const messageHash = Middleware.LC.generateApprovalMessageHash({
            rootHash: ROOT_HASH,
            prevHash: documentId,
            contentHash,
            url: "https://fpt.com.vn/LCPlatform/",
            signedTime: new BN("1669261586"),
            acknowledgeSignature,
        });

        expect(messageHash).to.deep.eq("0x751dc41c674b71ca1336861eca08f040134ed68fe144c2417e440422d956b1eb");
    });

    it("Generate request id", () => {
        const proposer = "0x0FDa058408bD342DA277A1bEb0DFa9F9145C3cfE";
        const nonce = new BN(3333);

        const requestId = Middleware.LC.generateRequestId(proposer, nonce);

        expect(requestId).to.deep.eq("0x79dadfe586c911bb0de0c07736fd5c0478e69de542b867ad0a4dc240d68ae8e6");
    });

    it("Get standard lc contract", async () => {
        const { StandardLCFactory } = Middleware.LC.loadContract(new Web3("http://1.54.89.229:32278"));

        const address = await StandardLCFactory?.methods.getLCAddress("102002754747479172180633994781527983425432706666881898828191417484204372588497").call();

        expect(address?.at(0)).to.deep.eq("0x9CbB5f2C4Aee5383e65fc9826527302430c28a0f");
    });

    it("Stage hash", () => {
        const messageHash = Middleware.LC.generateStageHash({
            rootHash: "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470",
            prevHash: "0x1841d653f9c4edda9d66a7e7737b39763d6bd40f569a3ec6859d3305b72310e6",
            contentHash: [
                "0xb4d0ada81a05d1b9d1647929f59495053ce478189fc2af55d72e74a1f06b94a9",
                "0x7884deeb865048dfe3b12ebc2093ba764be251624fe5f317920bde308ca45b13",
                "0x87a769378d095d70a264efcac7df27ed2179d6905bab7e608fdd2a973b89a208",
                "0x6a03827c16d56fb40f423d70c108f74c80fd19bf29b01dab5ba15c7d0e3b42c1",
                "0x7d6599f79c3b43d26b457bf9624c0cc2e30ebfbac1a0c042f8899b21b05240e3",
            ],
            url: "google.com",
            signedTime: new BN("3000"),
            acknowledgeSignature: "0x",
            approvalSignature:
                "0x4a6539668647e326e581212eb3fc40e59ac51451b78b1cb361742bfedca42f6d116523e04d4d4c9aede3e3364c75fd6d85851a9314e1de494a8f1204cc6770491b",
        });

        expect(messageHash).to.deep.eq("0x8f4d7d2917d64cf7009c8196071ba861ced910a491e05331d88ad5aa7cf4449b");
    });
});
