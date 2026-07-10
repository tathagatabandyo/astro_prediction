import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { Toaster } from 'sonner'
import './index.css'
import App from './App.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <App />
      <Toaster
        position="top-right"
        toastOptions={{
          style: {
            background: '#1A103C',
            border: '1px solid rgba(255,255,255,0.08)',
            color: '#F8FAFC'
          }
        }}
      />
    </BrowserRouter>
  </StrictMode>,
)
