import {AppBar, Toolbar, Typography, Button, Box, Container, IconButton, useTheme, useMediaQuery} from '@mui/material';
import {Link as RouterLink, NavLink} from 'react-router-dom';
import MenuBookIcon from '@mui/icons-material/MenuBook';
import MenuIcon from '@mui/icons-material/Menu';
import React from "react";
import {alpha} from "@mui/material/styles";

const Header = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const [mobileOpen, setMobileOpen] = React.useState(false);

    const handleDrawerToggle = () => {
        setMobileOpen(!mobileOpen);
    };

    const navItems = [
        {label: 'Home', path: '/'},
        {label: 'Books', path: '/books'},
        {label: 'Authors', path: '/authors'},
        {label: 'Countries', path: '/countries'},
    ];

    const navLinkStyles = ({isActive}) => ({
        fontWeight: isActive ? 700 : 500,
        color: isActive ? theme.palette.primary.main : theme.palette.text.primary,
        padding: theme.spacing(1, 2),
        borderRadius: theme.shape.borderRadius / 2,
        '&:hover': {
            backgroundColor: alpha(theme.palette.primary.main, 0.08),
            color: theme.palette.primary.dark,
        },
        textDecoration: 'none',
        display: 'inline-block'
    });

    return (
        <AppBar
            position="sticky"
            sx={{
                backgroundColor: alpha(theme.palette.background.paper, 0.9),
                backdropFilter: 'blur(8px)',
                borderBottom: `1px solid ${theme.palette.divider}`,
                color: theme.palette.text.primary,
            }}
        >
            <Container maxWidth="lg">
                <Toolbar disableGutters>
                    <MenuBookIcon sx={{mr: 1.5, color: 'primary.main', fontSize: '2rem'}}/>
                    <Typography
                        variant="h5"
                        component={RouterLink}
                        to="/"
                        sx={{
                            flexGrow: 1,
                            fontWeight: 'bold',
                            color: 'inherit',
                            textDecoration: 'none',
                            letterSpacing: '0.5px',
                        }}
                    >
                        BookShelf
                    </Typography>
                    {isMobile ? (
                        <IconButton
                            color="inherit"
                            aria-label="open drawer"
                            edge="end"
                            onClick={handleDrawerToggle}
                        >
                            <MenuIcon/>
                        </IconButton>
                    ) : (
                        <Box component="nav" sx={{display: 'flex', alignItems: 'center', gap: 0.5}}>
                            {navItems.map((item) => (
                                <Button
                                    key={item.label}
                                    component={NavLink}
                                    to={item.path}
                                    style={({isActive}) => navLinkStyles({isActive})}
                                >
                                    {item.label}
                                </Button>
                            ))}
                            <Button
                                component={RouterLink}
                                to="/login"
                                variant="outlined"
                                sx={{ml: 2}}
                            >
                                Login
                            </Button>
                            <Button
                                component={RouterLink}
                                to="/register"
                                variant="contained"
                                sx={{ml: 1}}
                            >
                                Register
                            </Button>
                        </Box>
                    )}
                </Toolbar>
            </Container>
            {/* Mobile Drawer would go here if implementing full mobile nav */}
        </AppBar>
    );
};

export default Header;