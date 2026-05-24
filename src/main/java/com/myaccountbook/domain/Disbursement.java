package com.myaccountbook.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="disbursement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String descr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "expense_dt", nullable = false)
    private LocalDateTime expenseDt;

    @Column(name = "created_dt", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "updated_dt", nullable = false)
    private LocalDateTime updatedDt;

    public void update(
            BigDecimal amount,
            String descr,
            Category category,
            LocalDateTime expenseDt
    ) {
        this.amount = amount;
        this.descr = descr;
        this.category = category;
        this.expenseDt = expenseDt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDt = LocalDateTime.now();
        this.updatedDt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDt = LocalDateTime.now();
    }
}
