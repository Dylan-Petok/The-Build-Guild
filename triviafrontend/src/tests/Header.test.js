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
  NavLink: ({ children, to, className }) => (
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
      friendsList: [],
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

  test('handles logout failure when response is not ok', async () => {
    const mockLogout = jest.fn()
    useAuth.mockReturnValue({
      isAuthenticated: true,
      logout: mockLogout,
      friendsList: [],
    });

    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
        json: () => Promise.resolve({ message: 'Logout failed' }),
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
      friendsList: [],
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

    expect(console.error).toHaveBeenCalledWith('Fetch error:', 'Network error');
    expect(toast.error).toHaveBeenCalledWith('Network error. Please try again later.');
    expect(useAuth().logout).toHaveBeenCalled();
  });
});

test('renders search bar and handles friend search', async () => {
  const mockAddFriend = jest.fn(() => Promise.resolve({ ok: true }));
  const mockDeleteFriend = jest.fn(() => Promise.resolve({ ok: true }));
  
  useAuth.mockReturnValue({
    isAuthenticated: true,
    logout: jest.fn(),
    addFriend: mockAddFriend,
    deleteFriend: mockDeleteFriend,
    friendsList: ['existingFriend'],
  });

  global.fetch = jest.fn(() => 
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve([
        { username: 'testUser' },
        { username: 'existingFriend' }
      ])
    })
  );

  render(
    <MemoryRouter>
      <Header />
    </MemoryRouter>
  );

  const searchInput = screen.getByPlaceholderText('Add a friend');
  fireEvent.change(searchInput, { target: { value: 'test' } });

  await waitFor(() => {
    expect(screen.getByText('testUser')).toBeInTheDocument();
    expect(screen.getByText('existingFriend')).toBeInTheDocument();
  });
});

test('handles adding a friend successfully', async () => {
  const mockAddFriend = jest.fn(() => Promise.resolve({ ok: true }));
  
  useAuth.mockReturnValue({
    isAuthenticated: true,
    logout: jest.fn(),
    addFriend: mockAddFriend,
    deleteFriend: jest.fn(),
    friendsList: [],
  });

  global.fetch = jest.fn(() => 
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve([{ username: 'testUser' }])
    })
  );

  render(
    <MemoryRouter>
      <Header />
    </MemoryRouter>
  );

  const searchInput = screen.getByPlaceholderText('Add a friend');
  fireEvent.change(searchInput, { target: { value: 'test' } });

  await waitFor(() => {
    const addButton = screen.getByRole('button', { name: '' });
    fireEvent.click(addButton);
  });

  expect(mockAddFriend).toHaveBeenCalledWith('testUser');
  expect(toast.success).toHaveBeenCalledWith('User: testUser added!');
});

test('handles adding a friend failure', async () => {
  const mockAddFriend = jest.fn(() => 
    Promise.resolve({
      ok: false,
      json: () => Promise.resolve({ message: 'User not found' })
    })
  );
  
  useAuth.mockReturnValue({
    isAuthenticated: true,
    logout: jest.fn(),
    addFriend: mockAddFriend,
    deleteFriend: jest.fn(),
    friendsList: [],
  });

  global.fetch = jest.fn(() => 
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve([{ username: 'testUser' }])
    })
  );

  render(
    <MemoryRouter>
      <Header />
    </MemoryRouter>
  );

  const searchInput = screen.getByPlaceholderText('Add a friend');
  fireEvent.change(searchInput, { target: { value: 'test' } });

  await waitFor(() => {
    const addButton = screen.getByRole('button', { name: '' });
    fireEvent.click(addButton);
  });

  expect(mockAddFriend).toHaveBeenCalledWith('testUser');
  await waitFor(() => {
    expect(toast.error).toHaveBeenCalledWith('Failed to send friend request: User not found');
  });
});

test('handles deleting a friend successfully', async () => {
  const mockDeleteFriend = jest.fn(() => Promise.resolve({ ok: true }));
  
  useAuth.mockReturnValue({
    isAuthenticated: true,
    logout: jest.fn(),
    addFriend: jest.fn(),
    deleteFriend: mockDeleteFriend,
    friendsList: ['testUser'],
  });

  global.fetch = jest.fn(() => 
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve([{ username: 'testUser' }])
    })
  );

  render(
    <MemoryRouter>
      <Header />
    </MemoryRouter>
  );

  const searchInput = screen.getByPlaceholderText('Add a friend');
  fireEvent.change(searchInput, { target: { value: 'test' } });

  await waitFor(() => {
    const deleteButton = screen.getByRole('button', { name: '' });
    fireEvent.click(deleteButton);
  });

  expect(mockDeleteFriend).toHaveBeenCalledWith('testUser');
  expect(toast.success).toHaveBeenCalledWith('Removed testUser from friends!');
});

test('handles search bar clear on click outside', async () => {
  useAuth.mockReturnValue({
    isAuthenticated: true,
    logout: jest.fn(),
    addFriend: jest.fn(),
    deleteFriend: jest.fn(),
    friendsList: [],
  });

  render(
    <MemoryRouter>
      <Header />
    </MemoryRouter>
  );

  const searchInput = screen.getByPlaceholderText('Add a friend');
  fireEvent.change(searchInput, { target: { value: 'test' } });
  
  fireEvent.mouseDown(document.body);
  
  expect(searchInput.value).toBe('');
});
