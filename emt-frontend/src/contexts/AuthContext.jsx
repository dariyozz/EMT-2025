// src/contexts/AuthContext.jsx
// 1.  Added useEffect so the token is attached the first time the app is loaded
// 2.  Added/removed the header inside login / logout to keep axios in-sync
// 3.  A couple of tiny refactors for clarity – nothing functional changed
import { createContext, useState, useCallback, useEffect } from 'react';
import api from '../api/api';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    /* ─────────────────────────  FIX  ───────────────────────── */
    const [user, setUser] = useState(() => {
        const raw = localStorage.getItem('user');

        // nothing stored OR the literal "undefined" ⇒ treat as not logged-in
        if (!raw || raw === 'undefined') return null;

        try {
            return JSON.parse(raw);
        } catch (err) {
            console.error('Invalid user JSON in localStorage → clearing it', err);
            localStorage.removeItem('user');
            return null;
        }
    });
    /* ───────────────────────────────────────────────────────── */

    /* ---------- NEW: keep axios header in sync on initial load ---------- */
    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            api.defaults.headers.common.Authorization = `Bearer ${token}`;
        }
    }, []);
    /* -------------------------------------------------------------------- */

    const login = useCallback(async (credentials) => {
        try {
            const response = await api.post('/auth/login', credentials);
            const { token, refreshToken, ...userData } = response.data;

            localStorage.setItem('accessToken', token);
            localStorage.setItem('refreshToken', refreshToken);
            localStorage.setItem('user', JSON.stringify(userData));

            /* ---------- NEW: attach token for all subsequent calls ---------- */
            api.defaults.headers.common.Authorization = `Bearer ${token}`;
            /* ---------------------------------------------------------------- */

            setUser(userData);
            return userData;
        } catch (error) {
            console.error('Login failed:', error);
            throw error; // Re-throw to handle in the component
        }
    }, []);

    const logout = useCallback(() => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');

        /* ---------- NEW: remove header so we don’t send stale tokens ------- */
        delete api.defaults.headers.common.Authorization;
        /* ------------------------------------------------------------------ */

        setUser(null);
    }, []);

    // const refreshToken = useCallback(async () => {
    //     try {
    //         const refreshToken = localStorage.getItem('refreshToken');
    //         if (!refreshToken) throw new Error('No refresh token available');
    //
    //         const response = await api.post('/auth/refresh-token', {
    //             refreshToken
    //         });
    //
    //         const { token } = response.data;
    //         localStorage.setItem('accessToken', token);
    //
    //         return token;
    //     } catch (error) {
    //         logout();
    //         throw error;
    //     }
    // }, [logout]);

    return (
        <AuthContext.Provider
            value={{
                user,
                isAuthenticated: !!user,
                login,
                logout
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};