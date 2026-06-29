import { create } from 'zustand';
import { apiClient } from '../api/apiClient';

export const useNotificationStore = create((set, get) => ({
  notifications: [],
  unreadCount: 0,

  fetchNotifications: async () => {
    const response = await apiClient.get('/notifications');
    set({ notifications: response.data || [] });
  },

  fetchUnreadCount: async () => {
    const response = await apiClient.get('/notifications/unread-count');
    set({ unreadCount: response.data || 0 });
  },

  markAsRead: async (id) => {
    await apiClient.put(`/notifications/${id}/read`);
    set({ notifications: get().notifications.map(n => n.id === id ? { ...n, isRead: true } : n) });
  },

  markAllRead: async () => {
    await apiClient.put('/notifications/read-all');
    set({ notifications: get().notifications.map(n => ({ ...n, isRead: true })), unreadCount: 0 });
  }
}));
