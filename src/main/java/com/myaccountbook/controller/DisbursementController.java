package com.myaccountbook.controller;

import com.myaccountbook.dto.DisbursementRequest;
import com.myaccountbook.dto.DisbursementResponse;
import com.myaccountbook.service.DisbursementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/disbursements")
@RequiredArgsConstructor
public class DisbursementController {
    private final DisbursementService disbursementService;

    @PostMapping
    public DisbursementResponse create(@Valid @RequestBody DisbursementRequest request) {
        return disbursementService.create(request);
    }
}
