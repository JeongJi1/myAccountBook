package com.myaccountbook.controller;

import com.myaccountbook.dto.RefreshRequest;
import com.myaccountbook.dto.TokenResponse;
import com.myaccountbook.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    /** 앱이 이 URL을 브라우저로 열면 카카오 로그인 페이지로 이동 */
    @GetMapping("/kakao/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoRestApiKey
                + "&redirect_uri=" + URLEncoder.encode(kakaoRedirectUri, StandardCharsets.UTF_8)
                + "&response_type=code";
        response.sendRedirect(kakaoAuthUrl);
    }

    /** 카카오가 인가 코드를 여기로 보낸다 */
    @GetMapping("/kakao/callback")
    public void callback(@RequestParam(required = false) String code,
                         @RequestParam(required = false) String error,
                         HttpServletResponse response) throws IOException {
        if (error != null || code == null) {
            response.sendRedirect("about:blank"); // 에러 시 그냥 닫힘
            return;
        }
        String appRedirectUrl = authService.processKakaoCallback(code);
        response.sendRedirect(appRedirectUrl);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request.refreshToken()));
    }
}
