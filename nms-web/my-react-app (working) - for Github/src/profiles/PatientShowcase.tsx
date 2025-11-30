import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from './profile.module.css';
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import PatientCard from "./templates/PatientCards";
import { withAuth } from "../../utils/withAuth";

// Type expected by PatientCard
interface PatientCardData {
    id: number;
    firstname: string;
    lastname: string;
}

// Type returned by API
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
    const navigate = useNavigate();
    const [patients, setPatients] = useState<PatientCardData[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        let isMounted = true; // prevent state updates if unmounted

        getUser().then(user => {
            if (!user) {
                setLoading(false);
                return;
            }

            fetchDoctorsPatients(user.userID).then(data => {
                if (isMounted) {
                    // Map API data to PatientCardData format
                    const mappedPatients: PatientCardData[] = data.map(p => ({
                        id: p.patientID,
                        firstname: p.firstName,
                        lastname: p.lastName,
                    }));

                    setPatients(mappedPatients);
                    setLoading(false);
                }
            });
        });

        return () => {
            isMounted = false;
        };
    }, []);

    return (
        <div className={styles.dashContainer}>
            <Header />
            <div className={styles.background}>
                <h1 className={styles.heading}>Your patients</h1>
                <div className={styles.allCards}>
                    {loading ? (
                        <p>Loading patients...</p>
                    ) : patients.length === 0 ? (
                        <p>No patients found.</p>
                    ) : (
                        patients.map((item) => (
                            <PatientCard key={item.id} patient={item} />
                        ))
                    )}
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default withAuth(PatientShowcase, ["doctor", "admin"]);