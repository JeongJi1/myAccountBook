import React, { useState } from 'react';
import { Alert, Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useAuth } from '../context/AuthContext';

export default function LoginScreen() {
  const { loginWithKakao } = useAuth();
  const [isLoading, setIsLoading] = useState(false);

  const handleKakaoLogin = async () => {
    setIsLoading(true);
    try {
      await loginWithKakao();
    } catch {
      Alert.alert('로그인 실패', '카카오 로그인 중 문제가 발생했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>가계부</Text>
        <Text style={styles.subtitle}>내 소비를 스마트하게</Text>
      </View>

      <View style={styles.footer}>
        <TouchableOpacity
          style={[styles.kakaoButton, isLoading && styles.kakaoButtonDisabled]}
          onPress={handleKakaoLogin}
          disabled={isLoading}
          activeOpacity={0.85}
        >
          <Text style={styles.kakaoIcon}>💬</Text>
          <Text style={styles.kakaoText}>
            {isLoading ? '로그인 중...' : '카카오로 시작하기'}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8F9FA' },
  header: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 12 },
  title: { fontSize: 48, fontWeight: '800', color: '#4F6EF7' },
  subtitle: { fontSize: 16, color: '#8E8E93' },
  footer: { padding: 32, paddingBottom: 48 },
  kakaoButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FEE500',
    borderRadius: 14,
    paddingVertical: 16,
    gap: 10,
  },
  kakaoButtonDisabled: { opacity: 0.6 },
  kakaoIcon: { fontSize: 22 },
  kakaoText: { fontSize: 16, fontWeight: '700', color: '#191919' },
});
