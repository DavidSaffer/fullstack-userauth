// pages/index.tsx
import React from 'react';
import LoginForm from '../components/LoginForm';
import SignupForm from '../components/SignupForm';

const Home: React.FC = () => {
    return (
        <div>
            <h1>Welcome to the App</h1>
            <LoginForm />
            <SignupForm />
        </div> 
    );
};

export default Home;
