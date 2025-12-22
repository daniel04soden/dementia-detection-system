import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Header from "../reusable/header/Header";
import Footer from "../reusable/footer/Footer";
import { withAuth } from "../../utils/withAuth";

interface Clinic {
  clinicID: number;
  name: string;
}

interface Doctor {
  doctorID: number;
  firstName: string;
  lastName: string;
}

interface Patient {
  patientID?: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  phone: string;
  doctorID?: number;
  eircode: string;
  clinicID?: number; // only for creation
}

const EditPatient: React.FC = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = Boolean(id);

  const [patient, setPatient] = useState<Patient>({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    phone: "",
    doctorID: 0,
    eircode: "",
    clinicID: undefined,
  });

  const [clinics, setClinics] = useState<Clinic[]>([]);
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [loading, setLoading] = useState(true);

  // Fetch clinics and doctors
  useEffect(() => {
    const fetchClinics = async () => {
      try {
        const res = await fetch("/api/clinics", { credentials: "include" });
        const data = await res.json();
        setClinics(data);
      } catch (err: any) {
        alert("Failed to load clinics: " + err.message || err);
      }
    };

    const fetchPatient = async () => {
      if (!isEditing) {
        setLoading(false);
        return;
      }
      try {
        const res = await fetch("/api/admin/patients", { credentials: "include" });
        const data = await res.json();
        const found = data.find((p: any) => p.patientID === Number(id));
        if (!found) throw new Error("Patient not found");
        setPatient({
          patientID: found.patientID,
          firstName: found.firstName,
          lastName: found.lastName,
          email: found.email,
          password: "",
          phone: found.phone,
          doctorID: found.doctorID,
          eircode: found.eircode,
        });
      } catch (err: any) {
        alert(err.message || err);
      } finally {
        setLoading(false);
      }
    };

    const fetchDoctors = async () => {
      try {
        const res = await fetch("/api/admin/doctors", { credentials: "include" });
        const data = await res.json();
        setDoctors(data.filter((d: any) => d.approved));
      } catch (err: any) {
        alert("Failed to load doctors: " + err.message || err);
      }
    };

    fetchClinics();
    fetchPatient();
    fetchDoctors();
  }, [id, isEditing]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setPatient(prev => ({
      ...prev,
      [name]: ["doctorID", "clinicID"].includes(name) ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const url = isEditing
      ? `/api/admin/patients?id=${patient.patientID}`
      : "/api/admin/patients";

    const payload = { ...patient };
    if (!payload.password) delete payload.password; // only send if set
    if (!isEditing) delete payload.doctorID; // backend assigns doctorID on creation

    try {
      const res = await fetch(url, {
        method: isEditing ? "PUT" : "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText);
      }

      alert(isEditing ? "Patient updated successfully" : "Patient created successfully");
      navigate("/admin");
    } catch (err: any) {
      alert("Error: " + (err.message || err));
    }
  };

  if (loading) return <p>Loading patient data...</p>;

  return (
    <div>
      
      <h2>{isEditing ? "Edit Patient" : "Create Patient"}</h2>

      <form onSubmit={handleSubmit}>
        <label>First Name:</label>
        <input name="firstName" value={patient.firstName} onChange={handleChange} required /><br />

        <label>Last Name:</label>
        <input name="lastName" value={patient.lastName} onChange={handleChange} required /><br />

        <label>Email:</label>
        <input type="email" name="email" value={patient.email} onChange={handleChange} required /><br />

        <label>Password:</label>
        <input
          type="password"
          name="password"
          value={patient.password}
          onChange={handleChange}
          placeholder={isEditing ? "Leave blank to keep current password" : ""}
          {...(!isEditing && { required: true })}
        /><br />

        <label>Phone:</label>
        <input name="phone" value={patient.phone} onChange={handleChange} required /><br />

        {!isEditing && (
          <>
            <label>Clinic:</label>
            <select name="clinicID" value={patient.clinicID || ""} onChange={handleChange} required>
              <option value="">Select clinic</option>
              {clinics.map(c => (
                <option key={c.clinicID} value={c.clinicID}>{c.name}</option>
              ))}
            </select>
            <br />
          </>
        )}

        {isEditing && (
          <>
            <label>Doctor:</label>
            <select name="doctorID" value={patient.doctorID} onChange={handleChange} required>
              <option value="">Select doctor</option>
              {doctors.map(d => (
                <option key={d.doctorID} value={d.doctorID}>
                  {d.firstName} {d.lastName}
                </option>
              ))}
            </select>
            <br />
          </>
        )}

        <label>Eircode:</label>
        <input name="eircode" value={patient.eircode} onChange={handleChange} required /><br />

        <button type="submit">{isEditing ? "Update Patient" : "Create Patient"}</button>
      </form>
      
    </div>
  );
};

export default withAuth(EditPatient, ["admin"]);
