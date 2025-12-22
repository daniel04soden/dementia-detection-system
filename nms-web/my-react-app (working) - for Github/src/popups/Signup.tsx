import React, { FormEvent, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import logo from '../assets/logo.png';
import styles from './pop.module.css';

export interface Clinic {
  clinicID: number;
  name: string;
  phone: string;
  eircode: string;
}

const SignUpPopUp: React.FC = () => {
  const navigate = useNavigate();

  const [clinics, setClinics] = useState<Clinic[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const goToLogin = () => {
    navigate("/login");
  };

  useEffect(() => {
    fetch("/api/clinics")
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch clinics");
        return res.json();
      })
      .then((data: Clinic[]) => {
        setClinics(data);
        setLoading(false);
      })
      .catch((err: Error) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  const handleSignUp = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.target as HTMLFormElement;
    const formData = new FormData(form);
    const formJson: Record<string, any> = Object.fromEntries(formData.entries());

    formJson.clinicID = Number(formJson.clinicID);

    try {
      const response = await fetch("/api/web/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formJson),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("User created:", data);
        goToLogin();
      } else {
        console.error("Error:", await response.text());
      }
    } catch (err) {
      console.error("Request failed:", err);
    }
  };

  return (
    <>
    
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
            Doctor Number:
            <input type="text" name="doctorNO" placeholder="Doctor Number" className={styles.inputField} required />
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
            <input type="password" name="password" placeholder="Password" className={styles.inputField} />
          </label>
          <label className={styles.inputLabel}>
            Confirm Password:
            <input type="password" placeholder="Confirm Password" className={styles.inputField} />
          </label>

          <label className={styles.inputLabel}>
            Primary Clinic:
            {loading ? (
              <p>Loading clinics...</p>
            ) : error ? (
              <p>Error: {error}</p>
            ) : (
              <select id={styles.bottomField} name="clinicID" className={styles.inputField}>
                {clinics.map((clinic) => (
                  <option key={clinic.clinicID} value={clinic.clinicID}>
                    {clinic.name}
                  </option>
                ))}
              </select>
            )}
          </label>

          <button type="submit" className={styles.accountBtn}>Sign up</button>
          <p className={styles.accountText}>Have an account?</p>
          <button type="button" className={styles.accountBtn} onClick={goToLogin}>Login</button>
        </form>
      </div>
    </div>
          
    </>
  );
};

export default SignUpPopUp;
