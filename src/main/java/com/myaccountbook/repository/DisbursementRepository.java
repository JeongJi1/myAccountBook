package com.myaccountbook.repository;

import com.myaccountbook.domain.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
}
