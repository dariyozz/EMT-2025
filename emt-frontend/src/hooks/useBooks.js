// src/hooks/useBooks.js
import {useState, useEffect, useCallback} from 'react';
import BookRepository from '../api/bookRepository';
import api from '../api/api';

const useBooks = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [pagination, setPagination] = useState({
        page: 0,
        size: 5,
        totalElements: 0,
        totalPages: 0,
        sortBy: 'id',
        direction: 'asc'
    });

    const fetchBooks = useCallback(async () => {
        console.log("Fetching books with current pagination:", pagination); // Log for debugging
        setLoading(true);
        setError(null); // Reset error before fetch
        try {
            const response = await api.get('/books/pagination', {
                params: {
                    page: pagination.page,
                    size: pagination.size,
                    sortBy: pagination.sortBy,
                    direction: pagination.direction
                }
            }); //

            if (response && response.data) {
                console.log("Books fetched successfully:", response.data); // Log for debugging
                setBooks(response.data.content || []); //
                setPagination(prevPagination => {
                    const responseData = response.data; // Assuming response.data is the PageResponse object
                    return {
                        ...prevPagination, // Start with everything from previous state

                        // Only update if responseData and the specific field are not undefined
                        page: responseData && responseData.number !== undefined ? responseData.number : prevPagination.page,
                        size: responseData && responseData.size !== undefined ? responseData.size : prevPagination.size,
                        totalElements: responseData && responseData.totalElements !== undefined ? responseData.totalElements : prevPagination.totalElements,
                        totalPages: responseData && responseData.totalPages !== undefined ? responseData.totalPages : prevPagination.totalPages,

                        // For sortBy and direction, your existing logic is already good,
                        // but ensure responseData itself is checked.
                        sortBy: responseData && responseData.sortBy !== undefined ? responseData.sortBy : prevPagination.sortBy,
                        direction: responseData && responseData.direction !== undefined ? responseData.direction : prevPagination.direction,
                    };
                });
            } else {
                console.error("Invalid response structure from API");
                setError("Failed to fetch books or invalid response structure.");
                setBooks([]);
            }
        } catch (err) {
            console.error("Error fetching books:", err);
            setError(err.message || 'Failed to fetch books'); //
            setBooks([]); // Clear books on error
        } finally {
            setLoading(false); //
        }
    }, [pagination.direction, pagination.page, pagination.size, pagination.sortBy]); // useCallback dependencies

    useEffect(() => {
        fetchBooks();
        // This useEffect runs when fetchBooks changes.
        // fetchBooks changes when its dependencies (pagination.page, etc.) change.
    }, [fetchBooks]); // useEffect dependency

    const getBook = useCallback(async (id) => {
        try {
            const response = await BookRepository.getById(id);
            return response.data;
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, []);

    const createBook = useCallback(async (bookData) => {
        try {
            await BookRepository.create(bookData);
            await fetchBooks(); // Refetch books after creation
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchBooks]);

    const updateBook = useCallback(async (id, bookData) => {
        try {
            await BookRepository.update(id, bookData);
            await fetchBooks(); // Refetch books after update
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchBooks]);

    const deleteBook = useCallback(async (id) => {
        try {
            await BookRepository.delete(id);
            console.log("Book deleted successfully");
            await fetchBooks(); // Refetch books after deletion
        } catch (err) {
            console.error(err);
            throw err;
        }
    }, [fetchBooks]);

    return {
        books,
        loading,
        error,
        pagination,
        setPagination, // <--- FIX 3.1: Expose setPagination
        fetchBooks, // Still exposed if direct calls are needed (e.g., manual refresh button)
        getBook,
        createBook,
        updateBook,
        deleteBook
    };
};

export default useBooks;