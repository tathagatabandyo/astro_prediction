import { useState, useEffect } from 'react';
import { useAuthStore } from '../stores/authStore';
import { useSessionStore } from '../stores/sessionStore';
import { useNavigate } from 'react-router-dom';
import { User, Mail, Phone, Calendar, MapPin, Globe, Clock, Loader2, Shield, Trash2 } from 'lucide-react';
import { toast } from 'sonner';

export default function Profile() {
  const navigate = useNavigate();
  const { user, isAuthenticated, updateProfile, fetchCurrentUser } = useAuthStore();
  const { sessions, fetchSessions, revokeSession, revokeAll } = useSessionStore();
  const [form, setForm] = useState({ fullName: '', phone: '', dateOfBirth: '', birthTime: '', birthPlace: '', gender: 'MALE', preferredLanguage: 'en', timezone: '' });
  const [activeTab, setActiveTab] = useState('personal');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!isAuthenticated) { navigate('/login'); return; }
    fetchCurrentUser();
    fetchSessions();
  }, [isAuthenticated]);

  useEffect(() => {
    if (user) {
      setForm({
        fullName: user.fullName || '', phone: user.phone || '', dateOfBirth: user.dateOfBirth || '',
        birthTime: user.birthTime || '', birthPlace: user.birthPlace || '', gender: user.gender || 'MALE',
        preferredLanguage: user.preferredLanguage || 'en', timezone: user.timezone || ''
      });
    }
  }, [user]);

  const handleSave = async () => {
    setSaving(true);
    try {
      await updateProfile(form);
      toast.success('Profile updated');
    } catch (e) { toast.error('Failed to update'); }
    setSaving(false);
  };

  const tabs = [
    { id: 'personal', label: 'Personal Info', icon: User },
    { id: 'birth', label: 'Birth Details', icon: Calendar },
    { id: 'security', label: 'Security', icon: Shield },
    { id: 'sessions', label: 'Active Sessions', icon: Globe },
  ];

  if (!user) return null;

  return (
    <div className="min-h-screen">
      <div className="gradient-bg py-12 px-6 rounded-b-3xl">
        <div className="max-w-[1440px] mx-auto">
          <h1 className="text-3xl font-bold text-white mb-2">My Profile</h1>
          <p className="text-white/70">Manage your account settings</p>
        </div>
      </div>

      <div className="max-w-[1440px] mx-auto px-6 py-8">
        <div className="flex flex-col lg:flex-row gap-8">
          {/* Sidebar */}
          <div className="lg:w-64 flex-shrink-0">
            <div className="card p-4">
              <div className="text-center mb-6">
                <div className="w-20 h-20 rounded-full gradient-bg mx-auto flex items-center justify-center text-white text-3xl font-bold mb-3">
                  {user.fullName?.charAt(0)}
                </div>
                <h3 className="font-semibold text-text-primary">{user.fullName}</h3>
                <p className="text-text-muted text-sm">{user.email}</p>
              </div>
              <nav className="space-y-1">
                {tabs.map(tab => {
                  const Icon = tab.icon;
                  return (
                    <button key={tab.id} onClick={() => setActiveTab(tab.id)}
                      className={`w-full flex items-center gap-3 px-4 py-2.5 rounded-lg text-sm transition-all text-left ${activeTab === tab.id ? 'bg-brand-primary/10 text-brand-primary border-l-2 border-brand-primary' : 'text-text-secondary hover:bg-[#251850]'}`}>
                      <Icon className="w-4 h-4" />{tab.label}
                    </button>
                  );
                })}
              </nav>
            </div>
          </div>

          {/* Content */}
          <div className="flex-1">
            <div className="card">
              {activeTab === 'personal' && (
                <div className="space-y-4">
                  <h3 className="text-lg font-semibold text-text-primary mb-4">Personal Information</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Full Name</label>
                      <input type="text" value={form.fullName} onChange={e => setForm({...form, fullName: e.target.value})} className="input-field" />
                    </div>
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Phone</label>
                      <input type="tel" value={form.phone} onChange={e => setForm({...form, phone: e.target.value})} className="input-field" />
                    </div>
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Language</label>
                      <select value={form.preferredLanguage} onChange={e => setForm({...form, preferredLanguage: e.target.value})} className="input-field">
                        <option value="en">English</option>
                        <option value="hi">Hindi</option>
                        <option value="ta">Tamil</option>
                        <option value="te">Telugu</option>
                      </select>
                    </div>
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Timezone</label>
                      <input type="text" value={form.timezone} onChange={e => setForm({...form, timezone: e.target.value})} placeholder="Asia/Kolkata" className="input-field" />
                    </div>
                  </div>
                  <button onClick={handleSave} disabled={saving} className="btn-primary flex items-center gap-2">
                    {saving && <Loader2 className="w-4 h-4 animate-spin" />}Save Changes
                  </button>
                </div>
              )}

              {activeTab === 'birth' && (
                <div className="space-y-4">
                  <h3 className="text-lg font-semibold text-text-primary mb-4">Birth Details</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Date of Birth</label>
                      <input type="date" value={form.dateOfBirth} onChange={e => setForm({...form, dateOfBirth: e.target.value})} className="input-field" />
                    </div>
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Birth Time</label>
                      <input type="time" value={form.birthTime} onChange={e => setForm({...form, birthTime: e.target.value})} className="input-field" />
                    </div>
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Birth Place</label>
                      <input type="text" value={form.birthPlace} onChange={e => setForm({...form, birthPlace: e.target.value})} placeholder="City, Country" className="input-field" />
                    </div>
                    <div>
                      <label className="block text-sm text-text-secondary mb-1">Gender</label>
                      <select value={form.gender} onChange={e => setForm({...form, gender: e.target.value})} className="input-field">
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                        <option value="OTHER">Other</option>
                      </select>
                    </div>
                  </div>
                  <p className="text-text-muted text-xs">Used for Kundli and AI predictions.</p>
                  <button onClick={handleSave} disabled={saving} className="btn-primary flex items-center gap-2">
                    {saving && <Loader2 className="w-4 h-4 animate-spin" />}Save Changes
                  </button>
                </div>
              )}

              {activeTab === 'security' && (
                <div className="space-y-4">
                  <h3 className="text-lg font-semibold text-text-primary mb-4">Security Settings</h3>
                  <div className="p-4 bg-[#0F0A1E] rounded-xl">
                    <p className="text-text-secondary text-sm">Password management and 2FA settings will be available here.</p>
                  </div>
                </div>
              )}

              {activeTab === 'sessions' && (
                <div className="space-y-4">
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-lg font-semibold text-text-primary">Active Sessions</h3>
                    <button onClick={revokeAll} className="text-danger text-sm hover:underline flex items-center gap-1">
                      <Trash2 className="w-4 h-4" /> Logout All Devices
                    </button>
                  </div>
                  {sessions.length === 0 ? (
                    <p className="text-text-muted">No active sessions</p>
                  ) : (
                    sessions.map(s => (
                      <div key={s.id} className="flex items-center justify-between p-3 bg-[#0F0A1E] rounded-xl">
                        <div>
                          <p className="text-sm font-medium text-text-primary">{s.deviceType} - {s.browser}</p>
                          <p className="text-xs text-text-muted">{s.operatingSystem} | {s.ipAddress} | {s.city}</p>
                        </div>
                        <button onClick={() => revokeSession(s.sessionId)} className="text-danger text-sm hover:underline">Revoke</button>
                      </div>
                    ))
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
