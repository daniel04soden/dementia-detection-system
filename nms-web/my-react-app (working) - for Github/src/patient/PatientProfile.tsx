import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styles from './profile.module.css';
import { withAuth } from "../../utils/withAuth.tsx";
import { generatePdf } from "./reportGenerator.tsx";
import NotFound from "../reusable/NotFound/notFound.tsx";
import LoadingScreen from "../reusable/Loading/loadingScreen.tsx";

interface PatientData {
  patientID: number;
  firstName: string;
  lastName: string;
}

interface LifestyleResponse {
  lifestyleID: number;
  lifestyleStatus: number;
  patientID: number;
  diabetic: number;
  alcoholLevel: number;
  heartRate: number;
  bloodOxygen: number;
  bodyTemperature: number;
  weight: number;
  mriDelay: number;
  age: number;
  dominantHand: number;
  gender: number;
  familyHistory: number;
  smoked: number;
  apoe4: number;
  physicalActivity: string;
  depressionStatus: number;
  cognitiveTestScores: number;
  medicationHistory: number;
  nutritionDiet: string;
  sleepQuality: number;
  chronicHealthConditions: string;
  education: string;
  dementiaStatus: string;
  firstName?: string;
  lastName?: string;
}

// Helpers
async function getUser() {
  try {
    const res = await fetch("/api/web/me", { credentials: "include" });
    if (!res.ok) return null;
    return await res.json();
  } catch (err) {
    console.error("Fetch /me failed:", err);
    return null;
  }
}

async function fetchPatients(): Promise<PatientData[]> {
  try {
    const res = await fetch(`/api/patients`);
    if (!res.ok) throw new Error("Failed to fetch patients");
    return await res.json();
  } catch (err) {
    console.error(err);
    return [];
  }
}

async function fetchLifestyle(patientID: number): Promise<LifestyleResponse | null> {
  try {
    const res = await fetch(`/api/lifestyle/review?id=${patientID}`);
    if (!res.ok) return null;
    return await res.json();
  } catch (err) {
    console.error("Fetch lifestyle failed:", err);
    return null;
  }
}

const PatientProfile: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState<LifestyleResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [notFound, setNotFound] = useState(false);


  useEffect(() => {
  let isMounted = true;

  const fetchData = async () => {
    if (!testId) return;

    const patientID = parseInt(testId, 10);
    const me = await getUser();
    if (!me) { 
      setLoading(false); 
      return; 
    }

    if (me.role !== "doctor" && me.role !== "admin") { 
      navigate("/unauthorized"); 
      return; 
    }

    const patients = await fetchPatients();
    const patient = patients.find(p => p.patientID === patientID);
    if (!patient) { 
      setNotFound(true); 
      setLoading(false); 
      return; 
    }

    const lifestyle = await fetchLifestyle(patientID);
    if (!lifestyle) { 
      setNotFound(true); 
      setLoading(false); 
      return; 
    }

    if (isMounted) {
      setUserInfo({ ...lifestyle, firstName: patient.firstName, lastName: patient.lastName });
      setLoading(false);
    }
  };

  fetchData();
  return () => { isMounted = false; };
}, [testId, navigate]);

  if (loading) return <LoadingScreen />;
  if (!userInfo) return <NotFound />;

  const { age, gender, firstName, lastName, education, patientID, dominantHand, lifestyleID, lifestyleStatus, ...restData } = userInfo;
  const booleanText = (value: number | string) => {
    if (value === 0 || value === "0") return "Not Present";
    if (value === 1 || value === "1") return "Present";
    return value;
  };

  const handText = (value: number) => {
    if (value === 0) return "Left";
    if (value === 1) return "Right";
    return "Unknown";
  };

  const genderText = (value: number) => {
    if (value === 0) return "Female";
    if (value === 1) return "Male";
    return "Unknown";
  };

  console.log(userInfo)
  
  return (
    <div className={styles.background}>
      
      <main className={styles.profileMain}>
  <section className={styles.heroSection}>
    <h1 className={styles.heroTitle}>Patient Profile</h1>
    <p className={styles.heroSubtitle}>
      Detailed information for {userInfo.firstName} {userInfo.lastName}
    </p>
  </section>

  <div className={styles.profileGrid}>
    {/* Left column: personal info */}
    <section className={styles.personalInfo}>
      <h2>Personal Information</h2>
      <div className={styles.infoGrid}>
      <div >
        <span className={styles.dataKey}>Patient ID: </span>
        <span className={styles.dataValue}>{userInfo.patientID}</span>
      </div>
      <div >
        <span className={styles.dataKey}>Name: </span>
        <span className={styles.dataValue}>{userInfo.firstName} {userInfo.lastName}</span>
      </div>
      <div>
        <span className={styles.dataKey}>Gender: </span>
        <span className={styles.dataValue}>{genderText(userInfo.gender)}</span>
      </div>
      <div >
        <span className={styles.dataKey}>Age: </span>
        <span className={styles.dataValue}>{userInfo.age}</span>
      </div>
      <div>
        <span className={styles.dataKey}>Education: </span>
        <span className={styles.dataValue}>{userInfo.education}</span>
      </div>
      <div>
        <span className={styles.dataKey}>Dominant Hand: </span>
        <span className={styles.dataValue}>{handText(userInfo.dominantHand)}</span>
      </div>
      </div>
    </section>

    {/* Right column: lifestyle info */}
    <section className={styles.lifestyleInfo}>
      <h2>Lifestyle & Medical Data</h2>
      <div className={styles.lifestyleGrid}>
        {Object.entries(restData).map(([key, value]) => {
          const booleanFields = ["diabetic", "familyHistory", "smoked", "apoe4", "depressionStatus"];
          const displayValue = booleanFields.includes(key) ? booleanText(Number(value)) : value;
          return (
            <div key={key} className={styles.dataRow}>
              <span className={styles.dataKey}>{key.replace(/([A-Z])/g, ' $1')}</span>
              <span className={styles.dataValue}>{displayValue}</span>
            </div>
          );
        })}
      </div>
    </section>
  </div>

  <button
    className={styles.pdfBtn}
    onClick={() => generatePdf(userInfo)}
  >
    Generate PDF Report
  </button>

  <button
    className={styles.pdfBtn}
    onClick={() => navigate(`/patients/${userInfo.patientID}/lifestyle/edit`)}
  >
    Edit Lifestyle Content
  </button>

</main>
      
    </div>
  );
};

export default withAuth(PatientProfile, ["doctor"]);