import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import styles from "../popups/pop.module.css";

const AdminLogin: React.FC = () => {
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
  e.preventDefault();

  const form = e.target as HTMLFormElement;
  const email = (form.elements.namedItem("email") as HTMLInputElement).value;
  const password = (form.elements.namedItem("password") as HTMLInputElement).value;

  try {
    const response = await fetch("/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({ email, password }),
    });

    if (response.ok) {
      const data = await response.json();
      console.log("Logged in:", data);
    }
  } catch (error) {
    console.error("Login failed:", error);
    alert("Network error");
  }
  navigate("/admin/dashboard")
};


  const handleSignUp = () => {
    navigate("/admin/signup"); // Navigate to the admin sign-up page
  };

  return (
    <div className={styles.screen}>
      <div className={styles.accountPopup}>
        <img className={styles.logoPopup} src={logo} alt="Logo" />

        <form className={styles.accountForm} onSubmit={handleLogin}>
          <label id={styles.topField} className={styles.inputLabel}>
            Email:
            <input type="email" name="email" placeholder="Email" className={styles.inputField} required />
          </label>

          <label className={styles.inputLabel}>
            Password:
            <input type="password" name="password" placeholder="Password" id={styles.bottomField} className={styles.inputField} required />
          </label>

          <button type="submit" className={styles.accountBtn}>Login</button>

          <a className={styles.forgot}>Forgot password?</a>

          <p className={styles.accountText}>Don't have an account?</p>
          <button type="button" className={styles.accountBtn} onClick={handleSignUp}>
            Sign up
          </button>
        </form>
      </div>
    </div>
  );
};

export default AdminLogin;
