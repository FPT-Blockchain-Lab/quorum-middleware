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
        LCManagement: "0x9Ce961Bd915c2b309e653a5615Dd01929Ebf6719",
        RouterService: "0x9f5798DF882DD5179869b0217DdA0F6560B75af5",
        LCFactory: "0xF4Cc4D0C489013f057eD2824b76948314E37b0f8",
        AmendRequest: "0xa7d9677EECcB88799d48c4F5C7993277555d688a",
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
        LCManagement: "0x9Ce961Bd915c2b309e653a5615Dd01929Ebf6719",
        RouterService: "0x9f5798DF882DD5179869b0217DdA0F6560B75af5",
        LCFactory: "0xF4Cc4D0C489013f057eD2824b76948314E37b0f8",
        AmendRequest: "0xa7d9677EECcB88799d48c4F5C7993277555d688a",
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
