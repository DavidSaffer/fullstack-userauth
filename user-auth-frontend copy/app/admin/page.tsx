'use client';

import React, { useState, useEffect } from 'react';
import styles from './admin.module.css'; // Assume you have a CSS module for styling
import { getAllUsers } from '../../services/apiService';

const AdminPortal = () => {
    const [fetchedUsers, setUsers] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        // Dummy data for example
        const fetchUsers = async () => {
            setLoading(true);
            const response = await getAllUsers();
            if (response.success && response.data && response.data.data) {
                setUsers(response.data.data);
            } else {
                console.error('Failed to fetch users:', response.data);
                // Optionally set an error state here to show an error message
            }
            console.log(response);
            // You would fetch this data from your API
            const fetchedUsers = [
                { id: 1, username: 'JohnDoe', email: 'john@example.com', role: 'Admin' },
                { id: 2, username: 'JaneDoe', email: 'jane@example.com', role: 'User' }
            ];
            //setUsers(fetchedUsers);
            setLoading(false);
        };
        
        fetchUsers();
    }, []);

    const handleEditUser = (userId: number) => {
        // Functionality to edit a user
        console.log('Edit user:', userId);
        // Likely set some state here to open a modal or navigate to a user-specific edit page
    };

    return (
        <div className={styles.container}>
            <h1>Admin Portal</h1>
            {loading ? (
                <p>Loading users...</p>
            ) : (
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Username</th>
                            <th>Password Hash</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Phone Number</th>
                            <th>Date Created</th>
                            <th>Date Updated</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {fetchedUsers.map(user => (
                            <tr key={user.userId}>
                                <td>{user.userId}</td>
                                <td>{user.username}</td>
                                <td>{user.password}</td>
                                <td>{user.email || 'N/A'}</td>
                                <td>{user.role}</td>
                                <td>{user.phoneNumber || 'N/A'}</td>
                                <td>{new Date(user.dateCreated).toLocaleString()}</td>
                                <td>{new Date(user.dateUpdated).toLocaleString()}</td>
                                <td>
                                    <button onClick={() => handleEditUser(user.userId)} className={styles.button}>Edit</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default AdminPortal;