Quorum Middleware

Multi-language middleware.
Currently supported languages: JavaScript, Java

```
curl -s "https://get.sdkman.io" | bash
sdk install java
sdk install maven

curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.2/install.sh | bash
nvm install --lts
npm install -g yarn
```

```
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
npm install -g typescript typechain @typechain/web3-v1
```

```
find ./abis/lc -maxdepth 1 -type f -exec web3j generate solidity -a={} --outputDir=java/src/main/java/com/fptblockchainlab/bindings --package=lc \;
find ./abis/permission -maxdepth 1 -type f -exec web3j generate solidity -a={} --outputDir=java/src/main/java/com/fptblockchainlab/bindings --package=permission \;
typechain --target=web3-v1 --out-dir=javascript/src/bindings/lc 'abis/lc/*.abi'
typechain --target=web3-v1 --out-dir=javascript/src/bindings/permission 'abis/permission/*.abi'
```

```
mvn clean
mvn compiler:compile
```

```
yarn run build
```