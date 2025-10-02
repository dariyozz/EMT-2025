import api from './api';

const BookRepository = {
    getAll: async () => {
        return await api.get('/books');
    },

    getById: async (id) => {
        return await api.get(`/books/${id}`);
    },

    create: async (bookData) => {
        return await api.post('/books', bookData);
    },

    update: async (id, bookData) => {
        return await api.put(`/books/${id}`, bookData);
    },

    delete: async (id) => {
        return await api.delete(`/books/${id}`);
    },
};

export default BookRepository;