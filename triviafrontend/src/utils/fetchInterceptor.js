// src/utils/fetchInterceptor.js
import { toast } from 'react-toastify';

const fetchInterceptor = async (url, options = {}, navigate) => {

    try {
        const response = await fetch(url, options);

        if (response.status === 401) {
            // Handle unauthorized response
            toast.error('Session expired. Please log in again.');
            localStorage.removeItem('isAuthenticated');
            localStorage.removeItem('friendsList');
            localStorage.removeItem('username');
            navigate('/signin');
            return null;
        }

        return response;
    } catch (error) {
        console.error('Fetch error:', error);
        throw error;
    }
};

export default fetchInterceptor;