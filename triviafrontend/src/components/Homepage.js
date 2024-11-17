import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/HomePage.css';

const HomePage = () => {
    const navigate = useNavigate();

    const handleStartGame = () => {
        navigate('/play');
    };

    return (
        <div className="homepage-container">
            <header className="homepage-header">
                <h1>Welcome to Trivia Trove!</h1>
                <p className="tagline">Test your knowledge and climb the leaderboard!</p>
            </header>

            <section className="homepage-content">
                <div className="intro-section">
                    <h2>How to Play</h2>
                    <p>
                        Think you know it all? Click on the 'Start Game' button and answer trivia questions from various categories. 
                        The faster you answer, the more points you earn!
                    </p>
                    <button className="start-game-btn" onClick={handleStartGame}>Start Game</button>
                </div>

                <div className="features-section">
                    <div className="feature-box">
                        <h3>Multiple Categories</h3>
                        <p>Choose from sports, science, history, and more to test your knowledge!</p>
                    </div>
                    <div className="feature-box">
                        <h3>Real-Time Leaderboard</h3>
                        <p>Compete against other players and see your name rise to the top of the leaderboard!</p>
                    </div>
                    <div className="feature-box">
                        <h3>Track Your Progress</h3>
                        <p>Review your game history and track your personal best scores.</p>
                    </div>
                </div>
            </section>

            <footer className="homepage-footer">
                <p>Good luck, and may the best trivia master win!</p>
            </footer>
        </div>
    );
};

export default HomePage;
