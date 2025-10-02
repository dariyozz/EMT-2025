import {Box, Typography, Button} from '@mui/material';
import {useNavigate} from 'react-router-dom';

const UnauthorizedPage = () => {
    const navigate = useNavigate();

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minHeight="100vh"
            textAlign="center"
            p={3}
        >
            <Typography variant="h1" color="error" gutterBottom>
                401
            </Typography>
            <Typography variant="h4" gutterBottom>
                Unauthorized Access
            </Typography>
            <Typography variant="body1" color="text.secondary" paragraph>
                Sorry, you don't have permission to access this page.
            </Typography>
            <Button
                variant="contained"
                color="primary"
                onClick={() => navigate('/')}
                sx={{mt: 2}}
            >
                Return to Home
            </Button>
        </Box>
    );
};

export default UnauthorizedPage;
