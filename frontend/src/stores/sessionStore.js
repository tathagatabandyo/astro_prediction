import { create } from 'zustand';
import { apiClient } from '../api/apiClient';

export const useSessionStore = create((set, get) => ({
  sessions: [],

  fetchSessions: async () => {
    try {
      const response = await apiClient.get('/sessions');
      set({ sessions: response.data || [] });
    } catch (e) { console.error(e); }
  },

  revokeSession: async (sessionId) => {
    await apiClient.delete(`/sessions/${sessionId}`);
    set({ sessions: get().sessions.filter(s => s.sessionId !== sessionId) });
  },

  revokeAll: async () => {
    await apiClient.post('/sessions/logout-all');
    set({ sessions: [] });
  }
}));
