package com.myaccountbook.service;

import com.myaccountbook.domain.RefreshToken;
import com.myaccountbook.domain.User;
import com.myaccountbook.dto.TokenResponse;
import com.myaccountbook.global.client.KakaoAuthClient;
import com.myaccountbook.global.client.KakaoToken;
import com.myaccountbook.global.client.KakaoUserInfo;
import com.myaccountbook.global.security.JwtTokenProvider;
import com.myaccountbook.repository.RefreshTokenRepository;
import com.myaccountbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoAuthClient kakaoAuthClient;

    @Value("${kakao.app-redirect-uri}")
    private String appRedirectUri;

    /**
     * Kakao 인가 코드를 받아 사용자 정보를 조회 후 JWT를 발급한다.
     * 완료 후 앱 딥링크 URI(토큰 포함)를 반환한다.
     */
    @Transactional
    public String processKakaoCallback(String code) {
        KakaoToken kakaoToken = kakaoAuthClient.getToken(code);
        KakaoUserInfo userInfo = kakaoAuthClient.getUserInfo(kakaoToken.accessToken());

        User user = userRepository.findByKakaoId(userInfo.getId())
                .orElseGet(() -> userRepository.save(
                        User.ofKakao(userInfo.getId(), userInfo.getEmail(), userInfo.getNickname())
                ));

        TokenResponse tokens = issueTokenPair(user.getId());

        return appRedirectUri
                + "?accessToken=" + tokens.accessToken()
                + "&refreshToken=" + tokens.refreshToken();
    }

    @Transactional
    public TokenResponse refresh(String rawToken) {
        RefreshToken stored = refreshTokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        if (stored.isExpired()) {
            refreshTokenRepository.delete(stored);
            throw new IllegalArgumentException("만료된 리프레시 토큰입니다. 다시 로그인해주세요.");
        }

        Long userId = stored.getUserId();
        refreshTokenRepository.delete(stored);
        return issueTokenPair(userId);
    }

    private TokenResponse issueTokenPair(Long userId) {
        String accessToken = jwtTokenProvider.generateAccessToken(userId);
        String rawRefresh = jwtTokenProvider.generateRefreshToken(userId);

        refreshTokenRepository.save(RefreshToken.of(
                userId,
                rawRefresh,
                LocalDateTime.now().plusDays(7)
        ));

        return new TokenResponse(accessToken, rawRefresh);
    }
}
