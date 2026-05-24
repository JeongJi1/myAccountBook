package com.myaccountbook.dto;

import java.math.BigDecimal;
import java.util.List;

public record MonthlyStatsResponse(
        int year,
        List<MonthEntry> months,
        BigDecimal yearTotal
) {
    public record MonthEntry(int month, BigDecimal total) {}
}
