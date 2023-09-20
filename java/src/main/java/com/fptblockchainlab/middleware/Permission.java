package com.fptblockchainlab.middleware;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Permission {
    @Getter
    @AllArgsConstructor
    public enum AccessType {
        READ_ONLY(0, "READ_ONLY"),
        VALUE_TRANSFER(1, "VALUE_TRANSFER"),
        CONTRACT_DEPLOY(2, "CONTRACT_DEPLOY"),
        FULL_ACCESS(3, "FULL_ACCESS"),
        CONTRACT_CALL(4, "CONTRACT_CALL"),
        VALUE_TRANSFER_AND_CONTRACT_CALL(5, "VALUE_TRANSFER_AND_CONTRACT_CALL"),
        VALUE_TRANSFER_AND_CONTRACT_DEPLOY(6, "VALUE_TRANSFER_AND_CONTRACT_DEPLOY"),
        CONTRACT_CALL_AND_DEPLOY(7, "CONTRACT_CALL_AND_DEPLOY");

        private final Integer baseAccess; // Onchain
        private final String name; // Offchain

        public static AccessType valueOf(int value) {
            AccessType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                AccessType responseCode = var1[var3];
                if (responseCode.getBaseAccess() == value) {
                    return responseCode; // code int
                }
            }
            return null;
        }

    }

    @Getter
    @AllArgsConstructor
    public enum Role {
        MEMBER(0, AccessType.VALUE_TRANSFER_AND_CONTRACT_CALL, "MEMBER", false, false),
        ORGADMIN(1, AccessType.VALUE_TRANSFER_AND_CONTRACT_CALL, "ORGADMIN", false, true);

        private final int value;
        private final Permission.AccessType accessType;
        private final String name;
        private final Boolean isVoter;
        private final Boolean isOrgAdmin;

        public static Role valueOf(int value) {
            Role[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                Role responseCode = var1[var3];
                if (responseCode.getValue() == value) {
                    return responseCode; // code int
                }
            }
            return null;
        }
    }
}
