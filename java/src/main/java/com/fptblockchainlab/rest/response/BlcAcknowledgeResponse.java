package com.fptblockchainlab.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlcAcknowledgeResponse {
    private String acknowledgeSignature;
    private String approvalSignature;
}
