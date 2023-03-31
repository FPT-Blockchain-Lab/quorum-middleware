package com.fptblockchainlab.model.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BlcWalletStatus {
    SUSPENDING(1, "SUSPENDING"),
    ACTIVATING(2, "ACTIVATING"),
    BLACKLISTING (3,"BLACKLISTING");

    private final Integer value;
    private final String name;

    public static BlcWalletStatus valueOf(int value) {
        BlcWalletStatus[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            BlcWalletStatus responseCode = var1[var3];
            if (responseCode.getValue() == value) {
                return responseCode; // code int
            }
        }
        return null;
    }
}
