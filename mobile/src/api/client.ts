import axios, { InternalAxiosRequestConfig } from 'axios';
import { API_BASE_URL } from '../config';
import { tokenStorage } from '../storage/tokenStorage';
import { authEvents } from '../utils/authEvents';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' },
});

// 요청마다 저장된 access token을 헤더에 주입
apiClient.interceptors.request.use(async (config: InternalAxiosRequestConfig) => {
  const token = await tokenStorage.getAccessToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 동시에 여러 요청이 401을 받을 때 refresh를 한 번만 호출하기 위한 큐
let isRefreshing = false;
let pendingQueue: Array<{ resolve: (token: string) => void; reject: (err: unknown) => void }> = [];

const flushQueue = (token: string | null, error: unknown = null) => {
  pendingQueue.forEach(({ resolve, reject }) => (token ? resolve(token) : reject(error)));
  pendingQueue = [];
};

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const original = error.config;

    if (error.response?.status !== 401 || original._retry) {
      return Promise.reject(error);
    }

    if (isRefreshing) {
      // 이미 갱신 중이면 큐에 넣고 대기
      return new Promise((resolve, reject) => {
        pendingQueue.push({ resolve, reject });
      }).then((newToken) => {
        original.headers.Authorization = `Bearer ${newToken}`;
        return apiClient(original);
      });
    }

    original._retry = true;
    isRefreshing = true;

    try {
      const refreshToken = await tokenStorage.getRefreshToken();
      if (!refreshToken) throw new Error('no_refresh_token');

      // 순환 참조를 피하기 위해 axios 인스턴스를 직접 사용
      const { data } = await axios.post(`${API_BASE_URL}/auth/refresh`, { refreshToken });
      const { accessToken, refreshToken: newRefresh } = data;

      await tokenStorage.saveTokens(accessToken, newRefresh);
      flushQueue(accessToken);

      original.headers.Authorization = `Bearer ${accessToken}`;
      return apiClient(original);
    } catch (refreshError) {
      flushQueue(null, refreshError);
      await tokenStorage.clearTokens();
      authEvents.triggerLogout(); // AuthContext에 로그아웃 신호
      return Promise.reject(refreshError);
    } finally {
      isRefreshing = false;
    }
  }
);

export default apiClient;
