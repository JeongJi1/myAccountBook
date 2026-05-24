package com.myaccountbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateDisbursementRequestDTO(
        @NotNull(message = "금액은 필수입니다.") BigDecimal amount,
        @NotBlank(message = "사용처는 필수입니다.") String descr,
        @NotNull(message = "카테고리는 필수입니다.") Long categoryId,
        @NotNull(message = "지출 일시는 필수입니다.") LocalDateTime expenseDt
) {}
