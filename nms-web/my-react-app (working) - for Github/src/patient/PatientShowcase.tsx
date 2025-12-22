import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from './profile.module.css';
import PatientCard from "./PatientCards";
import { withAuth } from "../../utils/withAuth";
import LoadingScreen from "../reusable/Loading/loadingScreen";

interface PatientCardData {
    id: number;
    firstname: string;
    lastname: string;
}

interface ApiPatient {
    patientID: number;
    doctorID: number;
    firstName: string;
    lastName: string;
    phone?: string;
    eircode?: string;
}

async function getUser() {
    try {
        const res = await fetch("/api/web/me", {
            method: "GET",
            credentials: "include",
        });
        if (!res.ok) {
            console.log("Failed to fetch /me");
            return null;
        }
        return await res.json();
    } catch (err) {
        console.error("Fetch error:", err);
        return null;
    }
}

async function fetchDoctorsPatients(doctorId: number): Promise<ApiPatient[]> {
    try {
        const res = await fetch(`/api/doctor/patients?id=${doctorId}`);
        if (!res.ok) throw new Error("Failed to fetch patients");
        return await res.json();
    } catch (err) {
        console.error(err);
        return [];
    }
}

const PatientShowcase: React.FC = () => {
    const [patients, setPatients] = useState<PatientCardData[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
  let isMounted = true;

  const fetchData = async () => {
    try {
      const user = await getUser();
      if (!user) return;

      const data = await fetchDoctorsPatients(user.userID);
      if (!isMounted) return;

      setPatients(data.map(p => ({
        id: p.patientID,
        firstname: p.firstName,
        lastname: p.lastName,
      })));
    } catch (err) {
      console.error(err);
    } finally {
      if (isMounted) setLoading(false);
    }
  };

  fetchData();
  return () => { isMounted = false; };
}, []);


    return (
        <div className={styles.dashContainer}>
            
            <div className={styles.background}>
                <h1 className={styles.heading}>Your patients</h1>

                {loading ? (
                    <LoadingScreen />
                ) : patients.length === 0 ? (
                    <p className={styles.noPatients}>No patients found.</p>
                ) : (
                    <div className={styles.allCards}>
                    {patients.map((item) => (
                        <PatientCard key={item.id} patient={item} />
                    ))}
                    </div>
                )}
            </div>
            
        </div>
    );
};

export default withAuth(PatientShowcase, ["doctor"]);