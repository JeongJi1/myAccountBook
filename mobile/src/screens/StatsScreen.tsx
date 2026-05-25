import { useQuery } from '@tanstack/react-query';
import React, { useState } from 'react';
import {
  ActivityIndicator,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import { statisticsApi } from '../api/statistics';

const MONTH_LABELS = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];

export default function StatsScreen() {
  const currentYear = new Date().getFullYear();
  const [year, setYear] = useState(currentYear);

  const { data: monthly, isLoading: monthlyLoading } = useQuery({
    queryKey: ['stats', 'monthly', year],
    queryFn: () => statisticsApi.getMonthly(year),
  });

  const { data: category } = useQuery({
    queryKey: ['stats', 'category', year],
    queryFn: () => statisticsApi.getCategory(year),
  });

  const maxMonthlyTotal = Math.max(...(monthly?.months.map((m) => m.total) ?? [1]));

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.content}>
      {/* 연도 선택 */}
      <View style={styles.yearRow}>
        <TouchableOpacity onPress={() => setYear((y) => y - 1)} style={styles.yearBtn}>
          <Text style={styles.yearBtnText}>{'<'}</Text>
        </TouchableOpacity>
        <Text style={styles.yearText}>{year}년</Text>
        <TouchableOpacity
          onPress={() => setYear((y) => y + 1)}
          style={[styles.yearBtn, year >= currentYear && styles.yearBtnDisabled]}
          disabled={year >= currentYear}
        >
          <Text style={[styles.yearBtnText, year >= currentYear && styles.yearBtnTextDisabled]}>{'>'}</Text>
        </TouchableOpacity>
      </View>

      {/* 연간 합계 */}
      <View style={styles.card}>
        <Text style={styles.cardLabel}>{year}년 총 지출</Text>
        <Text style={styles.cardValue}>₩{(monthly?.yearTotal ?? 0).toLocaleString('ko-KR')}</Text>
      </View>

      {/* 월별 현황 */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>월별 지출</Text>
        {monthlyLoading ? (
          <ActivityIndicator color="#4F6EF7" />
        ) : (
          monthly?.months.map((entry) => {
            const ratio = maxMonthlyTotal > 0 ? entry.total / maxMonthlyTotal : 0;
            return (
              <View key={entry.month} style={styles.barRow}>
                <Text style={styles.barLabel}>{MONTH_LABELS[entry.month - 1]}</Text>
                <View style={styles.barTrack}>
                  <View style={[styles.barFill, { flex: ratio }]} />
                  <View style={{ flex: 1 - ratio }} />
                </View>
                <Text style={styles.barAmount}>
                  {entry.total > 0 ? `₩${entry.total.toLocaleString('ko-KR')}` : '-'}
                </Text>
              </View>
            );
          })
        )}
      </View>

      {/* 카테고리별 현황 */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>카테고리별 지출</Text>
        {(category?.categories ?? []).length === 0 ? (
          <Text style={styles.emptyText}>데이터 없음</Text>
        ) : (
          category?.categories.map((entry) => {
            const ratio = (category.total ?? 0) > 0 ? entry.total / category.total : 0;
            return (
              <View key={entry.categoryName} style={styles.categoryRow}>
                <View style={styles.categoryHeader}>
                  <Text style={styles.categoryName}>{entry.categoryName}</Text>
                  <Text style={styles.categoryAmount}>₩{entry.total.toLocaleString('ko-KR')}</Text>
                </View>
                <View style={styles.barTrack}>
                  <View style={[styles.barFillCategory, { flex: ratio }]} />
                  <View style={{ flex: 1 - ratio }} />
                </View>
                <Text style={styles.categoryMeta}>{Math.round(ratio * 100)}% · {entry.count}건</Text>
              </View>
            );
          })
        )}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#F8F9FA' },
  content: { padding: 16, gap: 16, paddingBottom: 40 },
  yearRow: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 20 },
  yearBtn: { padding: 8 },
  yearBtnDisabled: { opacity: 0.3 },
  yearBtnText: { fontSize: 20, color: '#4F6EF7', fontWeight: '700' },
  yearBtnTextDisabled: { color: '#C7C7CC' },
  yearText: { fontSize: 20, fontWeight: '700', color: '#1A1A2E' },
  card: {
    backgroundColor: '#4F6EF7',
    borderRadius: 16,
    padding: 20,
    alignItems: 'center',
  },
  cardLabel: { fontSize: 14, color: 'rgba(255,255,255,0.8)' },
  cardValue: { fontSize: 26, fontWeight: '800', color: '#FFFFFF', marginTop: 4 },
  section: { backgroundColor: '#FFFFFF', borderRadius: 16, padding: 16, gap: 12 },
  sectionTitle: { fontSize: 16, fontWeight: '700', color: '#1A1A2E', marginBottom: 4 },
  barRow: { flexDirection: 'row', alignItems: 'center', gap: 8 },
  barLabel: { width: 30, fontSize: 12, color: '#8E8E93', textAlign: 'right' },
  barTrack: { flex: 1, height: 8, flexDirection: 'row', backgroundColor: '#F2F2F7', borderRadius: 4, overflow: 'hidden' },
  barFill: { backgroundColor: '#4F6EF7', borderRadius: 4 },
  barFillCategory: { backgroundColor: '#34C759', borderRadius: 4 },
  barAmount: { width: 80, fontSize: 11, color: '#1A1A2E', textAlign: 'right' },
  categoryRow: { gap: 4 },
  categoryHeader: { flexDirection: 'row', justifyContent: 'space-between' },
  categoryName: { fontSize: 14, fontWeight: '600', color: '#1A1A2E' },
  categoryAmount: { fontSize: 14, fontWeight: '600', color: '#1A1A2E' },
  categoryMeta: { fontSize: 11, color: '#8E8E93' },
  emptyText: { color: '#8E8E93', textAlign: 'center', paddingVertical: 12 },
});
