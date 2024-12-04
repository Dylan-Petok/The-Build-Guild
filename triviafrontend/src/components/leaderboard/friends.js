import React, { useEffect, useState } from 'react';
import { useAuth } from '../../AuthContext';
import '../../css/Leaderboard.css';

const FriendsLeaderboard = () => {
    const { logout } = useAuth();
    const [leaderboard, setLeaderboard] = useState([]);
    const [error, setError] = useState(false);

    useEffect(() => {
        // Fetch leaderboard data for friends
        fetch('http://localhost:8080/api/users/leaderboard/friends', {
            method: 'GET',
            credentials: 'include', 
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch leaderboard');
                }
                return response.json();
            })
            .then(data => {
                if (Array.isArray(data)) {
                    setLeaderboard(data);
                } else {
                    setError(true);
                }
            })
            .catch(error => {
                console.error('Error fetching leaderboard data:', error);
                setError(true);
            });
    }, [logout]); 

    if (error) {
        return <div className="leaderboard-container">Stats are currently unavailable.</div>;
    }

    if (leaderboard.length === 0) {
        return <div className="leaderboard-container">No friends to display.</div>;
    }

    return (
        <div className="leaderboard-container">
            <h2>Friends Leaderboard</h2>
            <ul>
                {leaderboard.map((friend, index) => (
                    <li key={index}>
                        {friend.username} - {friend.score} points
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default FriendsLeaderboard;
