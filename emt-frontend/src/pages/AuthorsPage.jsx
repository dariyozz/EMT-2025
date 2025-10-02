// src/pages/AuthorsPage.jsx
import React from 'react';
import { Typography, Box, Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import AuthorList from '../components/Authors/AuthorList';
import AuthorDialog from '../components/Authors/AuthorDialog';
import useAuthors from '../hooks/useAuthors';

const AuthorsPage = () => {
    const { authors, loading, error, createAuthor, updateAuthor, deleteAuthor } = useAuthors();
    const [open, setOpen] = React.useState(false);
    const [currentAuthor, setCurrentAuthor] = React.useState(null);

    const handleOpenDialog = (author = null) => {
        setCurrentAuthor(author);
        setOpen(true);
    };

    const handleCloseDialog = () => {
        setOpen(false);
        setCurrentAuthor(null);
    };

    const handleSave = async (authorData) => {
        try {
            if (currentAuthor) {
                await updateAuthor(currentAuthor.id, authorData);
            } else {
                await createAuthor(authorData);
            }
            handleCloseDialog();
        } catch (error) {
            console.error('Error saving author:', error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteAuthor(id);
        } catch (error) {
            console.error('Error deleting author:', error);
        }
    };

    return (
        <Box>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h4" component="h1">
                    Authors
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon />}
                    onClick={() => handleOpenDialog()}
                >
                    Add New Author
                </Button>
            </Box>

            {error && <Typography color="error">{error}</Typography>}

            <AuthorList
                authors={authors}
                loading={loading}
                onEdit={handleOpenDialog}
                onDelete={handleDelete}
            />

            <AuthorDialog
                open={open}
                author={currentAuthor}
                onClose={handleCloseDialog}
                onSave={handleSave}
            />
        </Box>
    );
};

export default AuthorsPage;