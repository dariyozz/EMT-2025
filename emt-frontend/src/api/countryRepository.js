// src/api/countryRepository.js
import api from './api';

const CountryRepository = {
    getAll: async () => {
        return await api.get('/countries');
    },

    getById: async (id) => {
        return await api.get(`/countries/${id}`);
    },

    create: async (countryData) => {
        return await api.post('/countries', countryData);
    },

    update: async (id, countryData) => {
        return await api.put(`/countries/${id}`, countryData);
    },

    delete: async (id) => {
        return await api.delete(`/countries/${id}`);
    },
};

export default CountryRepository;