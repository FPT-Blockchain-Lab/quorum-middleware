import BN from "bn.js";
import Web3 from "web3";
import { encodePacked, keccak256 } from "web3-utils";

export namespace Utils {
    export const EMPTY_BYTES = "0x";
    export const DEFAULT_ROOT_HASH = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";

    export function keccak256Utf8(value: string): string {
        return keccak256(value);
    }

    export function keccak256BigNumber(value: BN): string {
        return keccak256(value);
    }

    export function keccak256EncodedPackedStr(value: string): string {
        const encodePackedValue = encodePacked({ v: value, t: "string" });
        if (!encodePackedValue) {
            throw new Error(`${value} can't encoded`);
        }

        return keccak256(encodePackedValue);
    }

    /**
     * Get current block timestamp
     * @param web3 an instance of Web3
     * @returns current block timestamp
     */
    export async function getCurrentBlockTimestamp(web3: Web3) {
        const blockNumber = await web3.eth.getBlockNumber();
        const block = await web3.eth.getBlock(blockNumber);
        return block.timestamp;
    }
}
