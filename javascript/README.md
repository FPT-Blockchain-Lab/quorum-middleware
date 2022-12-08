<p align="center" style="font-size: 48px">
  Quorum Middleware in Javascript
</p>

### Installation

```
npm i quorum-middleware
```

or

```
yarn add quorum-middleware
```

### Usage

```
import Web3 from "web3";
import { Middleware } from "quorum-middleware";

const { LC } = Middleware;
const {
  AmendRequest,
  LCManagement,
  Mode,
  RouterService,
  StandardLCFactory,
  UPASLCFactory,
} = LC.loadContract(new Web3("http://localhost:8545"));
```

### Examples

-   Frontend React: [example](https://github.com/FPT-Blockchain-Lab/quorum-middleware/tree/main/example/ui)
