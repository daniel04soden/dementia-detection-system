import React from "react";
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

  const handleLogout = () => {
    alert("Logged out successfully!");
    navigate("/login");
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
