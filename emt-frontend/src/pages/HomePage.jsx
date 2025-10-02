import React from 'react';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import LibraryBooksIcon from '@mui/icons-material/LibraryBooks';
import GroupIcon from '@mui/icons-material/Group';
import PublicIcon from '@mui/icons-material/Public';
import {Box, Button, Container, Grid, Paper, Typography, useTheme} from "@mui/material";
import {alpha} from "@mui/material/styles";
import {Link as RouterLink} from 'react-router-dom';
import useAuth from '../hooks/useAuth';

const FeatureCard = ({icon, title, description, ctaLink, ctaText}) => {
    const theme = useTheme();

    return (
        <Paper
            elevation={0}
            sx={{
                p: 3,
                textAlign: 'center',
                height: '100%',
                border: `1px solid ${theme.palette.divider}`,
                borderRadius: theme.shape.borderRadius,
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                '&:hover': {
                    borderColor: theme.palette.primary.main,
                    boxShadow: `0 0 15px ${alpha(theme.palette.primary.main, 0.2)}`,
                }
            }}
        >
            <Box>
                <Box sx={{fontSize: 48, color: 'primary.main', mb: 2}}>{icon}</Box>
                <Typography variant="h6" gutterBottom sx={{fontWeight: 'bold'}}>{title}</Typography>
                <Typography variant="body2" color="text.secondary" sx={{mb: 2}}>{description}</Typography>
            </Box>
            {ctaLink && ctaText && (
                <Button
                    variant="outlined"
                    color="primary"
                    component={RouterLink}
                    to={ctaLink}
                    endIcon={<ArrowForwardIcon/>}
                    sx={{mt: 'auto'}}
                >
                    {ctaText}
                </Button>
            )}
        </Paper>
    );
};


