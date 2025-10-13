import React from "react";
import { useNavigate } from "react-router-dom";
import logo from '../assets/logo.png';
import styles from './pop.module.css';

const SignupPage: React.FC = () => {
  const navigate = useNavigate();

  const handleSignUp = () => {
  };

  const handleLogin = () => {
    navigate("/login");
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
            <input type="email" placeholder="Email" className={styles.inputField} required />
          </label>

          <label className={styles.inputLabel}>
            Telephone:
            <input type="tel" placeholder="Phone Number" className={styles.inputField} required />
          </label>

          <label className={styles.inputLabel}>
            First Name:
            <input type="text" placeholder="First Name" className={styles.inputField} />
          </label>

          <label className={styles.inputLabel}>
            Last Name:
            <input type="text" placeholder="Last Name" className={styles.inputField} />
          </label>

          <label className={styles.inputLabel}>
            Password:
            <input type="password" placeholder="Password" className={styles.inputField} />
          </label>

          <label className={styles.inputLabel}>
            Confirm Password:
            <input type="password" placeholder="Confirm Password" className={styles.inputField} />
          </label>

          <label className={styles.inputLabel}>
            Primary Clinic:
            <select id={styles.bottomField} className={styles.inputField}>
              <option value="">Mayo Clinic</option>
              <option value="cleveland">Cleveland Clinic</option>
              <option value="johns-hopkins">Johns Hopkins</option>
              <option value="ucla">UCLA Medical Center</option>
              <option value="other">Other</option>
            </select>
          </label>

          <button type="submit" className={styles.accountBtn}>Sign up</button>

          <p className={styles.accountText}>Have an account?</p>
          <button
            type="button"
            className={styles.accountBtn}
            onClick={handleLogin}
          >
            Login
          </button>
        </form>
      </div>
  );
};

export default SignupPage;
