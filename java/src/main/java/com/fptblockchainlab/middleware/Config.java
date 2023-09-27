package com.fptblockchainlab.middleware;

import java.util.Optional;

// TODO use config based for Middleware
public class Config {
    private String quorumUrl;
    private Optional<String> privateKey;
    private Optional<Long> chainId;
    private Optional<String> accountMgrAddress;
    private Optional<String> orgMgrAddress;
    private Optional<String> roleMgrAddress;
    private Optional<String> interfaceAddress;
    private Optional<String> lcManagmentAddress;
}
