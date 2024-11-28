import React, { useEffect, useState } from 'react';
import { useAuth } from '../AuthContext';
import '../css/ProfilePage.css';
import fetchInterceptor from '../utils/fetchInterceptor';
import { useNavigate } from 'react-router-dom';



const ProfilePage = () => {
    const { isAuthenticated } = useAuth();
    const { logout } = useAuth();
    const navigate = useNavigate();
    const [profileData, setProfileData] = useState({});
    const [username, setUsername] = useState('');


    useEffect(() => {
        const storedUsername = localStorage.getItem('username');
        setUsername(storedUsername);
        
        if (isAuthenticated) {
            // Fetch profile data from the backend
            fetchInterceptor('http://localhost:8080/api/users/profileInfo', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            },logout)
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
    }, []);



    return (
        <div className="profile-container">
            <h1>{username}</h1>
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
