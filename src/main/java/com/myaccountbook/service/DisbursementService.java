package com.myaccountbook.service;

import com.myaccountbook.domain.Disbursement;
import com.myaccountbook.dto.DisbursementRequest;
import com.myaccountbook.dto.DisbursementResponse;
import com.myaccountbook.repository.DisbursementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DisbursementService {
    private final DisbursementRepository disbursementRepositoy;

    public DisbursementResponse create(DisbursementRequest request) {
        Disbursement disbursement = Disbursement.builder()
                .amount(request.getAmount())
                .descr(request.getDescr())
                .category(request.getCategory())
                .expenseDt(request.getExpenseDt())
                .build();

        Disbursement saved = disbursementRepositoy.save(disbursement);
        return DisbursementResponse.from(saved);
    }
}
