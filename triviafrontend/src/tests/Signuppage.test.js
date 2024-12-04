import React from 'react';
import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import SignUpPage from '../components/Signuppage';
import { MemoryRouter } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { toast } from 'react-toastify';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

jest.mock('../AuthContext', () => ({
  useAuth: jest.fn(),
}));

jest.mock('react-toastify', () => ({
  toast: {
    success: jest.fn(),
    error: jest.fn(),
  },
}));

describe('SignUpPage Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    global.fetch = jest.fn();
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test('renders without crashing', () => {
    useAuth.mockReturnValue({
      login: jest.fn(),
      logout: jest.fn(),
    });

    render(
      <MemoryRouter>
        <SignUpPage />
      </MemoryRouter>
    );
  });

  test('displays all form fields and sign-up button', () => {
    useAuth.mockReturnValue({
      login: jest.fn(),
      logout: jest.fn(),
    });

    render(
      <MemoryRouter>
        <SignUpPage />
      </MemoryRouter>
    );

    expect(screen.getByLabelText('Username:')).toBeInTheDocument();
    expect(screen.getByLabelText('Email:')).toBeInTheDocument();
    expect(screen.getByLabelText('Password:')).toBeInTheDocument();
    expect(screen.getByLabelText('Confirm Password:')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Sign Up' })).toBeInTheDocument();
  });

  test('shows alert if passwords do not match', () => {
    useAuth.mockReturnValue({
      login: jest.fn(),
      logout: jest.fn(),
    });

    jest.spyOn(window, 'alert').mockImplementation(() => {});

    render(
      <MemoryRouter>
        <SignUpPage />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText('Password:'), {
      target: { value: 'password123' },
    });
    fireEvent.change(screen.getByLabelText('Confirm Password:'), {
      target: { value: 'password456' },
    });

    fireEvent.click(screen.getByRole('button', { name: 'Sign Up' }));

    expect(window.alert).toHaveBeenCalledWith('Passwords do not match.');
  });

  test('submits form and handles success', async () => {
    const mockLogin = jest.fn();
    const mockLogout = jest.fn();
    useAuth.mockReturnValue({
      login: mockLogin,
      logout: mockLogout,
    });

    const mockNavigate = jest.fn();
    useNavigate.mockReturnValue(mockNavigate);

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        token: 'testtoken',
        username: 'testuser',
      }),
    });

    const localStorageSetItemSpy = jest.spyOn(Storage.prototype, 'setItem');

    render(
      <MemoryRouter>
        <SignUpPage />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText('Username:'), {
      target: { value: 'testuser' },
    });
    fireEvent.change(screen.getByLabelText('Email:'), {
      target: { value: 'test@example.com' },
    });
    fireEvent.change(screen.getByLabelText('Password:'), {
      target: { value: 'password123' },
    });
    fireEvent.change(screen.getByLabelText('Confirm Password:'), {
      target: { value: 'password123' },
    });

    await act(async () => {
      fireEvent.click(screen.getByRole('button', { name: 'Sign Up' }));
    });

    expect(mockLogout).toHaveBeenCalled();
    expect(global.fetch).toHaveBeenCalledWith(
      'http://localhost:8080/api/users/create',
      expect.objectContaining({
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: 'testuser',
          email: 'test@example.com',
          password: 'password123',
        }),
      })
    );

    await waitFor(() => {
      expect(localStorageSetItemSpy).toHaveBeenCalledWith('username', 'testuser');
      expect(toast.success).toHaveBeenCalledWith('Account successfully created!');
      expect(mockLogin).toHaveBeenCalled();
      expect(mockNavigate).toHaveBeenCalledWith('/');
    });
  });

  test('submits form and handles failure', async () => {
    const mockLogin = jest.fn();
    const mockLogout = jest.fn();
    useAuth.mockReturnValue({
      login: mockLogin,
      logout: mockLogout,
    });

    const mockNavigate = jest.fn();
    useNavigate.mockReturnValue(mockNavigate);

    global.fetch.mockResolvedValueOnce({
      ok: false,
      json: async () => ({
        message: 'Username already exists',
      }),
    });

    render(
      <MemoryRouter>
        <SignUpPage />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText('Username:'), {
      target: { value: 'existinguser' },
    });
    fireEvent.change(screen.getByLabelText('Email:'), {
      target: { value: 'existing@example.com' },
    });
    fireEvent.change(screen.getByLabelText('Password:'), {
      target: { value: 'password123' },
    });
    fireEvent.change(screen.getByLabelText('Confirm Password:'), {
      target: { value: 'password123' },
    });

    await act(async () => {
      fireEvent.click(screen.getByRole('button', { name: 'Sign Up' }));
    });

    expect(mockLogout).toHaveBeenCalled();
    expect(global.fetch).toHaveBeenCalledWith(
      'http://localhost:8080/api/users/create',
      expect.any(Object)
    );

    await waitFor(() => {
      expect(toast.error).toHaveBeenCalledWith('Sign up failed: Username already exists');
      expect(mockLogin).not.toHaveBeenCalled();
      expect(mockNavigate).not.toHaveBeenCalled();
    });
  });
});
