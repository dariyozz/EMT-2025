import React from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import {ThemeProvider, createTheme, responsiveFontSizes} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Layout from './components/Layout/Layout';
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import BookDetailPage from "./components/Books/BookDetailPage.jsx"; // Assuming you have these
import {AuthProvider} from './contexts/AuthContext.jsx';
import HomePage from "./pages/HomePage.jsx";
import BooksPage from "./pages/BooksPage.jsx";
import LoginPage from "./components/LoginPage.jsx";
import RegisterPage from "./components/RegisterPage.jsx";
import UnauthorizedPage from "./components/UnauthorizedPage.jsx";
import AuthorsPage from "./pages/AuthorsPage.jsx";
import AdminBooksPage from "./components/AdminBooksPage.jsx";
import CountriesPage from "./pages/CountriesPage.jsx";
import UsersManagementPage from "./components/UsersManagementPage.jsx";
import ProfilePage from "./components/ProfilePage.jsx";
import AdminAuthorsPage from "./components/AdminAuthorsPage.jsx";
import AdminCountriesPage from "./components/AdminCountriesPage .jsx";
import MyBooksPage from "./components/MyBooksPage.jsx";
// ... other imports

let theme = createTheme({
    palette: {
        mode: 'light', // Consider adding a dark mode toggle in the future
        primary: {
            main: '#00796b', // A slightly desaturated teal
            light: '#48a999',
            dark: '#004c40',
        },
        secondary: {
            main: '#ffc107', // Amber for contrast
            light: '#fff350',
            dark: '#c79100',
        },
        background: {
            default: '#f4f6f8', // Light grey background for the page
            paper: '#ffffff',    // White for cards, dialogs, paper elements
        },
        text: {
            primary: '#263238', // Dark grey for primary text
            secondary: '#546e7a', // Lighter grey for secondary text
        },
    },
    typography: {
        fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif', // Modern sans-serif
        h1: {fontWeight: 700, letterSpacing: '-0.5px'},
        h2: {fontWeight: 700, letterSpacing: '-0.25px'},
        h3: {fontWeight: 600},
        h4: {fontWeight: 600},
        h5: {fontWeight: 500},
        h6: {fontWeight: 500},
        button: {
            textTransform: 'none', // Modern buttons often don't use ALL CAPS
            fontWeight: 600,
        },
    },
    shape: {
        borderRadius: 8, // Slightly more rounded corners
    },
    components: {
        MuiButton: {
            styleOverrides: {
                root: {
                    borderRadius: 6, // Consistent button border radius
                },
                containedPrimary: {
                    boxShadow: '0 3px 5px 2px rgba(0, 121, 107, .2)', // Softer shadow for primary buttons
                    '&:hover': {
                        boxShadow: '0 6px 10px 4px rgba(0, 121, 107, .25)',
                    }
                }
            }
        },
        MuiCard: {
            styleOverrides: {
                root: {
                    transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
                    '&:hover': {
                        transform: 'translateY(-4px)',
                        boxShadow: '0 4px 20px 0 rgba(0,0,0,0.12)', // More pronounced shadow on hover
                    },
                },
            },
        },
        MuiAppBar: {
            styleOverrides: {
                root: {
                    boxShadow: 'none', // Flatter app bar by default
                }
            }
        },
        MuiTextField: {
            defaultProps: {
                variant: 'outlined', // Default to outlined for modern look
            }
        },
        MuiSelect: {
            defaultProps: {
                variant: 'outlined',
            }
        }
    },
});

theme = responsiveFontSizes(theme);

function App() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <AuthProvider>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<Layout/>}>
                            {/* Public routes */}
                            <Route index element={<HomePage/>}/>
                            <Route path="login" element={<LoginPage/>}/>
                            <Route path="register" element={<RegisterPage/>}/>
                            <Route path="unauthorized" element={<UnauthorizedPage/>}/>

                            {/* Public book routes */}
                            <Route path="books" element={<BooksPage/>}/>
                            <Route path="books/:id" element={<BookDetailPage/>}/>
                            <Route path="authors" element={<AuthorsPage/>}/>
                            <Route path="countries" element={<CountriesPage/>}/>

                            {/* Protected routes for authenticated users */}
                            <Route element={<ProtectedRoute/>}>
                                <Route path="profile" element={<ProfilePage/>}/>
                            </Route>

                            {/* Protected routes for users with ROLE_USER */}
                            <Route element={<ProtectedRoute requiredRoles={['USER']}/>}>
                                <Route path="my-books" element={<MyBooksPage/>}/>
                            </Route>

                            {/* Protected routes for administrators */}
                            <Route element={<ProtectedRoute requiredRoles={['ADMIN']}/>}>
                                <Route path="admin">
                                    <Route path="books-admin" element={<AdminBooksPage/>}/>
                                    <Route path="authors-admin" element={<AdminAuthorsPage/>}/>
                                    <Route path="countries-admin" element={<AdminCountriesPage/>}/>
                                    <Route path="users" element={<UsersManagementPage/>}/>
                                </Route>
                            </Route>
                        </Route>
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </ThemeProvider>
    );
}

export default App;