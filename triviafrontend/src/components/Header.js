import React from 'react';
import { useAuth } from '../AuthContext';
import { useNavigate, NavLink } from 'react-router-dom';
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
                        <NavLink to="/" className="navbar-link" activeClassName="active">Home</NavLink>
                    </li>
                    <li className="navbar-item">
                        <NavLink to="/play" className="navbar-link" activeClassName="active">Play</NavLink>
                    </li>
                    <li className="navbar-item">
                        <NavLink to="/leaderboard" className="navbar-link" activeClassName="active">Leaderboard</NavLink>
                    </li>
                    
                    <div className="auth-item">
                    {isAuthenticated ? (
                        <>
                            <li className="navbar-item">
                                <NavLink to="/profile" className="navbar-link" activeClassName="active">Profile</NavLink>
                            </li>
                            
                            <li className="navbar-item">
                                <button onClick={handleLogout} className="navbar-link logout">Logout</button>
                            </li>
                        </>
                        ) : (
                            <>
                                <li className="navbar-item">
                                    <NavLink to="/signin" className="navbar-link" activeClassName="active">Sign-In</NavLink>
                                </li>
                                <li className="navbar-item">
                                    <NavLink to="/signup" className="navbar-link" activeClassName="active">Sign-Up</NavLink>
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
