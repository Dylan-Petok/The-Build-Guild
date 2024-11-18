// SignUpPage.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import { toast } from 'react-toastify';
import '../css/AuthPage.css';

const SignUpPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
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
        if (formData.password !== formData.confirmPassword) {
            alert('Passwords do not match.');
            return;
        }
           // Create an object excluding confirmPassword to log what data was sent over
        const dataToSend = {
            username: formData.username,
            email: formData.email,
            password: formData.password
        };
        console.log('Signup request sent with data:', dataToSend);
        try {
            const response = await fetch('http://localhost:8080/api/users/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: formData.username,
                    email: formData.email,
                    password: formData.password
                })
            });
            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', data.username);
                toast.success('Account successfully created!');
                login();
                navigate('/');
            } else {
                const errorData = await response.json()
                toast.error(`Sign up failed: ${errorData.message}`);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div className="auth-container">
            <h1>Sign Up</h1>
            <form className="auth-form" onSubmit={handleSubmit}>
                <label>Username:</label>
                <input
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                />
                <label>Email:</label>
                <input
                    type="email"
                    name="email"
                    value={formData.email}
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
                <label>Confirm Password:</label>
                <input
                    type="password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleChange}
                    required
                />
                <button type="submit" className="auth-btn">Sign Up</button>
            </form>
            <footer className="auth-footer">
                <p>Already have an account? <a href="/signin">Sign In</a></p>
            </footer>
        </div>
    );
};

export default SignUpPage;
