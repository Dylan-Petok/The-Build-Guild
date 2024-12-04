import React, { useEffect, useState } from 'react';
import { useAuth } from '../AuthContext';
import '../css/ProfilePage.css';
import fetchInterceptor from '../utils/fetchInterceptor';

const ProfilePage = () => {
    const { isAuthenticated, logout } = useAuth();
    const [profileData, setProfileData] = useState({
        totalGamesPlayed: 'N/A',
        highestScore: 'N/A',
        globalRank: 'N/A',
        currentScore: 'N/A',
    });
    const [username, setUsername] = useState('');

    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        setUsername(storedUsername);

        if (isAuthenticated) {
            fetchInterceptor('http://localhost:8080/api/users/profileInfo', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            }, logout)
                .then(response => response.json())
                .then(data => {
                    console.log('Fetched Profile Data:', data); // Debugging API response
                    setProfileData({
                        totalGamesPlayed: data.gamesPlayedCount ?? 'N/A',
                        highestScore: data.highestScoreInGame ?? 'N/A',
                        globalRank: data.globalRank ?? 'N/A',
                        currentScore: data.currentScore ?? 'N/A',
                    });
                })
                .catch(error => console.error('Error fetching profile data:', error));
        }
    }, [isAuthenticated, logout]);

    return (
        <div className="profile-container">
            <h1>{username}</h1>
            <div className="profile-info">
                <p>
                    <strong>Total Games Played:</strong> {profileData.totalGamesPlayed}
                </p>
                <p>
                    <strong>Highest Score in a Game:</strong> {profileData.highestScore}
                </p>
                <p>
                    <strong>Global Rank:</strong> {profileData.globalRank}
                </p>
                <p>
                    <strong>Current Score:</strong> {profileData.currentScore}
                </p>
            </div>

            {/* Achievements Section */}
            <div className="profile-achievements">
                <h2>Achievements</h2>
                <div className="achievement-badge">🏆 Top Scorer</div>
                <div className="achievement-badge">🎮 100 Games Played</div>
            </div>
        </div>
    );
};

export default ProfilePage;