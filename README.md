# Quorum Middleware

**Multi-language middleware for Quorum blockchain with LC protocol supports**

## Features

- Supported languages: JavaScript, Java
- Platform: Linux, MacOS, WSL

## Prerequisites

```
curl -s "https://get.sdkman.io" | bash
sdk install java # java >= 1.7 (java 8)
sdk install maven #

curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.2/install.sh | bash
nvm install --lts # node >= v14
npm install -g yarn
```

- We have some CLIs required to install to generate bindings

```
wget -O jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64
chmod +x ./jq
sudo mv jq /usr/bin
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
npm install -g typescript typechain @typechain/web3-v1
```

## Build

1. (Optional) Generate bindings

```
find ./abis/lc -maxdepth 1 -type f -exec web3j generate solidity -a={} --outputDir=java/src/main/java/com/fptblockchainlab/bindings --package=lc \;
find ./abis/permission -maxdepth 1 -type f -exec web3j generate solidity -a={} --outputDir=java/src/main/java/com/fptblockchainlab/bindings --package=permission \;
typechain --target=web3-v1 --out-dir=javascript/src/bindings/lc 'abis/lc/*.abi'
typechain --target=web3-v1 --out-dir=javascript/src/bindings/permission 'abis/permission/*.abi'
```

2. Build java

```
mvn clean
mvn compiler:compile
```

3. Build node.js

```
yarn run build
```

## How to run local network (WIP)

1. Go to `quorum-test-network` directory

```
cd quorum-test-network
```

2. Start network

3. (Optional) Install permission contract

```
cd quorum-test-network/smart_contracts/permissioning
yarn
# (optional) install solidity compiler https://github.com/ethereum/solidity/releases/tag/v0.5.17
wget -O solc https://github.com/ethereum/solidity/releases/download/v0.5.17/solc-static-linux
chmod +x solc
sudo mv solc /usr/bin/


# compile solidity
./scripts/compile.sh
# deploy output quorum-test-network/smart_contracts/permissioning/permission-config.json
./scripts/deploy.sh
# copy permisisoned-config.json to goquorum/data for each node
cp quorum-test-network/smart_contracts/permissioning/permission-config.json quorum-test-network/config/permissions

cd quorum-test-network
./restart.sh
```
