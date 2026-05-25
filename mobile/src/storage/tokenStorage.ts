import * as SecureStore from 'expo-secure-store';

const KEYS = {
  ACCESS: 'access_token',
  REFRESH: 'refresh_token',
} as const;

export const tokenStorage = {
  getAccessToken: () => SecureStore.getItemAsync(KEYS.ACCESS),
  getRefreshToken: () => SecureStore.getItemAsync(KEYS.REFRESH),

  saveTokens: async (accessToken: string, refreshToken: string) => {
    await SecureStore.setItemAsync(KEYS.ACCESS, accessToken);
    await SecureStore.setItemAsync(KEYS.REFRESH, refreshToken);
  },

  clearTokens: async () => {
    await SecureStore.deleteItemAsync(KEYS.ACCESS);
    await SecureStore.deleteItemAsync(KEYS.REFRESH);
  },
};
