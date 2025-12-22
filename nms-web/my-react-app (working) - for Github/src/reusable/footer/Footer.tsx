import React from "react";
import styles from "./footer.module.css";

const Footer: React.FC = () => {
  return (
    <div className={styles.footerWrapper}>
      <div className={styles.footerContent}>
        <span>© 2025 Dementia & Alzheimer's Foundation</span>
        <nav>
          <ul className={styles.footerNavList}>
            <li><a href="/about" className={styles.footerNavItem}>About</a></li>
            <li><a href="/contact" className={styles.footerNavItem}>Contact</a></li>
          </ul>
        </nav>
      </div>
    </div>
  );
};

export default Footer;
