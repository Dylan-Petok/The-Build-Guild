import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../css/Leaderboard.css';

const Leaderboard = () => {
    const navigate = useNavigate();
    
    const handleFriendsLeaderboardClick = () => {
        navigate('/leaderboard/friends');
    };

    const handlePersonalLeaderboardClick = () => {
        navigate('/leaderboard/personal');
    };

    const handleAllTimeLeaderboardClick = () => {
        navigate('/leaderboard/alltime');
    };

    return (
        <div className="leaderboard-container">
            <header className="leaderboard-header">
                <h1>Leaderboard</h1>
                <p className="tagline">See where you rank among players!</p>
            </header>

            <section className="leaderboard-content">
                <div className="leaderboard-box all-time-board"  onClick={handleAllTimeLeaderboardClick}>
                    <h2>All-Time Leaderboard</h2>
                    <p>Top players of all time!</p>
                </div>

                <div className="leaderboard-box personal-best-board" onClick={handlePersonalLeaderboardClick}>
                    <h2>Personal Best</h2>
                    <p>Your highest scores and rankings.</p>
                </div>

                <div className="leaderboard-box friends-board" onClick={handleFriendsLeaderboardClick}>
                    <h2>Friends Leaderboard</h2>
                    <p>Compete with your friends and see who's on top!</p>
                </div>
            </section>

            <footer className="leaderboard-footer">
                <p>Keep playing and improve your ranking!</p>
            </footer>
        </div>
    );
};

export default Leaderboard;
