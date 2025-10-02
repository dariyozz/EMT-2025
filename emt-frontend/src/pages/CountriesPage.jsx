// src/pages/CountriesPage.jsx
import React from 'react';
import {Typography, Box, Button} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import CountryList from '../components/Countries/CountryList';
import CountryDialog from '../components/Countries/CountryDialog';
import useCountries from '../hooks/useCountries';

const CountriesPage = () => {
    const {countries, loading, error, createCountry, updateCountry, deleteCountry} = useCountries();
    const [open, setOpen] = React.useState(false);
    const [currentCountry, setCurrentCountry] = React.useState(null);

    const handleOpenDialog = (country = null) => {
        setCurrentCountry(country);
        setOpen(true);
    };

    const handleCloseDialog = () => {
        setOpen(false);
        setCurrentCountry(null);
    };

    const handleSave = async (countryData) => {
        try {
            if (currentCountry) {
                await updateCountry(currentCountry.id, countryData);
            } else {
                await createCountry(countryData);
            }
            handleCloseDialog();
        } catch (error) {
            console.error('Error saving country:', error);
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteCountry(id);
        } catch (error) {
            console.error('Error deleting country:', error);
        }
    };

    return (
        <Box>
            <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
                <Typography variant="h4" component="h1">
                    Countries
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon/>}
                    onClick={() => handleOpenDialog()}
                >
                    Add New Country
                </Button>
            </Box>

            {error && <Typography color="error">{error}</Typography>}

            <CountryList
                countries={countries}
                loading={loading}
                onEdit={handleOpenDialog}
                onDelete={handleDelete}
            />

            <CountryDialog
                open={open}
                country={currentCountry}
                onClose={handleCloseDialog}
                onSave={handleSave}
            />
        </Box>
    );
};
export default CountriesPage;