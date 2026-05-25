import { CreateDisbursementRequest, Disbursement, UpdateDisbursementRequest } from '../types';
import client from './client';

export const disbursementApi = {
  getAll: async (): Promise<Disbursement[]> => {
    const { data } = await client.get('/disbursements');
    return data;
  },

  getById: async (id: number): Promise<Disbursement> => {
    const { data } = await client.get(`/disbursements/${id}`);
    return data;
  },

  create: async (request: CreateDisbursementRequest): Promise<Disbursement> => {
    const { data } = await client.post('/disbursements', request);
    return data;
  },

  update: async (id: number, request: UpdateDisbursementRequest): Promise<Disbursement> => {
    const { data } = await client.put(`/disbursements/${id}`, request);
    return data;
  },

  remove: async (id: number): Promise<void> => {
    await client.delete(`/disbursements/${id}`);
  },
};
