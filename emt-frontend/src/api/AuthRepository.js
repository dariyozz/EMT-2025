// src/api/authRepository.js
import api from './api';

const AuthRepository = {
    login: async (credentials) => {
        return await api.post('/auth/login', credentials);
    },

    register: async (userData) => {
        return await api.post('/auth/register', userData);
    },

    refreshToken: async (refreshToken) => {
        return await api.post('/auth/refresh-token', { refreshToken });
    },
};

export default AuthRepository;