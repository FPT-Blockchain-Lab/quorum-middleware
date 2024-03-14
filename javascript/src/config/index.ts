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
        LCManagement: "0x1fAD96BB4cdA3e0d679f1376070DAbc3e0bf9494",
        RouterService: "0xEC18381BC1f6a3ed6E8647F5E05C87312aD77Cfc",
        LCFactory: "0x4c164ff3838FAd6254B6FA7b67ed804fDee8b5dE",
        AmendRequest: "0xcb3D33AdF153c31305e2ad1d82b8D31ac496ff3d",
    },
    permissionContractAddresses: {
        OrgManager: "0x47024e7a55ec7AE2c79722fA5e2ff1b1C1a44929",
        AccountManager: "0xD09b787abed2A64A6B4B3E33EC884Dad8b47B3D0",
        PermissionsImplementation: "0x6E7EC582D50C081481dE36D9dC21345CdC9844c6",
        PermissionsInterface: "0x0c88bDf910C2de7d501ef677bEb5B10fFa8C8b8d",
        PermissionsUpgradable: "0xb045b8B5e2A3a3e5f762Bc834D76670169Bde7F1",
        NodeManager: "0x28D03b8499Be7fFd25C7F9AFA32eA0FF8d2227ec",
        RoleManager: "0xD53AA14Cb6bBD37261797C7d958735187C1C88c4",
        VoterManager: "0xf593ad7FD8faa6AfDf896B3Ee84975573b079296",
    },
    chainId: 6789,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "http://10.14.104.6:32443",
};

export const STAGING_CONFIG: config = {
    lCContractAddresses: {
        LCManagement: "0x1fAD96BB4cdA3e0d679f1376070DAbc3e0bf9494",
        RouterService: "0xEC18381BC1f6a3ed6E8647F5E05C87312aD77Cfc",
        LCFactory: "0x4c164ff3838FAd6254B6FA7b67ed804fDee8b5dE",
        AmendRequest: "0xcb3D33AdF153c31305e2ad1d82b8D31ac496ff3d",
    },
    permissionContractAddresses: {
        OrgManager: "0x47024e7a55ec7AE2c79722fA5e2ff1b1C1a44929",
        AccountManager: "0xD09b787abed2A64A6B4B3E33EC884Dad8b47B3D0",
        PermissionsImplementation: "0x6E7EC582D50C081481dE36D9dC21345CdC9844c6",
        PermissionsInterface: "0x0c88bDf910C2de7d501ef677bEb5B10fFa8C8b8d",
        PermissionsUpgradable: "0xb045b8B5e2A3a3e5f762Bc834D76670169Bde7F1",
        NodeManager: "0x28D03b8499Be7fFd25C7F9AFA32eA0FF8d2227ec",
        RoleManager: "0xD53AA14Cb6bBD37261797C7d958735187C1C88c4",
        VoterManager: "0xf593ad7FD8faa6AfDf896B3Ee84975573b079296",
    },
    chainId: 6789,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "http://10.14.104.6:32443",
};

// Suppose we have PROD_CONFIG || STAGING_CONFIG || TESTNET_CONFIG
export const DEFAULT_CONFIG = STAGING_CONFIG;
