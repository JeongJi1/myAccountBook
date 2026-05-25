import DateTimePicker, { DateTimePickerEvent } from '@react-native-community/datetimepicker';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RouteProp } from '@react-navigation/native';
import React, { useState } from 'react';
import {
  Alert,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import { categoryApi } from '../api/categories';
import { disbursementApi } from '../api/disbursements';
import { HomeStackParamList } from '../navigation/types';

type Props = {
  navigation: NativeStackNavigationProp<HomeStackParamList, 'DisbursementForm'>;
  route: RouteProp<HomeStackParamList, 'DisbursementForm'>;
};

function toLocalISOString(date: Date): string {
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}:00`;
}

export default function DisbursementFormScreen({ navigation, route }: Props) {
  const existing = route.params?.disbursement;
  const isEdit = !!existing;
  const queryClient = useQueryClient();

  const [amount, setAmount] = useState(existing ? String(existing.amount) : '');
  const [descr, setDescr] = useState(existing?.descr ?? '');
  const [categoryId, setCategoryId] = useState<number | null>(existing?.categoryId ?? null);
  const [expenseDt, setExpenseDt] = useState(existing ? new Date(existing.expenseDt) : new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);

  const { data: categories } = useQuery({
    queryKey: ['categories'],
    queryFn: categoryApi.getAll,
  });

  const createMutation = useMutation({
    mutationFn: disbursementApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['disbursements'] });
      navigation.goBack();
    },
    onError: () => Alert.alert('오류', '저장에 실패했습니다.'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: Parameters<typeof disbursementApi.update>[1] }) =>
      disbursementApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['disbursements'] });
      navigation.goBack();
    },
    onError: () => Alert.alert('오류', '수정에 실패했습니다.'),
  });

  const handleSubmit = () => {
    const parsedAmount = parseFloat(amount);
    if (!amount || isNaN(parsedAmount) || parsedAmount <= 0) {
      Alert.alert('입력 오류', '올바른 금액을 입력하세요.');
      return;
    }
    if (!descr.trim()) {
      Alert.alert('입력 오류', '사용처를 입력하세요.');
      return;
    }
    if (!categoryId) {
      Alert.alert('입력 오류', '카테고리를 선택하세요.');
      return;
    }

    const payload = {
      amount: parsedAmount,
      descr: descr.trim(),
      categoryId,
      expenseDt: toLocalISOString(expenseDt),
    };

    if (isEdit && existing) {
      updateMutation.mutate({ id: existing.id, data: payload });
    } else {
      createMutation.mutate(payload);
    }
  };

  const handleDateChange = (_: DateTimePickerEvent, selectedDate?: Date) => {
    setShowDatePicker(Platform.OS === 'ios');
    if (selectedDate) setExpenseDt(selectedDate);
  };

  const isPending = createMutation.isPending || updateMutation.isPending;

  return (
    <KeyboardAvoidingView behavior={Platform.OS === 'ios' ? 'padding' : undefined} style={{ flex: 1 }}>
      <ScrollView style={styles.container} contentContainerStyle={styles.content}>
        <Text style={styles.label}>금액 (₩)</Text>
        <TextInput
          style={styles.input}
          value={amount}
          onChangeText={setAmount}
          keyboardType="numeric"
          placeholder="10000"
          placeholderTextColor="#C7C7CC"
        />

        <Text style={styles.label}>사용처</Text>
        <TextInput
          style={styles.input}
          value={descr}
          onChangeText={setDescr}
          placeholder="어디서 썼나요?"
          placeholderTextColor="#C7C7CC"
        />

        <Text style={styles.label}>카테고리</Text>
        <View style={styles.categoryRow}>
          {categories?.map((cat) => (
            <TouchableOpacity
              key={cat.id}
              style={[styles.categoryChip, categoryId === cat.id && styles.categoryChipActive]}
              onPress={() => setCategoryId(cat.id)}
            >
              <Text style={[styles.categoryChipText, categoryId === cat.id && styles.categoryChipTextActive]}>
                {cat.name}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        <Text style={styles.label}>지출 일시</Text>
        <TouchableOpacity style={styles.dateButton} onPress={() => setShowDatePicker(true)}>
          <Text style={styles.dateText}>{toLocalISOString(expenseDt).replace('T', ' ').slice(0, 16)}</Text>
        </TouchableOpacity>

        {showDatePicker && (
          <DateTimePicker
            value={expenseDt}
            mode="datetime"
            display={Platform.OS === 'ios' ? 'spinner' : 'default'}
            onChange={handleDateChange}
            locale="ko-KR"
          />
        )}

        <TouchableOpacity
          style={[styles.submitBtn, isPending && styles.submitBtnDisabled]}
          onPress={handleSubmit}
          disabled={isPending}
        >
          <Text style={styles.submitText}>{isEdit ? '수정하기' : '저장하기'}</Text>
        </TouchableOpacity>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8F9FA' },
  content: { padding: 20, gap: 8 },
  label: { fontSize: 14, fontWeight: '600', color: '#1A1A2E', marginTop: 12, marginBottom: 4 },
  input: {
    backgroundColor: '#FFFFFF',
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 12,
    fontSize: 16,
    color: '#1A1A2E',
    borderWidth: 1,
    borderColor: '#E5E5EA',
  },
  categoryRow: { flexDirection: 'row', flexWrap: 'wrap', gap: 8 },
  categoryChip: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: '#FFFFFF',
    borderWidth: 1,
    borderColor: '#E5E5EA',
  },
  categoryChipActive: { backgroundColor: '#4F6EF7', borderColor: '#4F6EF7' },
  categoryChipText: { fontSize: 14, color: '#8E8E93' },
  categoryChipTextActive: { color: '#FFFFFF', fontWeight: '600' },
  dateButton: {
    backgroundColor: '#FFFFFF',
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 12,
    borderWidth: 1,
    borderColor: '#E5E5EA',
  },
  dateText: { fontSize: 16, color: '#1A1A2E' },
  submitBtn: {
    marginTop: 32,
    backgroundColor: '#4F6EF7',
    borderRadius: 12,
    paddingVertical: 16,
    alignItems: 'center',
  },
  submitBtnDisabled: { backgroundColor: '#A0AFFE' },
  submitText: { fontSize: 16, fontWeight: '700', color: '#FFFFFF' },
});
