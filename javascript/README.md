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

const web3 = new Web3("http://localhost:8545");
const lcWrapper = new LCWrapper(web3);
const permissionWrapper = new PermissionWrapper(web3);
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
    | numOfDocuments | BN |
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
    | numOfDocuments | BN |
    | acknowledgeSignature | string |
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.approveLC(documentId, stage, subStage, content, from)

-   `documentId`: _string_ - hash of documentId
-   `stage`: _BN_
-   `subStage`: _BN_
-   `content`: _object_
    | Name | Type |
    |----------------------|----------|
    | contentHash | string[] |
    | url | string |
    | signedTime | BN |
    | numOfDocuments | BN |
    | acknowledgeSignature | string |
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.closeLC(documentId, from)

-   `documentId`: _string_ - hash of documentId
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.submitAmendment(documentId, stage, subStage, content, migrateStages, from)

-   `documentId`: _string_ - hash of documentId
-   `stage`: _BN_
-   `subStage`: _BN_
-   `content`: _object_
    | Name | Type |
    |----------------------|----------|
    | contentHash | string[] |
    | url | string |
    | signedTime | BN |
    | numOfDocuments | BN |
    | acknowledgeSignature | string |
-   `migrateStages`:
-   `from`: _string_ - account send transaction

Return transaction receipt

#### LCWrapper.approveAmendment(documentId, nonce, from)

-   `documentId`: _string_ - hash of documentId
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
