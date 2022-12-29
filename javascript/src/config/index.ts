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
        LCManagement: "0x579aB824e8F43648970980b34523e4daA559DDbE",
        Mode: "0xD26917262a44F749B8caECDEdb371A35f510cE8E",
        RouterService: "0x30bd88f9DAb5Db8A5470D8a0B2Fbd94be76F4Cf7",
        StandardLCFactory: "0x68AdC525e49cDD3fDED7a7DD25a093d122DF42be",
        UPASLCFactory: "0x312AabAD67C576731326c364D455C11bF5748DaB",
        AmendRequest: "0x57BBAEdeE8841Fe0dD489E136878B88C9Bb76baa",
    },
    permissionContractAddresses: {
        OrgManager: "0x1136216Bbf1b9d246924F4e6fde050bd8295E219",
        AccountManager: "0x743faFfc6B732C954b00766fB64372CB9Fe1F094",
        PermissionsImplementation: "0x5C4221C331E50110b31E45aa30C5615eFc6f6Aaa",
        PermissionsInterface: "0xEE455baA75A1D8f856094B3dA86Eea479de64582",
        PermissionsUpgradable: "0x304AAd8e4Bd0A10ee2dEb04C24d61AA6E068c229",
        NodeManager: "0x29af984cf4D92669Fb88e6022cAAC9028A1Eb510",
        RoleManager: "0xbe4DcF0c5E310847b3D7750fD2DdA41455a588Cd",
        VoterManager: "0x01866bC496097C77d298696e2A9d194A487AED03",
    },
    chainId: 6788,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "https://lc-blockchain.dev.etradevn.com/",
};

export const STAGING_CONFIG: config = {
    lCContractAddresses: {
        LCManagement: "0xcA4628a709613147D8B309b617db5cF3FE14Aee3",
        Mode: "0x4D47336309867650E29E845A36A6f2f9aDe214E3",
        RouterService: "0xB6D5DCBc2C66DB73205046eb3d63497EF1cfE3Cc",
        StandardLCFactory: "0x9eE98A0396032e099101b45d42f9f45727bFCf3a",
        UPASLCFactory: "0x494ACd952430317B32D6421C921C62ba07fE69e9",
        AmendRequest: "0x5cAb8549Edbc743c8A6FfAD933a5621b8Ba13Ed4",
    },
    permissionContractAddresses: {
        OrgManager: "0x1136216Bbf1b9d246924F4e6fde050bd8295E219",
        AccountManager: "0x743faFfc6B732C954b00766fB64372CB9Fe1F094",
        PermissionsImplementation: "0x5C4221C331E50110b31E45aa30C5615eFc6f6Aaa",
        PermissionsInterface: "0xEE455baA75A1D8f856094B3dA86Eea479de64582",
        PermissionsUpgradable: "0x304AAd8e4Bd0A10ee2dEb04C24d61AA6E068c229",
        NodeManager: "0x29af984cf4D92669Fb88e6022cAAC9028A1Eb510",
        RoleManager: "0xbe4DcF0c5E310847b3D7750fD2DdA41455a588Cd",
        VoterManager: "0x01866bC496097C77d298696e2A9d194A487AED03",
    },
    chainId: 6788,
    chainName: "FPT Quorum Testnet FIS Gateway",
    url: "https://lc-blockchain.dev.etradevn.com/",
};

// Suppose we have PROD_CONFIG || STAGING_CONFIG || TESTNET_CONFIG
export const DEFAULT_CONFIG = TESTNET_CONFIG;
