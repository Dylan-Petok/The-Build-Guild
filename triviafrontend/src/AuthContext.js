import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const storedAuthState = localStorage.getItem('isAuthenticated');
        if (storedAuthState === 'true'){
            setIsAuthenticated(true);
        }
    }, []);

    const login = () => {
        setIsAuthenticated(true);
        localStorage.setItem('isAuthenticated', 'true'); //save authentication state to storage
    };

    const logout = (callback) => {
        setIsAuthenticated(false);
        localStorage.removeItem('isAuthenticated'); //remove authentication state from storage
        if (callback) callback();
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);