package com.myaccountbook.controller;

import com.myaccountbook.domain.Disbursement;
import com.myaccountbook.dto.CreateDisbursementRequestDTO;
import com.myaccountbook.dto.DisbursementResponse;
import com.myaccountbook.service.DisbursementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disbursements")
public class DisbursementController {

    @Autowired
    private DisbursementService disbursementService;

    // Create a new Disbursement
    @PostMapping
    public ResponseEntity<DisbursementResponse> createDisbursement(@RequestBody @Valid CreateDisbursementRequestDTO request) {
        Disbursement createdDisbursement = disbursementService.createDisbursement(request);
        return new ResponseEntity<>(DisbursementResponse.from(createdDisbursement), HttpStatus.CREATED);
    }

    // List all Disbursements
    @GetMapping
    public ResponseEntity<List<DisbursementResponse>> getAllDisbursements() {
        List<Disbursement> disbursements = disbursementService.getAllDisbursements();
        return new ResponseEntity<>(disbursements.stream()
                .map(DisbursementResponse::from)
                .toList(), HttpStatus.OK);
    }

    // Get a single Disbursement by ID
    @GetMapping("/{id}")
    public ResponseEntity<DisbursementResponse> getDisbursementById(@PathVariable Long id) {
        Disbursement disbursement = disbursementService.getDisbursementById(id);
        return new ResponseEntity<>(DisbursementResponse.from(disbursement), HttpStatus.OK);
    }

    // Update an existing Disbursement
    @PutMapping("/{id}")
    public ResponseEntity<DisbursementResponse> updateDisbursement(@PathVariable Long id, @RequestBody @Valid CreateDisbursementRequestDTO request) {
        Disbursement updatedDisbursement = disbursementService.updateDisbursement(id, request);
        return new ResponseEntity<>(DisbursementResponse.from(updatedDisbursement), HttpStatus.OK);
    }

    // Delete a Disbursement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisbursement(@PathVariable Long id) {
        disbursementService.deleteDisbursement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}