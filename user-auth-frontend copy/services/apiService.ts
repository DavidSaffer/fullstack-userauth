// services/apiService.ts
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const loginUser = async (username: string, password: string) => {
    try {
        const response = await axios.post(`${API_URL}/auth/login`, { username, password });
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
        const response = await axios.post(`${API_URL}/auth/signup`, { username, password, email, phone });
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
        const response = await axios.post(`${API_URL}/auth/validate-token`, token);
        return response.data
    } catch (error: any) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.message || 'Failed to signup');
        }
        throw new Error('Failed to signup');
    }

}
