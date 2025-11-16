import React from "react";
import styles from "./profile.module.css";
import pfp from '../assets/temp_pfp.png';

interface Patient {
  firstName: string;
  lastName: string;
}

const PatientProfile: React.FC<{ patient : Patient }> = ({ patient }) => {
  return (
    <>
        <p>
            
        </p>
    </>
    );
};

export default PatientProfile;