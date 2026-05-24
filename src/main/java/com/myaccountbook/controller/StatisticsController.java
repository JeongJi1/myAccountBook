package com.myaccountbook.controller;

import com.myaccountbook.dto.CategoryStatsResponse;
import com.myaccountbook.dto.MonthlyStatsResponse;
import com.myaccountbook.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyStatsResponse> getMonthlyStats(@RequestParam int year) {
        return ResponseEntity.ok(statisticsService.getMonthlyStats(year));
    }

    @GetMapping("/category")
    public ResponseEntity<CategoryStatsResponse> getCategoryStats(
            @RequestParam int year,
            @RequestParam(required = false) Integer month
    ) {
        return ResponseEntity.ok(statisticsService.getCategoryStats(year, month));
    }
}
