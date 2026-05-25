import * as Linking from 'expo-linking';
import * as WebBrowser from 'expo-web-browser';
import React, { createContext, useCallback, useContext, useEffect, useState } from 'react';
import { API_BASE_URL } from '../config';
import { tokenStorage } from '../storage/tokenStorage';
import { authEvents } from '../utils/authEvents';

interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  loginWithKakao: () => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  const logout = useCallback(async () => {
    await tokenStorage.clearTokens();
    setIsAuthenticated(false);
  }, []);

  useEffect(() => {
    tokenStorage.getAccessToken().then((token) => {
      setIsAuthenticated(!!token);
      setIsLoading(false);
    });
  }, []);

  useEffect(() => {
    authEvents.setLogoutHandler(logout);
  }, [logout]);

  const loginWithKakao = async () => {
    const authUrl = `${API_BASE_URL}/auth/kakao/authorize`;

    // 백엔드가 OAuth 흐름을 처리하고 exp:// 딥링크로 토큰을 보내준다
    const result = await WebBrowser.openAuthSessionAsync(authUrl, 'exp://');

    if (result.type !== 'success' || !result.url) return;

    // exp://ip:port/--/auth?accessToken=xxx&refreshToken=xxx 파싱
    const parsed = Linking.parse(result.url);
    const accessToken = parsed.queryParams?.accessToken as string | undefined;
    const refreshToken = parsed.queryParams?.refreshToken as string | undefined;

    if (!accessToken || !refreshToken) return;

    await tokenStorage.saveTokens(accessToken, refreshToken);
    setIsAuthenticated(true);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, isLoading, loginWithKakao, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
