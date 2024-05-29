// services/apiService.ts
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const loginUser = async (username: string, password: string) => {
    try {
        const response = await axios.post(`${API_URL}/auth/login`, { username, password }, { withCredentials: true });
        return response.data;  // Assuming the token and user details are in the response
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.data || 'Failed to login');
        }
        throw new Error('Failed to login');
    }
};

export const signupUser = async (username: string, password: string, email: string, phone: string) => {
    try {
        const response = await axios.post(`${API_URL}/auth/signup`, { username, password, email, phone }, { withCredentials: true });
        return response.data;
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.data || 'Failed to signup');
        }
        throw new Error('Failed to signup');
    }
};

export const validateToken = async(token: string) => {
    try {
        const response = await axios.post(`${API_URL}/auth/validate-token`, token, { withCredentials: true });
        return response.data
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.message || 'Failed to signup');
        }
        throw new Error('Failed to signup');
    }
};

export const checkIsAdmin = async(token: string) => {
    try {
        const response = await axios.post(`${API_URL}/auth/is-admin`, token, {withCredentials: true});
        return {success: true, data: response.data};
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            return {success: false, data: error.response.data || 'Failed to check if user is admin1'};
        }
        return {success: false, data: 'Failed to check if user is admin2'};
    }
};

export const checkIsAdminGet = async() => {
    try {
        const response = await axios.get(`${API_URL}/auth/is-admin`, { withCredentials: true });
        return {success: true, data: response.data};
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            return {success: false, data: error.response.data || 'Failed to check if user is admin1'};
        }
        return {success: false, data: 'Failed to check if user is admin2'};
    }
};

export const logoutUser = async () => {
    try {
        const response = await axios.post(`${API_URL}/auth/logout`, {}, {
            withCredentials: true
        });
        return response.data;
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.message || 'Failed to logout');
        }
        throw new Error('Failed to logout');
    }
};

export const getUserInfo = async () => {
    try {
        const response = await axios.get(`${API_URL}/user/get-user-info`, { withCredentials: true });
        return { success: true, data: response.data }; 
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            // Return a specific error message if available
            return { success: false, error: error.response.data.message || 'Failed to get user info' };
        }
        // Return a general error message if the error structure is unexpected
        return { success: false, error: 'Failed to get user info' };
    }
};

export const updateUserInfo = async (oldUsername: string, newUsername: string, email: string, phoneNumber: string, role: string) => {
    try {
        const response = await axios.post(`${API_URL}/user/update-user`, {
            oldUsername,
            newUsername,
            email,
            phoneNumber,
            role
        }, { withCredentials: true });

        return { success: true, data: response.data };
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            // Return a specific error message if available
            return { success: false, error: error.response.data.message +": " + error.response.data.data|| 'Failed to update user info' };
        }
        // Return a general error message if the error structure is unexpected
        return { success: false, error: 'Failed to update user info' };
    }
};

export const getAllUsers = async () => {
    try {
        const response = await axios.get(`${API_URL}/user/users`, { withCredentials: true});
        return {success: true, data: response.data};
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            // Return a specific error message if available
            return { success: false, error: error|| 'Failed to get users' };
        }
        // Return a general error message if the error structure is unexpected
        return { success: false, error: 'Request failed' };
    }
};

/**
 * Deletes a user with the specified username.
 * @param {string} username - The username of the user to delete.
 * @returns {Promise<{success: boolean, data?: any, error?: string}>}
 */
export const deleteUser = async (username: string) => {
    try {
        const response = await axios.delete(`${API_URL}/auth/user/${username}`, { withCredentials: true });
        return { success: true, data: response.data };
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            // Return a specific error message if available
            return { success: false, error: error.response || 'Failed to delete user' };
        }
        // Return a general error message if the error structure is unexpected
        return { success: false, error: 'Request failed' };
    }
};

