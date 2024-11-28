import React, { useEffect, useState } from 'react';
import '../../css/Leaderboard.css';

const PersonalLeaderboard = ({ username }) => {
    const [leaderboard, setLeaderboard] = useState([]);
    const [error, setError] = useState(false);

    useEffect(() => {
        if (!username) {
            console.error("No username provided to PersonalLeaderboard");
            setError(true);
            return;
        }
        console.log(`Fetching leaderboard for username: ${username}`);
        fetch(`http://localhost:8080/api/leaderboard/personal?username=${username}`, {
            method: 'GET',
            credentials: 'include', // Include credentials with the request
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    console.error(`Error fetching leaderboard: ${response.statusText}`);
                    throw new Error('Failed to fetch');
                }
                return response.json();
            })
            .then(data => {
                console.log("Leaderboard data:", data);
                setLeaderboard(data);
            })
            .catch(err => {
                console.error("Error occurred:", err);
                setError(true);
            });
    }, [username]);

    if (error) {
        return <div className="leaderboard-container">Stats are currently unavailable.</div>;
    }

    if (leaderboard.length === 0) {
        return <div className="leaderboard-container">No personal best records found.</div>;
    }


    return (
        <div className="leaderboard-container">
            <h2>Personal Best Leaderboard</h2>
            <table className="leaderboard-table">
                <thead>
                    <tr>
                        <th>Rank</th>
                        <th>Score</th>
                        <th>Date Played</th>
                        <th>Difficulty</th>
                    </tr>
                </thead>
                <tbody>
                    {leaderboard.map((game, index) => (
                        <tr key={index}>
                            <td>{index + 1}</td>
                            <td>{game.gameScore}</td>
                            <td>{new Date(game.datePlayed).toLocaleDateString()}</td>
                            <td>{game.difficulty}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default PersonalLeaderboard;
