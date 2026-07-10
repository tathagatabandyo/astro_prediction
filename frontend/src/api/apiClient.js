const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api';

async function handleResponse(response) {
  const data = await response.json();
  if (data.error) {
    throw new Error(data.message || 'Request failed');
  }
  return data;
}

export const apiClient = {
  async get(endpoint, options = {}) {
    const token = localStorage.getItem('accessToken');
    const response = await fetch(`${API_BASE}${endpoint}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.headers
      },
      ...options
    });
    if (response.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
      throw new Error('Session expired');
    }
    return handleResponse(response);
  },

  async post(endpoint, body, options = {}) {
    const token = localStorage.getItem('accessToken');
    const isFormData = body instanceof FormData;
    const response = await fetch(`${API_BASE}${endpoint}`, {
      method: 'POST',
      headers: {
        ...(isFormData ? {} : { 'Content-Type': 'application/json' }),
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.headers
      },
      body: isFormData ? body : JSON.stringify(body),
      ...options
    });
    if (response.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
      throw new Error('Session expired');
    }
    return handleResponse(response);
  },

  async put(endpoint, body, options = {}) {
    const token = localStorage.getItem('accessToken');
    const response = await fetch(`${API_BASE}${endpoint}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.headers
      },
      body: JSON.stringify(body),
      ...options
    });
    if (response.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
      throw new Error('Session expired');
    }
    return handleResponse(response);
  },

  async delete(endpoint, options = {}) {
    const token = localStorage.getItem('accessToken');
    const response = await fetch(`${API_BASE}${endpoint}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...options.headers
      },
      ...options
    });
    if (response.status === 401) {
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
      throw new Error('Session expired');
    }
    return handleResponse(response);
  }
};
