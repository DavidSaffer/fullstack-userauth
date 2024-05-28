'use client';

import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { logoutUser, getUserInfo, updateUserInfo } from '@/services/apiService';
import styles from './page.module.css';

const HomePage = () => {
    const router = useRouter();
    const [userInfo, setUserInfo] = useState({ username: '', email: '', phoneNumber: '', role: '' });
    const [editMode, setEditMode] = useState(false);
    const [editedUsername, setEditedUsername] = useState('');
    const [editedEmail, setEditedEmail] = useState('');
    const [editedPhoneNumber, setEditedPhoneNumber] = useState('');
    const [editedRole, setEditedRole] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [showSuccess, setShowSuccess] = useState(false);

    useEffect(() => {
      const fetchUserInfo = async () => {
          const result = await getUserInfo();
          console.log(result);
          if (result.success) {
              setUserInfo(result.data.data);
              updateEditFields(result.data.data);
          } else {
              setError(result.error || "Failed to fetch user info");
          }
      };
      fetchUserInfo();
  }, []);

  const updateEditFields = (data:any) => {
      setEditedUsername(data.username);
      setEditedEmail(data.email);
      setEditedPhoneNumber(data.phoneNumber);
      setEditedRole(data.role);
  };

  useEffect(() => {
      if (!editMode) {
          updateEditFields(userInfo);
      }
  }, [editMode, userInfo]);

  useEffect(() => {
    // Clear the error message whenever any of the edited fields changes
    setError('');
}, [editedUsername, editedEmail, editedPhoneNumber, editedRole]);

    const handleLogout = async () => {
        await logoutUser();
        router.push('/auth');
    };

    const handleUpdateUserInfo = async () => {
      const result = await updateUserInfo(userInfo.username, editedUsername, editedEmail, editedPhoneNumber, editedRole);
      if (result.success) {
          console.log(result.data);
          setUserInfo({ ...userInfo, username: editedUsername, email: editedEmail, phoneNumber: editedPhoneNumber, role: editedRole });
          setEditMode(false);
          setSuccessMessage('Profile updated successfully!');
          setShowSuccess(true);
          setTimeout(() => {
            setShowSuccess(false);
        }, 3000); // Message will fade out after 3 seconds
      } else {
          console.error('Update failed:', result.error);
          setError(typeof result.error === 'string' ? result.error : "Internal Type Error");
      }
    };

    const handleAdminPortal = () => {
      router.push('/admin');
    };

    const hasChanges = () => {
      return userInfo.username !== editedUsername || userInfo.email !== editedEmail ||
             userInfo.phoneNumber !== editedPhoneNumber || userInfo.role !== editedRole;
    };

    return (
      <div className={styles.container}>
        <h1 className={styles.title}>Welcome to the Home Page</h1>
        {error && <p className={styles.error}>{error}</p>}
        { showSuccess && (
           <p className={`${styles.successMessage} ${showSuccess ? styles.fadeOut : ''}`}>{successMessage}</p>
        )}
        <div className={styles.userDetails}>
          {editMode ? (
            <>
              <label className={styles.label}>Username:
                <input value={editedUsername} onChange={(e) => setEditedUsername(e.target.value)} className={styles.input} />
              </label>
              <label className={styles.label}>Email:
                <input type="email" value={editedEmail} onChange={(e) => setEditedEmail(e.target.value)} className={styles.input} />
              </label>
              <label className={styles.label}>Phone:
                <input type="tel" value={editedPhoneNumber} onChange={(e) => setEditedPhoneNumber(e.target.value)} className={styles.input} />
              </label>
              <label className={styles.label}>Role:
                <select value={editedRole} onChange={(e) => setEditedRole(e.target.value)} className={styles.select}>
                  <option value="USER">User</option>
                  <option value="ADMIN">Admin</option>
                  <option value="GUEST">Guest</option>
                </select>
              </label>
              <button onClick={handleUpdateUserInfo} disabled={!hasChanges()} className={styles.button}>Save</button>
              <button onClick={() => setEditMode(false)} className={styles.button}>Cancel Edit</button>
            </>
          ) : (
            <>
              <p>Username: {userInfo.username}</p>
              <p>Email: {userInfo.email}</p>
              <p>Phone: {userInfo.phoneNumber}</p>
              <p>Role: {userInfo.role}</p>
              <button onClick={() => setEditMode(true)} className={styles.button}>Edit</button>
              {userInfo.role === 'ADMIN' && (
                    <button onClick={handleAdminPortal} className={styles.button}>Admin Portal</button>
                )}
            </>
          )}
        </div>
        <button onClick={handleLogout} className={styles.logoutButton}>Logout</button>
      </div>
    );
};

export default HomePage;