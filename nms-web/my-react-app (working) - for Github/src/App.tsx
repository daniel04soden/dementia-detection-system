import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/loginPage";
import SignupPage from "./pages/signUpPage";
import Dashboard from "./dashboard/Dashboard";
import Stage1 from "./stages/Stage1";
import Stage2 from "./stages/Stage2";
import ReviewStage1 from "./stages/ReviewStage1";
import ReviewStage2 from "./stages/ReviewStage2";
import PatientShowcase from "./profiles/PatientShowcase";
import PatientProfile from "./profiles/templates/PatientProfile";
import './testing.module.css';

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/initial" element={<Stage1 />} />
        <Route path="/secondary" element={<Stage2 />} />
        <Route path="/patients" element={<PatientShowcase />} />
        <Route path="/review/:testId" element={<ReviewStage1 />} />
        <Route path="/next/:testId" element={<ReviewStage2 />} />
        <Route path="/user/:testId" element={<PatientProfile />} />
      </Routes>
    </Router>
  );
};

export default App;
