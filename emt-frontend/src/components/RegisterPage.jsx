// src/pages/RegisterPage.jsx
import React, {useState} from 'react';
import {
    TextField,
    Button,
    Container,
    Typography,
    Box,
    Alert,
    Paper,
    Avatar,
    Link,
    Fade,
    useTheme
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import {useNavigate} from 'react-router-dom';
import api from '../api/api';
import {motion} from 'framer-motion';

const RegisterPage = () => {
    const theme = useTheme();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: '',
        email: '',
        firstName: '',
        lastName: '',
        roles: ['ADMIN']
    });
    const [errors, setErrors] = useState({});
    const [apiError, setApiError] = useState('');

    const validate = () => {
        const newErrors = {};
        // Username validation
        if (!formData.username.trim()) {
            newErrors.username = 'Username is required';
        } else if (formData.username.length < 3 || formData.username.length > 50) {
            newErrors.username = 'Username must be between 3 and 50 characters';
        }

        // Password validation
        if (!formData.password) {
            newErrors.password = 'Password is required';
        } else if (formData.password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters';
        }

        // Confirm password validation
        if (!formData.confirmPassword) {
            newErrors.confirmPassword = 'Confirm password is required';
        } else if (formData.password !== formData.confirmPassword) {
            newErrors.confirmPassword = 'Passwords do not match';
        }

        // Email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!formData.email.trim()) {
            newErrors.email = 'Email is required';
        } else if (!emailRegex.test(formData.email)) {
            newErrors.email = 'Email should be valid';
        }

        // Name validation
        if (!formData.firstName.trim()) newErrors.firstName = 'First name is required';
        if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({...prev, [name]: ''}));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validate()) return;

        setLoading(true);
        try {
            await api.post('/auth/register', formData);
            navigate('/login', {
                state: {message: 'Registration successful! Please log in.'}
            });
        } catch (error) {
            const message = error.response?.data?.message || 'Registration failed';
            setApiError(message);
            // Animate the error message
            const alertElement = document.getElementById('error-alert');
            if (alertElement) {
                alertElement.style.animation = 'shake 0.5s';
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <Fade in timeout={800}>
            <Container component="main" maxWidth="sm">
                <motion.div
                    initial={{opacity: 0, y: 20}}
                    animate={{opacity: 1, y: 0}}
                    transition={{duration: 0.5}}
                >
                    <Paper
                        elevation={0}
                        sx={{
                            marginTop: 8,
                            p: 4,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            borderRadius: 3,
                            backgroundColor: 'rgba(255, 255, 255, 0.8)',
                            backdropFilter: 'blur(10px)',
                            boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.07)',
                        }}
                    >
                        <Avatar sx={{
                            m: 1,
                            bgcolor: 'primary.main',
                            transform: 'scale(1.2)',
                            transition: 'transform 0.2s ease-in-out',
                            '&:hover': {
                                transform: 'scale(1.3)',
                            }
                        }}>
                            <LockOutlinedIcon/>
                        </Avatar>
                        <Typography
                            component="h1"
                            variant="h4"
                            sx={{
                                mb: 3,
                                fontWeight: 700,
                                background: `linear-gradient(45deg, ${theme.palette.primary.main}, ${theme.palette.secondary.main})`,
                                WebkitBackgroundClip: 'text',
                                WebkitTextFillColor: 'transparent'
                            }}
                        >
                            Create Account
                        </Typography>

                        {apiError && (
                            <Alert
                                severity="error"
                                sx={{
                                    width: '100%',
                                    mb: 2,
                                    borderRadius: 2
                                }}
                                id="error-alert"
                            >
                                {apiError}
                            </Alert>
                        )}

                        <Box component="form" onSubmit={handleSubmit} sx={{mt: 1, width: '100%'}}>
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="username"
                                label="Username"
                                name="username"
                                autoComplete="username"
                                autoFocus
                                value={formData.username}
                                onChange={handleChange}
                                error={!!errors.username}
                                helperText={errors.username}
                                sx={{mb: 2}}
                            />
                            <Box sx={{display: 'flex', gap: 2, mb: 2}}>
                                <TextField
                                    required
                                    fullWidth
                                    id="firstName"
                                    label="First Name"
                                    name="firstName"
                                    value={formData.firstName}
                                    onChange={handleChange}
                                    error={!!errors.firstName}
                                    helperText={errors.firstName}
                                />
                                <TextField
                                    required
                                    fullWidth
                                    id="lastName"
                                    label="Last Name"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                    error={!!errors.lastName}
                                    helperText={errors.lastName}
                                />
                            </Box>
                            <TextField
                                required
                                fullWidth
                                id="email"
                                label="Email Address"
                                name="email"
                                autoComplete="email"
                                value={formData.email}
                                onChange={handleChange}
                                error={!!errors.email}
                                helperText={errors.email}
                                sx={{mb: 2}}
                            />
                            <TextField
                                required
                                fullWidth
                                name="password"
                                label="Password"
                                type="password"
                                id="password"
                                value={formData.password}
                                onChange={handleChange}
                                error={!!errors.password}
                                helperText={errors.password}
                                sx={{mb: 2}}
                            />
                            <TextField
                                required
                                fullWidth
                                name="confirmPassword"
                                label="Confirm Password"
                                type="password"
                                id="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                                error={!!errors.confirmPassword}
                                helperText={errors.confirmPassword}
                                sx={{mb: 2}}
                            />
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                disabled={loading}
                                sx={{
                                    mt: 2,
                                    mb: 2,
                                    py: 1.5,
                                    borderRadius: 2,
                                    transition: 'transform 0.2s ease-in-out',
                                    '&:hover': {
                                        transform: 'translateY(-2px)',
                                    }
                                }}
                            >
                                {loading ? 'Registering...' : 'Register'}
                            </Button>
                            <Box sx={{textAlign: 'center'}}>
                                <Link
                                    to={"/login"}
                                    variant="body2"
                                    sx={{
                                        textDecoration: 'none',
                                        '&:hover': {
                                            textDecoration: 'underline'
                                        }
                                    }}
                                >
                                    Already have an account? Sign in
                                </Link>
                            </Box>
                        </Box>
                    </Paper>
                </motion.div>

            </Container>
        </Fade>
    );
};

export default RegisterPage;