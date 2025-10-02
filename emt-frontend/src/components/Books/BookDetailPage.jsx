import {useEffect, useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {Box, Typography, Paper, Grid, CircularProgress, Button} from '@mui/material';
import BookRepository from '../../api/bookRepository';

const BookDetailPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBook = async () => {
            try {
                const response = await BookRepository.getById(id);
                setBook(response.data);
                setError(null);
            } catch (err) {
                setError(err.response?.data?.message || 'Failed to fetch book details');
            } finally {
                setLoading(false);
            }
        };

        fetchBook();
    }, [id]);

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="60vh">
                <CircularProgress/>
            </Box>
        );
    }

    if (error) {
        return (
            <Box display="flex" flexDirection="column" alignItems="center" gap={2} p={3}>
                <Typography color="error" variant="h6">{error}</Typography>
                <Button variant="contained" onClick={() => navigate('/books')}>
                    Back to Books
                </Button>
            </Box>
        );
    }

    return (
        <Paper elevation={3} sx={{p: 3, m: 2}}>
            <Grid container spacing={3}>
                <Grid item xs={12}>
                    <Typography variant="h4" component="h1" gutterBottom>
                        {book?.name}
                    </Typography>
                </Grid>
                <Grid item xs={12} sm={6}>
                    <Typography variant="subtitle1" gutterBottom>
                        <strong>Author:</strong> {book?.author?.name} {book?.author?.surname}
                    </Typography>
                </Grid>
                <Grid item xs={12} sm={6}>
                    <Typography variant="subtitle1" gutterBottom>
                        <strong>Category:</strong> {book?.category?.replace(/_/g, ' ')}
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    <Typography variant="subtitle1" gutterBottom>
                        <strong>Available Copies:</strong> {book?.availableCopies}
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    <Box display="flex" gap={2}>
                        <Button
                            variant="contained"
                            onClick={() => navigate(`/books/edit/${id}`)}
                        >
                            Edit Book
                        </Button>
                        <Button
                            variant="outlined"
                            onClick={() => navigate('/books')}
                        >
                            Back to Books
                        </Button>
                    </Box>
                </Grid>
            </Grid>
        </Paper>
    );
};

export default BookDetailPage;
