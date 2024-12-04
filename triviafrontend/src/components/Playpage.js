import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import fetchInterceptor from '../utils/fetchInterceptor';
import '../css/Playpage.css';

const Playpage = () => {
    const [formData, setFormData] = useState({
        numberOfQuestions: '',
        category: '',
        difficulty: ''
    });
    const { logout } = useAuth();


    const [categories, setCategories] = useState([]);
    const [questions, setQuestions] = useState([]);
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
    const [selectedOption, setSelectedOption] = useState('');
    const [correctAnswers, setCorrectAnswers] = useState(0);
    const [feedback, setFeedback] = useState('');
    const [isCorrect, setIsCorrect] = useState(null);
    const [correctAnswer, setCorrectAnswer] = useState(''); 
    const [isSubmitDisabled, setIsSubmitDisabled] = useState(false);
    const [quizStarted, setQuizStarted] = useState(false); 
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch categories from the Open Trivia API when the component mounts
        fetch('https://opentdb.com/api_category.php')
            .then(response => response.json())
            .then(data => setCategories(data.trivia_categories))
            .catch(error => console.error('Error fetching categories:', error));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const username = localStorage.getItem('username');
        console.log('Username:', username);
        console.log('Form submitted:', formData);
       
        fetchInterceptor('http://localhost:8080/api/trivia/play', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData),
            credentials: 'include'
        }, logout, navigate)
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
              
                const processedQuestions = data.results.map(result => {
                    const options = [...result.incorrect_answers, result.correct_answer].map(option => decodeURIComponent(option));
                    return {
                        ...result,
                        question: decodeURIComponent(result.question),
                        options: options.sort(() => Math.random() - 0.5) 
                    };
                });
                setQuestions(processedQuestions);
                setQuizStarted(true);
            })
            .catch((error) => {
                console.error('Error:', error);
             
            });
    };

    const handleOptionChange = (e) => {
        setSelectedOption(e.target.value);
    };

    const handleNextQuestion = () => {
        let scoreValue = 0;

        if (formData.difficulty === "easy") {
            scoreValue = 10;
        } else if (formData.difficulty === "medium") {
            scoreValue = 20;
        } else {
            scoreValue = 30;
        }
    
        const correctAnswer = decodeURIComponent(questions[currentQuestionIndex].correct_answer);
        setCorrectAnswer(correctAnswer); 
    
        let isAnswerCorrect = decodeURIComponent(selectedOption) === correctAnswer;
        if (isAnswerCorrect) {
            setCorrectAnswers(prevCorrectAnswers => prevCorrectAnswers + 1);
            setFeedback('Correct!'); 
            setIsCorrect(true); 
        } else {
            setFeedback('Incorrect!'); 
            setIsCorrect(false); 
        }
        setIsSubmitDisabled(true);
    
        setTimeout(() => {
            if (currentQuestionIndex < questions.length - 1) {
                setCurrentQuestionIndex(currentQuestionIndex + 1);
                setSelectedOption('');
                setFeedback(''); // Clear feedback
                setIsCorrect(null); // Reset isCorrect
                setCorrectAnswer(''); // Clear correct answer
                setIsSubmitDisabled(false);
            } else {
                console.log('Quiz completed');

                const finalCorrectAnswers = isAnswerCorrect ? correctAnswers + 1 : correctAnswers;
                const gameScore = finalCorrectAnswers * scoreValue;
                const gameData = {
                    userId: localStorage.getItem('username'), // Retrieve username from local storage
                    topic: formData.category,
                    difficulty: formData.difficulty,
                    correctAnswers: finalCorrectAnswers,
                    totalQuestions: questions.length,
                    gameScore: gameScore,
                    datePlayed: new Date()
                };
                console.log(gameData);

                // Send game data to the backend
                fetchInterceptor('http://localhost:8080/api/trivia/saveGame', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(gameData),
                    credentials: 'include'
                }, logout, navigate)
                .then(response => response.json())
                .then(data => {
                    console.log('Game data saved:', data);
                    // Handle success response
                })
                .catch(error => {
                    console.error('Error saving game data:', error);
                    // Handle error response
                });

            // Navigate to the results page
            navigate('/results', { state: { finalCorrectAnswers, totalQuestions: questions.length } });
        }
    }, 3000); // Delay for 2 seconds
};
   

    return (
        <div className="playpage-container">
            {!quizStarted ? (
                <>
            <h1>Quiz Settings</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="numberOfQuestions">Number of Questions:</label>
                    <input
                        type="number"
                        id="numberOfQuestions"
                        name="numberOfQuestions"
                        value={formData.numberOfQuestions}
                        onChange={handleChange}
                        min="1"
                        max="50"
                        required
                    />
                </div>
                <div>
                    <label htmlFor="category">Category:</label>
                    <select
                        id="category"
                        name="category"
                        value={formData.category}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select Category</option>
                        {categories.map(category => (
                            <option key={category.id} value={category.id}>{category.name}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label htmlFor="difficulty">Difficulty:</label>
                    <select
                        id="difficulty"
                        name="difficulty"
                        value={formData.difficulty}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Select Difficulty</option>
                        <option value="easy">Easy</option>
                        <option value="medium">Medium</option>
                        <option value="hard">Hard</option>
                    </select>
                </div>
                <button type="submit">Submit</button>
            </form>
            </>
        ) : (
                <div className="quiz-container">
                    <h2>Question {currentQuestionIndex + 1}</h2>
                    <p  className="question-text">{questions[currentQuestionIndex].question}</p>
                    {feedback && (
                        <p className={isCorrect ? 'correct' : 'incorrect'}>{feedback}</p> // Display feedback message
                    )}
                    <div className="answer-total-container">
                    {questions[currentQuestionIndex].options.map((option, index) => (
                        <div
                        className={`answer-single-container ${isCorrect === false && option === correctAnswer ? 'correct-answer' : ''}`}
                        key={index}
                        >
                            <input
                                type="radio"
                                id={`option${index}`}
                                name="option"
                                value={option}
                                checked={selectedOption === option}
                                onChange={handleOptionChange}
                            />
                                <label htmlFor={`option${index}`}>{option}</label>
                            </div>
                    ))}
                    </div>
                    <button className="nextButton" onClick={handleNextQuestion} disabled={isSubmitDisabled}>Next</button>
                </div>
            )}
        </div>
    );
};

export default Playpage;