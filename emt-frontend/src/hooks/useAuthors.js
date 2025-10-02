// src/hooks/useAuthors.js
import { useState, useEffect, useCallback } from 'react';
import AuthorRepository from '../api/authorRepository';

const useAuthors = () => {
    const [authors, setAuthors] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchAuthors = useCallback(async () => {
        setLoading(true);
        try {
            const response = await AuthorRepository.getAll();
            setAuthors(response.data);
            setError(null);
        } catch (err) {
            setError('Failed to fetch authors');
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchAuthors();
    }, [fetchAuthors]);

    const getAuthor = useCallback(async (id) => {
        try {
            const response = await AuthorRepository.getById(id);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, []);

    const createAuthor = useCallback(async (authorData) => {
        try {
            await AuthorRepository.create(authorData);
            await fetchAuthors(); // Refetch authors after creation
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchAuthors]);

    const updateAuthor = useCallback(async (id, authorData) => {
        try {
            await AuthorRepository.update(id, authorData);
            await fetchAuthors(); // Refetch authors after update
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchAuthors]);

    const deleteAuthor = useCallback(async (id) => {
        try {
            await AuthorRepository.delete(id);
            await fetchAuthors(); // Refetch authors after deletion
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchAuthors]);

    return {
        authors,
        loading,
        error,
        fetchAuthors,
        getAuthor,
        createAuthor,
        updateAuthor,
        deleteAuthor
    };
};

export default useAuthors;