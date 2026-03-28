import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';
import apiClient from '../api/client';

interface User {
  id: number;
  username: string;
  role: string;
}

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  login: (username: string, password: string) => Promise<void>;
  register: (username: string, email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

function parseToken(token: string): User | null {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return {
      id: payload.userId,
      username: payload.sub,
      role: payload.role || 'ROLE_USER',
    };
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const parsed = parseToken(token);
      if (parsed) {
        setUser(parsed);
      } else {
        localStorage.removeItem('token');
      }
    }
  }, []);

  const login = async (username: string, password: string) => {
    const response = await apiClient.post('/api/users/login', { username, password });
    const token = typeof response.data === 'string' ? response.data : response.data.token;
    localStorage.setItem('token', token);
    setUser(parseToken(token));
  };

  const register = async (username: string, email: string, password: string) => {
    await apiClient.post('/api/users/register', { username, email, password });
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
