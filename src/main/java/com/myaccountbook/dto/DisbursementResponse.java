package com.myaccountbook.dto;

import com.myaccountbook.domain.Disbursement;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class DisbursementResponse {

    private Long id;
    private BigDecimal amount;
    private String descr;
    private String category;
    private LocalDateTime expenseDt;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;

    public static DisbursementResponse from(Disbursement disbursement) {
        return DisbursementResponse.builder()
                .id(disbursement.getId())
                .amount(disbursement.getAmount())
                .descr(disbursement.getDescr())
                .category(disbursement.getCategory())
                .expenseDt(disbursement.getExpenseDt())
                .createdDt(disbursement.getCreatedDt())
                .updatedDt(disbursement.getUpdatedDt())
                .build();
    }
}
