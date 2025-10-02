// src/components/Books/BookList.jsx
import React from 'react';
import { Grid, CircularProgress, Box, Typography, Fade } from '@mui/material';
import {motion} from "framer-motion";
import BookItem from './BookItem';

const BookList = ({ books, loading, onEdit, onDelete, isAdmin }) => {
    if (loading) {
        return (
            <Box sx={{ 
                display: 'flex', 
                justifyContent: 'center', 
                alignItems: 'center', 
                minHeight: '200px'
            }}>
                <CircularProgress 
                    sx={{
                        animation: 'pulse 1.5s ease-in-out infinite',
                        '@keyframes pulse': {
                            '0%': { opacity: 0.6 },
                            '50%': { opacity: 1 },
                            '100%': { opacity: 0.6 }
                        }
                    }}
                />
            </Box>
        );
    }

    if (!books || books.length === 0) {
        return (
            <Fade in={true} timeout={1000}>
                <Box
                    sx={{
                        textAlign: 'center',
                        py: 8,
                        backgroundColor: 'rgba(0, 0, 0, 0.02)',
                        borderRadius: 2,
                        border: '2px dashed rgba(0, 0, 0, 0.08)'
                    }}
                >
                    <Typography 
                        variant="h6" 
                        color="text.secondary"
                        sx={{ 
                            fontWeight: 500,
                            mb: 1
                        }}
                    >
                        No books available
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        {isAdmin ? 'Click the "Add New Book" button to get started.' : 'Check back later for new books.'}
                    </Typography>
                </Box>
            </Fade>
        );
    }

    return (
        <Grid container spacing={3}>
            {books.map((book, index) => (
                <Grid item xs={12} sm={6} md={4} key={book.id}>
                    <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.3, delay: index * 0.1 }}
                    >
                        <BookItem 
                            book={book} 
                            onEdit={onEdit} 
                            onDelete={onDelete}
                            isAdmin={isAdmin}
                        />
                    </motion.div>
                </Grid>
            ))}
        </Grid>
    );
};

export default BookList;