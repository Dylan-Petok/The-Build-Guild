import React, { useState } from 'react';  
import { useNavigate } from 'react-router-dom';  
import { toast } from 'react-toastify';  
import { useAuth } from '../AuthContext';
import '../css/AuthPage.css';

const SignUpPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const {logout, } = useAuth();

    const navigate = useNavigate();  

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        logout();
        if (formData.password !== formData.confirmPassword) {
            alert('Passwords do not match.');
            return;
        }
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
                body: JSON.stringify(dataToSend),
                credentials: 'include'
            });
            if (response.ok) {
                const data = await response.json()
                localStorage.setItem('username', data.username);
                toast.success('Account successfully created!');  // Show success message
            } else {
                const errorData = await response.json();
                toast.error(`Sign up failed: ${errorData.message}`);  // Show error message
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div className="auth-container">
            <h1 className="auth-header">Sign Up</h1> 
            <form className="auth-form" onSubmit={handleSubmit}>
                <label for="username">Username:</label>
                <input
                    type="text"
                    name="username"
                    id="username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                />
                <label for="email">Email:</label>
                <input
                    type="email"
                    name="email"
                    id="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                />
                <label for="password">Password:</label>
                <input
                    type="password"
                    name="password"
                    id="password"
                    value={formData.password}
                    onChange={handleChange}
                    required
                />
                <label for="confirmPassword">Confirm Password:</label>
                <input
                    type="password"
                    name="confirmPassword"
                    id="confirmPassword"
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
