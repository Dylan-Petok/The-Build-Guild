import React, { useEffect, useState} from 'react';
import { useAuth } from '../../AuthContext';
import fetchInterceptor from '../../utils/fetchInterceptor';
import '../../css/Leaderboard.css';

const PersonalLeaderboard = () =>{
    const { logout } = useAuth();
    const [leaderboard, setLeaderboard] = useState([]);
    const [error, setError] = useState(false);


    useEffect(() => {
        const fetchLeaderboard = async () => {
            try {
                const response = await fetchInterceptor('http://localhost:8080/api/leaderboard/personal', {
                    method: 'GET',
                    credentials: 'include'
                }, logout);
                if (response) {
                    const data = await response.json();
                    if (Array.isArray(data)) {
                        setLeaderboard(data);
                    } else {
                        setError(true);
                    }
                } else {
                    setError(true);
                }
            } catch (error) {
                console.error('Error fetching leaderboard data:', error);
                setError(true);
            }
        };

        fetchLeaderboard();
    }, [logout]);

    if (error) {
        return <div className="leaderboard-container">Stats are currently unavailable</div>;
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

export default PersonalLeaderboard;