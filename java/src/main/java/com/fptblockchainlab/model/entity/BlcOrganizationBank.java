package com.fptblockchainlab.model.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.web3j.crypto.Hash;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "blc_organization_bank")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "FieldHandler"})
public class BlcOrganizationBank extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "org_bank_name")
    private String orgBankName;

    @Column(name = "org_bank_code")
    private String orgBankCode;

    @Column(name = "org_channel")
    private String orgChannel;

    @Column(name = "org_connection_model")
    private String orgConnectionModel;

    @Column(name = "org_connection_node")
    private String orgConnectionNode;

    @Column(name = "org_tech_model")
    private String orgTechModel;

    @Column(name = "org_roles_model")
    private String orgRolesModel;

    @Column(name = "org_use_metamask")
    private Integer orgUseMetamask;

    @Column(name = "org_description")
    private String orgDescription;

    @Column(name = "org_level_1")
    private String orgLevel1;

    @Column(name = "org_level_2")
    private String orgLevel2;

    @Column(name = "org_level_3")
    private String orgLevel3;

    @Column(name = "org_level_4")
    private String orgLevel4;

    @Column(name = "org_bank_status")
    private Integer orgBankStatus;

    @OneToMany(mappedBy = "blcOrganizationBank", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BlcUser> blcUserList;

    public String getOrgFullId() {
        return Hash.sha3String(this.getOrgLevel1()) + "." + Hash.sha3String(this.getOrgLevel2()) + "."
                + Hash.sha3String(this.getOrgLevel3()) + "."
                + Hash.sha3String(this.getOrgLevel4());
    }
}