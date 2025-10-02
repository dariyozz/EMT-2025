// src/api/authorRepository.js
import api from './api';

const AuthorRepository = {
    getAll: async () => {
        return await api.get('/authors');
    },

    getById: async (id) => {
        return await api.get(`/authors/${id}`);
    },

    create: async (authorData) => {
        return await api.post('/authors', authorData);
    },

    update: async (id, authorData) => {
        return await api.put(`/authors/${id}`, authorData);
    },

    delete: async (id) => {
        return await api.delete(`/authors/${id}`);
    },
};

export default AuthorRepository;