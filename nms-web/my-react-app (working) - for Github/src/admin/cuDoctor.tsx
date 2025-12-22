import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { withAuth } from "../../utils/withAuth";

interface Doctor {
  doctorID?: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
  doctorNO: string;
  clinics: string[];
  approved: boolean;
}

const EditDoctor: React.FC = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = Boolean(id);

  const [doctor, setDoctor] = useState<Doctor>({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    phone: "",
    doctorNO: "",
    clinics: [],
    approved: false,
  });

  const [availableClinics, setAvailableClinics] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchClinics = async () => {
      try {
        const res = await fetch("/api/clinics", { credentials: "include" });
        if (!res.ok) throw new Error("Failed to load clinics");
        const data = await res.json();
        setAvailableClinics(data.map((c: any) => c.name));
      } catch (err: any) {
        alert(err.message || err);
      }
    };

    const fetchDoctor = async () => {
      if (!isEditing) {
        setLoading(false);
        return;
      }

      try {
        const res = await fetch("/api/admin/doctors", { credentials: "include" });
        if (!res.ok) throw new Error("Failed to fetch doctors");
        const data = await res.json();
        const found = data.find((d: any) => d.doctorID === Number(id));
        if (!found) throw new Error("Doctor not found");

        setDoctor({
          doctorID: found.doctorID,
          firstName: found.firstName,
          lastName: found.lastName,
          email: found.email,
          password: "",
          phone: found.phone,
          doctorNO: found.doctorNO,
          clinics: found.clinics ?? [],
          approved: found.approved ?? false,
        });
      } catch (err: any) {
        alert(err.message || err);
      } finally {
        setLoading(false);
      }
    };

    fetchClinics();
    fetchDoctor();
  }, [id, isEditing]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setDoctor((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleClinicChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value, checked } = e.target;
    setDoctor((prev) => ({
      ...prev,
      clinics: checked
        ? [...prev.clinics, value]
        : prev.clinics.filter((c) => c !== value),
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const method = isEditing ? "PUT" : "POST";
    const url = isEditing
      ? `/api/admin/doctors?id=${doctor.doctorID}`
      : "/api/admin/doctors";

    const payload = {
      ...doctor,
      password: doctor.password || undefined,
    };

    try {
      const res = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText);
      }
      alert(isEditing ? "Doctor updated successfully" : "Doctor created successfully");
      navigate("/admin/dashboard");
    } catch (err: any) {
      alert("Error: " + (err.message || err));
    }
  };

  if (loading) return <p>Loading doctor...</p>;

  return (
    <div>
      
      <h2>{isEditing ? "Edit Doctor" : "Create Doctor"}</h2>
      <form onSubmit={handleSubmit}>
        <label>First Name:</label>
        <input name="firstName" value={doctor.firstName} onChange={handleChange} required /><br />

        <label>Last Name:</label>
        <input name="lastName" value={doctor.lastName} onChange={handleChange} required /><br />

        <label>Email:</label>
        <input type="email" name="email" value={doctor.email} onChange={handleChange} required /><br />

        <label>Password:</label>
        <input
          type="password"
          name="password"
          value={doctor.password}
          onChange={handleChange}
          placeholder={isEditing ? "Leave blank to keep current password" : ""}
          {...(!isEditing && { required: true })}
        /><br />

        <label>Phone:</label>
        <input name="phone" value={doctor.phone} onChange={handleChange} required /><br />

        <label>Doctor Number:</label>
        <input name="doctorNO" value={doctor.doctorNO} onChange={handleChange} required /><br />

        <label>Clinics:</label>
        <div>
          {availableClinics.map((clinic) => (
            <div key={clinic}>
              <label>
                <input
                  type="checkbox"
                  value={clinic}
                  checked={doctor.clinics.includes(clinic)}
                  onChange={handleClinicChange}
                />
                {clinic}
              </label>
            </div>
          ))}
        </div><br />

        <label>
          <input type="checkbox" name="approved" checked={doctor.approved} onChange={handleChange} />
          Approved
        </label><br />

        <button type="submit">{isEditing ? "Update Doctor" : "Create Doctor"}</button>
      </form>
      
    </div>
  );
};

export default withAuth(EditDoctor, ["admin"]);