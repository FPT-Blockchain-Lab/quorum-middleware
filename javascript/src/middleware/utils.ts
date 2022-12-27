import BN from "bn.js";
import { keccak256 } from "web3-utils";

export namespace Utils {
    export function keccak256Utf8(value: string): string {
        return keccak256(value);
    }

    export function keccak256BigNumber(value: BN): string {
        return keccak256(value);
    }
}