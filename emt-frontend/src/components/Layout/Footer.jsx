// src/components/Layout/Footer.jsx
import React from 'react';
import { Box, Typography, Container } from '@mui/material'; // Already imported

const Footer = () => {
    return (
        <Box
            component="footer"
            sx={{
                py: 4, // Increased padding
                mt: 'auto',
                backgroundColor: 'background.paper',
                borderTop: (theme) => `1px solid ${theme.palette.divider}`,
            }}
        >
            <Container maxWidth="lg">
                <Typography
                    variant="body2"
                    color="text.secondary"
                    align="center"
                >
                    BookShelf &copy; {new Date().getFullYear()} - Modern Reading Starts Here.
                </Typography>
            </Container>
        </Box>
    );
};
export default Footer;