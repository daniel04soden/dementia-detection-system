import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/loginPage";
import SignupPage from "./pages/signUpPage";
import Dashboard from "./dashboard/Dashboard";


import Stage1 from "./stages/Stage1";
import Stage2 from "./stages/Stage2";

import GradeDashboard from "./dashboard/gradeDashboard";

import GradingStage1 from "./stages/gradingStage1";
import ReviewStage1 from "./stages/reviewingStage1";


import PatientShowcase from "./profiles/PatientShowcase";
import PatientProfile from "./profiles/templates/PatientProfile";
import './testing.module.css';
import AdminDash from "./admin/admin_dash";
import AdminSignUp from "./admin/admin_signup";
import AdminLogin from "./admin/admin_login";
import { TestAuto1 } from "../utils/auto1";
import { TestAuto2 } from "../utils/auto2";

import EditClinic from "./admin/cuClinic";
import EditPatient from "./admin/EditPatient";
import EditDoctor from "./admin/cuDoctor";
import GradingStage2 from "./stages/gradingStage2";
import ReviewStage2 from "./stages/ReviewStage2";

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/dashboard" element={<GradeDashboard />} />
        <Route path="/initial" element={<Stage1 />} />
        <Route path="/secondary" element={<Stage2 />} />

        
        <Route path="/grading/one/:testId" element={<GradingStage1 />} />
        <Route path="/review/one/:testId" element={<ReviewStage1 />} />

        <Route path="/grading/two/:testId" element={<GradingStage2 />} />
        <Route path="/review/two/:testId" element={<ReviewStage2 />} />


        <Route path="/user/:testId" element={<PatientProfile />} />
        <Route path="/patients" element={<PatientShowcase />} />

        <Route path="/admin/signup" element={<AdminSignUp />} />
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route path="/admin/dashboard" element={<AdminDash />} />

        <Route path="/admin/create/Clinic" element={<EditClinic />} />
        <Route path="/admin/update/Clinic/:id" element={<EditClinic />} />

        <Route path="/admin/create/doctor" element={<EditDoctor />} />
        <Route path="/admin/update/doctor/:id" element={<EditDoctor />} />

        <Route path="/admin/create/patient" element={<EditPatient />} />
        <Route path="/admin/update/patient/:id" element={<EditPatient />} />


        


        <Route path="/admin/update/Patient/:id" element={<EditPatient />} />
        <Route path="/admin/update/Doctor/:id" element={<EditDoctor />} />

        <Route path="/demotest" element={<TestAuto1 />}/>
        <Route path="/demotestmore" element={<TestAuto2 />}/>
      </Routes>
    </Router>
  );
};

export default App;
