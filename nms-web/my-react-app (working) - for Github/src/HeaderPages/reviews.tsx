import { useEffect, useState } from "react";
import LoadingScreen from "../reusable/Loading/loadingScreen";
import styles from "./reviews.module.css";

type ReviewResponse = {
  reviewID: number;
  date: string;
  patientID: number;
  score: number;
  critique: string;
};

type PatientData = {
  patientID: number;
  firstName: string;
  lastName: string;
};

async function fetchReviews(): Promise<ReviewResponse[]> {
  try {
    const res = await fetch("/api/reviews");
    if (!res.ok) throw new Error("Failed to fetch reviews");
    return await res.json();
  } catch (err) {
    console.error(err);
    return [];
  }
}

async function fetchPatients(): Promise<PatientData[]> {
  try {
    const res = await fetch("/api/patients");
    if (!res.ok) throw new Error("Failed to fetch patients");
    return await res.json();
  } catch (err) {
    console.error(err);
    return [];
  }
}

// Helper: render score as stars
const renderStars = (score: number) => {
  const fullStars = "★".repeat(score);
  const emptyStars = "☆".repeat(5 - score);
  return fullStars + emptyStars;
};

const Reviews: React.FC = () => {
  const [reviews, setReviews] = useState<ReviewResponse[]>([]);
  const [patients, setPatients] = useState<PatientData[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let isMounted = true;

    Promise.all([fetchReviews(), fetchPatients()]).then(([reviewsData, patientsData]) => {
      if (isMounted) {
        setReviews(reviewsData);
        setPatients(patientsData);
        setLoading(false);
      }
    });

    return () => { isMounted = false; };
  }, []);

  if (loading) return <LoadingScreen />;

  // Map patientID -> name for display
  const getPatientName = (id: number) => {
    const patient = patients.find((p) => p.patientID === id);
    return patient ? `${patient.firstName} ${patient.lastName}` : `ID ${id}`;
  };

  return (
    <div className={styles.pageBackground}>
      
      <main className={styles.mainContent}>
        <h1 className={styles.pageTitle}>Reviews</h1>
        {reviews === null || reviews.length === 0 ? (
          <p className={styles.noReviews}>No reviews found.</p>
        ) : (
          <div className={styles.reviewGrid}>
            {reviews.map((review) => (
                <div key={review.reviewID} className={styles.reviewCard}>
                <p><strong>Date:</strong> {review.date}</p>
                <p><strong>Patient:</strong> {getPatientName(review.patientID)}</p>
                <p><strong>Score:</strong> <span className={styles.stars}>{renderStars(review.score)}</span></p>
                <p><strong>Critique:</strong> {review.critique}</p>
                </div>
            ))}
        </div>
        )}
      </main>
      
    </div>
  );
};

export default Reviews;
