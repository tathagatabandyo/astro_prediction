import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAstrologerStore } from '../stores/astrologerStore';
import { Search, Star, MessageCircle, Heart, Filter, Loader2 } from 'lucide-react';

const languages = ['Hindi', 'English', 'Tamil', 'Telugu', 'Marathi', 'Gujarati', 'Bengali'];
const expertiseList = ['Vedic', 'Numerology', 'Tarot', 'Palmistry', 'Vastu', 'Kundli', 'Relationship', 'Career'];

export default function Astrologers() {
  const navigate = useNavigate();
  const { astrologers, searchAstrologers, isLoading, filters } = useAstrologerStore();
  const [search, setSearch] = useState('');
  const [selLang, setSelLang] = useState('');
  const [selExpertise, setSelExpertise] = useState('');
  const [selRating, setSelRating] = useState('');
  const [showFilters, setShowFilters] = useState(false);

  useEffect(() => {
    searchAstrologers({});
  }, []);

  const handleSearch = () => {
    searchAstrologers({ search, languages: selLang, expertise: selExpertise, minRating: selRating || null, page: 0 });
  };

  return (
    <div className="min-h-screen">
      {/* Hero */}
      <div className="gradient-bg py-12 px-6 rounded-b-3xl">
        <div className="max-w-[1440px] mx-auto">
          <h1 className="text-3xl md:text-4xl font-bold text-white mb-2">Find Your Astrologer</h1>
          <p className="text-white/70">Connect with certified astrologers for personalized guidance</p>
        </div>
      </div>

      {/* Filter Bar */}
      <div className="sticky top-16 z-30 bg-[#1A103C]/95 backdrop-blur-md border-b border-[rgba(255,255,255,0.08)] px-6 py-3">
        <div className="max-w-[1440px] mx-auto flex flex-wrap items-center gap-3">
          <div className="relative flex-1 min-w-[200px] max-w-sm">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-text-muted" />
            <input type="text" value={search} onChange={e => setSearch(e.target.value)}
              onKeyPress={e => e.key === 'Enter' && handleSearch()}
              placeholder="Search by name or expertise..." className="input-field pl-10 text-sm" />
          </div>
          <button onClick={() => setShowFilters(!showFilters)}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-[#251850] text-text-secondary hover:text-text-primary text-sm transition-colors">
            <Filter className="w-4 h-4" /> Filters
          </button>
          <button onClick={handleSearch} className="btn-primary text-sm py-2 px-4">Search</button>
        </div>
        {showFilters && (
          <div className="max-w-[1440px] mx-auto mt-3 flex flex-wrap gap-3 animate-fade-in">
            <select value={selLang} onChange={e => setSelLang(e.target.value)} className="input-field text-sm py-2 w-40">
              <option value="">All Languages</option>
              {languages.map(l => <option key={l} value={l}>{l}</option>)}
            </select>
            <select value={selExpertise} onChange={e => setSelExpertise(e.target.value)} className="input-field text-sm py-2 w-40">
              <option value="">All Expertise</option>
              {expertiseList.map(e => <option key={e} value={e}>{e}</option>)}
            </select>
            <select value={selRating} onChange={e => setSelRating(e.target.value)} className="input-field text-sm py-2 w-36">
              <option value="">Any Rating</option>
              <option value="4">4+ Stars</option>
              <option value="3">3+ Stars</option>
            </select>
          </div>
        )}
      </div>

      {/* Astrologer Grid */}
      <div className="max-w-[1440px] mx-auto px-6 py-8">
        {isLoading ? (
          <div className="flex items-center justify-center py-20">
            <Loader2 className="w-8 h-8 text-brand-primary animate-spin" />
          </div>
        ) : astrologers.length === 0 ? (
          <div className="text-center py-20">
            <Search className="w-16 h-16 text-text-muted mx-auto mb-4" />
            <h3 className="text-xl font-semibold text-text-primary mb-2">No astrologers found</h3>
            <p className="text-text-secondary">Try adjusting your filters</p>
            <button onClick={() => { setSearch(''); setSelLang(''); setSelExpertise(''); setSelRating(''); handleSearch(); }}
              className="btn-outline mt-4">Clear Filters</button>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {astrologers.map(a => (
              <div key={a.id} className="card group hover:-translate-y-1 transition-all overflow-hidden">
                <div className="h-24 gradient-bg relative -mx-6 -mt-6 mb-0" />
                <div className="relative text-center -mt-10">
                  <div className="w-20 h-20 rounded-full gradient-bg mx-auto flex items-center justify-center text-white text-2xl font-bold border-4 border-[#1A103C] relative">
                    {a.fullName?.charAt(0)}
                    {a.isOnline && <div className="absolute bottom-0 right-0 w-4 h-4 bg-success rounded-full border-2 border-[#1A103C]" />}
                  </div>
                  <h4 className="font-semibold text-text-primary mt-2">{a.fullName}</h4>
                  {a.isVerified && <span className="badge bg-info/10 text-info text-xs mt-1">Verified</span>}
                </div>
                <div className="flex flex-wrap gap-1 justify-center mt-3">
                  {a.expertise?.slice(0, 3).map((e, i) => (
                    <span key={i} className="badge bg-[#251850] text-text-secondary text-xs">{e}</span>
                  ))}
                </div>
                <div className="flex items-center justify-center gap-1 mt-3">
                  {[...Array(5)].map((_, i) => (
                    <Star key={i} className={`w-4 h-4 ${i < Math.round(a.averageRating || 0) ? 'text-star-gold fill-star-gold' : 'text-text-muted'}`} />
                  ))}
                  <span className="text-xs text-text-muted ml-1">({a.totalReviews || 0})</span>
                </div>
                <div className="flex items-center justify-between mt-4">
                  <span className="text-brand-primary font-bold">Rs. {a.pricingPerMinute}/min</span>
                  <span className="text-text-muted text-xs">{a.experienceYears} yrs exp</span>
                </div>
                <div className="flex gap-2 mt-4">
                  <button onClick={() => navigate(`/chat?astrologer=${a.userId || a.id}`)}
                    className="btn-primary flex-1 text-sm py-2 flex items-center justify-center gap-1">
                    <MessageCircle className="w-4 h-4" /> Chat
                  </button>
                  <button className="btn-outline p-2"><Heart className="w-4 h-4" /></button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
