import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

jest.mock('./components/Header', () => () => <div>Header</div>);
jest.mock('./components/Footer', () => () => <div>Footer</div>);
jest.mock('./components/Homepage', () => () => <div>Homepage</div>);
jest.mock('./components/ProfilePage', () => () => <div>ProfilePage</div>);
jest.mock('./components/Signinpage', () => () => <div>Signinpage</div>);
jest.mock('./components/Signuppage', () => () => <div>Signuppage</div>);
jest.mock('./components/leaderboard/Leaderboardpage', () => () => <div>Leaderboardpage</div>);
jest.mock('./components/Playpage', () => () => <div>Playpage</div>);
jest.mock('./components/TriviaResults', () => () => <div>TriviaResults</div>);
jest.mock('./components/leaderboard/friends', () => () => <div>FriendsLeaderboard</div>);
jest.mock('./components/leaderboard/allTime', () => () => <div>AllTimeLeaderboard</div>);
jest.mock('./components/leaderboard/personal', () => () => <div>PersonalLeaderboard</div>);
jest.mock('./components/PrivateRoute', () => ({ children }) => <div>{children}</div>);

jest.mock('./AuthContext', () => ({
  AuthProvider: ({ children }) => <div>{children}</div>,
}));

describe('App Component', () => {
  test('renders without crashing', () => {
    render(<App />);
  });

  test('renders Header and Footer components', () => {
    render(<App />);
    expect(screen.getByText('Header')).toBeInTheDocument();
    expect(screen.getByText('Footer')).toBeInTheDocument();
  });

  test('renders Homepage component at default route', () => {
    render(<App />);
    expect(screen.getByText('Homepage')).toBeInTheDocument();
  });

  test('renders Signinpage component at /signin route', () => {
    window.history.pushState({}, 'Test page', '/signin');
    render(<App />);
    expect(screen.getByText('Signinpage')).toBeInTheDocument();
  });

  test('renders Signuppage component at /signup route', () => {
    window.history.pushState({}, 'Test page', '/signup');
    render(<App />);
    expect(screen.getByText('Signuppage')).toBeInTheDocument();
  });

  test('renders Leaderboardpage component at /leaderboard route', () => {
    window.history.pushState({}, 'Test page', '/leaderboard');
    render(<App />);
    expect(screen.getByText('Leaderboardpage')).toBeInTheDocument();
  });

  test('renders Playpage component at /play route', () => {
    window.history.pushState({}, 'Test page', '/play');
    render(<App />);
    expect(screen.getByText('Playpage')).toBeInTheDocument();
  });

  test('renders ProfilePage component at /profile route', () => {
    window.history.pushState({}, 'Test page', '/profile');
    render(<App />);
    expect(screen.getByText('ProfilePage')).toBeInTheDocument();
  });

  test('renders TriviaResults component at /results route', () => {
    window.history.pushState({}, 'Test page', '/results');
    render(<App />);
    expect(screen.getByText('TriviaResults')).toBeInTheDocument();
  });

  test('renders FriendsLeaderboard component at /leaderboard/friends route', () => {
    window.history.pushState({}, 'Test page', '/leaderboard/friends');
    render(<App />);
    expect(screen.getByText('FriendsLeaderboard')).toBeInTheDocument();
  });

  test('renders AllTimeLeaderboard component at /leaderboard/alltime route', () => {
    window.history.pushState({}, 'Test page', '/leaderboard/alltime');
    render(<App />);
    expect(screen.getByText('AllTimeLeaderboard')).toBeInTheDocument();
  });

  test('renders PersonalLeaderboard component at /leaderboard/personal route', () => {
    window.history.pushState({}, 'Test page', '/leaderboard/personal');
    render(<App />);
    expect(screen.getByText('PersonalLeaderboard')).toBeInTheDocument();
  });
});
