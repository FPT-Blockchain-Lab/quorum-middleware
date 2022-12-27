import BN from "bn.js";
import { encodePacked, keccak256 } from "web3-utils";

export namespace Utils {
    export const EMPTY_BYTES = "0x";
    
    export function keccak256Utf8(value: string): string {
        return keccak256(value);
    }

    export function keccak256BigNumber(value: BN): string {
        return keccak256(value);
    }

    export function keccak256EncodedPackedStr(value: string): string {
        const encodePackedValue = encodePacked({ v: value, t: 'string' });
        if (!encodePackedValue) {
            throw new Error(`${value} can't encoded`)
        }
        
        return keccak256(encodePackedValue);
    }
}