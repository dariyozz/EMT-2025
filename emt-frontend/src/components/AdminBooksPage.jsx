import React, {useState, useEffect} from 'react';
import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from '@mui/material';
import {Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon} from '@mui/icons-material';
import BookForm from './Books/BookForm';
import BookRepository from '../api/bookRepository';

const AdminBooksPage = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
    const [selectedBook, setSelectedBook] = useState(null);
    const [formValid, setFormValid] = useState(false);
    const [formData, setFormData] = useState(null);

    useEffect(() => {
        loadBooks();
    }, []);

    const loadBooks = async () => {
        try {
            setLoading(true);
            const response = await BookRepository.getAll();
            setBooks(response.data);
            setError(null);
        } catch (err) {
            setError('Failed to load books. Please try again later.');
            console.error('Error loading books:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleOpenDialog = (book = null) => {
        setSelectedBook(book);
        setOpenDialog(true);
        setFormData(null);
        setFormValid(false);
    };

    const handleCloseDialog = () => {
        setSelectedBook(null);
        setOpenDialog(false);
        setFormData(null);
        setFormValid(false);
    };

    const handleOpenDeleteDialog = (book) => {
        setSelectedBook(book);
        setOpenDeleteDialog(true);
    };

    const handleCloseDeleteDialog = () => {
        setSelectedBook(null);
        setOpenDeleteDialog(false);
    };

    const handleFormChange = (data, isValid) => {
        setFormData(data);
        setFormValid(isValid);
    };

    const handleSave = async () => {
        if (!formValid || !formData) return;

        try {
            if (selectedBook) {
                await BookRepository.update(selectedBook.id, formData);
            } else {
                await BookRepository.create(formData);
            }
            handleCloseDialog();
            await loadBooks();
        } catch (err) {
            console.error('Error saving book:', err);
            setError('Failed to save book. Please try again.');
        }
    };

    const handleDelete = async () => {
        if (!selectedBook) return;

        try {
            await BookRepository.delete(selectedBook.id);
            handleCloseDeleteDialog();
            await loadBooks();
        } catch (err) {
            console.error('Error deleting book:', err);
            setError('Failed to delete book. Please try again.');
        }
    };

    if (loading) return <Typography>Loading...</Typography>;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box sx={{p: 3}}>
            <Box sx={{display: 'flex', justifyContent: 'space-between', mb: 3}}>
                <Typography variant="h4">Books Management</Typography>
                <Button
                    variant="contained"
                    startIcon={<AddIcon/>}
                    onClick={() => handleOpenDialog()}
                >
                    Add New Book
                </Button>
            </Box>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Category</TableCell>
                            <TableCell>Author</TableCell>
                            <TableCell>Available Copies</TableCell>
                            <TableCell align="right">Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {books.map((book) => (
                            <TableRow key={book.id}>
                                <TableCell>{book.name}</TableCell>
                                <TableCell>{book.category?.replace(/_/g, ' ')}</TableCell>
                                <TableCell>{book.author ? `${book.author.name} ${book.author.surname}` : ''}</TableCell>
                                <TableCell>{book.availableCopies}</TableCell>
                                <TableCell align="right">
                                    <IconButton onClick={() => handleOpenDialog(book)} aria-label="edit">
                                        <EditIcon/>
                                    </IconButton>
                                    <IconButton onClick={() => handleOpenDeleteDialog(book)} aria-label="delete">
                                        <DeleteIcon/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
                <DialogTitle>
                    {selectedBook ? 'Edit Book' : 'Add New Book'}
                </DialogTitle>
                <DialogContent>
                    <Box sx={{mt: 2}}>
                        <BookForm
                            book={selectedBook}
                            onFormChange={handleFormChange}
                        />
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>Cancel</Button>
                    <Button
                        onClick={handleSave}
                        disabled={!formValid}
                        variant="contained"
                    >
                        Save
                    </Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openDeleteDialog} onClose={handleCloseDeleteDialog}>
                <DialogTitle>Confirm Deletion</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Are you sure you want to delete "{selectedBook?.name}"?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDeleteDialog}>Cancel</Button>
                    <Button onClick={handleDelete} color="error" variant="contained">
                        Delete
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default AdminBooksPage;