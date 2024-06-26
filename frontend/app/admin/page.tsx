'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import styles from './admin.module.css'; // Assume you have a CSS module for styling
import { getAllUsers, updateUserInfo, deleteUser } from '../../services/apiService';

const AdminPortal = () => {
    const [users, setUsers] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);

    const roleOptions = ['ADMIN', 'USER', 'GUEST'];

    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const router = useRouter();

    const [filter, setFilter] = useState('');
    const [sortField, setSortField] = useState('userId');
    const [sortDirection, setSortDirection] = useState('asc'); // 'asc' or 'desc'

    const filteredUsers = users
        .filter(user => user.username.toLowerCase().includes(filter.toLowerCase()))
        .sort((a, b) => {
            if (a[sortField] < b[sortField]) return sortDirection === 'asc' ? -1 : 1;
            if (a[sortField] > b[sortField]) return sortDirection === 'asc' ? 1 : -1;
            return 0;
        });


    useEffect(() => {
        const fetchUsers = async () => {
            setLoading(true);
            const response = await getAllUsers();
            if (response.success && response.data && response.data.data) {
                // Initialize users with editable fields
                let editableUsers = response.data.data.map((user: { username: any; email: any; role: any; phoneNumber: any; }) => ({
                    ...user,
                    isEditing: false,
                    editableUsername: user.username,
                    editableEmail: user.email,
                    editableRole: user.role,
                    editablePhoneNumber: user.phoneNumber || ''
                }));
                setUsers(editableUsers);
            } else {
                console.error('Failed to fetch users:', response.data);
                // Optionally set an error state here to show an error message
            }
            //setUsers(fetchedUsers);
            setLoading(false);
        };
        
        fetchUsers();
    }, []);

    const handleGoHome = () => {
        router.push('/');
    }

    const hasChanges = (userId: any) => {
        const user = users.find(user => user.userId === userId);
        const hasChanges = user.editableUsername !== user.username ||
        user.editableEmail !== user.email ||
        user.editableRole !== user.role ||
        user.editablePhoneNumber !== user.phoneNumber;
        return hasChanges;
    };

    const handleEditToggle = (userId: any) => {
        const updatedUsers = users.map(user => {
            if (user.userId === userId) {
                return { ...user, isEditing: !user.isEditing };
            }
            return user;
        });
        setUsers(updatedUsers);
    };

    const handleCancel = (userId: any) => {
        // Revert changes by resetting the fields to their initial values
        const updatedUsers = users.map(user => {
            if (user.userId === userId) {
                return {
                    ...user,
                    isEditing: false,
                    editableUsername: user.username,
                    editableEmail: user.email,
                    editableRole: user.role,
                    editablePhoneNumber: user.phoneNumber
                };
            }
            return user;
        });
        setUsers(updatedUsers);
    };

    const handleSave = async (userId: any) => {
        const user = users.find(user => user.userId === userId);
        if (user) {
            // Use the original username as 'oldUsername'
            // and the currently edited username as 'newUsername'
            const { username: oldUsername, editableUsername, editableEmail, editableRole, editablePhoneNumber } = user;
            const response = await updateUserInfo(
                oldUsername,
                editableUsername,
                editableEmail,
                editablePhoneNumber,
                editableRole
            );
            if (response.success) {
                user.username = editableUsername;
                user.email = editableEmail;
                user.phoneNumber = editablePhoneNumber;
                user.role = editableRole;
                setSuccessMessage('User updated successfully!');
                setErrorMessage(''); // Clear any previous error messages
                setTimeout(() => setSuccessMessage(''), 3000);
            } else {
                console.error('Failed to update user:', response.error);
                user.editableUsername = oldUsername;
                user.editableEmail = user.email;
                user.editableRole = user.role;
                user.editablePhoneNumber = user.phoneNumber;
                // user.isEditing = false;
                setErrorMessage(response.error || 'Failed to update user info');
                setSuccessMessage(''); // Clear any previous success messages
                setTimeout(() => setErrorMessage(''), 5000);
            }
        }
        // Placeholder for save logic
        handleEditToggle(userId);  // Turn off editing mode after save
    };

    const handleDelete = async (username: string) => {
        // Confirm before deleting
        if (window.confirm(`Are you sure you want to delete the user: ${username}?`)) {
            const user = users.find(user => user.username === username);
            const response = await deleteUser(username);
            if (response.success) {
                setUsers(prevUsers => prevUsers.filter(user => user.username !== username)); // Assuming `username` is unique
                setSuccessMessage('User deleted successfully!');
                setTimeout(() => setSuccessMessage(''), 3000);
            } else {
                setErrorMessage(response.error?.toString() || 'Failed to delete user');
                setTimeout(() => setErrorMessage(''), 5000);
            }
        }
    };

    const handleChange = (userId: any, field: any, value: any) => {
        const updatedUsers = users.map(user => {
            if (user.userId === userId) {
                return { ...user, [field]: value };
            }
            return user;
        });
        setUsers(updatedUsers);
    };

    return (
        <div className={styles.container}>
            <h1>Admin Portal</h1>
            <button onClick={handleGoHome} className={styles.button}>Go Home</button>
            {loading ? (
                <p>Loading users...</p>
            ) : (
                <>
                <div className={styles.filters}>
                    <input
                        type="text"
                        placeholder="Filter by username..."
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                    />
                    <select value={sortField} onChange={(e) => setSortField(e.target.value)}>
                        <option value="userId">User ID</option>
                        <option value="username">Username</option>
                        <option value="email">Email</option>
                        <option value="role">Role</option>
                        <option value="phoneNumber">Phone Number</option>
                    </select>
                    <button onClick={() => setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc')}>
                        {sortDirection === 'asc' ? 'Sort Ascending' : 'Sort Descending'}
                    </button>
                    </div>
                {successMessage && <p className={styles.success}>{successMessage}</p>}
                {errorMessage && <p className={styles.error}>{errorMessage}</p>}
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Password (Partial Hash)</th>
                            <th>Phone Number</th>
                            <th>Date Created</th>
                            <th>Date Updated</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredUsers.map(user => (
                            <tr key={user.userId}>
                                <td>{user.userId}</td>
                                <td>{user.isEditing ? (
                                    <input
                                        type="text"
                                        value={user.editableUsername || ''}
                                        onChange={(e) => handleChange(user.userId, 'editableUsername', e.target.value)}
                                    />
                                ) : user.username}</td>
                                <td>{user.isEditing ? (
                                    <input
                                        type="email"
                                        value={user.editableEmail || ''}
                                        onChange={(e) => handleChange(user.userId, 'editableEmail', e.target.value)}
                                    />
                                ) :  (user.email || 'N/A')}</td>
                                <td>{user.isEditing ? (
                                    <select
                                        value={user.editableRole}
                                        onChange={(e) => handleChange(user.userId, 'editableRole', e.target.value)}
                                        className={styles.input}
                                    >
                                        {roleOptions.map(option => (
                                            <option key={option} value={option}>{option}</option>
                                        ))}
                                    </select>
                                ) : user.role}</td>
                                <td>{user.password.substring(0, 10)}</td>
                                <td>{user.isEditing ? (
                                    <input
                                        type="tel"
                                        value={user.editablePhoneNumber || ''}
                                        onChange={(e) => handleChange(user.userId, 'editablePhoneNumber', e.target.value)}
                                    />
                                ) : (user.phoneNumber || 'N/A')}</td>
                                <td>{new Date(user.dateCreated).toLocaleString()}</td>
                                <td>{new Date(user.dateUpdated).toLocaleString()}</td>
                                <td>
                                    {user.isEditing ? (
                                        <>
                                            <button onClick={() => handleSave(user.userId)} disabled={!hasChanges(user.userId)} className={styles.button} >Save</button>
                                            <button onClick={() => handleCancel(user.userId)} className={styles.button}>Cancel</button>
                                        </>
                                    ) : (
                                        <>
                                        <button onClick={() => handleEditToggle(user.userId)} className={styles.button}>Edit</button>
                                        <button onClick={() => handleDelete(user.username)} className={styles.button}>Delete</button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                </>
            )}
        </div>
    );
};

export default AdminPortal;