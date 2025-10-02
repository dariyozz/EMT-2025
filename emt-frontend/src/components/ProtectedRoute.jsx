// src/components/ProtectedRoute.jsx
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { CircularProgress, Box } from '@mui/material';
import useAuth from '../hooks/useAuth';
import { useEffect, useState } from 'react';

const ProtectedRoute = ({ requiredRoles }) => {
    const { user, isAuthenticated } = useAuth();
    const location = useLocation();
    const [isVerifying, setIsVerifying] = useState(true);
    const [hasAccess, setHasAccess] = useState(false);

    useEffect(() => {
        const verifyAccess = () => {
            // If no authentication is required, grant access
            if (!requiredRoles) {
                setHasAccess(isAuthenticated);
                setIsVerifying(false);
                return;
            }

            // If authentication is required but user is not authenticated
            if (!isAuthenticated || !user) {
                setHasAccess(false);
                setIsVerifying(false);
                return;
            }

            // If roles are required, verify user has required role
            if (requiredRoles && user.roles) {
                const hasRequiredRole = requiredRoles.some(role => 
                    user.roles.includes(role)
                );
                setHasAccess(hasRequiredRole);
            } else {
                setHasAccess(false);
            }
            
            setIsVerifying(false);

        };

        verifyAccess();
    }, [isAuthenticated, user, requiredRoles]);

    // Show loading state while verifying
    if (isVerifying) {
        return (
            <Box 
                display="flex" 
                justifyContent="center" 
                alignItems="center" 
                minHeight="100vh"
            >
                <CircularProgress />
            </Box>
        );
    }

    // If not authenticated, redirect to login with return path
    if (!isAuthenticated) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    // If roles are required but user doesn't have access
    if (requiredRoles && !hasAccess) {
        return <Navigate to="/unauthorized" replace />;
    }

    // If all checks pass, render the protected content
    return <Outlet />;
};

export default ProtectedRoute;