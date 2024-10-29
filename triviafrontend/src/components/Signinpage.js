import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';


const Signinpage = () => {
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
        console.log('Signup request sent with data:', formData); // Log the form data
        try{
            const response = await fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData)
        });
        if (response.ok) {
            // Handle successful response
            console.log('Signin successful');
            login();
            navigate('/');
        } else {
            // Handle error response
            console.error('Signin failed');
        }
    } catch (error) {
        console.error('Error:', error);
    }
};
    return (
        <div>
            <h1>Sign in here</h1>
            <form onSubmit={handleSubmit}>
                <label>Username:</label>
                <input type="text" name="username" value={formData.username} onChange={handleChange} />
                <label>Password:</label>
                <input type="password" name="password" value={formData.password} onChange={handleChange}/>
                <button type="submit">Sign In</button>
            </form>
        </div>
    );
};

export default Signinpage;