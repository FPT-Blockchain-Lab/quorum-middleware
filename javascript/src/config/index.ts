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
        LCManagement: "0x6EE44b831d15D4613eB6A17F826A5c5EFe147117",
        RouterService: "0xd51b0FBA544C89Eb0b7C9f290c3eEfBA95b059Ce",
        LCFactory: "0x7D7f2163Ce8B70c7A23ADf7768cf105BA0A5CBA1",
        AmendRequest: "0x282f7C1c8E700B2C6aC50518E67E8734C4b32913",
    },
    permissionContractAddresses: {
        OrgManager: "0xf5B2c0829f9485EEB114B11a7C1cb3227B8749Ee",
        AccountManager: "0xc7123BEA05D26823f99bba87a83BADDD021C3c2d",
        PermissionsImplementation: "0x48272bB87cdA70aCd600B7B8883ada2e799983b8",
        PermissionsInterface: "0x638449F761362fB1041a75eE92e86a2e8fe92780",
        PermissionsUpgradable: "0x40686513a73B3e861810345B08Ca2d952CE8E4F4",
        NodeManager: "0xeE2EF0e68A72654C20c6f3dfa43e03816f362B4F",
        RoleManager: "0x09Ad12B57407dEd24ad2789043F8962fe8679DB3",
        VoterManager: "0xF5E507788E7eaa9Dbf8f24a5D24C2de1F9192416",
    },
    chainId: 6788,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "https://lc-blockchain.dev.etradevn.com/",
};

export const STAGING_CONFIG: config = {
    lCContractAddresses: {
        LCManagement: "0x31027078f39C57eDf9F2924Be06040F0426aE30f",
        RouterService: "0x9Fba929C44657889FFcAC5e6049a89aE5c3A1AF5",
        LCFactory: "0x72edb4CCD2Cacf73bBBf6D6b48aB905A1453745D",
        AmendRequest: "0xBAb50987C5b81494f2E1A0D37DBf4fE3B7982CBA",
    },
    permissionContractAddresses: {
        OrgManager: "0x30Ef77c025eB92c9dCc45079d716CbEbBFdB7808",
        AccountManager: "0x467Bc413aDe3807f4BA57501c16e744c713C1352",
        PermissionsImplementation: "0x7dAcBA044b65A700c7BB6020124003dD68928018",
        PermissionsInterface: "0x4046B8636D9F6964b2e99B23cb578B3a29E0d75d",
        PermissionsUpgradable: "0x3A6Fc633fcF2A93f3AA8BfEA64aC5c6CEB3De2Af",
        NodeManager: "0xd9D5bcAA7c68b8ACBE7DF4827f69e5c95Eb2B75E",
        RoleManager: "0xAb83F7d5f1Bb99BC085136a9189a3Bdcba8CA713",
        VoterManager: "0x0D746fCCe1c31F4D59a58dA7F9Cb12767fc083Fc",
    },
    chainId: 6787,
    chainName: "FPT Quorum Testnet FIS Staging Gateway",
    // url: "https://lc-blockchain.staging.etradevn.com/",
    url: "https://fis-rpc-gateway-testnet.fpt-blockchain-lab.com",
};

// Suppose we have PROD_CONFIG || STAGING_CONFIG || TESTNET_CONFIG
export const DEFAULT_CONFIG = STAGING_CONFIG;
