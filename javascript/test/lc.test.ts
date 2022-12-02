import { asciiToHex, keccak256 } from "web3-utils";
import { expect } from "chai";
import Web3 from "web3";
import BN from "bn.js";

import { LC } from "../src/middleware";

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
        const messageHash = LC.generateAcknowledgeMessageHash(contentHash.slice(1, numOfDocument + 1));

        expect(messageHash).to.deep.eq("0xb4bfd37a323afdf767e777452a6befaeb3823cd934b9d208388865b127faa770");
    });

    it("Generate approval message hash", () => {
        const messageHash = LC.generateApprovalMessageHash({
            rootHash: ROOT_HASH,
            prevHash: documentId,
            contentHash,
            URL: "https://fpt.com.vn/LCPlatform/",
            signedTime: new BN("1669261586"),
            acknowledgeSignature,
        });

        expect(messageHash).to.deep.eq("0x751dc41c674b71ca1336861eca08f040134ed68fe144c2417e440422d956b1eb");
    });

    it("Get standard lc contract", async () => {
        const { StandardLCFactory } = LC.loadContract(new Web3("http://1.54.89.229:32278"));

        const address = await StandardLCFactory?.methods.getLCAddress("102002754747479172180633994781527983425432706666881898828191417484204372588497").call();

        expect(address?.at(0)).to.deep.eq("0x9CbB5f2C4Aee5383e65fc9826527302430c28a0f");
    });
});
