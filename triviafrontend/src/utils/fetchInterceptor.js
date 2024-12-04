// src/utils/fetchInterceptor.js
import { toast } from 'react-toastify';

const fetchInterceptor = async (url, options = {}, logout, navigate) => {

    try {
        const response = await fetch(url, options);

        if (response.status === 401) {
            // Handle unauthorized response
            toast.error('Session expired. Please log in again.');
            logout();
            navigate('/');
            return null;
        }

        return response;
    } catch (error) {
        console.error('Fetch error:', error);
        toast.error('Network error. Please try again later.');
        logout();
        navigate('/');
        return null;
    }
};

export default fetchInterceptor;