# Quorum Middleware in Javascript

## Installation

```
npm i quorum-middleware
```

or

```
yarn add quorum-middleware
```

## Usage

```
import Web3 from "web3";
import { LC, LCWrapper, Permission, PermissionWrapper } from "quorum-middleware";
```

```
const web3 = new Web3("http://localhost:8545");
const lc = new LC(web3);
const lcWrapper = new LCWrapper(web3);
const permission = new Permission(web3);
const permissionWrapper = new PermissionWrapper(web3);
```

OR

```
const web3 = new Web3("http://localhost:8545");

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
        PermissionsImplementation: "0x48272bB87cdA70aCd600B7B8883ada2e799983b8",
        PermissionsInterface: "0x638449F761362fB1041a75eE92e86a2e8fe92780",
        PermissionsUpgradable: "0x40686513a73B3e861810345B08Ca2d952CE8E4F4",
        NodeManager: "0xeE2EF0e68A72654C20c6f3dfa43e03816f362B4F",
        RoleManager: "0x09Ad12B57407dEd24ad2789043F8962fe8679DB3",
        VoterManager: "0xF5E507788E7eaa9Dbf8f24a5D24C2de1F9192416",
    },
    chainId: 6788,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "http://localhost:8545",
}

const lc = new LC(web3, customConfig);
const lcWrapper = new LCWrapper(web3, customConfig);
const permission = new Permission(web3, customConfig);
const permissionWrapper = new PermissionWrapper(web3, customConfig);
```

### API

#### LCWrapper.createStandardLC(parties, content, from)

-   `parties`: _string[]_ - an array of organizations
-   `content`: _object_
    | Name | Type |
    |----------------------|----------|
    | prevHash | string |
    | contentHash | string[] |
    | url | string |
    | signedTime | BN |
    | numOfDocuments | _number_ |
    | acknowledgeSignature | string |
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.createUPASLC(parties, content, from)

-   `parties`: _string[]_ - an array of organizations
-   `content`: _object_
    | Name | Type |
    |----------------------|----------|
    | prevHash | string |
    | contentHash | string[] |
    | url | string |
    | signedTime | BN |
    | numOfDocuments | _number_ |
    | acknowledgeSignature | string |
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.approveLC(documentId, stage, subStage, content, from)

-   `documentId`: _string_ - hash of documentId
-   `stage`: _number_
-   `subStage`: _number_
-   `content`: _object_
    | Name | Type |
    |----------------------|----------|
    | contentHash | string[] |
    | url | string |
    | signedTime | BN |
    | numOfDocuments | number |
    | acknowledgeSignature | string |
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.closeLC(documentId, from)

-   `documentId`: _string_ - hash of documentId
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.submitAmendment(documentId, stage, subStage, content, migrateStages, from)

-   `documentId`: _string_ - hash of documentId
-   `stage`: _number_
-   `subStage`: _number_
-   `content`: _object_
    | Name | Type |
    |----------------------|----------|
    | contentHash | string[] |
    | url | string |
    | signedTime | BN |
    | numOfDocuments | _number_ |
    | acknowledgeSignature | string |
-   `migrateStages`:
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.approveAmendment(documentId, nonce, from)

-   `documentId`: _string_ - hash of documentId
-   `proposer`: _string_ - account submit amend request
-   `nonce`: _BN_ - nonce when approve LC
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.fulfillAmendment(documentId, nonce, from)

-   `documentId`: _string_ - hash of documentId
-   `nonce`: _BN_ - nonce when approve LC
-   `from`: _string_ - account send transaction

Return transaction receipt

#### PermissionWrapper.assignAccountRole(account, orgId, roleId, from)

-   `account`: _string_ - account id
-   `orgId`: _string_ - organization id to which the account belongs
-   `roleId`: _string_ - role id to be assigned to the account
-   `from`: _string_ - account send transaction

Return transaction receipt

#### PermissionWrapper.updateAccountStatus(orgId, account, action, from)

-   `orgId`: _string_ - unique id of the organization to which the account belongs
-   `account`: _string_ - account id
-   `action`: _number_
    | Value | Description |
    |-------|--------------------------------------------|
    | 1 | for suspending the account |
    | 2 | for activating the suspended account |
    | 3 | for denylisting (blacklisting) the account |

-   `from`: _string_ - account send transaction

Return transaction receipt

### Examples

-   Frontend React: [example](https://github.com/FPT-Blockchain-Lab/quorum-middleware/tree/main/example/ui)
