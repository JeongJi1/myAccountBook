import { CategoryStats, MonthlyStats } from '../types';
import client from './client';

export const statisticsApi = {
  getMonthly: async (year: number): Promise<MonthlyStats> => {
    const { data } = await client.get('/statistics/monthly', { params: { year } });
    return data;
  },

  getCategory: async (year: number, month?: number): Promise<CategoryStats> => {
    const { data } = await client.get('/statistics/category', { params: { year, month } });
    return data;
  },
};
