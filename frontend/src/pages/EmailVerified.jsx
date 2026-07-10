import { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { CheckCircle, XCircle, Loader2, Star } from 'lucide-react';

export default function EmailVerified() {
  const [searchParams] = useSearchParams();
  const success = searchParams.get('success') === 'true';
  const message = searchParams.get('message')?.replace(/\+/g, ' ');
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    const t = setTimeout(() => setChecking(false), 600);
    return () => clearTimeout(t);
  }, []);

  if (checking) {
    return (
      <div className="min-h-screen bg-[#0F0A1E] flex items-center justify-center">
        <Loader2 className="w-10 h-10 text-brand-primary animate-spin" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#0F0A1E] flex items-center justify-center p-6">
      <div className="w-full max-w-md">
        <Link to="/" className="flex items-center gap-2 justify-center mb-8">
          <Star className="w-6 h-6 text-brand-primary" />
          <span className="text-xl font-bold gradient-text">AstroPrediction</span>
        </Link>

        <div className="card text-center">
          {success ? (
            <>
              <div className="flex justify-center mb-4">
                <CheckCircle className="w-16 h-16 text-green-400" />
              </div>
              <h2 className="text-2xl font-bold text-text-primary mb-2">Email Verified!</h2>
              <p className="text-text-secondary mb-6">
                Your email has been verified successfully. You can now sign in to your account.
              </p>
              <Link
                to="/login"
                className="btn-primary w-full flex items-center justify-center gap-2"
              >
                Continue to Login
              </Link>
            </>
          ) : (
            <>
              <div className="flex justify-center mb-4">
                <XCircle className="w-16 h-16 text-red-400" />
              </div>
              <h2 className="text-2xl font-bold text-text-primary mb-2">Verification Failed</h2>
              <p className="text-text-secondary mb-6">
                {message || 'The verification link is invalid or has expired.'}
              </p>
              <div className="flex flex-col gap-3">
                <Link
                  to="/register"
                  className="btn-primary w-full flex items-center justify-center gap-2"
                >
                  Back to Register
                </Link>
                <Link
                  to="/login"
                  className="btn-secondary w-full flex items-center justify-center gap-2"
                >
                  Already verified? Login
                </Link>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
