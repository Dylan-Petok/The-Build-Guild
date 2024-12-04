import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import Playpage from '../components/Playpage';
import { MemoryRouter } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

jest.mock('../AuthContext', () => ({
  useAuth: jest.fn(),
}));

describe('Playpage Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    useAuth.mockReturnValue({
      logout: jest.fn(),
    });
  });

  test('renders without crashing', () => {
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve({ trivia_categories: [] }),
      })
    );

    render(
      <MemoryRouter>
        <Playpage />
      </MemoryRouter>
    );
  });

  test('fetches categories and displays them', async () => {
    const mockCategories = {
      trivia_categories: [
        { id: 9, name: 'General Knowledge' },
        { id: 10, name: 'Entertainment: Books' },
      ],
    };

    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockCategories),
      })
    );

    render(
      <MemoryRouter>
        <Playpage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('General Knowledge')).toBeInTheDocument();
      expect(screen.getByText('Entertainment: Books')).toBeInTheDocument();
    });
  });

  test('submits form and displays the first question', async () => {
    const mockCategories = {
      trivia_categories: [{ id: 9, name: 'General Knowledge' }],
    };

    const mockQuestionsResponse = {
      results: [
        {
          category: 'General Knowledge',
          type: 'multiple',
          difficulty: 'easy',
          question: encodeURIComponent('What is the capital of France?'),
          correct_answer: encodeURIComponent('Paris'),
          incorrect_answers: [
            encodeURIComponent('London'),
            encodeURIComponent('Berlin'),
            encodeURIComponent('Madrid'),
          ],
        },
      ],
    };

    global.fetch = jest.fn((url) => {
      if (url === 'https://opentdb.com/api_category.php') {
        return Promise.resolve({
          json: () => Promise.resolve(mockCategories),
        });
      } else if (url === 'http://localhost:8080/api/trivia/play') {
        return Promise.resolve({
          json: () => Promise.resolve(mockQuestionsResponse),
        });
      }
      return Promise.reject(new Error('Unknown URL'));
    });

    render(
      <MemoryRouter>
        <Playpage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('General Knowledge')).toBeInTheDocument();
    });

    fireEvent.change(screen.getByLabelText('Number of Questions:'), {
      target: { value: '1' },
    });
    fireEvent.change(screen.getByLabelText('Category:'), {
      target: { value: '9' },
    });
    fireEvent.change(screen.getByLabelText('Difficulty:'), {
      target: { value: 'easy' },
    });

    fireEvent.click(screen.getByText('Submit'));

    await waitFor(() => {
      expect(screen.getByText('Question 1')).toBeInTheDocument();
      expect(screen.getByText('What is the capital of France?')).toBeInTheDocument();
    });

    expect(screen.getByLabelText('Paris')).toBeInTheDocument();
    expect(screen.getByLabelText('London')).toBeInTheDocument();
    expect(screen.getByLabelText('Berlin')).toBeInTheDocument();
    expect(screen.getByLabelText('Madrid')).toBeInTheDocument();
  });

  test('selecting the correct option shows feedback and navigates to results', async () => {
    jest.useFakeTimers();

    const mockCategories = {
      trivia_categories: [{ id: 9, name: 'General Knowledge' }],
    };

    const mockQuestionsResponse = {
      results: [
        {
          category: 'General Knowledge',
          type: 'multiple',
          difficulty: 'easy',
          question: encodeURIComponent('What is the capital of France?'),
          correct_answer: encodeURIComponent('Paris'),
          incorrect_answers: [
            encodeURIComponent('London'),
            encodeURIComponent('Berlin'),
            encodeURIComponent('Madrid'),
          ],
        },
      ],
    };

    global.fetch = jest.fn((url) => {
      if (url === 'https://opentdb.com/api_category.php') {
        return Promise.resolve({
          json: () => Promise.resolve(mockCategories),
        });
      } else if (url === 'http://localhost:8080/api/trivia/play') {
        return Promise.resolve({
          json: () => Promise.resolve(mockQuestionsResponse),
        });
      } else if (url === 'http://localhost:8080/api/trivia/saveGame') {
        return Promise.resolve({
          json: () => Promise.resolve({}),
        });
      }
      return Promise.reject(new Error('Unknown URL'));
    });

    const mockNavigate = jest.fn();
    useNavigate.mockReturnValue(mockNavigate);

    jest.spyOn(Storage.prototype, 'getItem').mockImplementation(() => 'testuser');

    render(
      <MemoryRouter>
        <Playpage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('General Knowledge')).toBeInTheDocument();
    });

    fireEvent.change(screen.getByLabelText('Number of Questions:'), {
      target: { value: '1' },
    });
    fireEvent.change(screen.getByLabelText('Category:'), {
      target: { value: '9' },
    });
    fireEvent.change(screen.getByLabelText('Difficulty:'), {
      target: { value: 'easy' },
    });

    fireEvent.click(screen.getByText('Submit'));

    await waitFor(() => {
      expect(screen.getByText('Question 1')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByLabelText('Paris'));

    fireEvent.click(screen.getByText('Next'));

    jest.advanceTimersByTime(3000);

    await waitFor(() => {
      expect(screen.getByText('Correct!')).toBeInTheDocument();
    });

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/trivia/saveGame',
        expect.objectContaining({
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: expect.any(String),
        })
      );
    });

    expect(mockNavigate).toHaveBeenCalledWith('/results', {
      state: { finalCorrectAnswers: 1, totalQuestions: 1 },
    });

    jest.useRealTimers();
  });

  test('selecting an incorrect option shows feedback and navigates to results', async () => {
    jest.useFakeTimers();

    const mockCategories = {
      trivia_categories: [{ id: 9, name: 'General Knowledge' }],
    };

    const mockQuestionsResponse = {
      results: [
        {
          category: 'General Knowledge',
          type: 'multiple',
          difficulty: 'easy',
          question: encodeURIComponent('What is the capital of France?'),
          correct_answer: encodeURIComponent('Paris'),
          incorrect_answers: [
            encodeURIComponent('London'),
            encodeURIComponent('Berlin'),
            encodeURIComponent('Madrid'),
          ],
        },
      ],
    };

    global.fetch = jest.fn((url) => {
      if (url === 'https://opentdb.com/api_category.php') {
        return Promise.resolve({
          json: () => Promise.resolve(mockCategories),
        });
      } else if (url === 'http://localhost:8080/api/trivia/play') {
        return Promise.resolve({
          json: () => Promise.resolve(mockQuestionsResponse),
        });
      } else if (url === 'http://localhost:8080/api/trivia/saveGame') {
        return Promise.resolve({
          json: () => Promise.resolve({}),
        });
      }
      return Promise.reject(new Error('Unknown URL'));
    });

    const mockNavigate = jest.fn();
    useNavigate.mockReturnValue(mockNavigate);

    jest.spyOn(Storage.prototype, 'getItem').mockImplementation(() => 'testuser');

    render(
      <MemoryRouter>
        <Playpage />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText('Number of Questions:'), {
      target: { value: '1' },
    });
    fireEvent.change(screen.getByLabelText('Category:'), {
      target: { value: '9' },
    });
    fireEvent.change(screen.getByLabelText('Difficulty:'), {
      target: { value: 'easy' },
    });

    fireEvent.click(screen.getByText('Submit'));

    await waitFor(() => {
      expect(screen.getByText('Question 1')).toBeInTheDocument();
    });

    fireEvent.click(screen.getByLabelText('London'));

    fireEvent.click(screen.getByText('Next'));

    jest.advanceTimersByTime(3000);

    await waitFor(() => {
      expect(screen.getByText('Incorrect!')).toBeInTheDocument();
    });

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        'http://localhost:8080/api/trivia/saveGame',
        expect.objectContaining({
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: expect.any(String),
        })
      );
    });

    expect(mockNavigate).toHaveBeenCalledWith('/results', {
      state: { finalCorrectAnswers: 0, totalQuestions: 1 },
    });

    jest.useRealTimers();
  });
});
