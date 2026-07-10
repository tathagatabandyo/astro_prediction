import { Link, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { useAstrologerStore } from '../stores/astrologerStore';
import { useEffect } from 'react';
import {
  MessageCircle, Sparkles, Hand, Sun, Heart, Briefcase,
  ArrowRight, Star, Users, CheckCircle
} from 'lucide-react';

const features = [
  { icon: MessageCircle, title: 'Live Chat Consultation', desc: 'Connect with astrologers in real-time via our secure chat platform.' },
  { icon: Sparkles, title: 'AI-Powered Kundli', desc: 'Get detailed birth chart analysis powered by advanced AI technology.' },
  { icon: Hand, title: 'Palm Reading', desc: 'Upload your palm image for AI-powered palmistry analysis.' },
  { icon: Sun, title: 'Daily Horoscope', desc: 'Read your daily, weekly, and monthly horoscope predictions.' },
  { icon: Heart, title: 'Compatibility', desc: 'Check relationship compatibility with your partner.' },
  { icon: Briefcase, title: 'Career Guidance', desc: 'Get astrological career advice based on your birth chart.' },
];

export default function Home() {
  const { isAuthenticated } = useAuthStore();
  const { featured, fetchFeatured } = useAstrologerStore();
  const navigate = useNavigate();

  useEffect(() => { fetchFeatured(); }, []);

  return (
    <div>
      {/* Hero */}
      <section className="relative min-h-[calc(100vh-64px)] flex items-center justify-center overflow-hidden">
        <div className="absolute inset-0 opacity-10">
          {[...Array(40)].map((_, i) => (
            <div key={i} className="absolute rounded-full bg-white animate-float"
              style={{ width: `${Math.random()*3+1}px`, height: `${Math.random()*3+1}px`,
                left: `${Math.random()*100}%`, top: `${Math.random()*100}%`,
                animationDelay: `${Math.random()*5}s`, animationDuration: `${3+Math.random()*4}s` }} />
          ))}
        </div>
        <div className="relative z-10 text-center px-6 max-w-3xl mx-auto">
          <h1 className="text-4xl md:text-5xl lg:text-6xl font-extrabold mb-6 leading-tight">
            <span className="gradient-text">Unlock Your</span><br />
            <span className="text-text-primary">Cosmic Destiny</span>
          </h1>
          <p className="text-text-secondary text-lg md:text-xl mb-8 max-w-xl mx-auto">
            Connect with expert astrologers, get AI-powered insights, and discover what the stars hold for you.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <button onClick={() => navigate('/astrologers')} className="btn-primary text-lg px-8 py-4 flex items-center justify-center gap-2">
              Consult Now <ArrowRight className="w-5 h-5" />
            </button>
            <button onClick={() => navigate('/horoscope')} className="btn-outline text-lg px-8 py-4">
              Get Free Horoscope
            </button>
          </div>
          <div className="mt-12 flex items-center justify-center gap-8 text-text-muted text-sm">
            <div className="flex items-center gap-2"><Users className="w-4 h-4" />500+ Expert Astrologers</div>
            <div className="flex items-center gap-2"><CheckCircle className="w-4 h-4" />1M+ Consultations</div>
            <div className="flex items-center gap-2"><Star className="w-4 h-4" />4.8 Average Rating</div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="py-20 px-6">
        <div className="max-w-[1440px] mx-auto">
          <h2 className="text-3xl font-bold text-text-primary text-center mb-12">Our Services</h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {features.map((f, i) => {
              const Icon = f.icon;
              return (
                <div key={i} className="card hover:shadow-lg hover:shadow-brand-primary/10 group">
                  <div className="w-16 h-16 rounded-2xl bg-brand-primary/10 flex items-center justify-center mb-4 group-hover:bg-brand-primary/20 transition-colors">
                    <Icon className="w-8 h-8 text-brand-primary" />
                  </div>
                  <h3 className="text-lg font-semibold text-text-primary mb-2">{f.title}</h3>
                  <p className="text-text-secondary text-sm">{f.desc}</p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* Featured Astrologers */}
      <section className="py-20 px-6">
        <div className="max-w-[1440px] mx-auto">
          <div className="flex items-center justify-between mb-8">
            <h2 className="text-3xl font-bold text-text-primary">Top Astrologers</h2>
            <Link to="/astrologers" className="text-brand-primary hover:underline text-sm font-medium flex items-center gap-1">
              View All <ArrowRight className="w-4 h-4" />
            </Link>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {featured.map(a => (
              <div key={a.id} className="card text-center group cursor-pointer" onClick={() => navigate(`/astrologers/${a.id}`)}>
                <div className="relative mx-auto mb-4">
                  <div className="w-20 h-20 rounded-full gradient-bg mx-auto flex items-center justify-center text-white text-2xl font-bold">
                    {a.fullName?.charAt(0)}
                  </div>
                  {a.isOnline && (
                    <div className="absolute bottom-1 right-1/3 w-4 h-4 bg-success rounded-full border-2 border-[#1A103C]" />
                  )}
                </div>
                <h4 className="font-semibold text-text-primary mb-1">{a.fullName}</h4>
                <div className="flex flex-wrap gap-1 justify-center mb-2">
                  {a.expertise?.slice(0, 2).map((e, i) => (
                    <span key={i} className="badge bg-brand-primary/10 text-brand-primary text-xs">{e}</span>
                  ))}
                </div>
                <div className="flex items-center justify-center gap-1 mb-3">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} className={`w-4 h-4 ${i < Math.round(a.averageRating || 0) ? 'text-star-gold fill-star-gold' : 'text-text-muted'}`} />
                  ))}
                  <span className="text-xs text-text-muted ml-1">{a.averageRating || 0}</span>
                </div>
                <p className="text-brand-primary font-semibold text-sm">Rs. {a.pricingPerMinute}/min</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section className="py-20 gradient-bg">
        <div className="max-w-[1440px] mx-auto px-6">
          <h2 className="text-3xl font-bold text-white text-center mb-12">How It Works</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
            {[
              { step: '01', title: 'Recharge Wallet', desc: 'Add funds using secure Stripe payments' },
              { step: '02', title: 'Choose Astrologer', desc: 'Browse and select your preferred expert' },
              { step: '03', title: 'Start Chat', desc: 'Begin real-time consultation' },
              { step: '04', title: 'Get Insights', desc: 'Receive personalized astrological guidance' },
            ].map((s, i) => (
              <div key={i} className="text-center">
                <div className="text-white/40 text-sm font-medium mb-2">Step {s.step}</div>
                <h4 className="text-white font-semibold text-lg mb-2">{s.title}</h4>
                <p className="text-white/60 text-sm">{s.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </div>
  );
}
