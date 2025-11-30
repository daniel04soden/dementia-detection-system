import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";

// Define the Clinic interface
interface Clinic {
  clinicID?: number;  // optional for creation
  name: string;
  phone: string;
  county: string;
  eircode: string;
}

const EditClinic: React.FC = () => {
  const { id } = useParams(); // Get clinic ID (if editing)
  const navigate = useNavigate();

  const [clinic, setClinic] = useState<Clinic>({
    name: "",
    eircode: "",
    county: "",
    phone: "",
  });

  // Fetch clinic only in edit mode
  useEffect(() => {
    if (!id) return; // creation mode → skip fetch

    const fetchClinic = async () => {
      const res = await fetch(`/api/clinic?id=${id}`, {
        credentials: "include",
      });

      if (!res.ok) {
        alert("Failed to fetch clinic data.");
        return;
      }

      const data = await res.json();
      setClinic(data);
    };

    fetchClinic();
  }, [id]);

  // Create OR Update logic
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const isEditing = Boolean(id);

    const url = isEditing
      ? `/api/admin/clinics?id=${clinic.clinicID}`
      : `/api/admin/clinics`;

    const method = isEditing ? "PUT" : "POST";

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(clinic),
    });

    if (res.ok) {
      alert(isEditing ? "Clinic updated successfully" : "Clinic created successfully");
      navigate("/admin/dashboard");
    } else {
      alert(isEditing ? "Failed to update clinic" : "Failed to create clinic");
    }
  };

  // Handle input changes
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setClinic((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <div>
      <Header />
      <h2>{id ? "Edit Clinic" : "Create Clinic"}</h2>

      <form onSubmit={handleSubmit}>
        <label>Name:</label>
        <input
          type="text"
          name="name"
          value={clinic.name}
          onChange={handleChange}
          required
        /><br />

        <label>Eircode:</label>
        <input
          type="text"
          name="eircode"
          value={clinic.eircode}
          onChange={handleChange}
          required
        /><br />

        <label>Phone:</label>
        <input
          type="text"
          name="phone"
          value={clinic.phone}
          onChange={handleChange}
          required
        /><br />
        
        <label>County:</label>
        <input
          type="text"
          name="county"
          value={clinic.county}
          onChange={handleChange}
          required
        />
        <br />

        <button type="submit">
          {id ? "Update Clinic" : "Create Clinic"}
        </button>
      </form>
      <Footer />
    </div>
  );
};

export default EditClinic;
