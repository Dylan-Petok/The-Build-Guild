import React from 'react';
import { useAuth } from '../AuthContext';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import '../css/Header.css';

function Header() {
    const { isAuthenticated, logout } = useAuth();
    const navigate = useNavigate();


    const handleLogout = () => {
        fetch('http://localhost:8080/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }).then(response => {
            if (response.ok) {
                logout(() => {
                    toast.success('Successfully logged out!');
                    navigate('/');
                });
            } else {
                console.error('Logout failed');
                toast.error('Logout failed');
            }
        }).catch(error => {
            console.error('Error:', error);
            toast.error('An error occurred. Please try again.');
        });
    };

    return (
        <header className='App-header'>
            <nav className="navbar">
                <ul className="navbar-list">
                    <li className="navbar-item">
                        <a href="/" className="navbar-link">Home</a>
                    </li>
                    <li className="navbar-item">
                        <a href="/play" className="navbar-link">Play</a>
                    </li>
                    <li className="navbar-item">
                        <a href="/leaderboard" className="navbar-link">Leaderboard</a>
                    </li>
                    
                    <div className="auth-item">
                    {isAuthenticated ? (
                        <>
                            <li className="navbar-item">
                                <a href="/profile" className="navbar-link">Profile</a>
                            </li>
                            
                            <li className="navbar-item">
                                <button onClick={handleLogout} className="navbar-link logout">Logout</button>
                            </li>
                        </>
                        ) : (
                            <>
                                <li className="navbar-item">
                                    <a href="/signin" className="navbar-link">Sign-In</a>
                                </li>
                                <li className="navbar-item">
                                    <a href="/signup" className="navbar-link">Sign-Up</a>
                                </li>
                            </>
                        )}
                    </div>
                </ul>
            </nav>
        </header>
    )
}

export default Header;
