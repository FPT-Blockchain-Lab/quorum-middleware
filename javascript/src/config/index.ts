import pino from "pino";

export const LCContractAddresses = {
    LCManagement: "0x76B88dd44c9c70b2BF2869Db7E00FDBE9f12eE77",
    Mode: "0x8E106A20BDF6161716a3D9D58951fA4C2cC32301",
    RouterService: "0xa76e4b1fA3287e220C2c70a3149c3a8f065887F5",
    StandardLCFactory: "0x6F1BbC8e7aC18dAea3247BD2504623B9D7Ea58e9",
    UPASLCFactory: "0x908839dA4161391e897097Fb404bef69060d05bc",
    AmendRequest: "0x6937fe172d03264599B029f36CD85330dA0FD775",
};

export const PermissionContractAddresses = {
    OrgManager: "0x1136216Bbf1b9d246924F4e6fde050bd8295E219",
    AccountManager: "0x743faFfc6B732C954b00766fB64372CB9Fe1F094",
    PermissionsImplementation: "0x5C4221C331E50110b31E45aa30C5615eFc6f6Aaa",
    PermissionsInterface: "0xEE455baA75A1D8f856094B3dA86Eea479de64582",
    PermissionsUpgradable: "0x304AAd8e4Bd0A10ee2dEb04C24d61AA6E068c229",
    NodeManager: "0x29af984cf4D92669Fb88e6022cAAC9028A1Eb510",
    RoleManager: "0xbe4DcF0c5E310847b3D7750fD2DdA41455a588Cd",
    VoterManager: "0x01866bC496097C77d298696e2A9d194A487AED03",
};

export const GLOBAL_LOGGER = pino({
    name: 'quorum-middleware',
    level: 'debug',
});
