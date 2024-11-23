import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import HomePage from '../components/Homepage';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
}));

describe('HomePage Component', () => {
  test('renders without crashing', () => {
    render(
      <MemoryRouter>
        <HomePage />
      </MemoryRouter>
    );
  });

  test('renders header with title and tagline', () => {
    render(
      <MemoryRouter>
        <HomePage />
      </MemoryRouter>
    );

    expect(screen.getByText('Welcome to Trivia Trove!')).toBeInTheDocument();
    expect(
      screen.getByText('Test your knowledge and climb the leaderboard!')
    ).toBeInTheDocument();
  });

  test('renders How to Play section with Start Game button', () => {
    render(
      <MemoryRouter>
        <HomePage />
      </MemoryRouter>
    );

    expect(screen.getByText('How to Play')).toBeInTheDocument();
    expect(
      screen.getByText(
        /Think you know it all\? Click on the 'Start Game' button and answer trivia questions from various categories./
      )
    ).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Start Game' })).toBeInTheDocument();
  });

  test('Start Game button navigates to /play', () => {
    const navigate = jest.fn();
    jest.spyOn(require('react-router-dom'), 'useNavigate').mockImplementation(() => navigate);

    render(
      <MemoryRouter>
        <HomePage />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByRole('button', { name: 'Start Game' }));
    expect(navigate).toHaveBeenCalledWith('/play');
  });

  test('renders feature boxes with correct content', () => {
    render(
      <MemoryRouter>
        <HomePage />
      </MemoryRouter>
    );

    expect(screen.getByText('Multiple Categories')).toBeInTheDocument();
    expect(
      screen.getByText(
        'Choose from sports, science, history, and more to test your knowledge!'
      )
    ).toBeInTheDocument();

    expect(screen.getByText('Real-Time Leaderboard')).toBeInTheDocument();
    expect(
      screen.getByText(
        'Compete against other players and see your name rise to the top of the leaderboard!'
      )
    ).toBeInTheDocument();

    expect(screen.getByText('Track Your Progress')).toBeInTheDocument();
    expect(
      screen.getByText('Review your game history and track your personal best scores.')
    ).toBeInTheDocument();
  });

  test('renders footer with the correct text', () => {
    render(
      <MemoryRouter>
        <HomePage />
      </MemoryRouter>
    );

    expect(
      screen.getByText('Good luck, and may the best trivia master win!')
    ).toBeInTheDocument();
  });
});
