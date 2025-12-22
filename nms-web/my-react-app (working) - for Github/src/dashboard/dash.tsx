import React, { useEffect, useState } from "react";
import AdminDashboard from "./admin_dash";
import DoctorDashboard from "./updatedDashboard";

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

const Dash: React.FC = () => {
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      const currentUser = await getUser();
      setUser(currentUser);
      setLoading(false);
    };

    fetchUser();
  }, []);

  if (loading) {
    return <p>Loading dashboard...</p>;
  }

  if (!user) {
    return <p>User not logged in. Please login to continue.</p>;
  }

  // Conditionally render dashboard based on role
  if (user.role === "admin") {
    return <AdminDashboard />;
  } else if (user.role === "doctor") {
    return <DoctorDashboard />;
  } else {
    return <p>Unauthorized role</p>;
  }
};

export default Dash;
