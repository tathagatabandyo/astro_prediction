import { useState } from 'react';
import { useAIStore } from '../stores/aiStore';
import { useAuthStore } from '../stores/authStore';
import { useNavigate } from 'react-router-dom';
import { Star, Sun, Hand, Heart, Briefcase, Shield, Hash, Calendar, Sparkles, Loader2, X } from 'lucide-react';
import { toast } from 'sonner';

const features = [
  { id: 'kundli', title: 'Kundli Analysis', icon: Star, desc: 'Detailed birth chart analysis' },
  { id: 'horoscope', title: 'Daily Horoscope', icon: Sun, desc: "Today's astrological forecast" },
  { id: 'palm', title: 'Palm Reading', icon: Hand, desc: 'AI-powered palm analysis' },
  { id: 'compatibility', title: 'Compatibility', icon: Heart, desc: 'Relationship matching' },
  { id: 'career', title: 'Career Guidance', icon: Briefcase, desc: 'Professional astrology advice' },
  { id: 'remedies', title: 'Remedies', icon: Shield, desc: 'Personalized solutions' },
  { id: 'lucky', title: 'Lucky Numbers', icon: Hash, desc: 'Your fortune numbers' },
  { id: 'yearly', title: 'Yearly Forecast', icon: Calendar, desc: 'Annual predictions' },
];

