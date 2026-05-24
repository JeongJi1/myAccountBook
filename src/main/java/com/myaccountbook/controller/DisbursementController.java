package com.myaccountbook.controller;

import com.myaccountbook.dto.CreateDisbursementRequestDTO;
import com.myaccountbook.dto.DisbursementResponse;
import com.myaccountbook.dto.UpdateDisbursementRequestDTO;
import com.myaccountbook.service.DisbursementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disbursements")
@RequiredArgsConstructor
public class DisbursementController {

    private final DisbursementService disbursementService;

    @PostMapping
    public ResponseEntity<DisbursementResponse> createDisbursement(@RequestBody @Valid CreateDisbursementRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(disbursementService.createDisbursement(request));
    }

    @GetMapping
    public ResponseEntity<List<DisbursementResponse>> getAllDisbursements() {
        return ResponseEntity.ok(disbursementService.getAllDisbursements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisbursementResponse> getDisbursementById(@PathVariable Long id) {
        return ResponseEntity.ok(disbursementService.getDisbursementById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisbursementResponse> updateDisbursement(
            @PathVariable Long id,
            @RequestBody @Valid UpdateDisbursementRequestDTO request
    ) {
        return ResponseEntity.ok(disbursementService.updateDisbursement(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisbursement(@PathVariable Long id) {
        disbursementService.deleteDisbursement(id);
        return ResponseEntity.noContent().build();
    }
}