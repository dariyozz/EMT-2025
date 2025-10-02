import { styled } from '@mui/material/styles';
import { Box, Paper, Container } from '@mui/material';

export const PageContainer = styled(Container)(({ theme }) => ({
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4),
}));

export const ContentWrapper = styled(Paper)(({ theme }) => ({
    padding: theme.spacing(3),
    borderRadius: theme.shape.borderRadius * 2,
    backgroundColor: 'rgba(255, 255, 255, 0.8)',
    backdropFilter: 'blur(10px)',
    boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.07)',
}));

export const HeaderBox = styled(Box)(({ theme }) => ({
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: theme.spacing(4),
    [theme.breakpoints.down('sm')]: {
        flexDirection: 'column',
        gap: theme.spacing(2),
    },
}));

export const ActionButton = styled(Box)(({ theme }) => ({
    '& .MuiButton-root': {
        borderRadius: theme.shape.borderRadius * 1.5,
        padding: theme.spacing(1, 3),
        transition: 'all 0.3s ease',
        '&:hover': {
            transform: 'translateY(-2px)',
            boxShadow: '0 5px 15px rgba(0,0,0,0.1)',
        },
    },
}));