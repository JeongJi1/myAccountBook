import { Category } from '../types';
import client from './client';

export const categoryApi = {
  getAll: async (): Promise<Category[]> => {
    const { data } = await client.get('/categories');
    return data;
  },

  create: async (name: string): Promise<Category> => {
    const { data } = await client.post('/categories', { name });
    return data;
  },

  remove: async (id: number): Promise<void> => {
    await client.delete(`/categories/${id}`);
  },
};
