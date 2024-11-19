import { render, screen } from '@testing-library/react';
import {React, act} from 'react'
import App from './App';
import '@testing-library/jest-dom';
import '@testing-library/dom'

it('renders nav bar', () => {
  render(<App/>);
  expect(screen.getByText('Home')).toBeInTheDocument();
  expect(screen.getByText('Play')).toBeInTheDocument();
  expect(screen.getByText('Leaderboard')).toBeInTheDocument();
  expect(screen.getByText('Sign-In')).toBeInTheDocument();
  expect(screen.getByText('Sign-Up')).toBeInTheDocument();
});