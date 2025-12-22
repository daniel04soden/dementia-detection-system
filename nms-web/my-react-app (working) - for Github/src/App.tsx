import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./popups/Login";
import SignupPage from "./popups/Signup";
import Stage1View from "./stages/1readonly";
import Stage1Edit from "./stages/1editable";
import ShowDashboard from "./dashboard/dash"
import PatientShowcase from "./patient/PatientShowcase";
import PatientProfile from "./patient/PatientProfile";
import './testing.module.css';
import AdminDash from "./dashboard/admin_dash";
import AdminSignUp from "./admin/admin_signup";
import EditClinic from "./admin/cuClinic";
import EditPatient from "./admin/EditPatient";
import EditDoctor from "./admin/cuDoctor";
import Stage2View from "./stages/2readonly";
import Reviews from "./HeaderPages/reviews";
import NotFound from "./reusable/NotFound/notFound";
import PageLayout from "./reusable/PageLayout/pageLayout";
import EditLifestyleReview from "./patient/EditLifestyleReview";
import AboutUs from "./HeaderPages/aboutUs";
import ContactUs from "./HeaderPages/contactUs";

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
      <Route element={<PageLayout />}>

        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/dashboard" element={<ShowDashboard />} />
        <Route path="/reviews" element={<Reviews />} />
        <Route path="/about" element={<AboutUs />} />
        <Route path="/contact" element={<ContactUs />} />
        <Route path="/grading/one/:testId" element={<Stage1Edit />} />
        <Route path="/review/one/:testId" element={<Stage1View />} />
        <Route path="/review/two/:testId" element={<Stage2View />} />
        <Route path="/user/:testId" element={<PatientProfile />} />
        <Route path="/patients" element={<PatientShowcase />} />
        <Route path="/admin/signup" element={<AdminSignUp />} />
        <Route path="/admin/dashboard" element={<AdminDash />} />
        <Route path="/admin/create/Clinic" element={<EditClinic />} />
        <Route path="/admin/update/Clinic/:id" element={<EditClinic />} />
        <Route path="/admin/create/doctor" element={<EditDoctor />} />
        <Route path="/admin/update/doctor/:id" element={<EditDoctor />} />
        <Route path="/admin/create/patient" element={<EditPatient />} />
        <Route path="/admin/update/patient/:id" element={<EditPatient />} />
        <Route path="*" element={<NotFound />} />
        <Route path="/admin/update/Patient/:id" element={<EditPatient />} />
        <Route path="/admin/update/Doctor/:id" element={<EditDoctor />} />
        <Route path="/patients/:testId/lifestyle/edit" element={<EditLifestyleReview />}/>

        </Route>
      </Routes>
    </Router>
  );
};

export default App;
