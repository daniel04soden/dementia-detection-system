import React from "react";
import styles from "./LoadingScreen.module.css";

const LoadingScreen: React.FC = () => {
  return (
    <div className={styles.background}>
      <div className={styles.card}>
        <div className={styles.spinner} />
        <h2>Loading</h2>
        <p>Please wait while we fetch your data…</p>
      </div>
    </div>
  );
};

export default LoadingScreen;
