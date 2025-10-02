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
    Typography
} from '@mui/material';
import {Add as AddIcon, Delete as DeleteIcon, Edit as EditIcon} from '@mui/icons-material';
import {useEffect, useState} from 'react';
import BookForm from './Books/BookForm';
import BookRepository from '../api/bookRepository';

const MyBooksPage = () => {
    const [books, setBooks] = useState([]);
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedBook, setSelectedBook] = useState(null);
    const [isFormValid, setIsFormValid] = useState(false);
    const [formData, setFormData] = useState(null);

    useEffect(() => {
        loadBooks();
    }, []);

    const loadBooks = async () => {
        try {
            const response = await BookRepository.getAll();
            setBooks(response.data);
        } catch (error) {
            console.error('Failed to load books:', error);
        }
    };

    const handleOpenDialog = (book = null) => {
        setSelectedBook(book);
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setSelectedBook(null);
        setFormData(null);
        setIsFormValid(false);
    };

    const handleFormChange = (data, isValid) => {
        setFormData(data);
        setIsFormValid(isValid);
    };

    const handleSave = async () => {
        try {
            if (selectedBook) {
                await BookRepository.update(selectedBook.id, formData);
            } else {
                await BookRepository.create(formData);
            }
            handleCloseDialog();
            loadBooks();
        } catch (error) {
            console.error('Failed to save book:', error);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this book?')) {
            try {
                await BookRepository.delete(id);
                loadBooks();
            } catch (error) {
                console.error('Failed to delete book:', error);
            }
        }
    };

    return (
        <div>
            <Typography variant="h4" gutterBottom>
                My Books
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon/>}
                    onClick={() => handleOpenDialog()}
                    style={{float: 'right'}}
                >
                    Add New Book
                </Button>
            </Typography>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Category</TableCell>
                            <TableCell>Author</TableCell>
                            <TableCell>Available Copies</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {books.map((book) => (
                            <TableRow key={book.id}>
                                <TableCell>{book.name}</TableCell>
                                <TableCell>{book.category.replace(/_/g, ' ')}</TableCell>
                                <TableCell>{`${book.author.name} ${book.author.surname}`}</TableCell>
                                <TableCell>{book.availableCopies}</TableCell>
                                <TableCell>
                                    <IconButton onClick={() => handleOpenDialog(book)} color="primary">
                                        <EditIcon/>
                                    </IconButton>
                                    <IconButton onClick={() => handleDelete(book.id)} color="error">
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
                    <BookForm
                        book={selectedBook}
                        onFormChange={handleFormChange}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>Cancel</Button>
                    <Button
                        onClick={handleSave}
                        disabled={!isFormValid}
                        variant="contained"
                        color="primary"
                    >
                        Save
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default MyBooksPage;
