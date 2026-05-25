import React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Disbursement } from '../types';

interface Props {
  item: Disbursement;
  onPress: (item: Disbursement) => void;
  onDelete: (id: number) => void;
}

function formatAmount(amount: number): string {
  return `₩${amount.toLocaleString('ko-KR')}`;
}

function formatDate(dateStr: string): string {
  const date = new Date(dateStr);
  return `${date.getFullYear()}/${String(date.getMonth() + 1).padStart(2, '0')}/${String(date.getDate()).padStart(2, '0')}`;
}

export default function DisbursementItem({ item, onPress, onDelete }: Props) {
  return (
    <TouchableOpacity style={styles.container} onPress={() => onPress(item)} activeOpacity={0.7}>
      <View style={styles.left}>
        {item.categoryName && (
          <View style={styles.categoryBadge}>
            <Text style={styles.categoryText}>{item.categoryName}</Text>
          </View>
        )}
        <Text style={styles.descr} numberOfLines={1}>{item.descr}</Text>
        <Text style={styles.date}>{formatDate(item.expenseDt)}</Text>
      </View>
      <View style={styles.right}>
        <Text style={styles.amount}>{formatAmount(item.amount)}</Text>
        <TouchableOpacity onPress={() => onDelete(item.id)} hitSlop={{ top: 8, bottom: 8, left: 8, right: 8 }}>
          <Text style={styles.deleteBtn}>삭제</Text>
        </TouchableOpacity>
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#FFFFFF',
    padding: 16,
    marginHorizontal: 16,
    marginVertical: 4,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.05,
    shadowRadius: 4,
    elevation: 2,
  },
  left: { flex: 1, marginRight: 12 },
  categoryBadge: {
    alignSelf: 'flex-start',
    backgroundColor: '#EEF2FF',
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 6,
    marginBottom: 4,
  },
  categoryText: { fontSize: 11, color: '#4F6EF7', fontWeight: '600' },
  descr: { fontSize: 15, color: '#1A1A2E', fontWeight: '500' },
  date: { fontSize: 12, color: '#8E8E93', marginTop: 4 },
  right: { alignItems: 'flex-end', gap: 6 },
  amount: { fontSize: 16, fontWeight: '700', color: '#1A1A2E' },
  deleteBtn: { fontSize: 12, color: '#FF3B30' },
});
