import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./footer.module.css";

const Footer: React.FC = () => {
  return (
    <div>
      <br />
      <br />
      <div className={styles.footer}></div>
    </div>
  );
};

export default Footer;
