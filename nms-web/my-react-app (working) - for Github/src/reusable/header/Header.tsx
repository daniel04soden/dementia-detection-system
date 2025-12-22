import React, { useEffect, useState } from "react";
import HeaderAdmin from "./HeaderAdmin";
import HeaderDoctor from "./HeaderDoctor";
import HeaderOff from "./HeaderOff";
import { getUser } from "../../../utils/auth";

const Header: React.FC = () => {
  const [role, setRole] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const user = await getUser();
        setRole(user?.role || null);
      } catch (err) {
        console.error("Error fetching user:", err);
        setRole(null);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  if (loading) {
    return <header>Loading...</header>;
  }

  switch (role) {
    case "admin":
      return <HeaderAdmin />;
    case "doctor":
      return <HeaderDoctor />;
    default:
      return <HeaderOff />;
  }
};

export default Header;
