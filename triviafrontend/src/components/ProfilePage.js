import React, { useEffect, useState } from 'react';
import { useAuth } from '../AuthContext';
import '../css/ProfilePage.css';

const ProfilePage = () => {
    const { isAuthenticated } = useAuth();
    const [profileData, setProfileData] = useState({
        totalGamesPlayed: 115,
        highestScore: 100,
        globalRank: 1,
        currentScore: 700
    });

    useEffect(() => {
        if (isAuthenticated) {
            // Fetch profile data from the backend
            fetch('http://localhost:8080/api/users/profileInfo', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}` 
                }
            })
                .then(response => response.json())
                .then(data => {
                    setProfileData({
                        totalGamesPlayed: data.totalGamesPlayed,
                        highestScore: data.highestScore,
                        globalRank: data.globalRank,
                        currentScore: data.currentScore
                    });
                })
                .catch(error => console.error('Error fetching profile data:', error));
        }
    }, [isAuthenticated]);

    return (
        <div className="profile-container">
            <h1>Profile Page</h1>
            <div className="profile-info">
                <p><strong>Total Games Played:</strong> {profileData.totalGamesPlayed}</p>
                <p><strong>Highest Score in a Game:</strong> {profileData.highestScore}</p>
                <p><strong>Global Rank:</strong> {profileData.globalRank}</p>
                <p><strong>Current Score:</strong> {profileData.currentScore}</p>
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
