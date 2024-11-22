import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Footer from './components/Footer';
import Homepage from './components/Homepage';
import ProfilePage from './components/ProfilePage';
import Signinpage from './components/Signinpage';
import Signuppage from './components/Signuppage';
import Leaderboardpage from './components/leaderboard/Leaderboardpage';
import Playpage from './components/Playpage';
import TriviaResults from './components/TriviaResults'; 
import FriendsLeaderboard from './components/leaderboard/friends';
import AllTimeLeaderboard from './components/leaderboard/allTime';
import PersonalLeaderboard from './components/leaderboard/personal';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AuthProvider } from './AuthContext';
import PrivateRoute from './components/PrivateRoute';
import './App.css';

function App() {
  return (
      <AuthProvider>
        <Router>
          <div className="App">
            <Header />
            <main className="App-main">
              <Routes>
                <Route path="/" element={<Homepage />} />
                <Route path="/signin" element={<Signinpage />} />
                <Route path="/signup" element={<Signuppage />} />
                <Route path="/leaderboard" element={<PrivateRoute><Leaderboardpage /></PrivateRoute>} />
                <Route path="/play" element={<PrivateRoute><Playpage /></PrivateRoute>} />
                <Route path="/results" element={<TriviaResults />} />
                <Route path="/leaderboard/friends" element={<FriendsLeaderboard/>} />
                <Route path="/leaderboard/personal" element={<AllTimeLeaderboard />}/>
                <Route path="/leaderboard/personal" element={<PersonalLeaderboard />} /> 
                <Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>} /> 
              </Routes>
            </main>
            <Footer />
            <ToastContainer /> 
          </div>
        </Router>
      </AuthProvider>
    
  );
}

export default App;
