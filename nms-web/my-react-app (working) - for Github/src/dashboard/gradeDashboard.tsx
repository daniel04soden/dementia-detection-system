// Fixed frontend files to match backend TestStageOne shape
// This document contains three files concatenated. Replace your existing files with the corresponding sections.

/* ===================== gradeDashboard.tsx ===================== */
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import { withAuth } from "../../utils/withAuth";

interface TestOverview {
  testID: number;
  patientID?: number;
  patientName?: string;
  testDate?: string;
  stageOneStatus?: number | boolean;
  stageTwoStatus?: number | boolean;
  result?: string | null;
}

// This matches the Go backend's HandleGetTestStageOne response
interface TestStageOneFromAPI {
  testID: number;
  testDate: string | null;
  clockID: number | null;
  dateQuestion: string | null;
  news: string | null;
  recallName: string | null;
  recallSurname: string | null;
  recallNumber: string | null;
  recallStreet: string | null;
  recallCity: string | null;
  clockNumberRes: boolean;
  clockHandsRes: boolean;
  dateQuestionRes: boolean;
  newsRes: boolean; // backend sends boolean
  recallRes: number; // backend sends number (count)
}

const GradeDashboard: React.FC = () => {
  const [tests, setTests] = useState<TestOverview[] | null>(null); // null = loading
  const navigate = useNavigate();
  async function getUser() {
    try {
      const res = await fetch("/api/web/me", { method: "GET", credentials: "include" });
      if (!res.ok) return null;
      return await res.json();
    } catch (err) {
      console.error("Fetch /me error:", err);
      return null;
    }
  }

  useEffect(() => {
    const fetchTests = async () => {
      try {
        await getUser(); // optional, just to check logged-in user

        const res = await fetch("/api/doctor/tests", { credentials: "include" });
        if (!res.ok) throw new Error("Failed to fetch tests");

        const data: TestOverview[] = await res.json();
        setTests(data);
      } catch (err) {
        console.error("Error fetching tests:", err);
        setTests([]);
      }
    };
    fetchTests();
  }, []);

  if (tests === null) return (
    <div>
      <Header />
      <div className="main-content">
        <h2>My Patients & Tests</h2>
        <p>No tests found</p>
      </div>
      <Footer />
    </div>
  );

  if (tests.length === 0) return (
    <div>
      <Header />
      <div className="main-content">
        <h2>My Patients & Tests</h2>
        <p>No tests found.</p>
      </div>
      <Footer />
    </div>
  );

  const handleReviewClick = async (testID: number) => {
  try {
    // Find the test from the list using testID
    const test = tests.find((t) => t.testID === testID);

    if (!test) {
      throw new Error("Test not found");
    }

    // Use stageOneStatus from the test overview

    if (test.stageOneStatus != 1) {
      navigate(`/review/one/${testID}`);
    } else {
      navigate(`/grading/one/${testID}`);
    }
  } catch (err) {
    console.error("Error checking stage 1:", err);
    alert("Could not check stage 1 status.");
  }
};

  return (
    <div>
      <Header />
      <div className="main-content">
        <h2>My Patients & Tests</h2>
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr>
              <th>Test ID</th>
              <th>Patient Name</th>
              <th>Stage One</th>
              <th>Stage Two</th>
              <th>Result</th>
              <th>View</th>
            </tr>
          </thead>
          <tbody>
            {tests.map((t) => (
              <tr key={t.testID}>
                <td>{t.testID}</td>
                <td>{t.patientName || "-"}</td>
                <td>{t.stageOneStatus ? String(t.stageOneStatus) : "-"}</td>
                <td>{t.stageTwoStatus ? String(t.stageTwoStatus) : "-"}</td>
                <td>{t.result || "-"}</td>
                <td>
                  <button onClick={() => handleReviewClick(t.testID)}>Review</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Footer />
    </div>
  );
};

export default withAuth(GradeDashboard, ["doctor"]);