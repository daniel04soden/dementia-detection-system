import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styles from "./profile.module.css";
import { withAuth } from "../../utils/withAuth";
import LoadingScreen from "../reusable/Loading/loadingScreen";
import NotFound from "../reusable/NotFound/notFound";

interface PatientData {
  patientID: number;
  firstName: string;
  lastName: string;
}

interface LifestyleForm {
  PatientID: number;
  Diabetic: number;
  AlcoholLevel: number;
  HeartRate: number;
  BloodOxygen: number;
  BodyTemperature: number;
  Weight: number;
  MRIDelay: number;
  Age: number;
  DominantHand: number;
  Gender: number;
  FamilyHistory: number;
  Smoked: number;
  APOE4: number;
  PhysicalActivity: string;
  DepressionStatus: number;
  CognitiveTestScores: number;
  MedicationHistory: number;
  NutritionDiet: string;
  SleepQuality: number;
  ChronicHealthConditions: string;
  Education: string;
  FirstName?: string;
  LastName?: string;
}

const EditLifestyleReview: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [form, setForm] = useState<LifestyleForm | null>(null);
  const [patient, setPatient] = useState<PatientData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const lifestyleRes = await fetch(`/api/lifestyle/review?id=${testId}`);
        if (!lifestyleRes.ok) throw new Error();
        const data = await lifestyleRes.json();

        const patientRes = await fetch("/api/patients");
        if (!patientRes.ok) throw new Error();
        const patients = await patientRes.json();
        const foundPatient = patients.find(
          (p: PatientData) => p.patientID === Number(testId)
        );
        if (!foundPatient) throw new Error();

        setPatient(foundPatient);

        setForm({
          PatientID: data.patientID,
          Diabetic: data.diabetic,
          AlcoholLevel: data.alcoholLevel,
          HeartRate: data.heartRate,
          BloodOxygen: data.bloodOxygen,
          BodyTemperature: data.bodyTemperature,
          Weight: data.weight,
          MRIDelay: data.mriDelay,
          Age: data.age,
          DominantHand: data.dominantHand,
          Gender: data.gender,
          FamilyHistory: data.familyHistory,
          Smoked: data.smoked,
          APOE4: data.apoe4,
          PhysicalActivity: data.physicalActivity,
          DepressionStatus: data.depressionStatus,
          CognitiveTestScores: data.cognitiveTestScores,
          MedicationHistory: data.medicationHistory,
          NutritionDiet: data.nutritionDiet,
          SleepQuality: data.sleepQuality,
          ChronicHealthConditions: data.chronicHealthConditions,
          Education: data.education,
          FirstName: foundPatient.firstName,
          LastName: foundPatient.lastName,
        });
      } catch {
        setForm(null);
        setPatient(null);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [testId]);

  if (loading) return <LoadingScreen />;
  if (!form) return <NotFound />;

  const handleChange = (key: keyof LifestyleForm, value: any) => {
    setForm(prev => (prev ? { ...prev, [key]: value } : prev));
  };

  const handleSubmit = async () => {
    if (!form) return;

    const payload = {
      patientID: form.PatientID,
      diabetic: form.Diabetic,
      alcoholLevel: form.AlcoholLevel,
      heartRate: form.HeartRate,
      bloodOxygen: form.BloodOxygen,
      bodyTemperature: form.BodyTemperature,
      weight: form.Weight,
      mriDelay: form.MRIDelay,
      age: form.Age,
      dominantHand: form.DominantHand,
      gender: form.Gender,
      familyHistory: form.FamilyHistory,
      smoked: form.Smoked,
      apoe4: form.APOE4,
      physicalActivity: form.PhysicalActivity,
      depressionStatus: form.DepressionStatus,
      cognitiveTestScores: form.CognitiveTestScores,
      medicationHistory: form.MedicationHistory,
      nutritionDiet: form.NutritionDiet,
      sleepQuality: form.SleepQuality,
      chronicHealthConditions: form.ChronicHealthConditions,
      education: form.Education,
    };

    try {
      const res = await fetch("/api/lifestyle/update", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error();

      navigate(`/user/${form.PatientID}`);
    } catch {
      alert("Failed to update lifestyle review");
    }
  };

  const binaryOptions = [
    { label: "No", value: 0 },
    { label: "Yes", value: 1 },
  ];

  const selectOptionsMap: Record<string, { label: string; value: string }[]> = {
    PhysicalActivity: [
      { label: "Sedentary", value: "Sedentary" },
      { label: "Mild Activity", value: "Mild Activity" },
      { label: "Moderate Activity", value: "Moderate Activity" },
    ],
    NutritionDiet: [
      { label: "Low-Carb Diet", value: "Low-Carb Diet" },
      { label: "Mediterranean Diet", value: "Mediterranean Diet" },
      { label: "Balanced Diet", value: "Balanced Diet" },
    ],
    ChronicHealthConditions: [
      { label: "N/A", value: "N/A" },
      { label: "Heart Disease", value: "Heart Disease" },
      { label: "Hypertension", value: "Hypertension" },
      { label: "Diabetes", value: "Diabetes" },
    ],
    Education: [
      { label: "Primary", value: "Primary" },
      { label: "Secondary", value: "Secondary" },
      { label: "Teritary", value: "Teritary" },
    ],
  };

  const renderInput = (
    key: keyof LifestyleForm,
    value: any,
    handleChange: (key: keyof LifestyleForm, value: any) => void
  ) => {
    if (
      ["Diabetic", "FamilyHistory", "Smoked", "APOE4", "DepressionStatus", "DominantHand", "Gender"].includes(key)
    ) {
      return (
        <select value={value} onChange={(e) => handleChange(key, Number(e.target.value))}>
          {binaryOptions.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      );
    }

    if (key in selectOptionsMap) {
      return (
        <select value={value} onChange={(e) => handleChange(key, e.target.value)}>
          {selectOptionsMap[key].map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      );
    }

    if (key === "SleepQuality") {
      return (
        <input
          type="number"
          min={0}
          max={5}
          value={value}
          onChange={(e) => {
            const num = Number(e.target.value);
            if (num >= 0 && num <= 5) handleChange(key, num);
          }}
        />
      );
    }

    if (typeof value === "number") {
      return (
        <input
          type="number"
          min={0}
          value={value}
          onChange={(e) => handleChange(key, Number(e.target.value))}
        />
      );
    }

    return <input type="text" value={value} onChange={(e) => handleChange(key, e.target.value)} />;
  };

  return (
    <div className={styles.background}>
      <main className={styles.profileMain}>
        <section className={styles.heroSection}>
          <h1 className={styles.heroTitle}>Edit Lifestyle Review</h1>
          <p className={styles.heroSubtitle}>
            Editing information for {patient?.firstName} {patient?.lastName}
          </p>
        </section>

        <div className={styles.profileGrid}>
          {/* Personal Info */}
          <section className={styles.personalInfo}>
            <h2>Personal Information</h2>
            <div className={styles.infoGrid}>
              <div>
                <span className={styles.dataKey}>Patient ID:</span>
                <span className={styles.dataValue}>{patient?.patientID}</span>
              </div>
              <div>
                <span className={styles.dataKey}>Name:</span>
                <span className={styles.dataValue}>
                  {patient?.firstName} {patient?.lastName}
                </span>
              </div>
              <div>
                <span className={styles.dataKey}>Age:</span>
                <input
                  type="number"
                  min={0}
                  value={form?.Age}
                  onChange={(e) => handleChange("Age", Number(e.target.value))}
                />
              </div>
              <div>
                <span className={styles.dataKey}>Gender:</span>
                <select
                  value={form?.Gender}
                  onChange={(e) => handleChange("Gender", Number(e.target.value))}
                >
                  <option value={0}>Female</option>
                  <option value={1}>Male</option>
                </select>
              </div>
              <div>
                <span className={styles.dataKey}>Dominant Hand:</span>
                <select
                  value={form?.DominantHand}
                  onChange={(e) => handleChange("DominantHand", Number(e.target.value))}
                >
                  <option value={0}>Left</option>
                  <option value={1}>Right</option>
                </select>
              </div>
              <div>
                <span className={styles.dataKey}>Education:</span>
                <select
                  value={form?.Education}
                  onChange={(e) => handleChange("Education", e.target.value)}
                >
                  <option value="Primary">Primary</option>
                  <option value="Secondary">Secondary</option>
                  <option value="Teritary">Degree</option>
                </select>
              </div>
            </div>
          </section>

          {/* Lifestyle & Medical Data */}
          <section className={styles.lifestyleInfo}>
            <h2>Lifestyle & Medical Data</h2>
            <div className={styles.lifestyleGrid}>
              {Object.entries(form).map(([key, value]) => {
                if (
                  ["PatientID", "Age", "Gender", "Education", "DominantHand", "FirstName", "LastName"].includes(key)
                )
                  return null;

                return (
                  <div key={key} className={styles.dataRow}>
                    <span className={styles.dataKey}>{key.replace(/([A-Z])/g, " $1")}</span>
                    {renderInput(key as keyof LifestyleForm, value, handleChange)}
                  </div>
                );
              })}
            </div>
          </section>
        </div>

        <button className={styles.pdfBtn} onClick={handleSubmit}>
          Save Changes
        </button>

        <button className={styles.pdfBtn} onClick={() => navigate(-1)}>
          Cancel
        </button>
      </main>
    </div>
  );
};

export default withAuth(EditLifestyleReview, ["doctor"]);
