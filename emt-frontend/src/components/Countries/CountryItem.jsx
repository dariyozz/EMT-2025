// src/components/Countries/CountryItem.jsx
import React from 'react';
import { Card, CardContent, CardActions, Typography, Button, Box } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import PublicIcon from '@mui/icons-material/Public';

const CountryItem = ({ country, onEdit, onDelete }) => {
    const handleDelete = (e) => {
        e.stopPropagation();
        if (window.confirm(`Are you sure you want to delete "${country.name}"?`)) {
            onDelete(country.id);
        }
    };

    return (
        <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            <CardContent sx={{ flexGrow: 1 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <PublicIcon sx={{ mr: 1, color: 'primary.main' }} />
                    <Typography variant="h6" component="h2">
                        {country.name}
                    </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary">
                    Continent: {country.continent}
                </Typography>
            </CardContent>
            <CardActions>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                    <Button
                        size="small"
                        startIcon={<EditIcon />}
                        onClick={() => onEdit(country)}
                    >
                        Edit
                    </Button>
                    <Button
                        size="small"
                        color="error"
                        startIcon={<DeleteIcon />}
                        onClick={handleDelete}
                    >
                        Delete
                    </Button>
                </Box>
            </CardActions>
        </Card>
    );
};

export default CountryItem;