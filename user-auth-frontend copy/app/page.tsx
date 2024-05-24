'use client';

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { logoutUser, getUserInfo } from '@/services/apiService';
import styles from './page.module.css';

const HomePage: React.FC = () => {
    const router = useRouter();
    const [userInfo, setUserInfo] = useState<UserInfo>({
      username: '',
      email: '',
      phoneNumber: '',
      role: ''
    });
    const [error, setError] = useState<string>('');

    const handleLogout = async () => {
        // Remove the JWT cookie
        try {
          const data = await logoutUser();
        } catch (error: any) {
          console.error('Logout failed' + error.message);
        }
        
        // Redirect to login page or refresh home page to show logged-out state
        router.push('/auth');
    };

    useEffect(() => {
      const fetchUserInfo = async () => {
        console.log("calling fetch user info");
        const result = await getUserInfo();
        console.log(result);
        if (result.success && result.data && result.data.data){
          setUserInfo(result.data.data);
        } else {
          setError(result.error || "Failed To Fetch User Info");
        }
      }

      fetchUserInfo();
    }, []);


    return (
      <div className={styles.container}>
        <h1 className={styles.title}>Welcome to the Home Page</h1>
        {error && <p className={styles.error}>Error: {error}</p>}
        <div className={styles.userDetails}>
            <p>Username: {userInfo.username}</p>
            <p>Email: {userInfo.email}</p>
            <p>Phone: {userInfo.phoneNumber}</p>
            <p>Role: {userInfo.role}</p>
        </div>
        <button className={styles.logoutButton} onClick={handleLogout}>Logout</button>
      </div>
    );
};

export default HomePage;
