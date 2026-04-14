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
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class disbursement {
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
    private LocalDateTime expense_dt;

    @Column(name = "created_dt", nullable = false)
    private LocalDateTime created_dt;

    @Column(name = "updated_dt", nullable = false)
    private LocalDateTime updated_dt;

    @PrePersist
    protected void onCreate() {
        this.created_dt = LocalDateTime.now();
        this.updated_dt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_dt = LocalDateTime.now();
    }


}
