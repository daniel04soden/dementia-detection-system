import React, { FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import logo from '../assets/logo.png';
import styles from './pop.module.css';

const SignUpPopUp: React.FC = () => {
  /*const navigate = useNavigate();

    const handleSignUp = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.target as HTMLFormElement;
    const formData = new FormData(form);

    try {
      const response = await fetch("http://10.0.0.1:8080/api/web/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        const data = await response.json();
        console.log("User created:", data);
      } else {
        console.error("Error:", await response.text());
      }
    } catch (err) {
      console.error("Request failed:", err);
    }
  };*/

  const handleSignUp = (e: FormEvent<HTMLFormElement>) => {
    // Prevent the default form submission behavior (page reload)
    e.preventDefault();

    // Get the form data
    const form = e.target as HTMLFormElement;
    const formData = new FormData(form);

    // Convert the form data to an object
    const formJson = Object.fromEntries(formData.entries());

    console.log(formJson)
    
    var count = 0;
    for (const key in formJson) {
        console.log(`${key}: ${formJson[key]}`);
        if(formJson[key] == "pass"){count++}
    }
  }


  return (
    <div className={styles.screen}>
    <div className={styles.accountPopup}>
        <img
            className={styles.logoPopup}
            src={logo}
            alt="Logo"
          />

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
            <input type="text" name="doctor" placeholder="Doctor Number" className={styles.inputField} required />
          </label>

          <label className={styles.inputLabel}>
            First Name:
            <input type="text" name="first_name" placeholder="First Name" className={styles.inputField} />
          </label>

          <label className={styles.inputLabel}>
            Last Name:
            <input type="text" name="last_name" placeholder="Last Name" className={styles.inputField} />
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
            <select id={styles.bottomField} name="clinic" className={styles.inputField}>
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
          >
            Login
          </button>
        </form>
      </div>
      </div>
  );
};

export default SignUpPopUp;
