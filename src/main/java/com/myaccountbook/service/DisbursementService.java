package com.myaccountbook.service;

import com.myaccountbook.domain.Category;
import com.myaccountbook.domain.Disbursement;
import com.myaccountbook.dto.CreateDisbursementRequestDTO;
import com.myaccountbook.dto.DisbursementResponse;
import com.myaccountbook.dto.UpdateDisbursementRequestDTO;
import com.myaccountbook.global.exception.EntityNotFoundException;
import com.myaccountbook.repository.CategoryRepository;
import com.myaccountbook.repository.DisbursementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisbursementService {

    private final DisbursementRepository disbursementRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public DisbursementResponse createDisbursement(CreateDisbursementRequestDTO request) {
        Category category = findCategoryById(request.categoryId());

        Disbursement disbursement = Disbursement.builder()
                .amount(request.amount())
                .descr(request.descr())
                .category(category)
                .expenseDt(request.expenseDt())
                .build();

        return DisbursementResponse.from(disbursementRepository.save(disbursement));
    }

    @Transactional(readOnly = true)
    public List<DisbursementResponse> getAllDisbursements() {
        return disbursementRepository.findAll().stream()
                .map(DisbursementResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DisbursementResponse getDisbursementById(Long id) {
        return DisbursementResponse.from(findById(id));
    }

    @Transactional
    public DisbursementResponse updateDisbursement(Long id, UpdateDisbursementRequestDTO request) {
        Disbursement disbursement = findById(id);
        Category category = findCategoryById(request.categoryId());

        disbursement.update(
                request.amount(),
                request.descr(),
                category,
                request.expenseDt()
        );

        return DisbursementResponse.from(disbursement);
    }

    @Transactional
    public void deleteDisbursement(Long id) {
        disbursementRepository.delete(findById(id));
    }

    private Disbursement findById(Long id) {
        return disbursementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지출 내역입니다. id: " + id));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다. id: " + categoryId));
    }
}