package com.myaccountbook.dto;

import com.myaccountbook.domain.Disbursement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DisbursementResponse(
        Long id,
        BigDecimal amount,
        String descr,
        String category,
        LocalDateTime expenseDt,
        LocalDateTime createdDt,
        LocalDateTime updatedDt
) {
    public static DisbursementResponse from(Disbursement disbursement) {
        return new DisbursementResponse(
                disbursement.getId(),
                disbursement.getAmount(),
                disbursement.getDescr(),
                disbursement.getCategory(),
                disbursement.getExpenseDt(),
                disbursement.getCreatedDt(),
                disbursement.getUpdatedDt()
        );
    }
}
