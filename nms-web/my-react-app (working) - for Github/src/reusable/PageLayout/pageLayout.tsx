import React from "react";
import Header from "../header/Header";
import Footer from "../footer/Footer";
import styles from "./pageLayout.module.css";
import { Outlet } from "react-router-dom";

const PageLayout: React.FC = () => {
  return (
    <div className={styles.page}>
      <Header />
      <main className={styles.main}>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default PageLayout;
