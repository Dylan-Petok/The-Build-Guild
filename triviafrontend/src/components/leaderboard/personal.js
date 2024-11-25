import React, { useEffect, useState} from 'react';
import '../../css/Leaderboard.css';

const PersonalLeaderboard = () =>{
    const [leaderboard, setLeaderboard] = useState([]);
<<<<<<< Updated upstream
    const [error, setError] = useState(false);

=======
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
>>>>>>> Stashed changes


    useEffect(() => {
        // Fetch leaderboard data from the backend
<<<<<<< Updated upstream
        fetch('http://localhost:8080/api/leaderboard/personal')
            .then(response => response.json())
            .then(data => {
                if (Array.isArray(data)) {
                    setLeaderboard(data);
                } else {
                    setError(true);
                }
            })            .catch(error => {
                console.error('Error fetching leaderboard data:', error)
                setError(true);
    });
    }, []);

    if(error){
        return <div className="leaderboard-container">Stats are currently unavailable</div>
=======
        const fetchLeaderboard = async () => {
            try {
                setLoading(true);
                const response = await fetch('http://localhost:8080/api/users/leaderboard/personal', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        'Content-Type': 'application/json'
                    },
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch leaderboard data');
                }

                const data = await response.json();
                setLeaderboard(data);
            } catch (error) {
                console.error('Error fetching leaderboard data:', error);
                setError('Failed to load leaderboard. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchLeaderboard();
    }, []);

    if (loading) {
        return <div className="leaderboard-container">Loading...</div>;
    }

    if (error) {
        return <div className="leaderboard-container error">{error}</div>;
>>>>>>> Stashed changes
    }

    return (
        <div className="leaderboard-container">
            <h2>Personal Leaderboard</h2>
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
                        <tr key={user.userId} className={user.username === localStorage.getItem('username') ? 'current-user' : ''}>
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

export default PersonalLeaderboard;