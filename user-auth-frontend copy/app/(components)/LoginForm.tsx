'use client';

import React, { useState } from 'react';
import { loginUser } from '../../services/apiService';
import styles from './LoginForm.module.css';
import { useRouter } from 'next/navigation';
import Cookies from 'js-cookie';  // Import js-cookie to manage cookies

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();

    const handleLogin = async () => {
        try {
            const data = await loginUser(username, password);
            if (data.success) {
                // Using js-cookie to manage cookies
                Cookies.set('jwt', data.data, { expires: 1, secure: true, sameSite: 'Strict' });
                router.push('/home');
            } else {
                setError(data.message);
            }
        } catch (error: any) {
            setError(error.message);
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
            {error && <p className={styles.error}>{error}</p>}
        </div>
    );
};

export default LoginForm;
