// src/components/Layout/Layout.jsx
import React from 'react';
import {Outlet} from 'react-router-dom';
// For transparent colors

import Header from './Header'; // Defined above
import Footer from './Footer';
import {Box, useTheme} from "@mui/material";
import CssBaseline from "@mui/material/CssBaseline"; // Defined above

const Layout = () => {
    const theme = useTheme(); // Make sure theme is accessible
    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                minHeight: '100vh',
                backgroundColor: theme.palette.background.default,
            }}
        >
            <CssBaseline/>
            <Header/>
            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    width: '100%', // Ensure main takes full width
                }}
            >
                {/* Container for page content is now within each page for more control */}
                <Outlet/>
            </Box>
            <Footer/>
        </Box>
    );
};

export default Layout;