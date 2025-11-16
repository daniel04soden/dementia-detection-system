import React from "react";
import { useNavigate } from "react-router-dom";
import styles from './dash.module.css';
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import LoginPopUp from "../popups/Login";

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
    return (
        <>
      <LoginPopUp/>
      <Footer />
      </>
  );
};

export default LoginPage;