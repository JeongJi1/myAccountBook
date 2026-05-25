package com.myaccountbook.service;

import com.myaccountbook.domain.Category;
import com.myaccountbook.domain.Disbursement;
import com.myaccountbook.domain.User;
import com.myaccountbook.dto.CreateDisbursementRequestDTO;
import com.myaccountbook.dto.DisbursementResponse;
import com.myaccountbook.dto.UpdateDisbursementRequestDTO;
import com.myaccountbook.global.exception.EntityNotFoundException;
import com.myaccountbook.global.util.SecurityUtils;
import com.myaccountbook.repository.CategoryRepository;
import com.myaccountbook.repository.DisbursementRepository;
import com.myaccountbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisbursementService {

    private final DisbursementRepository disbursementRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public DisbursementResponse createDisbursement(CreateDisbursementRequestDTO request) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = findUserById(userId);
        Category category = findCategoryById(request.categoryId());

        Disbursement disbursement = Disbursement.builder()
                .user(user)
                .amount(request.amount())
                .descr(request.descr())
                .category(category)
                .expenseDt(request.expenseDt())
                .build();

        return DisbursementResponse.from(disbursementRepository.save(disbursement));
    }

    @Transactional(readOnly = true)
    public List<DisbursementResponse> getAllDisbursements() {
        Long userId = SecurityUtils.getCurrentUserId();
        return disbursementRepository.findAllByUserId(userId).stream()
                .map(DisbursementResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DisbursementResponse getDisbursementById(Long id) {
        return DisbursementResponse.from(findOwnedById(id));
    }

    @Transactional
    public DisbursementResponse updateDisbursement(Long id, UpdateDisbursementRequestDTO request) {
        Disbursement disbursement = findOwnedById(id);
        Category category = findCategoryById(request.categoryId());

        disbursement.update(request.amount(), request.descr(), category, request.expenseDt());
        return DisbursementResponse.from(disbursement);
    }

    @Transactional
    public void deleteDisbursement(Long id) {
        disbursementRepository.delete(findOwnedById(id));
    }

    private Disbursement findOwnedById(Long id) {
        Disbursement disbursement = disbursementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지출 내역입니다. id: " + id));

        if (!disbursement.isOwnedBy(SecurityUtils.getCurrentUserId())) {
            throw new EntityNotFoundException("존재하지 않는 지출 내역입니다. id: " + id);
        }
        return disbursement;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다. id: " + userId));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다. id: " + categoryId));
    }
}
