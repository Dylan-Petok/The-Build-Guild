import React, { useEffect } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { toast } from 'react-toastify';

const PrivateRoute = ({ children }) => {
    const { isAuthenticated } = useAuth();
    const location = useLocation();

    useEffect(() => {
        if (!isAuthenticated) {
            toast.error('You must be logged in to access this page.');
        }
    }, [isAuthenticated, location.pathname]);

    if (!isAuthenticated) {
        return <Navigate to="/signin" />;
    }

    return children;
};

export default PrivateRoute;