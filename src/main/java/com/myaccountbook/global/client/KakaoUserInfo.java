package com.myaccountbook.global.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public Long getId() { return id; }

    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.email : null;
    }

    public String getNickname() {
        if (kakaoAccount == null || kakaoAccount.profile == null) return null;
        return kakaoAccount.profile.nickname;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class KakaoAccount {
        @JsonProperty("email")
        String email;

        @JsonProperty("profile")
        Profile profile;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Profile {
        @JsonProperty("nickname")
        String nickname;
    }
}
