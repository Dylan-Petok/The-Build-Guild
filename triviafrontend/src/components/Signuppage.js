import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';



const Signuppage = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
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
        console.log('Signup request sent with data:', formData); // Log the form data
        try{
            const response = await fetch('http://localhost:8080/api/users/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
        });
        if (response.ok) {
            // Handle successful response
            console.log('Signup successful');
            login();
            navigate('/');
        } else {
            // Handle error response
            console.error('Signup failed');
        }
    } catch (error) {
        console.error('Error:', error);
    }
};
    return (
        <div>
            <h1>Sign up here</h1>
            <form onSubmit={handleSubmit}>
                <label>Username:</label>
                <input type="text" name="username" value={formData.username} onChange={handleChange} />
                <label>Email:</label>
                <input type="email" name="email" value={formData.email} onChange={handleChange} />
                <label>Password:</label>
                <input type="password" name="password" value={formData.password} onChange={handleChange} />
                <button type="submit">Sign Up</button>
            </form>
        </div>
    );
};

export default Signuppage;