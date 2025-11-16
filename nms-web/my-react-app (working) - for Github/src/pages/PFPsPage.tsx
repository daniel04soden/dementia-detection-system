import React from "react";
import { useNavigate } from "react-router-dom";
import PatientProfile from "../profiles/PatientCards.tsx";
import Header from "../dashboard/header/Header.tsx";
import styles from "../profiles/profile.module.css";

const ProfilePage: React.FC = () => {
  const navigate = useNavigate();

  const handleNewTest = () => {
    navigate("/initial");

  };

    const sampleTestData = [
        { "firstName": "John", "lastName": "Smith" },
        { "firstName": "Mary", "lastName": "Johnson" },
        { "firstName": "Robert", "lastName": "Brown" },
        { "firstName": "Patricia", "lastName": "Davis" },
        { "firstName": "Michael", "lastName": "Miller" },
        { "firstName": "Linda", "lastName": "Wilson" },
        { "firstName": "William", "lastName": "Moore" },
        { "firstName": "Barbara", "lastName": "Taylor" },
        { "firstName": "James", "lastName": "Anderson" },
        { "firstName": "Elizabeth", "lastName": "Thomas" },
        { "firstName": "David", "lastName": "Jackson" },
        { "firstName": "Jennifer", "lastName": "White" },
        { "firstName": "Richard", "lastName": "Harris" },
        { "firstName": "Susan", "lastName": "Martin" },
        { "firstName": "Joseph", "lastName": "Thompson" }
    ]


  return (
    <>
    <Header />
    <h1>Patients:</h1>
    <div className={styles.allProfiles}>
    {sampleTestData.map((patient, index) => (
        <PatientProfile key={index} patient={patient} />
      ))}
    </div>
    </>
  );
};

export default ProfilePage;
