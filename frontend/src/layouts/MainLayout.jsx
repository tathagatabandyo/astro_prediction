import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { useWalletStore } from '../stores/walletStore';
import { useNotificationStore } from '../stores/notificationStore';
import { useEffect, useState } from 'react';
import { MessageCircle, Star, Sparkles, Sun, Wallet, User, LogOut, Bell, ChevronDown, Menu, X, Home, Search } from 'lucide-react';

export default function MainLayout({ children }) {
  const { user, logout } = useAuthStore();
  const { wallet, fetchWallet } = useWalletStore();
  const { unreadCount, fetchUnreadCount } = useNotificationStore();
  const location = useLocation();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [userMenuOpen, setUserMenuOpen] = useState(false);

  useEffect(() => {
    if (user) { fetchWallet(); fetchUnreadCount(); }
  }, [user]);

  const navLinks = [
    { path: '/', label: 'Home', icon: Home },
    { path: '/astrologers', label: 'Astrologers', icon: Search },
    { path: '/horoscope', label: 'Horoscope', icon: Sun },
    { path: '/ai-insights', label: 'AI Insights', icon: Sparkles },
    { path: '/chat', label: 'My Chats', icon: MessageCircle },
  ];

  return (
    <div className="min-h-screen bg-[#0F0A1E]">
      <header className="fixed top-0 left-0 right-0 h-16 bg-[#1A103C]/95 backdrop-blur-md border-b border-[rgba(255,255,255,0.08)] z-50">
        <div className="max-w-[1440px] mx-auto px-6 h-full flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2">
            <Star className="w-6 h-6 text-brand-primary" />
            <span className="text-xl font-bold gradient-text">AstroPrediction</span>
          </Link>
          <nav className="hidden md:flex items-center gap-1">
            {navLinks.map(link => {
              const Icon = link.icon;
              const isActive = location.pathname === link.path;
              return (
                <Link key={link.path} to={link.path}
                  className={`flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all ${isActive ? 'bg-brand-primary/20 text-brand-primary' : 'text-text-secondary hover:text-text-primary hover:bg-[#251850]'}`}>
                  <Icon className="w-4 h-4" />{link.label}
                </Link>
              );
            })}
          </nav>
          <div className="flex items-center gap-3">
            {user && (
              <Link to="/wallet" className="hidden sm:flex items-center gap-2 px-3 py-1.5 rounded-lg bg-success/10 text-success text-sm font-medium">
                <Wallet className="w-4 h-4" />Rs. {wallet?.balance?.toFixed?.(2) || '0.00'}
              </Link>
            )}
            {user && (
              <button className="relative p-2 rounded-lg hover:bg-[#251850] transition-colors">
                <Bell className="w-5 h-5 text-text-secondary" />
                {unreadCount > 0 && (
                  <span className="absolute -top-0.5 -right-0.5 w-4 h-4 bg-brand-primary text-white text-[10px] font-bold rounded-full flex items-center justify-center">{unreadCount}</span>
                )}
              </button>
            )}
            {user ? (
              <div className="relative">
                <button onClick={() => setUserMenuOpen(!userMenuOpen)} className="flex items-center gap-2 p-1 rounded-lg hover:bg-[#251850] transition-colors">
                  <div className="w-8 h-8 rounded-full gradient-bg flex items-center justify-center text-white text-sm font-semibold">
                    {user?.fullName?.charAt(0)?.toUpperCase() || 'U'}
                  </div>
                  <ChevronDown className="w-4 h-4 text-text-muted" />
                </button>
                {userMenuOpen && (
                  <>
                    <div className="fixed inset-0" onClick={() => setUserMenuOpen(false)} />
                    <div className="absolute right-0 mt-2 w-56 bg-[#1A103C] rounded-xl border border-[rgba(255,255,255,0.08)] shadow-xl py-2 animate-fade-in">
                      <div className="px-4 py-3 border-b border-[rgba(255,255,255,0.08)]">
                        <p className="text-sm font-semibold text-text-primary">{user?.fullName}</p>
                        <p className="text-xs text-text-muted">{user?.email}</p>
                      </div>
                      <Link to="/profile" onClick={() => setUserMenuOpen(false)} className="flex items-center gap-3 px-4 py-2.5 text-sm text-text-secondary hover:bg-[#251850] hover:text-text-primary transition-colors">
                        <User className="w-4 h-4" /> Profile
                      </Link>
                      <Link to="/wallet" onClick={() => setUserMenuOpen(false)} className="flex items-center gap-3 px-4 py-2.5 text-sm text-text-secondary hover:bg-[#251850] hover:text-text-primary transition-colors">
                        <Wallet className="w-4 h-4" /> Wallet
                      </Link>
                      <button onClick={() => { logout(); navigate('/login'); }} className="w-full flex items-center gap-3 px-4 py-2.5 text-sm text-danger hover:bg-[#251850] transition-colors text-left">
                        <LogOut className="w-4 h-4" /> Logout
                      </button>
                    </div>
                  </>
                )}
              </div>
            ) : (
              <div className="flex items-center gap-2">
                <Link to="/login" className="btn-outline text-sm py-2 px-4">Sign In</Link>
                <Link to="/register" className="btn-primary text-sm py-2 px-4">Sign Up</Link>
              </div>
            )}
            <button className="md:hidden p-2 rounded-lg hover:bg-[#251850]" onClick={() => setMobileMenuOpen(!mobileMenuOpen)}>
              {mobileMenuOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
            </button>
          </div>
        </div>
      </header>
      {mobileMenuOpen && (
        <div className="fixed inset-0 top-16 bg-[#0F0A1E] z-40 md:hidden p-6">
          <nav className="flex flex-col gap-2">
            {navLinks.map(link => {
              const Icon = link.icon;
              return (
                <Link key={link.path} to={link.path} onClick={() => setMobileMenuOpen(false)}
                  className="flex items-center gap-3 px-4 py-3 rounded-lg text-text-secondary hover:bg-[#251850] hover:text-text-primary transition-colors">
                  <Icon className="w-5 h-5" />{link.label}
                </Link>
              );
            })}
          </nav>
        </div>
      )}
      <main className="pt-16 min-h-screen">{children}</main>
    </div>
  );
}
