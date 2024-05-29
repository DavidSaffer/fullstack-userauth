'use client';

import dynamic from 'next/dynamic';

const LoginForm = dynamic(() => import('../(components)/LoginForm'), { ssr: false });
const SignupForm = dynamic(() => import('../(components)/SignupForm'), { ssr: false });
// import LoginForm from '../(components)/LoginForm';
// import SignupForm from '../(components)/SignupForm';

import styles from './page.module.css';

// const LoginForm = () => <div>Login Form Static Content</div>;
// const SignupForm = () => <div>Signup Form Static Content</div>;

const AuthPage: React.FC = () => {
  return (
    <>
    <h1>Authentication</h1>
    <div className={styles.container}>
      
      <LoginForm />
      <SignupForm />
    </div>
    </>
  );
};

export default AuthPage;
