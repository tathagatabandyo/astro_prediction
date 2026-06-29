import { create } from 'zustand';
import { apiClient } from '../api/apiClient';

export const useAstrologerStore = create((set, get) => ({
  astrologers: [],
  featured: [],
  online: [],
  selected: null,
  isLoading: false,
  filters: { search: '', languages: '', expertise: '', minPrice: '', maxPrice: '', minRating: '', sortBy: 'averageRating', page: 0, size: 20 },

  searchAstrologers: async (filters = {}) => {
    set({ isLoading: true });
    const f = { ...get().filters, ...filters };
    const params = new URLSearchParams();
    Object.entries(f).forEach(([k, v]) => { if (v) params.append(k, v); });
    const response = await apiClient.get(`/astrologers/search?${params.toString()}`);
    set({ astrologers: response.data?.content || [], filters: f, isLoading: false });
  },

  fetchFeatured: async () => {
    const response = await apiClient.get('/astrologers/featured');
    set({ featured: response.data || [] });
  },

  fetchOnline: async () => {
    const response = await apiClient.get('/astrologers/online');
    set({ online: response.data || [] });
  },

  getById: async (id) => {
    const response = await apiClient.get(`/astrologers/${id}`);
    set({ selected: response.data });
    return response.data;
  }
}));
