import {useState, useEffect} from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Button,
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Typography,
    Box
} from '@mui/material';
import {Edit as EditIcon, Delete as DeleteIcon} from '@mui/icons-material';
import api from '../api/api';

const AdminAuthorsPage = () => {
    const [authors, setAuthors] = useState([]);
    const [open, setOpen] = useState(false);
    const [selectedAuthor, setSelectedAuthor] = useState(null);
    const [formData, setFormData] = useState({name: '', surname: '', country: ''});
    const [errors, setErrors] = useState({});

    useEffect(() => {
        fetchAuthors();
    }, []);

    const fetchAuthors = async () => {
        try {
            const response = await api.get('/authors');
            setAuthors(response.data);
        } catch (error) {
            console.error('Error fetching authors:', error);
        }
    };

    const handleOpen = (author = null) => {
        setSelectedAuthor(author);
        setFormData(author ? {...author} : {name: '', surname: '', country: ''});
        setErrors({});
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setSelectedAuthor(null);
        setFormData({name: '', surname: '', country: ''});
    };

    const validateForm = () => {
        const newErrors = {};
        if (!formData.name.trim()) newErrors.name = 'Name is required';
        if (!formData.surname.trim()) newErrors.surname = 'Surname is required';
        if (!formData.country.trim()) newErrors.country = 'Country is required';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async () => {
        if (!validateForm()) return;

        try {
            if (selectedAuthor) {
                await api.put(`/authors/${selectedAuthor.id}`, formData);
            } else {
                await api.post('/authors', formData);
            }
            fetchAuthors();
            handleClose();
        } catch (error) {
            console.error('Error saving author:', error);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this author?')) {
            try {
                await api.delete(`/authors/${id}`);
                fetchAuthors();
            } catch (error) {
                console.error('Error deleting author:', error);
            }
        }
    };

    return (
        <Box sx={{p: 3}}>
            <Box sx={{display: 'flex', justifyContent: 'space-between', mb: 2}}>
                <Typography variant="h4">Authors Management</Typography>
                <Button variant="contained" color="primary" onClick={() => handleOpen()}>
                    Add New Author
                </Button>
            </Box>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Surname</TableCell>
                            <TableCell>Country</TableCell>
                            <TableCell align="right">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {authors.map((author) => (
                            <TableRow key={author.id}>
                                <TableCell>{author.name}</TableCell>
                                <TableCell>{author.surname}</TableCell>
                                <TableCell>{author.country}</TableCell>
                                <TableCell align="right">
                                    <IconButton onClick={() => handleOpen(author)}>
                                        <EditIcon/>
                                    </IconButton>
                                    <IconButton onClick={() => handleDelete(author.id)}>
                                        <DeleteIcon/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>{selectedAuthor ? 'Edit Author' : 'Add New Author'}</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="Name"
                        fullWidth
                        value={formData.name}
                        onChange={(e) => setFormData({...formData, name: e.target.value})}
                        error={!!errors.name}
                        helperText={errors.name}
                    />
                    <TextField
                        margin="dense"
                        label="Surname"
                        fullWidth
                        value={formData.surname}
                        onChange={(e) => setFormData({...formData, surname: e.target.value})}
                        error={!!errors.surname}
                        helperText={errors.surname}
                    />
                    <TextField
                        margin="dense"
                        label="Country"
                        fullWidth
                        value={formData.country}
                        onChange={(e) => setFormData({...formData, country: e.target.value})}
                        error={!!errors.country}
                        helperText={errors.country}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleSubmit} variant="contained">
                        {selectedAuthor ? 'Save' : 'Add'}
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default AdminAuthorsPage;
