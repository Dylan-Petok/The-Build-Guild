import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [friendsList, setFriendsList] = useState([]);
    const [username, setUsername] = useState('');

    useEffect(() => {
        const storedAuthState = localStorage.getItem('isAuthenticated');
        const storedFriendsList = localStorage.getItem('friendsList');
        const storedUsername = localStorage.getItem('username') || '';

        if (storedAuthState === 'true'){
            setIsAuthenticated(true);
            setFriendsList(storedFriendsList ? JSON.parse(storedFriendsList) : []);
            setUsername(storedUsername);
        } else {
            setIsAuthenticated(false);
        }
    }, []);

    const login = () => {
        setIsAuthenticated(true);
        setUsername(username);
        localStorage.setItem('isAuthenticated', 'true');
    };

    const logout = (callback) => {
        setIsAuthenticated(false);
        setFriendsList([]);
        setUsername('');
        localStorage.removeItem('isAuthenticated');
        localStorage.removeItem('friendsList');
        localStorage.removeItem('username');
        if (callback) callback();
    };

    const addFriend = async (username) => {
        try {
            const response = await fetch('http://localhost:8080/api/users/addFriend', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username }),
                credentials: 'include'
            });
    
            if (response.ok) {
                const data = await response.json();
                if (data.friendsList) {
                    setFriendsList(data.friendsList);
                    localStorage.setItem('friendsList', JSON.stringify(data.friendsList));
                    return { ok: true };
                } else {
                    console.error('Invalid response format:', data);
                    return { ok: false, message: 'Invalid response format' };
                }
            } else {
                const errorData = await response.json();
                console.error('Failed to add friend:', errorData);
                return { ok: false, message: errorData.message };
            }
        } catch (error) {
            console.error('Error adding friend:', error);
            return { ok: false, message: 'Error adding friend' };
        }
    };
    
    const deleteFriend = async (username) => {
        try {
            const response = await fetch('http://localhost:8080/api/users/deleteFriend', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username }),
                credentials: 'include'
            });

            if (response.ok) {
                const data = await response.json();
                setFriendsList(data.friendsList);
                localStorage.setItem('friendsList', JSON.stringify(data.friendsList));
                return{ok : true};
            } else {
                console.error('Failed to delete friend');
            }
        } catch (error) {
            console.error('Error deleting friend:', error);
        }
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, friendsList, login, logout, addFriend, deleteFriend }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);