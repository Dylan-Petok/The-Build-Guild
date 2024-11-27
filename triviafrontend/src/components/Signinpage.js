import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { toast } from 'react-toastify';
import fetchInterceptor from '../utils/fetchInterceptor';
import '../css/AuthPage.css';

const SignInPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    const navigate = useNavigate();
    const { login } = useAuth();

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Sign-in request sent with data:', formData);
        try {
            const response = await fetchInterceptor('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData),
                credentials: 'include'
            });
            if (response.ok) {
                const data = await response.json()
                localStorage.setItem('username', data.username);
                localStorage.setItem('friends', data.friends);
                toast.success('Log-in successful!')
                console.log('Sign-in successful');
                login();
                navigate('/');
            } else {
                console.error('Sign-in failed');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div className="auth-container">
            <h1 className="auth-header">Sign In</h1>  
            <form className="auth-form" onSubmit={handleSubmit}>
                <label>Username:</label>
                <input
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                />
                <label>Password:</label>
                <input
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    required
                />
                <button type="submit" className="auth-btn">Sign In</button>
            </form>
            <footer className="auth-footer">
                <p>Don't have an account? <a href="/signup">Sign Up</a></p>
            </footer>
        </div>
    );
};

export default SignInPage;
