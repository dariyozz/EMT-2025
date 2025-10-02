import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
} from '@mui/material';
import {Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon} from '@mui/icons-material';
import {useState, useEffect} from 'react';
import api from '../api/api';

const AdminCountriesPage = () => {
    const [countries, setCountries] = useState([]);
    const [open, setOpen] = useState(false);
    const [selectedCountry, setSelectedCountry] = useState(null);
    const [countryName, setCountryName] = useState('');
    const [continent, setContinent] = useState('');

    useEffect(() => {
        fetchCountries();
    }, []);

    const fetchCountries = async () => {
        try {
            const response = await api.get('/countries');
            setCountries(response.data);
        } catch (error) {
            console.error('Error fetching countries:', error);
        }
    };

    const handleOpen = (country = null) => {
        if (country) {
            setSelectedCountry(country);
            setCountryName(country.name);
            setContinent(country.continent);
        } else {
            setSelectedCountry(null);
            setCountryName('');
            setContinent('');
        }
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setSelectedCountry(null);
        setCountryName('');
        setContinent('');
    };

    const handleSave = async () => {
        try {
            if (selectedCountry) {
                await api.put(`/countries/${selectedCountry.id}`, {
                    name: countryName,
                    continent: continent,
                });
            } else {
                await api.post('/countries', {
                    name: countryName,
                    continent: continent,
                });
            }
            fetchCountries();
            handleClose();
        } catch (error) {
            console.error('Error saving country:', error);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this country?')) {
            try {
                await api.delete(`/countries/${id}`);
                fetchCountries();
            } catch (error) {
                console.error('Error deleting country:', error);
            }
        }
    };

    return (
        <div>
            <Button
                variant="contained"
                color="primary"
                startIcon={<AddIcon/>}
                onClick={() => handleOpen()}
                style={{marginBottom: '1rem'}}
            >
                Add Country
            </Button>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Continent</TableCell>
                            <TableCell align="right">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {countries.map((country) => (
                            <TableRow key={country.id}>
                                <TableCell>{country.name}</TableCell>
                                <TableCell>{country.continent}</TableCell>
                                <TableCell align="right">
                                    <IconButton onClick={() => handleOpen(country)}>
                                        <EditIcon/>
                                    </IconButton>
                                    <IconButton onClick={() => handleDelete(country.id)}>
                                        <DeleteIcon/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>{selectedCountry ? 'Edit Country' : 'Add Country'}</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Country Name"
                        type="text"
                        fullWidth
                        value={countryName}
                        onChange={(e) => setCountryName(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        label="Continent"
                        type="text"
                        fullWidth
                        value={continent}
                        onChange={(e) => setContinent(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleSave} variant="contained" color="primary">
                        {selectedCountry ? 'Update' : 'Create'}
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default AdminCountriesPage;
