import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styles from '../profile.module.css';
import Header from "../../dashboard/header/Header.tsx";
import Footer from "../../dashboard/footer/Footer.tsx";
import { withAuth } from "../../../utils/withAuth";

// API return types
interface PatientData {
  patientID: number;
  firstName: string;
  lastName: string;
}

interface LifestyleData {
  lifestyleID: number;
  patientID: number;
  gender?: string; // make optional to handle type mismatches
  age?: number;
  dHand?: number;
  weight?: number;
  avgTemp?: number;
  restingHr?: number;
  oxLv?: number;
  history?: boolean;
  smoke?: boolean;
  apoe?: boolean;
  activityLv?: string;
  depressed?: boolean;
  diet?: string;
  goodSleep?: boolean;
  edu?: string;
}

// Front-end state type
interface TestData {
  id: number;
  firstname: string;
  lastname: string;
  gender: string;
  age: number;
  dHand: number;
  weight: number;
  avgTemp: number;
  restingHR: number;
  boxLv: number;
  history: boolean;
  smoke: boolean;
  apoeGene: boolean;
  activityLv: string;
  depressed: boolean;
  diet: string;
  goodSleep: boolean;
  edu: string;
}

// Helper to fetch current logged-in user
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

// Helper to fetch patients for a doctor
async function fetchDoctorsPatients(doctorId: number): Promise<PatientData[]> {
  try {
    const res = await fetch(`/api/web/fetchpatients?id=${doctorId}`);
    if (!res.ok) throw new Error("Failed to fetch patients");
    return await res.json();
  } catch (err) {
    console.error(err);
    return [];
  }
}

// Helper to fetch lifestyle data
async function fetchLifestyle(patientID: number): Promise<LifestyleData | null> {
  try {
    const res = await fetch(`/api/web/lifestyle?id=${patientID}`);
    if (!res.ok) {
      console.error(`Lifestyle fetch returned ${res.status} for patient ${patientID}`);
      return null;
    }
    return await res.json();
  } catch (err) {
    console.error("Fetch lifestyle failed:", err);
    return null;
  }
}

const PatientProfile: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState<TestData | null>(null);
  const [loading, setLoading] = useState(true);

  const getApplicableRisks = (user: TestData) => {
    const risks: string[] = [];
    if (user.history) risks.push("Family history of dementia");
    if (user.apoeGene) risks.push("Carries the APOE_4 gene");
    if (user.smoke) risks.push("Smoking (increases heart disease and other risks)");
    if (user.depressed) risks.push("History of depression (may contribute to cognitive decline)");
    if (!user.goodSleep) risks.push("Poor quality sleep (can affect mental health)");
    return risks;
  };

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

      const patients = await fetchDoctorsPatients(me.userID);
      const patient = patients.find(p => p.patientID === patientID);
      if (!patient) {
        console.error("Patient not found");
        setLoading(false);
        return;
      }

      const lifestyle = await fetchLifestyle(patientID);

      // Map to front-end state safely
      const mappedData: TestData = {
        id: patient.patientID,
        firstname: patient.firstName,
        lastname: patient.lastName,
        gender: lifestyle?.gender ?? "Unknown",
        age: lifestyle?.age ?? 0,
        dHand: lifestyle?.dHand ?? 0,
        weight: lifestyle?.weight ?? 0,
        avgTemp: lifestyle?.avgTemp ?? 0,
        restingHR: lifestyle?.restingHr ?? 0,
        boxLv: lifestyle?.oxLv ?? 0,
        history: lifestyle?.history ?? false,
        smoke: lifestyle?.smoke ?? false,
        apoeGene: lifestyle?.apoe ?? false,
        activityLv: lifestyle?.activityLv ?? "Unknown",
        depressed: lifestyle?.depressed ?? false,
        diet: lifestyle?.diet ?? "Unknown",
        goodSleep: lifestyle?.goodSleep ?? true,
        edu: lifestyle?.edu ?? "Unknown",
      };

      if (isMounted) {
        setUserInfo(mappedData);
        setLoading(false);
      }
    };

    fetchData();

    return () => {
      isMounted = false;
    };
  }, [testId, navigate]);

  if (loading) return <p>Loading patient data...</p>;
  if (!userInfo) return <p>Patient data not found.</p>;

  return (
    <div className={styles.dashContainer}>
      <Header />
      <main className={styles.mainContent}>
        <h1>{userInfo.firstname} {userInfo.lastname}</h1>
        <p>Gender: {userInfo.gender}</p>
        <p>Age: {userInfo.age}</p>

        <h2>Noted Risks</h2>
        <ul>
          {getApplicableRisks(userInfo).length > 0 ? (
            getApplicableRisks(userInfo).map((risk, idx) => <li key={idx}>{risk}</li>)
          ) : (
            <li>No significant risks.</li>
          )}
        </ul>
      </main>
      <Footer />
    </div>
  );
};

export default withAuth(PatientProfile, ["doctor", "admin"]);
