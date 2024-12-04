import React, { useEffect, useState } from 'react';
import { useAuth } from '../../AuthContext';
import '../../css/Leaderboard.css';

const AllTimeLeaderboard = () =>{
    const { logout } = useAuth();
    const [leaderboard, setLeaderboard] = useState([]);


    useEffect(() => {
        // Fetch leaderboard data from the backend with credentials
        fetch('http://localhost:8080/api/leaderboard/allTime', {
            method: 'GET',
            credentials: 'include', // Include credentials with the request
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => setLeaderboard(data))
            .catch(error => console.error('Error fetching leaderboard data:', error));
    }, [logout]);

    return (
        <div className="leaderboard-container">
            <h2>Alltime Leaderboard</h2>
            <table className="leaderboard-table">
                <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Username</th>
                        <th>Score</th>
                    </tr>
                </thead>
                <tbody>
                    {leaderboard.map((user, index) => (
                        <tr key={user.username}>
                            <td>{index + 1}</td>
                            <td>{user.username}</td>
                            <td>{user.score}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AllTimeLeaderboard;