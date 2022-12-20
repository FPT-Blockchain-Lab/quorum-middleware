import BN from "bn.js";
import Web3 from "web3";
import { OrgManager } from "../bindings/permission";
import { PermissionContractABIs } from "../abi/permission";
import { PermissionContractAddresses } from "../config";
import { AbiItem } from "web3-utils";

const SIGNATURE_LENGTH = 132;

export async function validateCreateStandardLC(
    web3: Web3,
    _parties: (string | number[])[],
    _content: [
        string | number[], // rootHash
        number | string | BN, // signedTime
        string | number[], // prevHash
        number | string | BN, // numOfDocuments
        (string | number[])[], // contentHash
        string, // url
        string | number[], // acknowledge
        string | number[] // signature
    ]
) {
    let error = "";
    const OrgManager = new web3.eth.Contract(
        PermissionContractABIs.OrgManager as any[] as AbiItem[],
        PermissionContractAddresses.OrgManager
    ) as any as OrgManager;

    if (_parties.length != 4) {
        error = "The number of involved parties does not match. Expected 4.";
    }

    const orgExsists =
        (await OrgManager.methods.checkOrgExists(_parties[0].toString()).call()) && (await OrgManager.methods.checkOrgExists(_parties[1].toString()).call());

    if (!orgExsists) {
        error = "Organization at index 0 or 1 does not exsist.";
    }

    if (_content[3] > _content[4].length) {
        error = "The number of documents cannot greater than the length of content hash.";
    }

    if (_content[2] != _content[4][0]) {
        error = "Unlink to previous";
    }

    if (_content[1] > Date.now()) {
        error = "Signed time cannot greater than current.";
    }

    if (_content[6].length != SIGNATURE_LENGTH) {
        error = "Invalid acknowledge signature.";
    }

    if (_content[7].length != SIGNATURE_LENGTH) {
        error = "Invalid approval signature.";
    }

    return error;
}

export async function validateCreateUPASLC(
    web3: Web3,
    _parties: (string | number[])[],
    _content: [
        string | number[], // rootHash
        number | string | BN, // signedTime
        string | number[], // prevHash
        number | string | BN, // numOfDocuments
        (string | number[])[], // contentHash
        string, // url
        string | number[], // acknowledge
        string | number[] // signature
    ]
) {
    let error = "";
    const OrgManager = new web3.eth.Contract(
        PermissionContractABIs.OrgManager as any[] as AbiItem[],
        PermissionContractAddresses.OrgManager
    ) as any as OrgManager;
    const orgExsists =
        (await OrgManager.methods.checkOrgExists(_parties[0].toString()).call()) &&
        (await OrgManager.methods.checkOrgExists(_parties[1].toString()).call()) &&
        (await OrgManager.methods.checkOrgExists(_parties[2].toString()).call());

    if (_parties.length != 5) {
        error = "The number of involved parties does not match. Expected 5.";
    }

    if (!orgExsists) {
        error = "Organization at index 0 or 1 does not exsist.";
    }

    if (_content[3] > _content[4].length) {
        error = "The number of documents cannot greater than the length of content hash.";
    }

    if (_content[2] != _content[4][0]) {
        error = "Unlink to previous.";
    }

    if (_content[1] > Date.now()) {
        error = "Signed time cannot greater than current.";
    }

    if (_content[6].length != SIGNATURE_LENGTH) {
        error = "Invalid acknowledge signature.";
    }

    if (_content[7].length != SIGNATURE_LENGTH) {
        error = "Invalid approval signature.";
    }

    return error;
}

export function validateApproveLC(
    web3: Web3,
    _documentId: number | string | BN,
    _stage: number | string | BN,
    _subStage: number | string | BN,
    _content: [
        string | number[],
        number | string | BN,
        string | number[],
        number | string | BN,
        (string | number[])[],
        string,
        string | number[],
        string | number[]
    ]
) {
    console.log(web3, _documentId, _stage, _subStage, _content);
}
