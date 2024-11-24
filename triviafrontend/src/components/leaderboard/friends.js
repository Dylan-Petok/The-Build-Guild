import React, { useEffect, useState} from 'react';
import '../../css/Leaderboard.css';

const FriendsLeaderboard = () =>{
    const [leaderboard, setLeaderboard] = useState([]);
    const [error, setError] = useState(false);


    useEffect(() => {
        // Fetch leaderboard data from the backend
        fetch('http://localhost:8080/api/leaderboard/friends')
            .then(response => response.json())
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
    }, []);

    if(error){
        return <div className="leaderboard-container">Stats are currently unavailable</div>
    }

    return (
        <div className="leaderboard-container">
            <h2>Friends Leaderboard</h2>
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
                        <tr key={user.userId}>
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

export default FriendsLeaderboard;