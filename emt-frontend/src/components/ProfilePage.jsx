import React, {useState, useEffect} from 'react';
import {
    Container,
    Paper,
    Typography,
    Grid,
    Avatar,
    Box,
    Divider,
    List,
    ListItem,
    ListItemText,
    CircularProgress
} from '@mui/material';
import {Person as PersonIcon} from '@mui/icons-material';
import api from '../api/api';

const ProfilePage = () => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await api.get('/users/profile');
                setUser(response.data);
                setLoading(false);
                // eslint-disable-next-line no-unused-vars
            } catch (err) {
                setError('Failed to load profile');
                setLoading(false);
            }
        };

        fetchUserProfile();
    }, []);

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
                <CircularProgress/>
            </Box>
        );
    }

    if (error) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
                <Typography color="error">{error}</Typography>
            </Box>
        );
    }

    return (
        <Container maxWidth="md" sx={{mt: 4, mb: 4}}>
            <Paper elevation={3} sx={{p: 3}}>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={4} sx={{textAlign: 'center'}}>
                        <Avatar
                            sx={{
                                width: 120,
                                height: 120,
                                margin: '0 auto',
                                bgcolor: 'primary.main'
                            }}
                        >
                            <PersonIcon sx={{fontSize: 60}}/>
                        </Avatar>
                    </Grid>
                    <Grid item xs={12} md={8}>
                        <Typography variant="h4" gutterBottom>
                            {user?.name} {user?.surname}
                        </Typography>
                        <Typography variant="body1" color="textSecondary" gutterBottom>
                            {user?.email}
                        </Typography>
                    </Grid>
                </Grid>

                <Divider sx={{my: 3}}/>

                <List>
                    <ListItem>
                        <ListItemText
                            primary="Username"
                            secondary={user?.username}
                        />
                    </ListItem>
                    <ListItem>
                        <ListItemText
                            primary="Role"
                            secondary={user?.role}
                        />
                    </ListItem>
                    <ListItem>
                        <ListItemText
                            primary="Member Since"
                            secondary={new Date(user?.createdAt).toLocaleDateString()}
                        />
                    </ListItem>
                </List>
            </Paper>
        </Container>
    );
};

export default ProfilePage;
