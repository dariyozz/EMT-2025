// src/components/Authors/AuthorList.jsx
import React from 'react';
import { Grid, CircularProgress, Box, Typography } from '@mui/material';
import AuthorItem from './AuthorItem';

const AuthorList = ({ authors, loading, onEdit, onDelete }) => {
    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (!authors || authors.length === 0) {
        return (
            <Typography variant="h6" color="text.secondary" align="center" sx={{ my: 4 }}>
                No authors found. Add a new author to get started.
            </Typography>
        );
    }

    return (
        <Grid container spacing={3}>
            {authors.map((author) => (
                <Grid item xs={12} sm={6} md={4} key={author.id}>
                    <AuthorItem author={author} onEdit={onEdit} onDelete={onDelete} />
                </Grid>
            ))}
        </Grid>
    );
};

export default AuthorList;