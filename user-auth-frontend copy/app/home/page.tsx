'use client';

import React from 'react';
import Cookies from 'js-cookie';
import { useRouter } from 'next/navigation';

const HomePage: React.FC = () => {
    const router = useRouter();

    const handleLogout = () => {
        // Remove the JWT cookie
        Cookies.remove('jwt');
        
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
