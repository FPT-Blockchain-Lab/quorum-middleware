export const CHAIN_ID = 6788;

export const EMPTY_BYTES = "0x";

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
