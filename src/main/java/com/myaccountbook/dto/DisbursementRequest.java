package com.myaccountbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class DisbursementRequest {
    @NotNull(message = "금액은 필수입니다.")
    private BigDecimal amount;

    @NotBlank(message = "사용처는 필수입니다.")
    private String descr;

    @NotBlank(message = "카테고리는 필수입니다.")
    private String category;

    @NotNull(message = "지출 일시는 필수입니다.")
    private LocalDateTime expenseDt;
}
