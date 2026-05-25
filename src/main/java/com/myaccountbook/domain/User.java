package com.myaccountbook.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private Long kakaoId;

    @Column
    private String email;

    @Column
    private String nickname;

    @Column(name = "created_dt", nullable = false, updatable = false)
    private LocalDateTime createdDt;

    public static User ofKakao(Long kakaoId, String email, String nickname) {
        User user = new User();
        user.kakaoId = kakaoId;
        user.email = email;
        user.nickname = nickname;
        return user;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDt = LocalDateTime.now();
    }
}
