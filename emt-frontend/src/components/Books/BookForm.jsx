// src/components/Books/BookForm.jsx
import {TextField, MenuItem, FormControl, InputLabel, Select, Grid, FormHelperText} from '@mui/material';
import {useEffect, useState} from "react";
import useAuthors from "../../hooks/useAuthors.js";

const bookCategories = [
    'NOVEL', 'THRILLER', 'HISTORY', 'FANTASY', 'BIOGRAPHY', 'CLASSICS', 'DRAMA', 'SCIENCE_FICTION', 'MYSTERY', 'POETRY'
]; // Expanded slightly

const BookForm = ({book, onFormChange}) => {
    const { authors } = useAuthors(); // Assuming you have this hook providing authors list
    // const authors = [{id: '1', name: 'John', surname: 'Doe'}, {id: '2', name: 'Jane', surname: 'Smith'}]; // Dummy authors if hook not provided

    const [formData, setFormData] = useState({
        name: '',
        category: '',
        authorId: '',
        availableCopies: '' // Changed to empty string for better UX with TextField type="number"
    });
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (book) {
            setFormData({
                name: book.name || '',
                category: book.category || '',
                authorId: book.author ? book.author.id : '',
                availableCopies: book.availableCopies !== undefined ? String(book.availableCopies) : ''
            });
            setErrors({}); // Clear errors when book changes
        } else {
            // Reset form for new book
            setFormData({name: '', category: '', authorId: '', availableCopies: ''});
            setErrors({});
        }
    }, [book]);

    // Debounced validation can be added here if needed for performance on every keystroke
    const validate = (currentFormData) => {
        const newErrors = {};
        if (!currentFormData.name.trim()) newErrors.name = 'Book name is required.';
        else if (currentFormData.name.trim().length < 3) newErrors.name = 'Book name must be at least 3 characters.';

        if (!currentFormData.category) newErrors.category = 'Category is required.';
        if (!currentFormData.authorId) newErrors.authorId = 'Author is required.';

        const copies = Number(currentFormData.availableCopies);
        if (currentFormData.availableCopies === '') newErrors.availableCopies = 'Available copies is required.';
        else if (isNaN(copies) || copies < 0) newErrors.availableCopies = 'Must be a non-negative number.';
        else if (!Number.isInteger(copies)) newErrors.availableCopies = 'Must be a whole number.';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };


    const handleChange = (e) => {
        const {name, value} = e.target;
        let processedValue = value;
        if (name === "availableCopies") {
            // Allow empty string, or enforce numeric input partially
            processedValue = value === '' ? '' : String(value).replace(/[^0-9]/g, '');
        }

        const newFormData = {...formData, [name]: processedValue};
        setFormData(newFormData);
        // Validate on change and pass validity up
        const isValid = validate(newFormData);
        onFormChange(newFormData, isValid);
    };


    return (
        <Grid container spacing={3}>
            <Grid item xs={12}>
                <TextField
                    fullWidth
                    label="Book Name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    error={!!errors.name}
                    helperText={errors.name || "e.g., The Great Gatsby"}
                    required
                    autoFocus // Focus on the first field
                />
            </Grid>

            <Grid item xs={12} sm={6}>
                <FormControl fullWidth error={!!errors.category} required>
                    <InputLabel id="category-label">Category</InputLabel>
                    <Select
                        labelId="category-label"
                        name="category"
                        value={formData.category}
                        onChange={handleChange}
                        label="Category"
                    >
                        <MenuItem value="" disabled><em>Select a category...</em></MenuItem>
                        {bookCategories.map((category) => (
                            <MenuItem key={category} value={category}>
                                {category.replace(/_/g, ' ')} {/* Replace underscores for display */}
                            </MenuItem>
                        ))}
                    </Select>
                    {errors.category && <FormHelperText>{errors.category}</FormHelperText>}
                </FormControl>
            </Grid>

            <Grid item xs={12} sm={6}>
                <FormControl fullWidth error={!!errors.authorId} required>
                    <InputLabel id="author-label">Author</InputLabel>
                    <Select
                        labelId="author-label"
                        name="authorId"
                        value={formData.authorId}
                        onChange={handleChange}
                        label="Author"
                        MenuProps={{PaperProps: {style: {maxHeight: 250}}}} // Limit dropdown height
                    >
                        <MenuItem value="" disabled><em>Select an author...</em></MenuItem>
                        {authors.map((author) => (
                            <MenuItem key={author.id} value={author.id}>
                                {author.name} {author.surname}
                            </MenuItem>
                        ))}
                    </Select>
                    {errors.authorId && <FormHelperText>{errors.authorId}</FormHelperText>}
                </FormControl>
            </Grid>

            <Grid item xs={12}>
                <TextField
                    fullWidth
                    label="Available Copies"
                    name="availableCopies"
                    type="text" // Changed to text to allow empty string and better control input
                    inputMode="numeric" // Hint for mobile keyboards
                    value={formData.availableCopies}
                    onChange={handleChange}
                    error={!!errors.availableCopies}
                    helperText={errors.availableCopies || "Enter a whole number (e.g., 0, 5, 10)"}
                    required
                />
            </Grid>
        </Grid>
    );
}

export default BookForm;