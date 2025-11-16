import React from "react";
import { useNavigate } from "react-router-dom";
import styles from './profile.module.css';
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import PatientCard from "./templates/PatientCards";

const PatientShowcase: React.FC = () => {
  const navigate = useNavigate();


const patients = [
        { id: 1, firstname: "John",		 lastname: "Smith"},
        { id: 2, firstname: "Mary",		 lastname: "Johnson"},
        { id: 3, firstname: "Robert",	 lastname: "Brown"},
        { id: 4, firstname: "Patricia",	 lastname: "Davis"},
        { id: 5, firstname: "Michael",	 lastname: "Miller"},
        { id: 6, firstname: "Linda",	 lastname: "Wilson"},
        { id: 7, firstname: "William",	 lastname: "Moore"},
        { id: 8, firstname: "Barbara",	 lastname: "Taylor"},
        { id: 9, firstname: "James",	 lastname: "Anderson"},
        { id: 10, firstname: "Elizabeth", lastname: "Thomas"},
        { id: 11, firstname: "David",	 lastname: "Jackson"},
        { id: 12, firstname: "Jennifer", lastname: "White"},
        { id: 13, firstname: "Richard",	 lastname: "Harris"},
        { id: 14, firstname: "Susan",	 lastname: "Martin"},
        { id: 15, firstname: "Joseph",	 lastname: "Thompson"},

        { id: 7, firstname: "William",	 lastname: "Moore"},
        { id: 8, firstname: "Barbara",	 lastname: "Taylor"},
        { id: 9, firstname: "James",	 lastname: "Anderson"},
        { id: 10, firstname: "Elizabeth", lastname: "Thomas"},
        { id: 11, firstname: "David",	 lastname: "Jackson"},
        { id: 12, firstname: "Jennifer", lastname: "White"},
        { id: 13, firstname: "Richard",	 lastname: "Harris"},
        { id: 14, firstname: "Susan",	 lastname: "Martin"},
        { id: 15, firstname: "Joseph",	 lastname: "Thompson"},
        { id: 7, firstname: "William",	 lastname: "Moore"},
        { id: 8, firstname: "Barbara",	 lastname: "Taylor"},
        { id: 9, firstname: "James",	 lastname: "Anderson"},
        { id: 10, firstname: "Elizabeth", lastname: "Thomas"},
        { id: 11, firstname: "David",	 lastname: "Jackson"},
        { id: 12, firstname: "Jennifer", lastname: "White"},
        { id: 13, firstname: "Richard",	 lastname: "Harris"},
        { id: 14, firstname: "Susan",	 lastname: "Martin"},
        { id: 15, firstname: "Joseph",	 lastname: "Thompson"}
    ]
  return (
    <div className={styles.dashContainer}>
      <Header />
      <div className={styles.background}>
      <h1 className={styles.heading}>Your patients</h1>
      <div className={styles.allCards}>
      {patients.map((item, index) => (
            <PatientCard key={index} patient={item}/>
        ))}
      </div>
      </div>
    <Footer />
    </div>
  )
}

export default PatientShowcase;

