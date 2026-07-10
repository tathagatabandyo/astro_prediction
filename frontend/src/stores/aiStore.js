import { create } from 'zustand';
import { apiClient } from '../api/apiClient';

export const useAIStore = create((set, get) => ({
  readings: [],
  currentReading: null,
  isLoading: false,

  generateKundli: async (data) => {
    set({ isLoading: true });
    const response = await apiClient.post('/ai/kundli', data);
    set({ currentReading: response.data, isLoading: false });
    return response.data;
  },

  analyzeCompatibility: async (data) => {
    set({ isLoading: true });
    const response = await apiClient.post('/ai/compatibility', data);
    set({ currentReading: response.data, isLoading: false });
    return response.data;
  },

  getDailyInsights: async () => {
    set({ isLoading: true });
    const response = await apiClient.get('/ai/daily-insights');
    set({ currentReading: response.data, isLoading: false });
    return response.data;
  },

  getCareerGuidance: async (data) => {
    set({ isLoading: true });
    const response = await apiClient.post('/ai/career', data);
    set({ currentReading: response.data, isLoading: false });
    return response.data;
  },

  getRemedies: async (data) => {
    set({ isLoading: true });
    const response = await apiClient.post('/ai/remedies', data);
    set({ currentReading: response.data, isLoading: false });
    return response.data;
  },

  fetchHistory: async (page = 0, size = 20) => {
    const response = await apiClient.get(`/ai/readings?page=${page}&size=${size}`);
    set({ readings: response.data?.content || [] });
  }
}));
