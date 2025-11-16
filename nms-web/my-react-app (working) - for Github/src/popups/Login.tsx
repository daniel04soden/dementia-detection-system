import React from "react";
import { useNavigate } from "react-router-dom";
import logo from '../assets/logo.png';
import styles from './pop.module.css';

const DUMMYDATA = new Map<string, string>([
      ["test1@example.com", "password123"],
      ["alice+junk@example.com", "alicepass"],
      ["bob.smith@example.com", "letmein!"],
      ["dev.user@example.local", "devpass2025"],
      ["R00246026@mymtu.ie", "mtu"],
      ["qa+login@example.com", "qapassword"],
    ]);

const LoginPopUp: React.FC = () => {
  const navigate = useNavigate();

  const handleLogin = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    
    const form = e.target as HTMLFormElement;
    const email = (form.elements.namedItem("email") as HTMLInputElement).value;
    const password = (form.elements.namedItem("password") as HTMLInputElement).value;

    const storedPassword = DUMMYDATA.get(email);

    if (!storedPassword || storedPassword !== password) {
      alert("incorrect email or password");
      return;
    }
    
    navigate("/dashboard");
  };

  const handleSignUp = () => {
    navigate("/signup");
  };


  return (
    <div className={styles.screen}>
    <div className={styles.accountPopup}>
          <img
            className={styles.logoPopup}
            src={logo}
            alt="Logo"
          />

        <form className={styles.accountForm} onSubmit={handleLogin}>
          <label id={styles.topField} className={styles.inputLabel}>
            Email:
            <input type="email" name="email" placeholder="Email" className={styles.inputField} required/>
          </label>

          <label className={styles.inputLabel}>
            Password:
            <input type="password" name="password" placeholder="Password" id={styles.bottomField} className={styles.inputField} required/>
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
      </div>
  );
};

export default LoginPopUp;
