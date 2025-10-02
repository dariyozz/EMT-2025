// src/hooks/useBookWithAuthor.js
import { useState, useEffect } from 'react';
import AuthorRepository from '../../src/api/authorRepository.js';

const useBookWithAuthor = (book) => {
    const [author, setAuthor] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchAuthor = async () => {
            if (!book?.authorId) {
                setLoading(false);
                return;
            }

            try {
                const response = await AuthorRepository.getById(book.authorId);
                setAuthor(response.data);
            } catch (err) {
                setError(err.message);
                console.error('Error fetching author:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchAuthor();
    }, [book?.authorId]);

    return { author, loading, error };
};

export default useBookWithAuthor;