const HomePage = () => {
    const theme = useTheme();
    const {user} = useAuth();

    const hasAdminAccess = user?.roles?.includes('ADMIN');

    return (
        <>
            {/* Hero Section */}
            <Box
                sx={{
                    textAlign: 'center',
                    py: {xs: 6, md: 10},
                    backgroundColor: alpha(theme.palette.primary.main, 0.05),
                    borderRadius: {sm: 0, md: theme.shape.borderRadius},
                    mb: 6,
                }}
            >
                <Container maxWidth="md">
                    <Typography variant="h2" component="h1" gutterBottom sx={{fontWeight: 700}}>
                        Welcome to BookShelf
                    </Typography>
                    <Typography variant="h5" color="text.secondary" sx={{mb: 4, maxWidth: '700px', mx: 'auto'}}>
                        Discover your next favorite read. Explore our vast collection of books, authors, and more.
                    </Typography>
                    <Button
                        variant="contained"
                        color="primary"
                        size="large"
                        component={RouterLink}
                        to="/books"
                        endIcon={<ArrowForwardIcon/>}
                        sx={{py: 1.5, px: 4, fontSize: '1.1rem'}}
                    >
                        Explore Books
                    </Button>
                </Container>
            </Box>

            {/* Features Section */}
            <Container maxWidth="lg" sx={{py: {xs: 4, md: 6}}}>
                <Typography variant="h4" component="h2" align="center" gutterBottom sx={{fontWeight: 'bold', mb: 6}}>
                    Why Choose BookShelf?
                </Typography>
                <Grid container spacing={4}>
                    <Grid item xs={12} sm={6} md={3}>
                        <FeatureCard
                            icon={<LibraryBooksIcon fontSize="inherit"/>}
                            title="Vast Collection"
                            description="Access thousands of books across all genres."
                            ctaLink="/books"
                            ctaText="Browse Now"
                        />
                    </Grid>
                    <Grid item xs={12} sm={6} md={3}>
                        <FeatureCard
                            icon={<GroupIcon fontSize="inherit"/>}
                            title="Curated Authors"
                            description="Learn about your favorite authors and discover new ones."
                            ctaLink="/authors"
                            ctaText="Meet Authors"
                        />
                    </Grid>
                    <Grid item xs={12} sm={6} md={3}>
                        <FeatureCard
                            icon={<PublicIcon fontSize="inherit"/>}
                            title="Global Reach"
                            description="Books from authors all around the world."
                            ctaLink="/countries"
                            ctaText="Explore Origins"
                        />
                    </Grid>
                    <Grid item xs={12} sm={6} md={3}>
                        <FeatureCard
                            icon={<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                       strokeWidth={1.5} stroke="currentColor" width="1em" height="1em">
                                <path strokeLinecap="round" strokeLinejoin="round"
                                      d="M9.568 3H5.25A2.25 2.25 0 003 5.25v4.318c0 .597.237 1.17.659 1.591l9.581 9.581c.699.699 1.78.872 2.607.33a18.095 18.095 0 005.223-5.223c.542-.827.369-1.908-.33-2.607L11.16 3.66A2.25 2.25 0 009.568 3z"/>
                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 6h.008v.008H6V6z"/>
                            </svg>}
                            title="Easy Tagging"
                            description="Organize and find books with our intuitive category system."
                        />
                    </Grid>
                </Grid>
            </Container>

            {/* Admin Features Section */}
            {hasAdminAccess && (
                <Container maxWidth="lg" sx={{py: {xs: 4, md: 6}}}>
                    <Typography variant="h4" component="h2" align="center" gutterBottom
                                sx={{fontWeight: 'bold', mb: 6}}>
                        Administrative Tools
                    </Typography>
                    <Grid container spacing={4}>
                        <Grid item xs={12} sm={6} md={3}>
                            <FeatureCard
                                icon={<LibraryBooksIcon fontSize="inherit"/>}
                                title="Manage Books"
                                description="Add, edit, and manage the book collection."
                                ctaLink="/admin/books-admin"
                                ctaText="Manage Books"
                            />
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <FeatureCard
                                icon={<GroupIcon fontSize="inherit"/>}
                                title="Manage Authors"
                                description="Maintain author information and details."
                                ctaLink="/admin/authors-admin"
                                ctaText="Manage Authors"
                            />
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <FeatureCard
                                icon={<PublicIcon fontSize="inherit"/>}
                                title="Manage Countries"
                                description="Update and organize country information."
                                ctaLink="/admin/countries-admin"
                                ctaText="Manage Countries"
                            />
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <FeatureCard
                                icon={<GroupIcon fontSize="inherit"/>}
                                title="User Management"
                                description="Manage user accounts and permissions."
                                ctaLink="/admin/users"
                                ctaText="Manage Users"
                            />
                        </Grid>
                    </Grid>
                </Container>
            )}

            {/* User Features Section */}
            <Container maxWidth="lg" sx={{py: {xs: 4, md: 6}}}>
                <Typography variant="h4" component="h2" align="center" gutterBottom sx={{fontWeight: 'bold', mb: 6}}>
                    Your Account
                </Typography>
                <Grid container spacing={4} justifyContent="center">
                    <Grid item xs={12} sm={6} md={3}>
                        <FeatureCard
                            icon={<LibraryBooksIcon fontSize="inherit"/>}
                            title="My Books"
                            description="View and manage your personal book collection."
                            ctaLink="/my-books"
                            ctaText="View My Books"
                        />
                    </Grid>
                    <Grid item xs={12} sm={6} md={3}>
                        <FeatureCard
                            icon={<GroupIcon fontSize="inherit"/>}
                            title="Profile"
                            description="Manage your account settings and preferences."
                            ctaLink="/profile"
                            ctaText="View Profile"
                        />
                    </Grid>
                </Grid>
            </Container>

            {/* Call to Action Section */}
            <Box sx={{py: {xs: 6, md: 8}, backgroundColor: theme.palette.background.paper, mt: 6}}>
                <Container maxWidth="md">
                    <Typography variant="h4" component="h2" align="center" gutterBottom sx={{fontWeight: 'bold'}}>
                        Ready to Dive In?
                    </Typography>
                    <Typography variant="body1" color="text.secondary" align="center" sx={{mb: 4}}>
                        Join our community of book lovers and start your reading journey today.
                    </Typography>
                    <Box sx={{textAlign: 'center'}}>
                        {!user ? (
                            <>
                                <Button
                                    component={RouterLink}
                                    to="/register"
                                    variant="contained"
                                    color="primary"
                                    size="large"
                                    sx={{mr: 2, py: 1.5, px: 4}}
                                >
                                    Sign Up
                                </Button>
                                <Button
                                    component={RouterLink}
                                    to="/login"
                                    variant="outlined"
                                    color="primary"
                                    size="large"
                                    sx={{py: 1.5, px: 4}}
                                >
                                    Login
                                </Button>
                            </>
                        ) : (
                            <Button
                                component={RouterLink}
                                to="/my-books"
                                variant="contained"
                                color="primary"
                                size="large"
                                sx={{py: 1.5, px: 4}}
                            >
                                View My Books
                            </Button>
                        )}
                    </Box>
                </Container>
            </Box>
        </>
    );
};

export default HomePage;