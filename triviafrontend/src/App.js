import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Homepage from './components/Homepage';
import Signinpage from './components/Signinpage';
import Signuppage from './components/Signuppage';
import Leaderboardpage from './components/Leaderboardpage';
import Playpage from './components/Playpage';
import './App.css';

function App() {
  return (
      <Router>
        <div className="App">
          <Header />
          <main className="App-main">
            <Routes>
              <Route path="/" element={<Homepage />} />
              <Route path="/signin" element={<Signinpage />} />
              <Route path="/signup" element={<Signuppage />} />
              <Route path="/leaderboard" element={<Leaderboardpage />} />
              <Route path="/play" element={<Playpage />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    
  );
}

export default App;
