// src/components/ResultsPage.js
import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../css/TriviaResults.css';

const ResultsPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { correctAnswers, totalQuestions } = location.state || { correctAnswers: 0, totalQuestions: 0 };
    const percentage = (correctAnswers / totalQuestions) * 100;

    const handlePlayAgain = () => {
        navigate('/play');
    };

    return (
        <div className="results-container">
            <h1>Quiz Results</h1>
            <p>You got {correctAnswers} out of {totalQuestions} questions right.</p>
            <p>Your score: {percentage.toFixed(2)}%</p>
            <button onClick={handlePlayAgain} className="play-again-btn">Play Again</button>
        </div>
    );
};

export default ResultsPage;