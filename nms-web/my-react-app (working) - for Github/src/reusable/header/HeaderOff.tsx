import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./header.module.css";
import logo from '../../assets/da.svg';

const HeaderOff: React.FC = () => {
  const navigate = useNavigate();

  const handleHomePage = () => navigate("/dashboard");
  const handleAboutPage = () => navigate("/about");
  const handleContactPage = () => navigate("/contact");

  return (
    <header className={styles.dashHeader}>
      <div className={styles.leftSection}>
        <img src={logo} alt="Logo" className={styles.logoStyling} />
        <span className={styles.newTitle} onClick={handleHomePage}>
          Dementia & Alzheimer's Foundation
        </span>
        <button className={styles.navBtn} onClick={handleAboutPage}>About</button>
        <button className={styles.navBtn} onClick={handleContactPage}>Contact</button>
      </div>
    </header>
  );
};

export default HeaderOff;
