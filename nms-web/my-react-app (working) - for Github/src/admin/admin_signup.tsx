import React, { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from '../assets/logo.png';
import styles from "../popups/pop.module.css";
import { withAuth } from "../../utils/withAuth";

const AdminSignUp: React.FC = () => {
  const navigate = useNavigate();

  const [error, setError] = useState<string | null>(null);

  const goToLogin = () => {
    navigate("/admin/login");
  };

  const handleSignUp = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.target as HTMLFormElement;
    const formData = new FormData(form);
    const formJson: Record<string, any> = Object.fromEntries(formData.entries());

    formJson.isAdmin = true;

    try {
      const response = await fetch("/api/web/admin/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formJson),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("Admin created:", data);
        goToLogin();
      } else {
        const errorText = await response.text();
        setError(errorText);
      }
    } catch (err) {
      console.error("Request failed:", err);
      setError("Network error");
    }
  };

  return (
    <div className={styles.screen}>
      <div className={styles.accountPopup}>
        <img className={styles.logoPopup} src={logo} alt="Logo" />
        <form className={styles.accountForm} method="post" onSubmit={handleSignUp}>
          <label id={styles.topField} className={styles.inputLabel}>
            Email:
            <input type="email" name="email" placeholder="Email" className={styles.inputField} required />
          </label>
          <label className={styles.inputLabel}>
            Telephone:
            <input type="tel" name="phone" placeholder="Phone Number" className={styles.inputField} required />
          </label>
          <label className={styles.inputLabel}>
            First Name:
            <input type="text" name="firstName" placeholder="First Name" className={styles.inputField} />
          </label>
          <label className={styles.inputLabel}>
            Last Name:
            <input type="text" name="lastName" placeholder="Last Name" className={styles.inputField} />
          </label>
          <label className={styles.inputLabel}>
            Password:
            <input type="password" name="password" placeholder="Password" className={styles.inputField} required />
          </label>
          <label className={styles.inputLabel}>
            Confirm Password:
            <input type="password" name="confirmPassword" placeholder="Confirm Password" className={styles.inputField} required />
          </label>

          <button type="submit" className={styles.accountBtn}>Sign up</button>

          {error && <p className={styles.error}>{error}</p>}

          <p className={styles.accountText}>Already have an account?</p>
          <button type="button" className={styles.accountBtn} onClick={goToLogin}>Login</button>
        </form>
      </div>
    </div>
  );
};

export default withAuth(AdminSignUp, ["admin"]);