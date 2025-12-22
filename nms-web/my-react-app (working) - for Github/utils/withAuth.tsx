import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { authorize } from "./auth";
import LoadingScreen from "../src/reusable/Loading/loadingScreen";

export function withAuth<P extends object>(
  WrappedComponent: React.ComponentType<P>,
  allowedRoles: string[]
) {
  const ComponentWithAuth: React.FC<P> = (props) => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
      (async () => {
        const hasAccess = await authorize(allowedRoles);
        if (!hasAccess) {
          navigate("/login");
        } else {
          setLoading(false);
        }
      })();
    }, [navigate]);

    if (loading) return <LoadingScreen />;

    return <WrappedComponent {...props} />;
  };

  return ComponentWithAuth;
}
