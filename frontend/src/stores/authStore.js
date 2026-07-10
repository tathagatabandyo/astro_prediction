import { create } from 'zustand';
import { apiClient } from '../api/apiClient';

export const useAuthStore = create((set, get) => ({
  accessToken: localStorage.getItem('accessToken'),
  user: null,
  isLoading: false,
  isAuthenticated: !!localStorage.getItem('accessToken'),

  login: async (email, password) => {
    set({ isLoading: true });
    try {
      const response = await apiClient.post('/auth/login', { email, password });
      const { accessToken, expiresIn, sessionId } = response.data;
      localStorage.setItem('accessToken', accessToken);
      set({ accessToken, isAuthenticated: true });
      // Fetch current user
      const userRes = await apiClient.get('/users/current');
      set({ user: userRes.data, isLoading: false });
      return { success: true };
    } catch (error) {
      set({ isLoading: false });
      return { success: false, error: error.message };
    }
  },

  register: async (fullName, email, password, confirmPassword) => {
    set({ isLoading: true });
    try {
      await apiClient.post('/auth/register', { fullName, email, password, confirmPassword });
      set({ isLoading: false });
      return { success: true };
    } catch (error) {
      set({ isLoading: false });
      return { success: false, error: error.message };
    }
  },

  logout: async () => {
    try { await apiClient.post('/auth/logout'); } catch (e) {}
    localStorage.removeItem('accessToken');
    set({ accessToken: null, user: null, isAuthenticated: false });
  },

  fetchCurrentUser: async () => {
    try {
      const response = await apiClient.get('/users/current');
      set({ user: response.data, isAuthenticated: true });
    } catch (error) {
      localStorage.removeItem('accessToken');
      set({ accessToken: null, user: null, isAuthenticated: false });
    }
  },

  updateProfile: async (data) => {
    const response = await apiClient.put('/users/profile', data);
    set({ user: response.data });
  }
}));
