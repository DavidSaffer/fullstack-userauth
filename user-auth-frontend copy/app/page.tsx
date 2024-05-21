'use client';

import React from 'react';
import Cookies from 'js-cookie';
import { useRouter } from 'next/navigation';
import { logoutUser } from '@/services/apiService';

const HomePage: React.FC = () => {
    const router = useRouter();

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

    return (
      <div>
        <h1>Welcome to the Home Page</h1>
        <p>You are logged in.</p>
        <button onClick={handleLogout}>Logout</button>
      </div>
    );
};

export default HomePage;