export default function AIInsights() {
  const navigate = useNavigate();
  const { user } = useAuthStore();
  const { currentReading, isLoading, generateKundli, analyzeCompatibility, getCareerGuidance, getRemedies, getDailyInsights } = useAIStore();
  const [activeFeature, setActiveFeature] = useState(null);
  const [formData, setFormData] = useState({ dateOfBirth: '', birthTime: '', birthPlace: '', gender: 'MALE' });
  const [compatData, setCompatData] = useState({ p1Name: '', p1Dob: '', p1Time: '', p1Place: '', p2Name: '', p2Dob: '', p2Time: '', p2Place: '' });

  const handleSubmit = async () => {
    try {
      if (activeFeature === 'kundli') await generateKundli(formData);
      else if (activeFeature === 'compatibility') await analyzeCompatibility(compatData);
      else if (activeFeature === 'career') await getCareerGuidance(formData);
      else if (activeFeature === 'remedies') await getRemedies(formData);
      else if (activeFeature === 'horoscope') await getDailyInsights(user?.id);
    } catch (e) { toast.error(e.message); }
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-xl font-semibold text-text-primary mb-4">Login to access AI Insights</h2>
          <button onClick={() => navigate('/login')} className="btn-primary">Sign In</button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen">
      <div className="gradient-bg py-12 px-6 rounded-b-3xl">
        <div className="max-w-[1440px] mx-auto">
          <h1 className="text-3xl font-bold text-white mb-2">AI Astrology Insights</h1>
          <p className="text-white/70">Powered by AI for personalized astrological guidance</p>
        </div>
      </div>

      <div className="max-w-[1440px] mx-auto px-6 py-8">
        {/* Feature Grid */}
        {!activeFeature && !currentReading && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {features.map(f => {
              const Icon = f.icon;
              return (
                <button key={f.id} onClick={() => setActiveFeature(f.id)}
                  className="card text-left hover:-translate-y-1 hover:shadow-brand-primary/10 group">
                  <div className="w-12 h-12 rounded-xl bg-brand-secondary/10 flex items-center justify-center mb-3 group-hover:bg-brand-secondary/20 transition-colors">
                    <Icon className="w-6 h-6 text-brand-secondary" />
                  </div>
                  <h3 className="font-semibold text-text-primary mb-1">{f.title}</h3>
                  <p className="text-text-secondary text-sm">{f.desc}</p>
                  <span className="text-brand-primary text-sm mt-3 inline-block">Get Insights &rarr;</span>
                </button>
              );
            })}
          </div>
        )}

        {/* Forms */}
        {activeFeature && !currentReading && (
          <div className="max-w-lg mx-auto card">
            <div className="flex items-center justify-between mb-6">
              <h3 className="text-lg font-semibold text-text-primary capitalize">{activeFeature.replace('-', ' ')}</h3>
              <button onClick={() => setActiveFeature(null)} className="p-1 hover:bg-[#251850] rounded-lg"><X className="w-5 h-5 text-text-muted" /></button>
            </div>

            {(activeFeature === 'kundli' || activeFeature === 'career' || activeFeature === 'remedies') && (
              <div className="space-y-4">
                <div>
                  <label className="block text-sm text-text-secondary mb-1">Date of Birth</label>
                  <input type="date" value={formData.dateOfBirth} onChange={e => setFormData({...formData, dateOfBirth: e.target.value})} className="input-field" />
                </div>
                <div>
                  <label className="block text-sm text-text-secondary mb-1">Birth Time</label>
                  <input type="time" value={formData.birthTime} onChange={e => setFormData({...formData, birthTime: e.target.value})} className="input-field" />
                </div>
                <div>
                  <label className="block text-sm text-text-secondary mb-1">Birth Place</label>
                  <input type="text" value={formData.birthPlace} onChange={e => setFormData({...formData, birthPlace: e.target.value})} placeholder="City, Country" className="input-field" />
                </div>
                <div>
                  <label className="block text-sm text-text-secondary mb-1">Gender</label>
                  <select value={formData.gender} onChange={e => setFormData({...formData, gender: e.target.value})} className="input-field">
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>
              </div>
            )}

            {activeFeature === 'compatibility' && (
              <div className="space-y-4">
                <h4 className="font-medium text-text-primary">Person 1</h4>
                <input type="text" value={compatData.p1Name} onChange={e => setCompatData({...compatData, p1Name: e.target.value})} placeholder="Name" className="input-field" />
                <input type="date" value={compatData.p1Dob} onChange={e => setCompatData({...compatData, p1Dob: e.target.value})} className="input-field" />
                <input type="time" value={compatData.p1Time} onChange={e => setCompatData({...compatData, p1Time: e.target.value})} className="input-field" />
                <input type="text" value={compatData.p1Place} onChange={e => setCompatData({...compatData, p1Place: e.target.value})} placeholder="Birth Place" className="input-field" />
                <h4 className="font-medium text-text-primary pt-2">Person 2</h4>
                <input type="text" value={compatData.p2Name} onChange={e => setCompatData({...compatData, p2Name: e.target.value})} placeholder="Name" className="input-field" />
                <input type="date" value={compatData.p2Dob} onChange={e => setCompatData({...compatData, p2Dob: e.target.value})} className="input-field" />
                <input type="time" value={compatData.p2Time} onChange={e => setCompatData({...compatData, p2Time: e.target.value})} className="input-field" />
                <input type="text" value={compatData.p2Place} onChange={e => setCompatData({...compatData, p2Place: e.target.value})} placeholder="Birth Place" className="input-field" />
              </div>
            )}

            {activeFeature === 'horoscope' && (
              <p className="text-text-secondary">Get your daily personalized astrological insights.</p>
            )}

            <button onClick={handleSubmit} disabled={isLoading}
              className="btn-primary w-full mt-6 flex items-center justify-center gap-2">
              {isLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <Sparkles className="w-4 h-4" />}
              Generate
            </button>
          </div>
        )}

        {/* Result */}
        {currentReading && (
          <div className="max-w-2xl mx-auto card border-l-4 border-brand-secondary">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-brand-secondary" />
                <h3 className="font-semibold text-text-primary">{currentReading.readingType}</h3>
              </div>
              <button onClick={() => { setActiveFeature(null); }} className="p-1 hover:bg-[#251850] rounded-lg"><X className="w-5 h-5 text-text-muted" /></button>
            </div>
            <p className="text-text-secondary mb-4">{currentReading.resultSummary}</p>
            <div className="bg-[#0F0A1E] rounded-xl p-4 mb-4">
              <p className="text-text-primary text-sm whitespace-pre-wrap">{currentReading.resultDetail}</p>
            </div>
            <div className="flex flex-wrap gap-4 mb-4">
              {currentReading.confidenceScore && (
                <div className="badge bg-brand-primary/10 text-brand-primary">
                  Confidence: {Math.round(currentReading.confidenceScore * 100)}%
                </div>
              )}
              {currentReading.luckyNumbers && (
                <div className="badge bg-success/10 text-success">
                  Lucky: {currentReading.luckyNumbers}
                </div>
              )}
              {currentReading.luckyColors && (
                <div className="badge bg-warning/10 text-warning">
                  Colors: {currentReading.luckyColors}
                </div>
              )}
            </div>
            {currentReading.remedies && (
              <div className="mb-4">
                <h4 className="text-sm font-medium text-text-primary mb-2">Remedies:</h4>
                <ul className="space-y-1">
                  {currentReading.remedies.map((r, i) => (
                    <li key={i} className="text-sm text-text-secondary flex items-center gap-2">
                      <span className="w-1.5 h-1.5 rounded-full bg-brand-primary flex-shrink-0" />{r}
                    </li>
                  ))}
                </ul>
              </div>
            )}
            <p className="text-text-muted text-xs italic border-l-2 border-warning/30 pl-3">{currentReading.disclaimer}</p>
          </div>
        )}
      </div>
    </div>
  );
}
