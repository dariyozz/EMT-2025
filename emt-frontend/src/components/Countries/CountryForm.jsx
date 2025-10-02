// src/components/Countries/CountryForm.jsx
import React, {useState, useEffect} from 'react';
import {TextField, Grid} from '@mui/material';

const CountryForm = ({country, onFormChange}) => {
    const [formData, setFormData] = useState({
        name: '',
        continent: ''
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (country) {
            setFormData({
                name: country.name || '',
                continent: country.continent || ''
            });
        }
    }, [country]);

    const validateForm = () => {
        const newErrors = {};

        if (!formData.name.trim()) {
            newErrors.name = 'Country name is required';
        }

        if (!formData.continent.trim()) {
            newErrors.continent = 'Continent is required';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleChange = (e) => {
        const {name, value} = e.target;
        const newFormData = {
            ...formData,
            [name]: value
        };
        setFormData(newFormData);
        onFormChange(newFormData, validateForm());
    };

    return (
        <Grid container spacing={2}>
            <Grid item xs={12}>
                <TextField
                    fullWidth
                    label="Country Name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    error={!!errors.name}
                    helperText={errors.name}
                    required
                />
            </Grid>

            <Grid item xs={12}>
                <TextField
                    fullWidth
                    label="Continent"
                    name="continent"
                    value={formData.continent}
                    onChange={handleChange}
                    error={!!errors.continent}
                    helperText={errors.continent}
                    required
                />
            </Grid>
        </Grid>
    );
};

export default CountryForm;