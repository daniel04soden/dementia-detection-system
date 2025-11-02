import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./popups/Login";
import SignupPage from "./popups/Signup";
import Dashboard from "./dashboard/Dashboard";
import Stage1 from "./stages/Stage1";
import Stage2 from "./stages/Stage2";

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
      </Routes>
    </Router>
  );
};

export default App;
