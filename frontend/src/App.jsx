import { Routes, Route, Navigate } from 'react-router-dom';
import { useEffect } from 'react';
import { useAuthStore } from './stores/authStore';
import MainLayout from './layouts/MainLayout';
import AuthLayout from './layouts/AuthLayout';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Astrologers from './pages/Astrologers';
import Chat from './pages/Chat';
import Wallet from './pages/Wallet';
import AIInsights from './pages/AIInsights';
import Horoscope from './pages/Horoscope';
import Profile from './pages/Profile';
import Admin from './pages/Admin';

function App() {
  const { isAuthenticated, fetchCurrentUser } = useAuthStore();

  useEffect(() => {
    if (isAuthenticated) {
      fetchCurrentUser();
    }
  }, []);

  return (
    <Routes>
      {/* Auth Routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Main Routes */}
      <Route path="/" element={<MainLayout><Home /></MainLayout>} />
      <Route path="/astrologers" element={<MainLayout><Astrologers /></MainLayout>} />
      <Route path="/chat" element={<MainLayout><Chat /></MainLayout>} />
      <Route path="/wallet" element={<MainLayout><Wallet /></MainLayout>} />
      <Route path="/ai-insights" element={<MainLayout><AIInsights /></MainLayout>} />
      <Route path="/horoscope" element={<MainLayout><Horoscope /></MainLayout>} />
      <Route path="/profile" element={<MainLayout><Profile /></MainLayout>} />
      <Route path="/admin" element={<MainLayout><Admin /></MainLayout>} />

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
