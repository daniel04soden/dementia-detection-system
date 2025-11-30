// src/utils/withAuth.tsx
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { authorize } from "./auth";

/**
 * Wraps a component and restricts access based on allowedRoles.
 * @param WrappedComponent React component to wrap
 * @param allowedRoles array of roles allowed to view the component
 */
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
          navigate("/login"); // redirect if unauthorized
        } else {
          setLoading(false);
        }
      })();
    }, [navigate]);

    if (loading) return <div>Loading...</div>;

    return <WrappedComponent {...props} />;
  };

  return ComponentWithAuth;
}
