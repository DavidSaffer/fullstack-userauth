'use client';

import LoginForm from '../(components)/LoginForm';
import SignupForm from '../(components)/SignupForm';

import styles from './page.module.css';

const AuthPage: React.FC = () => {
  return (
    <div className={styles.container}>
      <h1>Authentication</h1>
      <LoginForm />
      <SignupForm />
    </div>
  );
};

export default AuthPage;
