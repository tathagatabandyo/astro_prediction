import { useEffect, useState } from 'react';
import { useWalletStore } from '../stores/walletStore';
import { useAuthStore } from '../stores/authStore';
import { useNavigate } from 'react-router-dom';
import { Wallet as WalletIcon, CreditCard, ArrowUpRight, ArrowDownRight, Tag, Loader2 } from 'lucide-react';
import { toast } from 'sonner';

export default function Wallet() {
  const navigate = useNavigate();
  const { user } = useAuthStore();
  const { wallet, transactions, plans, fetchWallet, fetchTransactions, fetchPlans, createCheckout } = useWalletStore();
  const [selectedPlan, setSelectedPlan] = useState(null);
  const [customAmount, setCustomAmount] = useState('');
  const [couponCode, setCouponCode] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!user) { navigate('/login'); return; }
    fetchWallet();
    fetchTransactions();
    fetchPlans();
  }, [user]);

  const handleRecharge = async () => {
    const amount = selectedPlan ? selectedPlan.amount : parseFloat(customAmount);
    if (!amount || amount < 10) { toast.error('Minimum recharge is Rs. 10'); return; }
    setLoading(true);
    try {
      const result = await createCheckout(selectedPlan?.id, amount, couponCode);
      if (result?.checkoutUrl) window.location.href = result.checkoutUrl;
      else toast.error('Failed to create checkout');
    } catch (e) { toast.error(e.message); }
    setLoading(false);
  };

  if (!user) return null;

  return (
    <div className="min-h-screen">
      {/* Hero */}
      <div className="gradient-bg py-12 px-6 rounded-b-3xl">
        <div className="max-w-[1440px] mx-auto">
          <h1 className="text-3xl font-bold text-white mb-2">My Wallet</h1>
          <p className="text-white/70">Manage your balance and transactions</p>
        </div>
      </div>

      <div className="max-w-[1440px] mx-auto px-6 py-8 space-y-8">
        {/* Balance Card */}
        <div className="gradient-bg rounded-2xl p-8 flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
          <div>
            <p className="text-white/70 text-sm mb-1">Available Balance</p>
            <h2 className="text-4xl font-bold text-white">Rs. {wallet?.balance?.toFixed?.(2) || '0.00'}</h2>
          </div>
          <div className="flex gap-3">
            <button className="bg-white/20 hover:bg-white/30 text-white px-6 py-3 rounded-xl text-sm font-semibold transition-colors backdrop-blur-sm">
              Transaction History
            </button>
          </div>
        </div>

        {/* Recharge Plans */}
        <div>
          <h3 className="text-xl font-semibold text-text-primary mb-4">Recharge Plans</h3>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">
            {plans.map(plan => (
              <button
                key={plan.id}
                onClick={() => { setSelectedPlan(plan); setCustomAmount(''); }}
                className={`card p-4 text-center border-2 transition-all ${selectedPlan?.id === plan.id ? 'border-brand-primary' : 'border-transparent'}`}>
                <h4 className="font-semibold text-text-primary text-sm">{plan.name}</h4>
                <p className="text-2xl font-bold gradient-text mt-2">Rs. {plan.amount}</p>
                {plan.bonusCredits > 0 && (
                  <p className="text-success text-xs mt-1">+Rs. {plan.bonusCredits} bonus</p>
                )}
              </button>
            ))}
          </div>
        </div>

        {/* Custom Amount + Coupon */}
        <div className="card">
          <h4 className="font-semibold text-text-primary mb-4">Or Enter Custom Amount</h4>
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="relative flex-1">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted">Rs.</span>
              <input type="number" value={customAmount} onChange={e => { setCustomAmount(e.target.value); setSelectedPlan(null); }}
                placeholder="Enter amount" min="10" className="input-field pl-10" />
            </div>
            <div className="relative flex-1">
              <Tag className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-text-muted" />
              <input type="text" value={couponCode} onChange={e => setCouponCode(e.target.value)}
                placeholder="Coupon code (optional)" className="input-field pl-10" />
            </div>
          </div>
          <button onClick={handleRecharge} disabled={loading || (!selectedPlan && !customAmount)}
            className="btn-primary w-full mt-4 flex items-center justify-center gap-2">
            {loading ? <Loader2 className="w-4 h-4 animate-spin" /> : <CreditCard className="w-4 h-4" />}
            Proceed to Payment
          </button>
        </div>

        {/* Transaction History */}
        <div>
          <h3 className="text-xl font-semibold text-text-primary mb-4">Recent Transactions</h3>
          <div className="space-y-2">
            {transactions.length === 0 ? (
              <div className="card text-center py-8 text-text-muted">No transactions yet</div>
            ) : (
              transactions.map(tx => (
                <div key={tx.id} className="card py-3 px-4 flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className={`w-8 h-8 rounded-full flex items-center justify-center ${tx.type === 'CREDIT' || tx.type === 'REFUND' ? 'bg-success/10' : 'bg-danger/10'}`}>
                      {tx.type === 'CREDIT' || tx.type === 'REFUND' ? <ArrowUpRight className="w-4 h-4 text-success" /> : <ArrowDownRight className="w-4 h-4 text-danger" />}
                    </div>
                    <div>
                      <p className="text-sm font-medium text-text-primary">{tx.description}</p>
                      <p className="text-xs text-text-muted">{new Date(tx.createdAt).toLocaleDateString()}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className={`text-sm font-semibold ${tx.type === 'CREDIT' || tx.type === 'REFUND' ? 'text-success' : 'text-danger'}`}>
                      {tx.type === 'CREDIT' || tx.type === 'REFUND' ? '+' : '-'}Rs. {Math.abs(tx.amount).toFixed(2)}
                    </p>
                    <p className="text-xs text-text-muted">Bal: Rs. {tx.balanceAfter?.toFixed(2)}</p>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
