import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import AuthLayout from '../layouts/AuthLayout';
import { Mail, Lock, Eye, EyeOff, Loader2 } from 'lucide-react';
import { toast } from 'sonner';

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuthStore();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email || !password) { toast.error('Please fill all fields'); return; }
    setLoading(true);
    const result = await login(email, password);
    setLoading(false);
    if (result.success) { toast.success('Login successful'); navigate('/'); }
    else { toast.error(result.error || 'Login failed'); }
  };

  return (
    <AuthLayout title="Welcome Back" subtitle="Sign in to your account to continue">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-text-secondary mb-1.5">Email</label>
          <div className="relative">
            <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-text-muted" />
            <input type="email" value={email} onChange={e => setEmail(e.target.value)}
              placeholder="Enter your email" className="input-field pl-10" required />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-text-secondary mb-1.5">Password</label>
          <div className="relative">
            <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-text-muted" />
            <input type={showPassword ? 'text' : 'password'} value={password}
              onChange={e => setPassword(e.target.value)} placeholder="Enter your password"
              className="input-field pl-10 pr-10" required />
            <button type="button" onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-text-muted hover:text-text-secondary">
              {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
            </button>
          </div>
        </div>
        <div className="flex items-center justify-between text-sm">
          <label className="flex items-center gap-2 text-text-secondary cursor-pointer">
            <input type="checkbox" className="rounded border-[rgba(255,255,255,0.2)] bg-[#251850]" />
            Remember me
          </label>
          <Link to="/forgot-password" className="text-brand-primary hover:underline">Forgot Password?</Link>
        </div>
        <button type="submit" disabled={loading} className="btn-primary w-full flex items-center justify-center gap-2">
          {loading && <Loader2 className="w-4 h-4 animate-spin" />} Sign In
        </button>
        <p className="text-center text-text-secondary text-sm">
          Don&apos;t have an account? <Link to="/register" className="text-brand-primary hover:underline font-medium">Sign Up</Link>
        </p>
      </form>
    </AuthLayout>
  );
}
