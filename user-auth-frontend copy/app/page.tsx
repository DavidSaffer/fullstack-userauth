'use client';

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { logoutUser, getUserInfo, updateUserInfo } from '@/services/apiService';
import styles from './page.module.css';

const HomePage: React.FC = () => {
    const router = useRouter();
    const [userInfo, setUserInfo] = useState<UserInfo>({
      username: '',
      email: '',
      phoneNumber: '',
      role: ''
    });
    const [error, setError] = useState<any>('');

    const [editedUsername, setEditedUsername] = useState(userInfo.username);
    const [editedEmail, setEditedEmail] = useState(userInfo.email);
    const [editedPhoneNumber, setEditedPhoneNumber] = useState(userInfo.phoneNumber);

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

    const handleUpdateUserInfo = async () => {
      const result = await updateUserInfo(userInfo.username, editedUsername, editedEmail, editedPhoneNumber, userInfo.role);
      if (result.success) {
          console.log(result.message);
          // Optionally, refresh user info or update UI state here
      } else {
          console.error('Update failed:', result.error);
          setError(result.error);
      }
  };

    useEffect(() => {
      const fetchUserInfo = async () => {
        console.log("calling fetch user info");
        const result = await getUserInfo();
        console.log(result);
        if (result.success && result.data && result.data.data){
          setUserInfo(result.data.data);
          setEditedEmail(result.data.data.email);
          setEditedPhoneNumber(result.data.data.phoneNumber);
          setEditedUsername(result.data.data.username);
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
          <label>
            Email:
            <input
              type="email"
              value={editedEmail}
              onChange={(e) => setEditedEmail(e.target.value)}
              className={styles.input}
            />
          </label>
          <label>
            Phone:
            <input
              type="tel"
              value={editedPhoneNumber}
              onChange={(e) => setEditedPhoneNumber(e.target.value)}
              className={styles.input}
            />
          </label>
          <p>Role: {userInfo.role}</p>
        </div>
        <button className={styles.updateButton} onClick={handleUpdateUserInfo}>Update Info</button>
        <button className={styles.logoutButton} onClick={handleLogout}>Logout</button>
      </div>
    );
};

export default HomePage;
