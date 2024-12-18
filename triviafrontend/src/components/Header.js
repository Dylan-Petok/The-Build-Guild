import React, { useState, useRef, useEffect } from 'react';
import { useAuth } from '../AuthContext';
import { useNavigate, NavLink } from 'react-router-dom';
import { toast } from 'react-toastify';
import fetchInterceptor from '../utils/fetchInterceptor';
import '../css/Header.css';
import '@fortawesome/fontawesome-free/css/all.min.css'; 

function Header() {
    const { isAuthenticated, logout, addFriend, deleteFriend, friendsList } = useAuth();
    const navigate = useNavigate();
    const [searchInput, setSearchInput] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const searchContainerRef = useRef(null);


    const handleLogout = () => {
        fetchInterceptor('http://localhost:8080/api/users/logout', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        }, logout, navigate).then(response => {
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

    const handleSearchChange = async (e) => {
        const query = e.target.value;
        setSearchInput(query);

        if (query.length > 0) {
            try {
                const response = await fetchInterceptor(`http://localhost:8080/api/users/search?query=${query}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include'
                }, logout, navigate);
                const data = await response.json();
                setSearchResults(data); 
            } catch (error) {
                console.error('Error fetching search results:', error);
            }
        } else {
            setSearchResults([]);
        }
    };

    
    const handleAddFriend = async (username) => {
        try {
            const response = await addFriend(username);
            if (response.ok) {
                toast.success(`User: ${username} added!`);
            } else {
                const errorData = await response.json();
                toast.error(`Failed to send friend request: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error sending friend request:', error);
            toast.error('Failed to send friend request.');
        }
    };
    
    const handleDeleteFriend = async (username) => {
        try {
            const response = await deleteFriend(username);
            if (response.ok) {
                toast.success(`Removed ${username} from friends!`);
            } else {
                const errorData = await response.json();
                toast.error(`Failed to remove friend: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error removing friend:', error);
            toast.error('Failed to remove friend.');
        }
    };

    // this is to clear the search bar characters when a user clicks out of the search bar
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (searchContainerRef.current && !searchContainerRef.current.contains(event.target)) {
                setSearchInput('');
                setSearchResults([]);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);


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
        {/* ---------------------------- search bar  -------------------------------------- */}
        <li className="navbar-item search-bar">
                                <div className="search-container" ref={searchContainerRef}>
                                    <input 
                                        type="text" 
                                        placeholder="Add a friend" 
                                        className="search-input" 
                                        value={searchInput}
                                        onChange={handleSearchChange}
                                    />
                                    <i className="fas fa-search search-icon"></i>
                                    {searchResults.length > 0 && (
                                        <ul className="search-results">
                                            {searchResults.map((user, index) => (
                                                <li key={index} className="search-result-item">
                                                    <span>{user.username}</span>
                                                    {friendsList.includes(user.username) ? (
                                                        <button onClick={() => handleDeleteFriend(user.username)} className="delete-friend-button">
                                                            <i className="fas fa-user-minus"></i>
                                                        </button>
                                                    ) : (
                                                        <button onClick={() => handleAddFriend(user.username)} className="add-friend-button">
                                                            <i className="fas fa-user-plus"></i>
                                                        </button>
                                                    )}
                                                </li>
                                            ))}
                                        </ul>
                                    )}
                                </div>
                            </li>
{/* ------------------------------- search bar ----------------------------------- */}
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
