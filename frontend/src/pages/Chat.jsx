import { useEffect, useState, useRef } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { useChatStore } from '../stores/chatStore';
import { useAstrologerStore } from '../stores/astrologerStore';
import { Send, ArrowLeft, Phone, MoreVertical, Loader2, User } from 'lucide-react';
import { toast } from 'sonner';

export default function Chat() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { user } = useAuthStore();
  const { sessions, currentSession, messages, fetchSessions, startChat, endChat, fetchMessages, sendMessage, addMessage } = useChatStore();
  const { getById } = useAstrologerStore();
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);
  const astrologerId = searchParams.get('astrologer');

  useEffect(() => {
    if (user) fetchSessions();
  }, [user]);

  useEffect(() => {
    if (astrologerId && !currentSession) {
      handleStartChat(astrologerId);
    }
  }, [astrologerId]);

  useEffect(() => {
    if (currentSession) {
      fetchMessages(currentSession.id);
    }
  }, [currentSession?.id]);

  const handleStartChat = async (id) => {
    try {
      setLoading(true);
      await startChat(id);
      setLoading(false);
    } catch (e) {
      setLoading(false);
      toast.error(e.message || 'Failed to start chat');
    }
  };

  const handleSend = async (e) => {
    e.preventDefault();
    if (!input.trim() || !currentSession) return;
    const content = input.trim();
    setInput('');
    try {
      await sendMessage(currentSession.id, content);
    } catch (err) {
      toast.error('Failed to send message');
    }
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-xl font-semibold text-text-primary mb-4">Please login to access chat</h2>
          <button onClick={() => navigate('/login')} className="btn-primary">Sign In</button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-[calc(100vh-64px)] flex">
      {/* Sidebar - Chat List */}
      <div className={`w-72 bg-[#1A103C] border-r border-[rgba(255,255,255,0.08)] flex-shrink-0 ${currentSession ? 'hidden md:flex' : 'flex'} flex-col`}>
        <div className="p-4 border-b border-[rgba(255,255,255,0.08)]">
          <h2 className="text-lg font-semibold text-text-primary">Messages</h2>
        </div>
        <div className="flex-1 overflow-y-auto">
          {sessions.length === 0 ? (
            <div className="p-4 text-center text-text-muted text-sm">No conversations yet</div>
          ) : (
            sessions.map(s => (
              <button
                key={s.id}
                onClick={() => handleStartChat(s.astrologerId)}
                className={`w-full flex items-center gap-3 p-3 text-left hover:bg-[#251850] transition-colors ${currentSession?.id === s.id ? 'bg-[#251850] border-l-2 border-brand-primary' : ''}`}>
                <div className="w-10 h-10 rounded-full gradient-bg flex items-center justify-center text-white text-sm font-bold flex-shrink-0">
                  {s.astrologerName?.charAt(0)}
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium text-text-primary truncate">{s.astrologerName}</p>
                  <p className="text-xs text-text-muted truncate">{s.lastMessage || 'No messages'}</p>
                </div>
                {s.unreadCount > 0 && (
                  <span className="w-5 h-5 bg-brand-primary text-white text-xs rounded-full flex items-center justify-center flex-shrink-0">{s.unreadCount}</span>
                )}
              </button>
            ))
          )}
        </div>
      </div>

      {/* Chat Area */}
      <div className="flex-1 flex flex-col bg-[#0F0A1E]">
        {currentSession ? (
          <>
            {/* Chat Header */}
            <div className="h-14 bg-[#1A103C] border-b border-[rgba(255,255,255,0.08)] flex items-center px-4 gap-3">
              <button onClick={() => {}} className="md:hidden p-1"><ArrowLeft className="w-5 h-5 text-text-secondary" /></button>
              <div className="w-8 h-8 rounded-full gradient-bg flex items-center justify-center text-white text-sm font-bold">
                {currentSession.astrologerName?.charAt(0)}
              </div>
              <div className="flex-1">
                <p className="text-sm font-medium text-text-primary">{currentSession.astrologerName}</p>
                <p className="text-xs text-success">Online</p>
              </div>
              <button onClick={() => endChat(currentSession.id)} className="text-danger text-sm hover:underline">End Chat</button>
            </div>

            {/* Messages */}
            <div className="flex-1 overflow-y-auto p-4 space-y-3">
              {messages.length === 0 && (
                <div className="text-center py-12">
                  <MessageCircleIcon />
                  <p className="text-text-muted text-sm mt-4">Start your conversation</p>
                </div>
              )}
              {[...messages].reverse().map(m => (
                <div key={m.id} className={`flex ${m.senderType === 'USER' ? 'justify-end' : m.senderType === 'SYSTEM' ? 'justify-center' : 'justify-start'}`}>
                  {m.senderType === 'SYSTEM' ? (
                    <span className="text-xs text-text-muted italic bg-[#1A103C] px-3 py-1.5 rounded-lg">{m.content}</span>
                  ) : (
                    <div className={`max-w-[70%] px-4 py-2.5 rounded-2xl ${m.senderType === 'USER'
                      ? 'bg-gradient-to-r from-brand-primary to-brand-secondary text-white rounded-br-md'
                      : 'bg-[#251850] text-text-primary rounded-bl-md'}`}>
                      <p className="text-sm">{m.content}</p>
                      <p className={`text-[10px] mt-1 ${m.senderType === 'USER' ? 'text-white/60' : 'text-text-muted'}`}>
                        {new Date(m.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </p>
                    </div>
                  )}
                </div>
              ))}
              <div ref={messagesEndRef} />
            </div>

            {/* Input */}
            <form onSubmit={handleSend} className="p-4 bg-[#1A103C] border-t border-[rgba(255,255,255,0.08)] flex gap-3">
              <input
                type="text" value={input} onChange={e => setInput(e.target.value)}
                placeholder="Type a message..." className="input-field flex-1"
              />
              <button type="submit" disabled={!input.trim()} className="btn-primary p-3 rounded-xl">
                <Send className="w-5 h-5" />
              </button>
            </form>
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center">
            <div className="text-center">
              <MessageCircleIcon />
              <h3 className="text-lg font-semibold text-text-primary mt-4 mb-2">Select a conversation</h3>
              <p className="text-text-secondary text-sm mb-6">Choose an astrologer to start chatting</p>
              <button onClick={() => navigate('/astrologers')} className="btn-primary">Browse Astrologers</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

function MessageCircleIcon() {
  return (
    <div className="w-16 h-16 rounded-full bg-brand-primary/10 flex items-center justify-center mx-auto">
      <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-brand-primary"><path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/></svg>
    </div>
  );
}
