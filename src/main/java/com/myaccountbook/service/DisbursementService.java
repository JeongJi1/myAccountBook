package com.myaccountbook.service;

import com.myaccountbook.domain.Disbursement;
import com.myaccountbook.dto.CreateDisbursementRequestDTO;
import com.myaccountbook.repository.DisbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DisbursementService {

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Transactional
    public Disbursement createDisbursement(CreateDisbursementRequestDTO request) {
        Disbursement disbursement = Disbursement.builder()
                .amount(request.getAmount())
                .descr(request.getDescr())
                .category(request.getCategory())
                .expenseDt(request.getExpenseDt())
                .build();

        return disbursementRepository.save(disbursement);
    }

    @Transactional(readOnly = true)
    public List<Disbursement> getAllDisbursements() {
        return disbursementRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Disbursement getDisbursementById(Long id) {
        return disbursementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disbursement not found with id: " + id));
    }

    @Transactional
    public Disbursement updateDisbursement(Long id, CreateDisbursementRequestDTO request) {
        Disbursement disbursement = getDisbursementById(id);

        disbursement.update(
                request.getAmount(),
                request.getDescr(),
                request.getCategory(),
                request.getExpenseDt()
        );

        return disbursement;
    }

    @Transactional
    public void deleteDisbursement(Long id) {
        Disbursement disbursement = getDisbursementById(id);
        disbursementRepository.delete(disbursement);
    }
}