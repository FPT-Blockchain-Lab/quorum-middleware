package com.fptblockchainlab;

import com.fptblockchainlab.model.entity.BlcOrganizationBank;
import com.fptblockchainlab.model.entity.BlcUser;
import com.fptblockchainlab.rest.response.BlcAcknowledgeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface BlockchainService {
    /**
     * send multiple transactions for create bank and default roles onchain
     * @param blcOrganizationBank
     */
    void createBankOnChainIfNotExist(BlcOrganizationBank blcOrganizationBank);

    /**
     * send transaction to allow a bank use lc protocol onchain
     * @param blcOrganizationBank
     */
    void allowBankUseLcOnchain(BlcOrganizationBank blcOrganizationBank);

    /**
     * send transaction to restrict a bank use lc protocol onchain
     * @param blcOrganizationBank
     */
    void restrictBankUseLcOnchain(BlcOrganizationBank blcOrganizationBank);

    /**
     * send transaction to suspend admin bank onchain
     * @param blcOrganizationBank
     * @param adminBank
     */
    void suspendAdminBankOnchain(BlcOrganizationBank blcOrganizationBank, BlcUser adminBank);
}
