import React, { useState } from 'react';
import { loginUser } from '../services/apiService';
import styles from './LoginForm.module.css';

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        try {
            const data = await loginUser(username, password);
            console.log('Login successful', data);
        } catch (error) {
            console.error('Login failed', error);
        }
    };

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Log In</h1>
            <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Username"
                className={styles.input}
            />
            <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Password"
                className={styles.input}
            />
            <button
                onClick={handleLogin}
                className={styles.button}
            >
                Log In
            </button>
        </div>
    );
};

export default LoginForm;
