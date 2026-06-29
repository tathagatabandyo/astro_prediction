import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import AuthLayout from '../layouts/AuthLayout';
import { User, Mail, Lock, Eye, EyeOff, Loader2, CheckCircle } from 'lucide-react';
import { toast } from 'sonner';

export default function Register() {
  const { register } = useAuthStore();
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!fullName || !email || !password || !confirmPassword) { toast.error('Please fill all fields'); return; }
    if (password !== confirmPassword) { toast.error('Passwords do not match'); return; }
    const pwRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!pwRegex.test(password)) { toast.error('Password must be 8+ chars with uppercase, lowercase, number, and special char'); return; }
    setLoading(true);
    const result = await register(fullName, email, password, confirmPassword);
    setLoading(false);
    if (result.success) { setSuccess(true); toast.success('Registration successful'); }
    else { toast.error(result.error || 'Registration failed'); }
  };

  if (success) {
    return (
      <AuthLayout title="Check Your Email" subtitle="">
        <div className="text-center py-8">
          <CheckCircle className="w-16 h-16 text-success mx-auto mb-4" />
          <p className="text-text-secondary">We&apos;ve sent a verification link to your email. Click the link to activate your account.</p>
          <Link to="/login" className="btn-primary mt-6 inline-block">Back to Login</Link>
        </div>
      </AuthLayout>
    );
  }

  return (
    <AuthLayout title="Create Account" subtitle="Join AstroPrediction today">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-text-secondary mb-1.5">Full Name</label>
          <div className="relative">
            <User className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-text-muted" />
            <input type="text" value={fullName} onChange={e => setFullName(e.target.value)}
              placeholder="Your full name" className="input-field pl-10" required />
          </div>
        </div>
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
              onChange={e => setPassword(e.target.value)} placeholder="Create a password"
              className="input-field pl-10 pr-10" required />
            <button type="button" onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-text-muted hover:text-text-secondary">
              {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
            </button>
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-text-secondary mb-1.5">Confirm Password</label>
          <div className="relative">
            <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-text-muted" />
            <input type="password" value={confirmPassword}
              onChange={e => setConfirmPassword(e.target.value)} placeholder="Confirm your password"
              className="input-field pl-10" required />
          </div>
        </div>
        <button type="submit" disabled={loading} className="btn-primary w-full flex items-center justify-center gap-2">
          {loading && <Loader2 className="w-4 h-4 animate-spin" />} Create Account
        </button>
        <p className="text-center text-text-secondary text-sm">
          Already have an account? <Link to="/login" className="text-brand-primary hover:underline font-medium">Sign In</Link>
        </p>
      </form>
    </AuthLayout>
  );
}
