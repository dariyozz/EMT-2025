import React from 'react';
import {
    Typography,
    Button,
    TablePagination,
    Alert,
    Fade,
    Box,
    Container,
    Paper,
    useTheme
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import LibraryBooksIcon from '@mui/icons-material/LibraryBooks';
import BookList from '../components/Books/BookList';
import BookDialog from '../components/Books/BookDialog';
import useBooks from '../hooks/useBooks';
import useAuth from '../hooks/useAuth';

const BooksPage = () => {
    const theme = useTheme();
    const {user} = useAuth();
    const isAdmin = user?.roles?.includes('ROLE_ADMIN');
    const {
        books,
        loading,
        error,
        pagination,
        setPagination,
        createBook,
        updateBook,
        deleteBook
    } = useBooks();
    const [open, setOpen] = React.useState(false);
    const [currentBook, setCurrentBook] = React.useState(null);

    const handlePageChange = (event, newPage) => {
        setPagination(prev => ({...prev, page: newPage}));
    };

    const handleChangeRowsPerPage = (event) => {
        const newSize = parseInt(event.target.value, 10);
        setPagination(prev => ({...prev, size: newSize, page: 0}));
    };

    const handleOpenDialog = (book = null) => {
        if (!isAdmin) return;
        setCurrentBook(book);
        setOpen(true);
    };

    const handleCloseDialog = () => {
        setOpen(false);
        setCurrentBook(null);
    };

    const handleSave = async (bookData) => {
        try {
            if (currentBook) {
                await updateBook(currentBook.id, bookData);
            } else {
                await createBook(bookData);
            }
            handleCloseDialog();
        } catch (err) {
            console.error('Error saving book:', err);
        }
    };

    const handleDelete = async (id) => {
        if (!isAdmin) return;
        try {
            await deleteBook(id);
        } catch (err) {
            console.error('Error deleting book:', err);
        }
    };

    return (
        <Fade in timeout={800}>
            <Container maxWidth="lg" sx={{py: 4, px: {xs: 2, sm: 4, md: 6}}}>
                <Paper
                    elevation={0}
                    sx={{
                        p: {xs: 2, sm: 3, md: 4},
                        borderRadius: 3,
                        backgroundColor: 'rgba(255, 255, 255, 0.8)',
                        backdropFilter: 'blur(10px)',
                        boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.07)',
                        maxWidth: '1200px',
                        margin: '0 auto'
                    }}
                >
                    <Box sx={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        mb: 4,
                        flexWrap: 'wrap',
                        gap: 2
                    }}>
                        <Box sx={{display: 'flex', alignItems: 'center', gap: 2}}>
                            <LibraryBooksIcon
                                sx={{
                                    fontSize: 40,
                                    color: 'primary.main',
                                    animation: 'float 3s ease-in-out infinite'
                                }}
                            />
                            <Typography
                                variant="h4"
                                component="h1"
                                sx={{
                                    fontWeight: 700,
                                    background: `linear-gradient(45deg, ${theme.palette.primary.main}, ${theme.palette.secondary.main})`,
                                    WebkitBackgroundClip: 'text',
                                    WebkitTextFillColor: 'transparent'
                                }}
                            >
                                Books Collection
                            </Typography>
                        </Box>
                        {isAdmin && (
                            <Button
                                variant="contained"
                                color="primary"
                                startIcon={<AddIcon/>}
                                onClick={() => handleOpenDialog()}
                                sx={{
                                    borderRadius: 2,
                                    px: 3,
                                    py: 1,
                                    transition: 'all 0.3s ease',
                                    '&:hover': {
                                        transform: 'translateY(-2px)',
                                        boxShadow: '0 5px 15px rgba(0,0,0,0.1)'
                                    }
                                }}
                            >
                                Add New Book
                            </Button>
                        )}
                    </Box>

                    {error && (
                        <Alert
                            severity="error"
                            sx={{
                                mb: 3,
                                borderRadius: 2,
                                animation: 'slideIn 0.5s ease-out'
                            }}
                        >
                            {error}
                        </Alert>
                    )}

                    <Typography
                        variant="subtitle1"
                        color="text.secondary"
                        sx={{
                            mb: 3,
                            opacity: loading ? 0.7 : 1,
                            transition: 'opacity 0.3s ease'
                        }}
                    >
                        {loading
                            ? 'Loading books...'
                            : (books?.length
                                ? `Showing ${books.length} of ${pagination?.totalElements || 0} books`
                                : 'No books available at the moment')}
                    </Typography>

                    <BookList
                        books={books || []}
                        loading={loading}
                        onEdit={isAdmin ? handleOpenDialog : undefined}
                        onDelete={isAdmin ? handleDelete : undefined}
                        isAdmin={isAdmin}
                    />

                    <TablePagination
                        component="div"
                        count={pagination?.totalElements || 0}
                        page={pagination?.page || 0}
                        onPageChange={handlePageChange}
                        rowsPerPage={pagination?.size || 5}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                        rowsPerPageOptions={[5, 10, 25, 50]}
                        sx={{
                            mt: 3,
                            '.MuiTablePagination-select': {
                                borderRadius: theme.shape.borderRadius,
                            }
                        }}
                    />

                    {isAdmin && (
                        <BookDialog
                            open={open}
                            book={currentBook}
                            onClose={handleCloseDialog}
                            onSave={handleSave}
                        />
                    )}
                </Paper>

                <style>
                    {`
                        @keyframes float {
                            0% { transform: translateY(0px); }
                            50% { transform: translateY(-10px); }
                            100% { transform: translateY(0px); }
                        }
                        @keyframes slideIn {
                            from { transform: translateY(-20px); opacity: 0; }
                            to { transform: translateY(0); opacity: 1; }
                        }
                    `}
                </style>
            </Container>
        </Fade>
    );
};

export default BooksPage;