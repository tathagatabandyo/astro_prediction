import { useState, useEffect } from 'react';
import { apiClient } from '../api/apiClient';
import { Sun, Hash, Palette, Heart, Smile, Loader2 } from 'lucide-react';

const zodiacSigns = [
  { name: 'Aries', symbol: '♈', dates: 'Mar 21 - Apr 19' },
  { name: 'Taurus', symbol: '♉', dates: 'Apr 20 - May 20' },
  { name: 'Gemini', symbol: '♊', dates: 'May 21 - Jun 20' },
  { name: 'Cancer', symbol: '♋', dates: 'Jun 21 - Jul 22' },
  { name: 'Leo', symbol: '♌', dates: 'Jul 23 - Aug 22' },
  { name: 'Virgo', symbol: '♍', dates: 'Aug 23 - Sep 22' },
  { name: 'Libra', symbol: '♎', dates: 'Sep 23 - Oct 22' },
  { name: 'Scorpio', symbol: '♏', dates: 'Oct 23 - Nov 21' },
  { name: 'Sagittarius', symbol: '♐', dates: 'Nov 22 - Dec 21' },
  { name: 'Capricorn', symbol: '♑', dates: 'Dec 22 - Jan 19' },
  { name: 'Aquarius', symbol: '♒', dates: 'Jan 20 - Feb 18' },
  { name: 'Pisces', symbol: '♓', dates: 'Feb 19 - Mar 20' },
];

const periods = ['DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'];

export default function Horoscope() {
  const [selectedSign, setSelectedSign] = useState('Aries');
  const [period, setPeriod] = useState('DAILY');
  const [horoscope, setHoroscope] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchHoroscope();
  }, [selectedSign, period]);

  const fetchHoroscope = async () => {
    setLoading(true);
    try {
      const response = await apiClient.get(`/horoscope/public/${selectedSign}?period=${period}`);
      setHoroscope(response.data);
    } catch (e) {
      console.error(e);
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen">
      <div className="gradient-bg py-12 px-6 rounded-b-3xl">
        <div className="max-w-[1440px] mx-auto">
          <h1 className="text-3xl font-bold text-white mb-2">Daily Horoscope</h1>
          <p className="text-white/70">Discover what the stars have in store for you today</p>
        </div>
      </div>

      <div className="max-w-[1440px] mx-auto px-6 py-8">
        {/* Sign Selector */}
        <div className="flex overflow-x-auto gap-3 pb-4 mb-6 scrollbar-hide">
          {zodiacSigns.map(sign => (
            <button
              key={sign.name}
              onClick={() => setSelectedSign(sign.name)}
              className={`flex-shrink-0 flex flex-col items-center gap-1 p-3 rounded-xl min-w-[90px] transition-all ${selectedSign === sign.name ? 'bg-brand-primary/10 border-2 border-brand-primary' : 'bg-[#1A103C] border-2 border-transparent hover:border-[rgba(255,255,255,0.1)]'}`}>
              <span className="text-2xl">{sign.symbol}</span>
              <span className={`text-xs font-medium ${selectedSign === sign.name ? 'text-brand-primary' : 'text-text-secondary'}`}>{sign.name}</span>
            </button>
          ))}
        </div>

        {/* Period Tabs */}
        <div className="flex gap-2 mb-6">
          {periods.map(p => (
            <button key={p} onClick={() => setPeriod(p)}
              className={`px-4 py-2 rounded-lg text-sm font-medium transition-all ${period === p ? 'bg-brand-primary text-white' : 'bg-[#1A103C] text-text-secondary hover:text-text-primary'}`}>
              {p.charAt(0) + p.slice(1).toLowerCase()}
            </button>
          ))}
        </div>

        {/* Horoscope Display */}
        {loading ? (
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-8 h-8 text-brand-primary animate-spin" />
          </div>
        ) : horoscope ? (
          <div className="card max-w-2xl mx-auto">
            <div className="text-center mb-6">
              <span className="text-4xl">
                {zodiacSigns.find(s => s.name === selectedSign)?.symbol}
              </span>
              <h2 className="text-2xl font-bold text-text-primary mt-2">{selectedSign}</h2>
              <p className="text-text-muted text-sm">{zodiacSigns.find(s => s.name === selectedSign)?.dates}</p>
              <p className="text-text-muted text-xs mt-1">{new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}</p>
            </div>
            <p className="text-text-primary text-lg leading-relaxed mb-6">{horoscope.content}</p>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div className="bg-[#0F0A1E] rounded-xl p-4 text-center">
                <Hash className="w-5 h-5 text-brand-primary mx-auto mb-2" />
                <p className="text-xs text-text-muted mb-1">Lucky Numbers</p>
                <p className="text-sm font-medium text-text-primary">{horoscope.luckyNumbers}</p>
              </div>
              <div className="bg-[#0F0A1E] rounded-xl p-4 text-center">
                <Palette className="w-5 h-5 text-brand-secondary mx-auto mb-2" />
                <p className="text-xs text-text-muted mb-1">Lucky Colors</p>
                <p className="text-sm font-medium text-text-primary">{horoscope.luckyColors}</p>
              </div>
              <div className="bg-[#0F0A1E] rounded-xl p-4 text-center">
                <Heart className="w-5 h-5 text-danger mx-auto mb-2" />
                <p className="text-xs text-text-muted mb-1">Compatibility</p>
                <p className="text-sm font-medium text-text-primary">{horoscope.compatibility}</p>
              </div>
              <div className="bg-[#0F0A1E] rounded-xl p-4 text-center">
                <Smile className="w-5 h-5 text-success mx-auto mb-2" />
                <p className="text-xs text-text-muted mb-1">Mood</p>
                <p className="text-sm font-medium text-text-primary">{horoscope.mood}</p>
              </div>
            </div>
          </div>
        ) : null}
      </div>
    </div>
  );
}
