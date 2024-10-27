import React from 'react';
import '../css/Header.css';

function Header() {
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
                        <li className="navbar-item">
                            <a href="/signin" className="navbar-link">Sign-In</a>
                        </li>
                        <li className="navbar-item">
                            <a href="/signup" className="navbar-link">Sign-Up</a>
                        </li>
                    </div>
                </ul>
            </nav>
        </header>
    )
}

export default Header;
