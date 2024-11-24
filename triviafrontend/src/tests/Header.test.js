import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Header from '../components/Header';
import { MemoryRouter } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';


jest.mock('../AuthContext', () => ({
  useAuth: jest.fn(),
}));


jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
  NavLink: ({ children, to, className, activeClassName }) => (
    <a href={to} className={className}>
      {children}
    </a>
  ),
}));


jest.mock('react-toastify', () => ({
  toast: {
    success: jest.fn(),
    error: jest.fn(),
  },
}));

describe('Header Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders without crashing', () => {
    useAuth.mockReturnValue({
      isAuthenticated: false,
      logout: jest.fn(),
    });

    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>
    );
  });

  test('displays correct navigation links when not authenticated', () => {
    useAuth.mockReturnValue({
      isAuthenticated: false,
      logout: jest.fn(),
    });

    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>
    );


    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('Play')).toBeInTheDocument();
    expect(screen.getByText('Leaderboard')).toBeInTheDocument();

    expect(screen.getByText('Sign-In')).toBeInTheDocument();
    expect(screen.getByText('Sign-Up')).toBeInTheDocument();

    expect(screen.queryByText('Profile')).not.toBeInTheDocument();
    expect(screen.queryByText('Logout')).not.toBeInTheDocument();
  });

  test('displays correct navigation links when authenticated', () => {
    useAuth.mockReturnValue({
      isAuthenticated: true,
      logout: jest.fn(),
    });

    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>
    );


    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('Play')).toBeInTheDocument();
    expect(screen.getByText('Leaderboard')).toBeInTheDocument();


    expect(screen.getByText('Profile')).toBeInTheDocument();
    expect(screen.getByText('Logout')).toBeInTheDocument();


    expect(screen.queryByText('Sign-In')).not.toBeInTheDocument();
    expect(screen.queryByText('Sign-Up')).not.toBeInTheDocument();
  });

  test('handles logout successfully', async () => {
    const mockLogout = jest.fn();
    useAuth.mockReturnValue({
      isAuthenticated: true,
      logout: mockLogout,
    });

    const mockNavigate = jest.fn();
    useNavigate.mockReturnValue(mockNavigate);


    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
      })
    );

    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText('Logout'));

    await waitFor(() =>
      expect(global.fetch).toHaveBeenCalledWith('http://localhost:8080/logout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      })
    );

    expect(mockLogout).toHaveBeenCalled();
    expect(toast.success).toHaveBeenCalledWith('Successfully logged out!');
    expect(mockNavigate).toHaveBeenCalledWith('/');
  });

  test('handles logout failure when response is not ok', async () => {
    useAuth.mockReturnValue({
      isAuthenticated: true,
      logout: jest.fn(),
    });


    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
      })
    );


    console.error = jest.fn();

    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText('Logout'));

    await waitFor(() => expect(global.fetch).toHaveBeenCalled());

    expect(console.error).toHaveBeenCalledWith('Logout failed');
    expect(toast.error).toHaveBeenCalledWith('Logout failed');
    expect(useAuth().logout).not.toHaveBeenCalled();
  });

  test('handles network error during logout', async () => {
    useAuth.mockReturnValue({
      isAuthenticated: true,
      logout: jest.fn(),
    });


    global.fetch = jest.fn(() => Promise.reject('Network error'));


    console.error = jest.fn();

    render(
      <MemoryRouter>
        <Header />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText('Logout'));

    await waitFor(() => expect(global.fetch).toHaveBeenCalled());

    expect(console.error).toHaveBeenCalledWith('Error:', 'Network error');
    expect(toast.error).toHaveBeenCalledWith('An error occurred. Please try again.');
    expect(useAuth().logout).not.toHaveBeenCalled();
  });
});
