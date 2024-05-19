import React, { useState } from 'react';
import { signupUser } from '../services/apiService';
import styles from './SignupForm.module.css';

const SignupForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');

    const handleSignup = async () => {
        try {
            const data = await signupUser(username, password, email, phone);
            console.log('Signup successful', data);
        } catch (error) {
            console.error('Signup failed', error);
        }
    };

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Sign Up</h1>
            <form onSubmit={e => e.preventDefault()} className={styles.form}>
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
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                    className={styles.input}
                />
                <input
                    type="text"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    placeholder="Phone Number"
                    className={styles.input}
                />
                <button
                    onClick={handleSignup}
                    className={styles.button}
                >
                    Sign Up
                </button>
            </form>
        </div>
    );
};

export default SignupForm;
