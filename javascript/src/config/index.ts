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
        LCManagement: "0x9fe96403440c397B4508aB6f96defC7c2C46BeC9",
        RouterService: "0x13612cdD40DE0becB742e7C006BD34A89FDe68D0",
        StandardLCFactory: "0xF3AAA8ABfAabf43c527208Eff7177dE9E86ac572",
        UPASLCFactory: "0x31EBbe37B3473870d8A6Bc5223c235Ad918202C2",
        AmendRequest: "0xa7A7A05A703e26530B31c43694edB0e5028AD323",
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
        LCManagement: "0xC5D01957d696d1b70615324f16DC6bd4f8904aF8",
        RouterService: "0x53b24F8D986308Bc5B79ee7dbf34f4e5cfb97a89",
        StandardLCFactory: "0xeE09B702a39eE2CF084f374E965Be7BfFB7b5CD3",
        UPASLCFactory: "0x64238c100c74f4927637B27be445982f9E4211c7",
        AmendRequest: "0xeFF968303ac676908e565F52B161262a2dF301FC",
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
    chainName: "FPT Quorum Testnet FIS Staging Gateway",
    url: "https://lc-blockchain.staging.etradevn.com/",
};

// Suppose we have PROD_CONFIG || STAGING_CONFIG || TESTNET_CONFIG
export const DEFAULT_CONFIG = STAGING_CONFIG;
