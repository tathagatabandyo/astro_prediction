import { Link } from 'react-router-dom';
import { Star, Sparkles, Moon } from 'lucide-react';

export default function AuthLayout({ children, title, subtitle }) {
  return (
    <div className="min-h-screen bg-[#0F0A1E] flex">
      <div className="hidden lg:flex lg:w-1/2 gradient-bg items-center justify-center relative overflow-hidden">
        <div className="absolute inset-0 opacity-10">
          {[...Array(20)].map((_, i) => (
            <div key={i} className="absolute rounded-full bg-white"
              style={{ width: `${Math.random()*4+2}px`, height: `${Math.random()*4+2}px`,
                left: `${Math.random()*100}%`, top: `${Math.random()*100}%`, opacity: Math.random()*0.5+0.2 }} />
          ))}
        </div>
        <div className="relative z-10 text-center text-white px-12">
          <div className="flex items-center justify-center gap-3 mb-8">
            <Star className="w-12 h-12" /><Moon className="w-10 h-10 opacity-80" /><Sparkles className="w-8 h-8 opacity-60" />
          </div>
          <h2 className="text-3xl font-bold mb-4">Unlock Your Cosmic Destiny</h2>
          <p className="text-white/70 text-lg max-w-md mx-auto">Connect with expert astrologers, get AI-powered insights, and discover what the stars hold for you.</p>
          <div className="mt-12 flex items-center justify-center gap-8 text-white/60 text-sm">
            <div><span className="text-white font-bold text-xl">500+</span><br />Astrologers</div>
            <div><span className="text-white font-bold text-xl">1M+</span><br />Consultations</div>
            <div><span className="text-white font-bold text-xl">4.8</span><br />Rating</div>
          </div>
        </div>
      </div>
      <div className="w-full lg:w-1/2 flex items-center justify-center p-6">
        <div className="w-full max-w-md">
          <Link to="/" className="flex items-center gap-2 justify-center mb-8 lg:hidden">
            <Star className="w-6 h-6 text-brand-primary" />
            <span className="text-xl font-bold gradient-text">AstroPrediction</span>
          </Link>
          <div className="card">
            <h2 className="text-2xl font-bold text-text-primary text-center mb-1">{title}</h2>
            {subtitle && <p className="text-text-secondary text-center text-sm mb-6">{subtitle}</p>}
            {children}
          </div>
        </div>
      </div>
    </div>
  );
}
