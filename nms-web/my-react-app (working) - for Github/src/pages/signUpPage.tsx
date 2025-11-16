import React from "react";
import { useNavigate } from "react-router-dom";
import styles from './dash.module.css';
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import SignUpPopUp from "../popups/Signup";

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
    return (
        <>
      <Header />
      <SignUpPopUp/>
      <Footer />
      </>
  );
};

export default LoginPage;