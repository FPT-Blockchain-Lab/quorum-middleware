import BN from "bn.js";

const SIGNATURE_LENGTH = 132;

export function validateCreateStandardLC(
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
    if (_parties.length != 4) {
        throw "The number of involved parties does not match. Expected 4.";
    }

    if (_content[3] > _content[4].length) {
        throw "The number of documents cannot greater than the length of content hash.";
    }

    if (_content[2] != _content[4][0]) {
        throw "Unlink to previous";
    }

    if (_content[1] > Date.now()) {
        throw "Signed time cannot greater than current.";
    }

    if (_content[6].length != SIGNATURE_LENGTH) {
        throw "Invalid acknowledge signature.";
    }

    if (_content[7].length != SIGNATURE_LENGTH) {
        throw "Invalid approval signature.";
    }
}

export function validateCreateUPASLC(
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
    if (_parties.length != 5) {
        throw "The number of involved parties does not match. Expected 5.";
    }

    if (_content[3] > _content[4].length) {
        throw "The number of documents cannot greater than the length of content hash.";
    }

    if (_content[2] != _content[4][0]) {
        throw "Unlink to previous.";
    }

    if (_content[1] > Date.now()) {
        throw "Signed time cannot greater than current.";
    }

    if (_content[6].length != SIGNATURE_LENGTH) {
        throw "Invalid acknowledge signature.";
    }

    if (_content[7].length != SIGNATURE_LENGTH) {
        throw "Invalid approval signature.";
    }
}

export function validateApproveLC(
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
) {}
