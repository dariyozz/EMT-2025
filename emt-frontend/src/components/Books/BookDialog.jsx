
// src/components/Books/BookDialog.jsx
import React, { useState, useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Box
} from '@mui/material';
import BookForm from './BookForm';

const BookDialog = ({ open, book, onClose, onSave }) => {
    const [formData, setFormData] = useState({
        name: '',
        category: '',
        authorId: '',
        availableCopies: 0
    });
    const [isValid, setIsValid] = useState(false);

    useEffect(() => {
        if (book) {
            setFormData({
                name: book.name || '',
                category: book.category || '',
                authorId: book.author ? book.author.id : '',
                availableCopies: book.availableCopies || 0
            });
        } else {
            setFormData({
                name: '',
                category: '',
                authorId: '',
                availableCopies: 0
            });
        }
    }, [book, open]);

    const handleFormChange = (data, valid) => {
        setFormData(data);
        setIsValid(valid);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (isValid) {
            onSave(formData);
        }
    };

    const dialogTitle = book ? 'Edit Book' : 'Add New Book';

    return (
        <Dialog
            open={open}
            onClose={onClose}
            fullWidth
            maxWidth="sm"
        >
            <form onSubmit={handleSubmit}>
                <DialogTitle>{dialogTitle}</DialogTitle>
                <DialogContent>
                    <Box sx={{ pt: 1 }}>
                        <BookForm book={book} onFormChange={handleFormChange} />
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                        disabled={!isValid}
                    >
                        Save
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
};

export default BookDialog;