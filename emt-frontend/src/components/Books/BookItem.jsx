// src/components/Books/BookItem.jsx
import React from 'react';
import {
    Card,
    CardContent,
    CardActions,
    CardMedia,
    Typography,
    Box,
    Chip,
    IconButton,
    Tooltip,
    useTheme,
    Skeleton
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import MenuBookIcon from '@mui/icons-material/MenuBook';
import PersonIcon from '@mui/icons-material/Person';
import { alpha } from '@mui/material/styles';
import useBookWithAuthor from '../../hooks/useBookWithAuthor';
import image from '../../assets/book-composition-with-open-book.jpg';

const BookItem = ({ book, onEdit, onDelete, isAdmin }) => {
    const theme = useTheme();
    const { author, loading: authorLoading } = useBookWithAuthor(book);

    const handleDelete = (e) => {
        e.stopPropagation();
        if (window.confirm(`Are you sure you want to delete "${book.name}"?`)) {
            onDelete(book.id);
        }
    };

    return (
        <Card
            sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                position: 'relative',
                transition: 'all 0.3s ease',
                borderRadius: 2,
                overflow: 'visible',
                '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: `0 12px 20px -8px ${alpha(theme.palette.primary.main, 0.2)}`,
                },
                '&::before': {
                    content: '""',
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    height: '4px',
                    background: `linear-gradient(90deg, ${theme.palette.primary.main}, ${theme.palette.secondary.main})`,
                    borderRadius: '4px 4px 0 0',
                }
            }}
        >
            <CardMedia
                component="img"
                height="200"
                image={book.imageUrl || image}
                alt={book.name}
                sx={{
                    objectFit: 'cover',
                    borderBottom: `1px solid ${theme.palette.divider}`
                }}
            />
            <CardContent sx={{ flexGrow: 1, pt: 3 }}>
                <Box sx={{ display: 'flex', alignItems: 'flex-start', mb: 2 }}>
                    <MenuBookIcon
                        sx={{
                            fontSize: '2rem',
                            color: 'primary.main',
                            mr: 1
                        }}
                    />
                    <Box>
                        <Typography variant="h6" component="h2" gutterBottom>
                            {book.name}
                        </Typography>
                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                            <PersonIcon sx={{ fontSize: '1rem', mr: 0.5, color: 'text.secondary' }} />
                            {authorLoading ? (
                                <Skeleton width={150} height={24} />
                            ) : (
                                <Typography variant="body2" color="text.secondary">
                                    {author ? `${author.name} ${author.surname}` : 'Unknown Author'}
                                </Typography>
                            )}
                        </Box>
                    </Box>
                </Box>

                <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mb: 2 }}>
                    <Chip
                        label={book.category}
                        size="small"
                        sx={{
                            backgroundColor: alpha(theme.palette.primary.main, 0.1),
                            color: theme.palette.primary.main,
                        }}
                    />
                    <Chip
                        label={`${book.availableCopies} copies`}
                        size="small"
                        sx={{
                            backgroundColor: book.availableCopies > 0
                                ? alpha(theme.palette.success.main, 0.1)
                                : alpha(theme.palette.error.main, 0.1),
                            color: book.availableCopies > 0
                                ? theme.palette.success.main
                                : theme.palette.error.main,
                        }}
                    />
                    {book.rented && (
                        <Chip
                            label="Rented"
                            size="small"
                            sx={{
                                backgroundColor: alpha(theme.palette.warning.main, 0.1),
                                color: theme.palette.warning.main,
                            }}
                        />
                    )}
                </Box>
            </CardContent>

            {isAdmin && (
                <CardActions sx={{
                    borderTop: `1px solid ${theme.palette.divider}`,
                    justifyContent: 'flex-end',
                    gap: 1
                }}>
                    <Tooltip title="Edit book">
                        <IconButton
                            size="small"
                            onClick={() => onEdit(book)}
                            sx={{
                                color: theme.palette.primary.main,
                                '&:hover': {
                                    backgroundColor: alpha(theme.palette.primary.main, 0.1),
                                }
                            }}
                        >
                            <EditIcon fontSize="small" />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Delete book">
                        <IconButton
                            size="small"
                            onClick={handleDelete}
                            sx={{
                                color: theme.palette.error.main,
                                '&:hover': {
                                    backgroundColor: alpha(theme.palette.error.main, 0.1),
                                }
                            }}
                        >
                            <DeleteIcon fontSize="small" />
                        </IconButton>
                    </Tooltip>
                </CardActions>
            )}
        </Card>
    );
};

export default BookItem;