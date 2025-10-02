// src/components/Countries/CountryList.jsx
import React from 'react';
import { Grid, CircularProgress, Box, Typography } from '@mui/material';
import CountryItem from './CountryItem';

const CountryList = ({ countries, loading, onEdit, onDelete }) => {
    if (loading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (!countries || countries.length === 0) {
        return (
            <Typography variant="h6" color="text.secondary" align="center" sx={{ my: 4 }}>
                No countries found. Add a new country to get started.
            </Typography>
        );
    }

    return (
        <Grid container spacing={3}>
            {countries.map((country) => (
                <Grid item xs={12} sm={6} md={4} key={country.id}>
                    <CountryItem country={country} onEdit={onEdit} onDelete={onDelete} />
                </Grid>
            ))}
        </Grid>
    );
};

export default CountryList;
