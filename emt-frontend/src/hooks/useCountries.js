// src/hooks/useCountries.js
import { useState, useEffect, useCallback } from 'react';
import CountryRepository from '../api/countryRepository';

const useCountries = () => {
    const [countries, setCountries] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchCountries = useCallback(async () => {
        setLoading(true);
        try {
            const response = await CountryRepository.getAll();
            setCountries(response.data);
            setError(null);
        } catch (err) {
            setError('Failed to fetch countries');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchCountries();
    }, [fetchCountries]);

    const getCountry = useCallback(async (id) => {
        try {
            const response = await CountryRepository.getById(id);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, []);

    const createCountry = useCallback(async (countryData) => {
        try {
            await CountryRepository.create(countryData);
            await fetchCountries(); // Refetch countries after creation
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchCountries]);

    const updateCountry = useCallback(async (id, countryData) => {
        try {
            await CountryRepository.update(id, countryData);
            await fetchCountries(); // Refetch countries after update
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchCountries]);

    const deleteCountry = useCallback(async (id) => {
        try {
            await CountryRepository.delete(id);
            await fetchCountries(); // Refetch countries after deletion
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchCountries]);

    return {
        countries,
        loading,
        error,
        fetchCountries,
        getCountry,
        createCountry,
        updateCountry,
        deleteCountry
    };
};

export default useCountries;