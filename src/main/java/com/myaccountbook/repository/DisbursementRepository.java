package com.myaccountbook.repository;

import com.myaccountbook.domain.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {

    boolean existsByCategoryId(Long categoryId);

    @Query(value = """
            SELECT EXTRACT(MONTH FROM expense_dt)::int AS month, SUM(amount) AS total
            FROM disbursement
            WHERE expense_dt >= :start AND expense_dt < :end
            GROUP BY EXTRACT(MONTH FROM expense_dt)
            ORDER BY 1
            """, nativeQuery = true)
    List<Object[]> findMonthlyStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT c.name AS category_name, SUM(d.amount) AS total, COUNT(d.id) AS count
            FROM disbursement d
            JOIN category c ON d.category_id = c.id
            WHERE d.expense_dt >= :start AND d.expense_dt < :end
            GROUP BY c.name
            ORDER BY total DESC
            """, nativeQuery = true)
    List<Object[]> findCategoryStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
