import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "./NotFound.module.css";

const NotFound: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className={styles.background}>
      <div className={styles.card}>
        <h1 className={styles.code}>404</h1>
        <h2>Page Not Found</h2>
        <p>
          Sorry, the page you are looking for doesn't exist or has been moved.
        </p>

        <button
          className={styles.button}
          onClick={() => navigate("/dashboard")}
        >
          Go Home
        </button>
      </div>
    </div>
  );
};

export default NotFound;
