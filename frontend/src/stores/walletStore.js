import { create } from 'zustand';
import { apiClient } from '../api/apiClient';

export const useWalletStore = create((set, get) => ({
  wallet: null,
  transactions: [],
  plans: [],
  isLoading: false,

  fetchWallet: async () => {
    const response = await apiClient.get('/wallet');
    set({ wallet: response.data });
  },

  fetchTransactions: async (page = 0, size = 20) => {
    const response = await apiClient.get(`/wallet/transactions?page=${page}&size=${size}`);
    set({ transactions: response.data.content || [] });
  },

  fetchPlans: async () => {
    const response = await apiClient.get('/wallet/plans');
    set({ plans: response.data || [] });
  },

  createCheckout: async (rechargePlanId, amount, couponCode) => {
    const response = await apiClient.post('/payments/stripe/checkout-session', {
      rechargePlanId, amount, couponCode
    });
    return response.data;
  }
}));
