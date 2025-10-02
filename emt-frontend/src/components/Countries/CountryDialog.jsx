// src/components/Countries/CountryDialog.jsx
import React, { useState, useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    Box
} from '@mui/material';
import CountryForm from './CountryForm';

const CountryDialog = ({ open, country, onClose, onSave }) => {
    const [formData, setFormData] = useState({
        name: '',
        continent: ''
    });
    const [isValid, setIsValid] = useState(false);

    useEffect(() => {
        if (country) {
            setFormData({
                name: country.name || '',
                continent: country.continent || ''
            });
        } else {
            setFormData({
                name: '',
                continent: ''
            });
        }
    }, [country, open]);

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

    const dialogTitle = country ? 'Edit Country' : 'Add New Country';

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
                        <CountryForm country={country} onFormChange={handleFormChange} />
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

export default CountryDialog;