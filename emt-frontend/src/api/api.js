// src/api/api.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor to add JWT token to requests
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor to handle token refresh
// api.interceptors.response.use(
//     (response) => response,
//     async (error) => {
//         const originalRequest = error.config;
//
//         // If the error is 401 and we haven't tried to refresh the token yet
//         if (error.response?.status === 401 && !originalRequest._retry) {
//             originalRequest._retry = true;
//
//             try {
//                 // Import here to avoid circular dependency
//                 const authService = (await import('../services/authService')).default;
//                 await authService.refreshToken();
//
//                 // Retry the original request with the new token
//                 const token = authService.getAccessToken();
//                 originalRequest.headers.Authorization = `Bearer ${token}`;
//                 return axios(originalRequest);
//             } catch (refreshError) {
//                 // If refresh token fails, logout user and redirect to login
//                 const authService = (await import('../services/authService')).default;
//                 authService.logout();
//                 window.location.href = '/login';
//                 return Promise.reject(refreshError);
//             }
//         }
//
//         return Promise.reject(error);
//     }
// );

export default api;