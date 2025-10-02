// src/components/Authors/AuthorForm.jsx
import React, { useState, useEffect } from 'react';
import {
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Grid,
    FormHelperText
} from '@mui/material';
import useCountries from '../../hooks/useCountries';

const AuthorForm = ({ author, onFormChange }) => {
    const { countries } = useCountries();
    const [formData, setFormData] = useState({
        name: '',
        surname: '',
        countryId: ''
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (author) {
            setFormData({
                name: author.name || '',
                surname: author.surname || '',
                countryId: author.country ? author.country.id : ''
            });
        }
    }, [author]);

    const validateForm = () => {
        const newErrors = {};

        if (!formData.name.trim()) {
            newErrors.name = 'Author name is required';
        }

        if (!formData.surname.trim()) {
            newErrors.surname = 'Author surname is required';
        }

        if (!formData.countryId) {
            newErrors.countryId = 'Country is required';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        const newFormData = {
            ...formData,
            [name]: value
        };
        setFormData(newFormData);
        onFormChange(newFormData, validateForm());
    };

    return (
        <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
                <TextField
                    fullWidth
                    label="Name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    error={!!errors.name}
                    helperText={errors.name}
                    required
                />
            </Grid>

            <Grid item xs={12} sm={6}>
                <TextField
                    fullWidth
                    label="Surname"
                    name="surname"
                    value={formData.surname}
                    onChange={handleChange}
                    error={!!errors.surname}
                    helperText={errors.surname}
                    required
                />
            </Grid>

            <Grid item xs={12}>
                <FormControl fullWidth error={!!errors.countryId} required>
                    <InputLabel id="country-label">Country</InputLabel>
                    <Select
                        labelId="country-label"
                        name="countryId"
                        value={formData.countryId}
                        onChange={handleChange}
                        label="Country"
                    >
                        {countries.map((country) => (
                            <MenuItem key={country.id} value={country.id}>
                                {country.name}
                            </MenuItem>
                        ))}
                    </Select>
                    {errors.countryId && <FormHelperText>{errors.countryId}</FormHelperText>}
                </FormControl>
            </Grid>
        </Grid>
    );
};

export default AuthorForm;
