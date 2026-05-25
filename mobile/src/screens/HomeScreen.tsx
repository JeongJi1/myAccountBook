import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import React from 'react';
import {
  ActivityIndicator,
  Alert,
  FlatList,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { disbursementApi } from '../api/disbursements';
import DisbursementItem from '../components/DisbursementItem';
import { useAuth } from '../context/AuthContext';
import { Disbursement } from '../types';
import { HomeStackParamList } from '../navigation/types';

type Props = {
  navigation: NativeStackNavigationProp<HomeStackParamList, 'Home'>;
};

export default function HomeScreen({ navigation }: Props) {
  const queryClient = useQueryClient();
  const { logout } = useAuth();

  const { data: disbursements, isLoading, isError, refetch } = useQuery({
    queryKey: ['disbursements'],
    queryFn: disbursementApi.getAll,
  });

  const deleteMutation = useMutation({
    mutationFn: disbursementApi.remove,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['disbursements'] }),
    onError: () => Alert.alert('오류', '삭제에 실패했습니다.'),
  });

  const handleDelete = (id: number) => {
    Alert.alert('삭제', '이 지출을 삭제할까요?', [
      { text: '취소', style: 'cancel' },
      { text: '삭제', style: 'destructive', onPress: () => deleteMutation.mutate(id) },
    ]);
  };

  const handleEdit = (item: Disbursement) => {
    navigation.navigate('DisbursementForm', { disbursement: item });
  };

  if (isLoading) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#4F6EF7" />
      </View>
    );
  }

  if (isError) {
    return (
      <View style={styles.centered}>
        <Text style={styles.errorText}>서버에 연결할 수 없습니다.</Text>
        <TouchableOpacity onPress={() => refetch()} style={styles.retryBtn}>
          <Text style={styles.retryText}>다시 시도</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const totalAmount = disbursements?.reduce((sum, d) => sum + d.amount, 0) ?? 0;

  return (
    <View style={styles.container}>
      <View style={styles.summaryCard}>
        <Text style={styles.summaryLabel}>전체 지출</Text>
        <Text style={styles.summaryAmount}>₩{totalAmount.toLocaleString('ko-KR')}</Text>
        <TouchableOpacity onPress={logout} style={styles.logoutBtn}>
          <Text style={styles.logoutText}>로그아웃</Text>
        </TouchableOpacity>
      </View>

      <FlatList
        data={disbursements}
        keyExtractor={(item) => String(item.id)}
        renderItem={({ item }) => (
          <DisbursementItem item={item} onPress={handleEdit} onDelete={handleDelete} />
        )}
        contentContainerStyle={styles.list}
        ListEmptyComponent={
          <Text style={styles.emptyText}>지출 내역이 없습니다.{'\n'}+ 버튼으로 추가해보세요.</Text>
        }
      />

      <TouchableOpacity
        style={styles.fab}
        onPress={() => navigation.navigate('DisbursementForm', {})}
        activeOpacity={0.8}
      >
        <Text style={styles.fabText}>+</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8F9FA' },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', gap: 12 },
  summaryCard: {
    margin: 16,
    padding: 20,
    backgroundColor: '#4F6EF7',
    borderRadius: 16,
    alignItems: 'center',
  },
  summaryLabel: { fontSize: 14, color: 'rgba(255,255,255,0.8)', marginBottom: 4 },
  summaryAmount: { fontSize: 28, fontWeight: '800', color: '#FFFFFF' },
  logoutBtn: { marginTop: 10 },
  logoutText: { fontSize: 12, color: 'rgba(255,255,255,0.7)' },
  list: { paddingVertical: 8, paddingBottom: 88 },
  emptyText: { textAlign: 'center', color: '#8E8E93', marginTop: 60, lineHeight: 24 },
  errorText: { fontSize: 16, color: '#FF3B30' },
  retryBtn: { paddingHorizontal: 24, paddingVertical: 10, backgroundColor: '#4F6EF7', borderRadius: 8 },
  retryText: { color: '#fff', fontWeight: '600' },
  fab: {
    position: 'absolute',
    bottom: 24,
    right: 24,
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: '#4F6EF7',
    justifyContent: 'center',
    alignItems: 'center',
    shadowColor: '#4F6EF7',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.4,
    shadowRadius: 8,
    elevation: 8,
  },
  fabText: { fontSize: 28, color: '#fff', lineHeight: 32 },
});
