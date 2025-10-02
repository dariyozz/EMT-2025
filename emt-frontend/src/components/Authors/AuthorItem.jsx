// src/components/Authors/AuthorItem.jsx
import React from 'react';
import { Card, CardContent, CardActions, Typography, Button, Box } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const AuthorItem = ({ author, onEdit, onDelete }) => {
    const handleDelete = (e) => {
        e.stopPropagation();
        if (window.confirm(`Are you sure you want to delete "${author.name} ${author.surname}"?`)) {
            onDelete(author.id);
        }
    };

    return (
        <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" component="h2" gutterBottom>
                    {author.name} {author.surname}
                </Typography>
                {author.country && (
                    <Typography color="text.secondary" gutterBottom>
                        Country: {author.country.name}
                    </Typography>
                )}
            </CardContent>
            <CardActions>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
                    <Button
                        size="small"
                        startIcon={<EditIcon />}
                        onClick={() => onEdit(author)}
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

export default AuthorItem;