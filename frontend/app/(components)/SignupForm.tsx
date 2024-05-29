'use client';

import React, { useState } from 'react';
import { signupUser } from '../../services/apiService';
import styles from './SignupForm.module.css';
import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie';  // Import js-cookie to manage cookies

const SignupForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();

    const handleSignup = async () => {
        try {
            const data = await signupUser(username, password, email, phone);
            if (data.success) {
                // Using js-cookie to manage cookies
                //Cookies.set('jwt', data.data, { expires: 1, secure: true, sameSite: 'Strict' });
                router.push('/');
            } else {
                setError(data.message);
            }
        } catch (error: any) {
            console.error('Signup failed' + error.message);
            setError(error.message);
            // Display Message
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
                {error && <p className={styles.error}>{error}</p>}
            </form>
        </div>
    );
};

export default SignupForm;
