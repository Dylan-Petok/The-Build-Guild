// SigninPage.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { toast } from 'react-toastify';
import '../css/AuthPage.css';

const SigninPage = () => {
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
        console.log('Signin request sent with data:', formData);
        try {
            const response = await fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
            });
            if (response.ok) {
                const data = await response.json(); // Parse the response JSON
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', data.username); // Store username
                toast.success('Successfully logged in!');
                login();
                navigate('/profile');
            } else {
                const errorText = await response.text();
                try {
                    const errorData = JSON.parse(errorText); // Try to parse as JSON
                    toast.error(`Signin failed: ${errorData.message}`);
                } catch (e) {
                    toast.error(`Signin failed: ${errorText}`); // Fallback to text
                }
            }
        } catch (error) {
            console.error('Error:', error);
            toast.error('An error occurred. Please try again.');
        }
    };

    return (
        <div className="auth-container">
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

export default SigninPage;
