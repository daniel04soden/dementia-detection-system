import React from "react";
import { useNavigate } from "react-router-dom";
import styles from "../profile.module.css";
import pfp from '../../assets/temp_pfp.png';

interface Patient {
  id: number,
  firstname: string;
  lastname: string;
}

const PatientCard: React.FC<{ patient : Patient }> = ({ patient }) => {
  const navigate = useNavigate();
  const viewInfo = (testId: number) => {
    navigate(`/user/${testId-1}`);
  };
  return (
    <>
    <div className={styles.cardContainer} onClick={() => viewInfo(patient.id+1)}>
        <img className={styles.pfp} src={pfp}></img>
        <h1>{patient.firstname} {patient.lastname}</h1>
    </div>
    </>
  );
};

export default PatientCard;