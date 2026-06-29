import { useEffect, useState } from 'react';
import { useAuthStore } from '../stores/authStore';
import { useNavigate } from 'react-router-dom';
import { apiClient } from '../api/apiClient';
import { Users, Star, DollarSign, MessageCircle, Clock, TrendingUp, Activity } from 'lucide-react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

export default function Admin() {
  const navigate = useNavigate();
  const { user } = useAuthStore();
  const [stats, setStats] = useState(null);
  const [users, setUsers] = useState([]);
  const [pendingApprovals, setPendingApprovals] = useState([]);
  const [activeTab, setActiveTab] = useState('dashboard');

  useEffect(() => {
    if (!user?.roles?.includes('ROLE_ADMIN')) {
      if (!user) navigate('/login');
      else navigate('/');
      return;
    }
    fetchDashboardData();
  }, [user]);

  const fetchDashboardData = async () => {
    try {
      const statsRes = await apiClient.get('/admin/dashboard/stats');
      setStats(statsRes.data);
    } catch (e) { console.error(e); }
  };

  if (!user?.roles?.includes('ROLE_ADMIN')) return null;

  const statCards = [
    { label: 'Total Users', value: stats?.totalUsers || 0, icon: Users, color: 'text-brand-primary', bg: 'bg-brand-primary/10' },
    { label: 'Astrologers', value: stats?.totalAstrologers || 0, icon: Star, color: 'text-brand-secondary', bg: 'bg-brand-secondary/10' },
    { label: 'Today Revenue', value: `Rs. ${stats?.todayRevenue || 0}`, icon: DollarSign, color: 'text-success', bg: 'bg-success/10' },
    { label: 'Active Chats', value: stats?.activeChats || 0, icon: MessageCircle, color: 'text-info', bg: 'bg-info/10' },
    { label: 'Pending', value: stats?.pendingApprovals || 0, icon: Clock, color: 'text-warning', bg: 'bg-warning/10' },
  ];

  const pieData = stats?.userDistribution?.map(d => ({ name: d.label, value: d.value })) || [
    { name: 'Users', value: 100 }, { name: 'Astrologers', value: 20 }
  ];
  const COLORS = ['#4F46E5', '#7C3AED', '#10B981', '#F59E0B'];

  return (
    <div className="min-h-screen">
      <div className="gradient-bg py-8 px-6">
        <div className="max-w-[1440px] mx-auto">
          <h1 className="text-2xl font-bold text-white">Admin Dashboard</h1>
          <p className="text-white/70 text-sm">Manage your platform</p>
        </div>
      </div>

      <div className="max-w-[1440px] mx-auto px-6 py-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4 mb-8">
          {statCards.map((card, i) => {
            const Icon = card.icon;
            return (
              <div key={i} className="card p-4">
                <div className={`w-10 h-10 rounded-xl ${card.bg} flex items-center justify-center mb-3`}>
                  <Icon className={`w-5 h-5 ${card.color}`} />
                </div>
                <p className="text-text-muted text-xs">{card.label}</p>
                <p className="text-xl font-bold text-text-primary">{card.value}</p>
              </div>
            );
          })}
        </div>

        {/* Charts */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
          <div className="card lg:col-span-2">
            <h3 className="text-lg font-semibold text-text-primary mb-4">Revenue Overview</h3>
            <ResponsiveContainer width="100%" height={300}>
              <AreaChart data={stats?.revenueChart || []}>
                <defs><linearGradient id="colorRev" x1="0" y1="0" x2="0" y2="1"><stop offset="5%" stopColor="#4F46E5" stopOpacity={0.3}/><stop offset="95%" stopColor="#4F46E5" stopOpacity={0}/></linearGradient></defs>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" />
                <XAxis dataKey="label" stroke="#64748B" fontSize={12} />
                <YAxis stroke="#64748B" fontSize={12} />
                <Tooltip contentStyle={{ background: '#1A103C', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px' }} />
                <Area type="monotone" dataKey="value" stroke="#4F46E5" fillOpacity={1} fill="url(#colorRev)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
          <div className="card">
            <h3 className="text-lg font-semibold text-text-primary mb-4">User Distribution</h3>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie data={pieData} cx="50%" cy="50%" innerRadius={60} outerRadius={100} dataKey="value">
                  {pieData.map((_, i) => <Cell key={`cell-${i}`} fill={COLORS[i % COLORS.length]} />)}
                </Pie>
                <Tooltip contentStyle={{ background: '#1A103C', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px' }} />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>
    </div>
  );
}
