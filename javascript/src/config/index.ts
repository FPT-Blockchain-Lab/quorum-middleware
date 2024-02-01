export interface config {
    lCContractAddresses: Partial<LCContractAddresses>;
    permissionContractAddresses: Partial<PermissionContractAddresses>;
    chainName: string;
    chainId: number | string;
    url: string;
}

export interface LCContractAddresses {
    LCManagement: string;
    RouterService: string;
    LCFactory: string;
    AmendRequest: string;
}

export interface PermissionContractAddresses {
    OrgManager: string;
    AccountManager: string;
    PermissionsImplementation: string;
    PermissionsInterface: string;
    PermissionsUpgradable: string;
    NodeManager: string;
    RoleManager: string;
    VoterManager: string;
}

export enum LC_ENUM {
    STANDARD_LC,
    UPAS_LC,
    UPAS_LC_IMPORT,
    STANDARD_LC_IMPORT,
    STANDARD_LC_EXPORT,
    STANDARD_LC_DISCOUNT,
}

export const TESTNET_CONFIG: config = {
    lCContractAddresses: {
        LCManagement: "0x5889E7A29720dff4F17Dcbe6C47C6c2C80045812",
        RouterService: "0x0cE081Ea06a5A230d66e5Ee8b392a283921c24c0",
        LCFactory: "0xC7Aecb95508561cA04726bBdf32E273A0b26E3B8",
        AmendRequest: "0xf50d5B27cC8D2841DB9da7238bcf0671c6F24988",
    },
    permissionContractAddresses: {
        OrgManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        AccountManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        PermissionsImplementation: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        PermissionsInterface: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        PermissionsUpgradable: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        NodeManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        RoleManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        VoterManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
    },
    chainId: 97,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "https://data-seed-prebsc-1-s1.binance.org:8545/",
};

export const STAGING_CONFIG: config = {
    lCContractAddresses: {
        LCManagement: "0x5889E7A29720dff4F17Dcbe6C47C6c2C80045812",
        RouterService: "0x0cE081Ea06a5A230d66e5Ee8b392a283921c24c0",
        LCFactory: "0xC7Aecb95508561cA04726bBdf32E273A0b26E3B8",
        AmendRequest: "0xf50d5B27cC8D2841DB9da7238bcf0671c6F24988",
    },
    permissionContractAddresses: {
        OrgManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        AccountManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        PermissionsImplementation: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        PermissionsInterface: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        PermissionsUpgradable: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        NodeManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        RoleManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
        VoterManager: "0x415A81aadF45932e34b1b22899Af03705f8b1934",
    },
    chainId: 97,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "https://data-seed-prebsc-1-s1.binance.org:8545/",
};

// Suppose we have PROD_CONFIG || STAGING_CONFIG || TESTNET_CONFIG
export const DEFAULT_CONFIG = STAGING_CONFIG;
