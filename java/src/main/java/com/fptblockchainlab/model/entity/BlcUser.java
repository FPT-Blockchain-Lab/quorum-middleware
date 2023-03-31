package com.fptblockchainlab.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import jakarta.persistence.*;
@Entity
@Table(name = "blc_user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "FieldHandler"})
public class BlcUser extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "blc_user_code")
    private String blcUserCode;

    @Column(name = "blc_wallet_address")
    private String blcWalletAddress;

    @Column(name = "blc_org_bank_code")
    private String blcOrgBankCode;

    @Column(name = "blc_user_roles")
    private String blcUserRoles;

    @Column(name = "blc_user_status")
    private Integer blcUserStatus;

    @Column(name = "blc_is_admin")
    private Integer blcIsAdmin;

    @Column(name = "is_onchain_status")
    @Builder.Default
    private Integer isOnchainStatus = 0;

    @Column(name = "blc_wallet_address_temp")
    private String blcWalletAddressTemp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "blc_organization_bank_id")
    private BlcOrganizationBank blcOrganizationBank;

    @Column(name = "is_onchain_by_address")
    private String isOnChainByAddress;

    @Column(name = "blc_wallet_address_deleted")
    private String blcWalletAddressDeleted;
}
