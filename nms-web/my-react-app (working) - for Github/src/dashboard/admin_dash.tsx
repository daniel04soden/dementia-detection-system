import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { withAuth } from "../../utils/withAuth";
import styles from "../dashboard/dash.module.css";
import LoadingScreen from "../reusable/Loading/loadingScreen";

interface Clinic {
  clinicID: number;
  name: string;
  phone: string;
  county: string;
  eircode: string;
}

interface Patient {
  patientID: number;
  firstName: string;
  lastName: string;
  phone: string;
  eircode: string;
  doctorID: number;
}

interface Doctor {
  doctorID: number;
  firstName: string;
  lastName: string;
  phone: string;
  doctorNo: string;
  clinics: string[];
  approved: boolean;
}

const AdminDashboard: React.FC = () => {
  const [clinics, setClinics] = useState<Clinic[]>([]);
  const [patients, setPatients] = useState<Patient[]>([]);
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const navigate = useNavigate();

  // Fetch all data
  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [clinicsRes, patientsRes, doctorsRes] = await Promise.all([
        fetch("/api/clinics", { credentials: "include" }),
        fetch("/api/admin/patients", { credentials: "include" }),
        fetch("/api/admin/doctors", { credentials: "include" }),
      ]);

      if (!clinicsRes.ok) throw new Error("Failed to fetch clinics");
      if (!patientsRes.ok) throw new Error("Failed to fetch patients");
      if (!doctorsRes.ok) throw new Error("Failed to fetch doctors");

      const clinicsData: Clinic[] = await clinicsRes.json();
      const patientsData: Patient[] = await patientsRes.json();
      const doctorsData: Doctor[] = await doctorsRes.json();

      setClinics(clinicsData || []);
      setPatients(patientsData || []);
      setDoctors(doctorsData || []);
    } catch (err: any) {
      console.error(err);
      setError(err.message || "Failed to fetch data");
      setClinics([]);
      setPatients([]);
      setDoctors([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  // Approve doctor
  const approveDoctor = async (doctorID: number) => {
    try {
      const res = await fetch(`/api/admin/approve?id=${doctorID}`, {
        method: "POST",
        credentials: "include",
      });
      if (!res.ok) throw new Error("Failed to approve doctor");
      setDoctors((prev) =>
        prev.map((d) => (d.doctorID === doctorID ? { ...d, approved: true } : d))
      );
      alert("Doctor approved successfully");
    } catch (err) {
      console.error(err);
      alert("Error approving doctor");
    }
  };

  // Delete entity
  const handleDelete = async (id: number, type: "clinic" | "patient" | "doctor") => {
    if (!window.confirm(`Are you sure you want to delete this ${type}?`)) return;
    try {
      const res = await fetch(`/api/admin/delete/${type}s?id=${id}`, {
        method: "DELETE",
        credentials: "include",
      });
      if (!res.ok) throw new Error(`Failed to delete ${type}`);
      alert(`${type} deleted successfully`);
      fetchData(); // Refresh all data after deletion
    } catch (err) {
      console.error(err);
      alert(`Error deleting ${type}`);
    }
  };

  // Navigate to create or update pages
  const handleCreate = (type: "Clinic" | "Patient" | "Doctor") => {
    navigate(`/admin/create/${type}`);
  };
  const handleUpdate = (id: number, type: "Clinic" | "Patient" | "Doctor") => {
    navigate(`/admin/update/${type}/${id}`);
  };

  if (loading)
    return (
      <div>
        <div className={styles.mainContent}>
          <LoadingScreen />;
        </div>
      </div>
    );

  if (error)
    return (
      <div>
        
        <div className={styles.mainContent}>
          <h2>{error}</h2>
        </div>
        
      </div>
    );

  return (
    <div className={styles.dashContainer}>
      
      <div className={styles.mainContent}>
        <div className={styles.leftSection}>
          <h2>Admin Dashboard</h2>

          <div className="crud-buttons">
            <button className={styles.newTestBtn} onClick={() => handleCreate("Clinic")}>
              Create Clinic
            </button>
            <button className={styles.newTestBtn} onClick={() => handleCreate("Patient")}>
              Create Patient
            </button>
            <button className={styles.newTestBtn} onClick={() => handleCreate("Doctor")}>
              Create Doctor
            </button>
          </div>

          {/* Clinics Table */}
          <h3>Clinics</h3>
          <div className={styles.tableSection}>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Phone</th>
                  <th>County</th>
                  <th>Eircode</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {clinics.length > 0 ? (
                  clinics.map((c) => (
                    <tr key={c.clinicID}>
                      <td>{c.clinicID}</td>
                      <td>{c.name}</td>
                      <td>{c.phone}</td>
                      <td>{c.county}</td>
                      <td>{c.eircode}</td>
                      <td>
                        <button onClick={() => handleUpdate(c.clinicID, "Clinic")}>Edit</button>
                        <button onClick={() => handleDelete(c.clinicID, "clinic")}>Delete</button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={6}>No clinics available</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* Patients Table */}
          <h3>Patients</h3>
          <div className={styles.tableSection}>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Phone</th>
                  <th>Eircode</th>
                  <th>Doctor ID</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {patients.length > 0 ? (
                  patients.map((p) => (
                    <tr key={p.patientID}>
                      <td>{p.patientID}</td>
                      <td>{p.firstName}</td>
                      <td>{p.lastName}</td>
                      <td>{p.phone}</td>
                      <td>{p.eircode}</td>
                      <td>{p.doctorID}</td>
                      <td>
                        <button onClick={() => handleUpdate(p.patientID, "Patient")}>Edit</button>
                        <button onClick={() => handleDelete(p.patientID, "patient")}>Delete</button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={7}>No patients available</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* Doctors Table */}
          <h3>Doctors</h3>
          <div className={styles.tableSection}>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Phone</th>
                  <th>Doctor No</th>
                  <th>Clinics</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {doctors.length > 0 ? (
                  doctors.map((d) => (
                    <tr key={d.doctorID}>
                      <td>{d.doctorID}</td>
                      <td>{d.firstName}</td>
                      <td>{d.lastName}</td>
                      <td>{d.phone}</td>
                      <td>{d.doctorNo}</td>
                      <td>{d.clinics.join(", ")}</td>
                      <td>
                        <button onClick={() => handleUpdate(d.doctorID, "Doctor")}>Edit</button>
                        <button onClick={() => handleDelete(d.doctorID, "doctor")}>Delete</button>
                        {!d.approved && (
                          <button onClick={() => approveDoctor(d.doctorID)}>Approve</button>
                        )}
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={7}>No doctors available</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
      
    </div>
  );
};

export default withAuth(AdminDashboard, ["admin"]);