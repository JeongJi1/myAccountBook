package com.myaccountbook.service;

import com.myaccountbook.dto.CategoryStatsResponse;
import com.myaccountbook.dto.MonthlyStatsResponse;
import com.myaccountbook.global.util.SecurityUtils;
import com.myaccountbook.repository.DisbursementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DisbursementRepository disbursementRepository;

    @Transactional(readOnly = true)
    public MonthlyStatsResponse getMonthlyStats(int year) {
        Long userId = SecurityUtils.getCurrentUserId();
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year + 1, 1, 1, 0, 0);

        List<Object[]> rows = disbursementRepository.findMonthlyStats(userId, start, end);

        List<MonthlyStatsResponse.MonthEntry> months = rows.stream()
                .map(row -> new MonthlyStatsResponse.MonthEntry(
                        ((Number) row[0]).intValue(),
                        (BigDecimal) row[1]
                ))
                .toList();

        BigDecimal yearTotal = months.stream()
                .map(MonthlyStatsResponse.MonthEntry::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlyStatsResponse(year, months, yearTotal);
    }

    @Transactional(readOnly = true)
    public CategoryStatsResponse getCategoryStats(int year, Integer month) {
        Long userId = SecurityUtils.getCurrentUserId();
        LocalDateTime start;
        LocalDateTime end;

        if (month != null) {
            start = LocalDateTime.of(year, month, 1, 0, 0);
            end = start.plusMonths(1);
        } else {
            start = LocalDateTime.of(year, 1, 1, 0, 0);
            end = LocalDateTime.of(year + 1, 1, 1, 0, 0);
        }

        List<Object[]> rows = disbursementRepository.findCategoryStats(userId, start, end);

        List<CategoryStatsResponse.CategoryEntry> categories = rows.stream()
                .map(row -> new CategoryStatsResponse.CategoryEntry(
                        (String) row[0],
                        (BigDecimal) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList();

        BigDecimal total = categories.stream()
                .map(CategoryStatsResponse.CategoryEntry::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CategoryStatsResponse(year, month, categories, total);
    }
}
