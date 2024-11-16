import React, { useState, useEffect } from 'react';
import '../css/Playpage.css';

const Playpage = () => {
  const [formData, setFormData] = useState({
    numberOfQuestions: '',
    category: '',
    difficulty: '',
  });

  const [categories, setCategories] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedOption, setSelectedOption] = useState('');

  // Fetch categories from the Open Trivia API when the component mounts
  useEffect(() => {
    fetch('https://opentdb.com/api_category.php')
      .then((response) => response.json())
      .then((data) => setCategories(data.trivia_categories))
      .catch((error) => console.error('Error fetching categories:', error));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted:', formData);

    // Send form data to the backend
    fetch('http://localhost:8080/api/trivia/play', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(formData),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log('Success:', data);

        // Process questions from API response
        const processedQuestions = data.results.map((result) => {
          const options = [...result.incorrect_answers, result.correct_answer].map(option =>
            decodeURIComponent(option)
          );
          return {
            ...result,
            question: decodeURIComponent(result.question),
            options: options.sort(() => Math.random() - 0.5), // Shuffle options
          };
        });
        setQuestions(processedQuestions);
      })
      .catch((error) => console.error('Error:', error));
  };

  const handleOptionChange = (e) => {
    setSelectedOption(e.target.value);
  };

  const handleNextQuestion = () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
      setSelectedOption('');
    } else {
      console.log('Quiz completed');
    }
  };

  return (
    <div className="playpage-container">
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
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
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

      {questions.length > 0 && (
        <div className="play-question-container">
          <h2>Question {currentQuestionIndex + 1}</h2>
          <p className="question-text">{questions[currentQuestionIndex].question}</p>
          <div className="play-options">
            {questions[currentQuestionIndex].options.map((option, index) => (
              <div className="play-option" key={index}>
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
          <button className="next-button" onClick={handleNextQuestion}>
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default Playpage;
