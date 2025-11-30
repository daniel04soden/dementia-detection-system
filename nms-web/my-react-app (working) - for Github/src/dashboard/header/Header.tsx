import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./header.module.css";
import logo from '../../assets/da.svg';
import pfp from '../../assets/temp_pfp.png';

const Header: React.FC = () => {
  const navigate = useNavigate();

  const handleHomePage = () => {
    navigate("/dashboard");
  };

  const handleProfilePage = () => {
    navigate("/patients");
  };

  const handleLogout = async () => {
    try {
      // Make a request to the backend to log the user out
      const response = await fetch('/api/web/logout', {
        method: 'POST',  // You can use GET or POST depending on your backend setup
        credentials: 'include',  // Ensure cookies are sent with the request
      });

      if (response.ok) {
        // Optionally, handle success (e.g., redirect, show message, etc.)
        console.log('Logged out successfully');
        // Redirect user to login page or home page
        window.location.href = '/login';  // Or whatever your login page route is
      } else {
      }
    } catch (err) {
      console.error('Logout error:', err);
    }
  };


  return (
      <header className={styles.dashHeader}>
        <nav className={styles.nav}>
        <ul className={styles.navList} >
            <li><img
                className={styles.logoStyling}
                src={logo}
                alt="Logo"
                onClick={handleHomePage}>
                </img></li>
            <li><a className={styles.navBtn} onClick={handleHomePage}>Dashboard</a></li>
            <li><a className={styles.navBtn} onClick={handleProfilePage}>Patients</a></li>
            <li><a className={styles.navBtn} href="">About</a></li>
            <li><a className={styles.navBtn} href="">Contact</a></li>
            <li><div className={styles.profileSection}>
                    <img
                    src={pfp}
                    alt="User"
                    className={styles.avatar}/>
                    <a className={styles.logoutBtn} onClick={handleLogout}>Logout</a>
                </div></li>
        </ul>
        </nav>
      </header>
  );
};

export default Header;
