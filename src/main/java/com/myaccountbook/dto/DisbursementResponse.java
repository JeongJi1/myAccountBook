package com.myaccountbook.dto;

import com.myaccountbook.domain.Disbursement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DisbursementResponse(
        Long id,
        BigDecimal amount,
        String descr,
        Long categoryId,
        String categoryName,
        LocalDateTime expenseDt,
        LocalDateTime createdDt,
        LocalDateTime updatedDt
) {
    public static DisbursementResponse from(Disbursement disbursement) {
        return new DisbursementResponse(
                disbursement.getId(),
                disbursement.getAmount(),
                disbursement.getDescr(),
                disbursement.getCategory() != null ? disbursement.getCategory().getId() : null,
                disbursement.getCategory() != null ? disbursement.getCategory().getName() : null,
                disbursement.getExpenseDt(),
                disbursement.getCreatedDt(),
                disbursement.getUpdatedDt()
        );
    }
}
