import axios from "axios";

export function checkStage(stage) {
  switch (stage) {
    case "1":
      return "Letter Of Credit";
    case "2":
      return "Presentation Document";
    case "3":
      return "Documentation Result Notification";
    case "4":
      return "LC Payment Acceptance";
    case "5":
      return "LC Issuance Bank To Advising Bank Payment";
    case "6":
      return "LC Advising Bank To Beneficiary Payment";
    case "7":
      return "UpasLC Payment Acceptance";
    default:
      return "";
  }
}

export function postMethod(data) {
  return axios({
    method: "post",
    url: "http://localhost:3000/json-rpc",
    data: data,
  });
}

export const CHAIN_ID = 6788;

export const EMPTY_BYTES = "0x";

export const STANDARD_FACTORY_ADDRESS =
  "0xf5e87bDE531B43A5e98847C15344F9Ec95dED0e7";

export const ROUTER_SERVICE_ADDRESS =
  "0x5EeE5a0028FC1Ff91e75B5168564b3232f2De2Fc";

export const AMEND_REQUEST_ADDRESS =
  "0x13e0cc465a3B7873391C239DD8A6b9C5473cC3fC";

export const setupDefaultNetwork = async () => {
  const provider = window.ethereum;
  if (provider) {
    const _chainId = `0x${CHAIN_ID.toString(16)}`;
    try {
      await provider.request({
        method: "wallet_switchEthereumChain",
        params: [{ chainId: _chainId }],
      });
    } catch (switchError) {
      // This error code indicates that the chain has not been added to MetaMask.
      if (switchError.code === 4902) {
        try {
          await provider.request({
            method: "wallet_addEthereumChain",
            params: [
              {
                chainId: _chainId,
                chainName: "FPT  Quorum testnet",
                nativeCurrency: {
                  name: "FPT",
                  symbol: "FPT",
                  decimals: 18,
                },
                rpcUrls: ["http://1.54.89.229:30308"],
              },
            ],
          });
        } catch (error) {
          console.error("Failed to setup the network in Metamask:", error);
          return false;
        }
      }
    }
  } else {
    console.error(
      "Can't setup the network on metamask because window.ethereum is undefined"
    );
    return false;
  }
};
