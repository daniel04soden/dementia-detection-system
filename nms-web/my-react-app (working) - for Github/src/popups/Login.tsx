import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/logo.png";
import styles from "./pop.module.css";

const LoginPopUp: React.FC = () => {
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
        navigate("/dashboard");
        window.location.reload();
      } else {
        const txt = await response.text();
        alert(txt);
      }
    } catch (error) {
      console.error("Login failed:", error);
      alert("Network error");
    }
  };

  const handleSignUp = () => {
    navigate("/signup");
  };

  return (
    <div className={styles.page}>
    <main className={styles.main}>
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

          <p className={styles.accountText}>Don't have an account?</p>
          <button type="button" className={styles.accountBtn} onClick={handleSignUp}>
            Sign up
          </button>
        </form>
      </div>
    </div>
    </main>
  </div>
  );
};

export default LoginPopUp;
