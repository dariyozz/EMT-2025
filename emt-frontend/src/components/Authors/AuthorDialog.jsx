// src/components/Authors/AuthorDialog.jsx
import React, { useState, useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Box
} from '@mui/material';
import AuthorForm from './AuthorForm';

const AuthorDialog = ({ open, author, onClose, onSave }) => {
    const [formData, setFormData] = useState({
        name: '',
        surname: '',
        countryId: ''
    });
    const [isValid, setIsValid] = useState(false);

    useEffect(() => {
        if (author) {
            setFormData({
                name: author.name || '',
                surname: author.surname || '',
                countryId: author.country ? author.country.id : ''
            });
        } else {
            setFormData({
                name: '',
                surname: '',
                countryId: ''
            });
        }
    }, [author, open]);

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

    const dialogTitle = author ? 'Edit Author' : 'Add New Author';

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
                        <AuthorForm author={author} onFormChange={handleFormChange} />
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

export default AuthorDialog;