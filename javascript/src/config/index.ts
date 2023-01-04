export interface config {
    lCContractAddresses: Partial<LCContractAddresses>;
    permissionContractAddresses: Partial<PermissionContractAddresses>;
    chainName: string;
    chainId: number | string;
    url: string;
}

export interface LCContractAddresses {
    LCManagement: string;
    Mode: string;
    RouterService: string;
    StandardLCFactory: string;
    UPASLCFactory: string;
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

export const TESTNET_CONFIG: config = {
    lCContractAddresses: {
        LCManagement: "0x05d54558021C8b26E080435257B7F1f2dA8FdFbC",
        Mode: "0x2d90566c65B8e7720608D0aA11d307d5B1beE760",
        RouterService: "0x00B45eDd16A9f480C681A15e78b35b2713F99602",
        StandardLCFactory: "0x86364F74eb62EC25754968653dd9E2009B87eca3",
        UPASLCFactory: "0xcF9a3d3ce772Dc63504193e5a10A3BE106F0447b",
        AmendRequest: "0x221F17Ef214044bD37a73dE5c2804F2AB2602Bc4",
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
        LCManagement: "0x795C2DeEf13e6D49dF73Ca194250c97511796255",
        Mode: "0x9c2ccBf987b9DC2144ae2c6767475017AeBF42b3",
        RouterService: "0x0B3b181264f97b005e2ceB1Acac082E4ec883d86",
        StandardLCFactory: "0x452EEfa90c9628Df5421e5E1A3fB8F044F2F364b",
        UPASLCFactory: "0x5692c93c784ef84711E6b460CF40577Fc95Bd039",
        AmendRequest: "0xD92cFf94741C78153190f9A060005653f9410d95",
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

// Suppose we have PROD_CONFIG || STAGING_CONFIG || TESTNET_CONFIG
export const DEFAULT_CONFIG = TESTNET_CONFIG;
