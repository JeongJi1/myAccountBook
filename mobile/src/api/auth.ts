import { TokenResponse } from '../types';
import client from './client';

export const authApi = {
  register: async (email: string, password: string): Promise<TokenResponse> => {
    const { data } = await client.post('/auth/register', { email, password });
    return data;
  },

  login: async (email: string, password: string): Promise<TokenResponse> => {
    const { data } = await client.post('/auth/login', { email, password });
    return data;
  },

  // 인터셉터에서 직접 호출하므로 별도 export
  refresh: async (refreshToken: string): Promise<TokenResponse> => {
    const { data } = await client.post('/auth/refresh', { refreshToken });
    return data;
  },
};
