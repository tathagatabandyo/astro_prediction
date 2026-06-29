import { create } from 'zustand';
import { apiClient } from '../api/apiClient';

export const useChatStore = create((set, get) => ({
  sessions: [],
  currentSession: null,
  messages: [],
  isLoading: false,

  fetchSessions: async () => {
    const response = await apiClient.get('/chat/sessions');
    set({ sessions: response.data || [] });
  },

  startChat: async (astrologerId) => {
    set({ isLoading: true });
    const response = await apiClient.post(`/chat/start/${astrologerId}`);
    set({ currentSession: response.data, isLoading: false });
    return response.data;
  },

  endChat: async (sessionId) => {
    await apiClient.post(`/chat/${sessionId}/end`);
    set({ currentSession: null, messages: [] });
  },

  fetchMessages: async (sessionId, page = 0, size = 50) => {
    const response = await apiClient.get(`/chat/${sessionId}/messages?page=${page}&size=${size}`);
    set({ messages: response.data || [] });
  },

  sendMessage: async (sessionId, content) => {
    const response = await apiClient.post(`/chat/${sessionId}/messages`, { content });
    const newMessages = [response.data, ...get().messages];
    set({ messages: newMessages });
  },

  markRead: async (sessionId) => {
    await apiClient.put(`/chat/${sessionId}/read`);
  },

  addMessage: (message) => {
    set({ messages: [message, ...get().messages] });
  }
}));
