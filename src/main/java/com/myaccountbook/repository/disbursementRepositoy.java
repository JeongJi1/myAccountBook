package com.myaccountbook.repository;

import com.myaccountbook.domain.disbursement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface disbursementRepositoy extends JpaRepository<disbursement, Long> {
}
