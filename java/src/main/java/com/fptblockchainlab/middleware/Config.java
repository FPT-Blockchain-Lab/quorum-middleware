package com.fptblockchainlab.middleware;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class Config {
    private String quorumUrl;
    private String privateKey;
    private Long chainId;
    private Optional<String> accountMgrAddress;
    private Optional<String> orgMgrAddress;
    private Optional<String> roleMgrAddress;
    private Optional<String> interfaceAddress;
    private Optional<String> lcManagmentAddress;
    private Optional<String> lcFactoryAddress;
    private Optional<String> routerServiceAddress;
    private Optional<String> ultimateParentOrg; // level 1
}
