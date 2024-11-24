import React from 'react';
import { render, act } from '@testing-library/react';
import { AuthProvider, useAuth } from './AuthContext';

const localStorageMock = (() => {
  let store = {};

  return {
    getItem: jest.fn((key) => store[key] || null),
    setItem: jest.fn((key, value) => {
      store[key] = value.toString();
    }),
    removeItem: jest.fn((key) => {
      delete store[key];
    }),
    clear: jest.fn(() => {
      store = {};
    }),
  };
})();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock,
});

describe('AuthContext', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    localStorage.clear();
  });

  const TestComponent = () => {
    const { isAuthenticated, login, logout } = useAuth();

    return (
      <div>
        <p>Authenticated: {isAuthenticated.toString()}</p>
        <button onClick={login}>Login</button>
        <button onClick={() => logout()}>Logout</button>
      </div>
    );
  };

  test('initializes with isAuthenticated false', () => {
    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(getByText('Authenticated: false')).toBeInTheDocument();
  });

  test('initializes with isAuthenticated true when localStorage has isAuthenticated true', () => {
    localStorage.getItem.mockReturnValueOnce('true');

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(localStorage.getItem).toHaveBeenCalledWith('isAuthenticated');
    expect(getByText('Authenticated: true')).toBeInTheDocument();
  });

  test('login function updates isAuthenticated to true and saves to localStorage', () => {
    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    act(() => {
      getByText('Login').click();
    });

    expect(getByText('Authenticated: true')).toBeInTheDocument();
    expect(localStorage.setItem).toHaveBeenCalledWith('isAuthenticated', 'true');
  });

  test('logout function updates isAuthenticated to false, removes from localStorage, and calls callback', () => {
    const mockCallback = jest.fn();

    const TestComponentWithCallback = () => {
      const { isAuthenticated, logout } = useAuth();

      return (
        <div>
          <p>Authenticated: {isAuthenticated.toString()}</p>
          <button onClick={() => logout(mockCallback)}>Logout</button>
        </div>
      );
    };

    const { getByText } = render(
      <AuthProvider>
        <TestComponentWithCallback />
      </AuthProvider>
    );

    act(() => {
      localStorage.setItem('isAuthenticated', 'true');
    });

    act(() => {
      getByText('Logout').click();
    });

    expect(getByText('Authenticated: false')).toBeInTheDocument();
    expect(localStorage.removeItem).toHaveBeenCalledWith('isAuthenticated');
    expect(mockCallback).toHaveBeenCalled();
  });

  test('useAuth hook provides the correct context values', () => {
    let contextValues;

    const TestComponent = () => {
      contextValues = useAuth();
      return null;
    };

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(contextValues).toHaveProperty('isAuthenticated', false);
    expect(typeof contextValues.login).toBe('function');
    expect(typeof contextValues.logout).toBe('function');
  });
});
