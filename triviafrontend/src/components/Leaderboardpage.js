import React from 'react';
import '../css/Leaderboard.css';

const Leaderboard = () => {
    return (
        <div className="leaderboard-container">
            <header className="leaderboard-header">
                <h1>Leaderboard</h1>
                <p className="tagline">See where you rank among players!</p>
            </header>

            <section className="leaderboard-content">
                <div className="leaderboard-box all-time-board">
                    <h2>All-Time Leaderboard</h2>
                    <p>Top players of all time!</p>
                </div>

                <div className="leaderboard-box personal-best-board">
                    <h2>Personal Best</h2>
                    <p>Your highest scores and rankings.</p>
                </div>

                <div className="leaderboard-box friends-board">
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
