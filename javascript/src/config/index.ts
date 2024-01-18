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
        LCManagement: "0x0F9DB2bB429b0231789c0A81Ff831cC14C63B7bE",
        RouterService: "0x8bD227a3CEf09106DD6fE02a84599e8a235a8773",
        LCFactory: "0xEb10A2227d152713345B551bdd935A584DEE081e",
        AmendRequest: "0x87a62C080a6c21cF6fB2733131cE0B66fD5E080B",
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
        LCManagement: "0x0F9DB2bB429b0231789c0A81Ff831cC14C63B7bE",
        RouterService: "0x8bD227a3CEf09106DD6fE02a84599e8a235a8773",
        LCFactory: "0xEb10A2227d152713345B551bdd935A584DEE081e",
        AmendRequest: "0x87a62C080a6c21cF6fB2733131cE0B66fD5E080B",
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
