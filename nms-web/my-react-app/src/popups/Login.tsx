import React from "react";
import { useNavigate } from "react-router-dom";
import logo from '../assets/logo.png';
import styles from './pop.module.css';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();

  const handleLogin = () => {
    navigate("/dashboard");
  };

  const handleSignUp = () => {
    navigate("/signup");
  };


  return (
    <div className={styles.accountPopup}>
          <img
            className={styles.logoPopup}
            src={logo}
            alt="Logo"
          />

        <form className={styles.accountForm} onSubmit={handleLogin}>
          <label id={styles.topField} className={styles.inputLabel}>
            Email:
            <input type="email" placeholder="Email" className={styles.inputField} required/>
          </label>

          <label className={styles.inputLabel}>
            Password:
            <input type="password" placeholder="Password" id={styles.bottomField} className={styles.inputField} required/>
          </label>

          <button type="submit" className={styles.accountBtn}>Login</button>

          <a className={styles.forgot}>Forgot password?</a>

          <p className={styles.accountText}>
            Don't have an account?
          </p>
          <button
            type="button"
            className={styles.accountBtn}
            onClick={handleSignUp}
          >
            Sign up
          </button>
        </form>
      </div>
  );
};

export default LoginPage;
