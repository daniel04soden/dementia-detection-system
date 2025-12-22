import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./header.module.css";
import logo from '../../assets/da.svg';
import pfp from '../../assets/temp_pfp.png';

const HeaderAdmin: React.FC = () => {
  const navigate = useNavigate();

  const handleHomePage = () => navigate("/dashboard");
  const handleReviewPage = () => navigate("/reviews");
  const handleAboutPage = () => navigate("/about");
  const handleContactPage = () => navigate("/contact");

  const handleLogout = async () => {
    try {
      const response = await fetch('/api/web/logout', {
        method: 'POST',
        credentials: 'include',
      });
      if (response.ok) window.location.href = '/login';
    } catch (err) {
      console.error('Logout error:', err);
    }
  };

  return (
    <header className={styles.dashHeader}>
      <div className={styles.leftSection}>
        <img src={logo} alt="Logo" className={styles.logoStyling} />
      </div>

      <nav className={styles.nav}>
        <ul className={styles.navList}>
          <li><button className={styles.navBtn} onClick={handleHomePage}>Dashboard</button></li>
          <li><button className={styles.navBtn} onClick={handleReviewPage}>Review</button></li>
          <li><button className={styles.navBtn} onClick={handleAboutPage}>About</button></li>
          <li><button className={styles.navBtn} onClick={handleContactPage}>Contact</button></li>
        </ul>
      </nav>

      <div className={styles.profileSection}>
        <img src={pfp} alt="User" className={styles.avatar} />
        <button className={styles.logoutBtn} onClick={handleLogout}>Logout</button>
      </div>
    </header>
  );
};

export default HeaderAdmin;
