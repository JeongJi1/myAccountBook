package com.myaccountbook.dto;

import java.math.BigDecimal;
import java.util.List;

public record CategoryStatsResponse(
        int year,
        Integer month,
        List<CategoryEntry> categories,
        BigDecimal total
) {
    public record CategoryEntry(String categoryName, BigDecimal total, long count) {}
}
