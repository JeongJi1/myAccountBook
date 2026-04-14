package com.myaccountbook.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="disbursement")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Disbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String descr;

    @Column(nullable = false)
    private String category;

    @Column(name = "expense_dt", nullable = false)
    private LocalDateTime expenseDt;

    @Column(name = "created_dt", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "updated_dt", nullable = false)
    private LocalDateTime updatedDt;

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
