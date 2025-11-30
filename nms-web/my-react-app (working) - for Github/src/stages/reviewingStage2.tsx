import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import { withAuth } from "../../utils/withAuth";
import styles from "./stage.module.css";

interface Stage2Data {
  id: number;
  rememberQ: boolean;
  recallQ: boolean;
  speakQ: boolean;
  financeQ: boolean;
  medQ: boolean;
  mobilityQ: boolean;
}

const ReviewingStage2: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<Stage2Data | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(`/api/web/stagetwo/review?testId=${testId}`);
        if (!res.ok) {
          throw new Error("Failed to fetch test data");
        }
        const data = await res.json();
        setTestData(data);
      } catch (err) {
        console.error(err);
        alert("Error loading test data.");
      }
    };

    fetchData();
  }, [testId]);

  if (!testData) return <p>Loading stage 2 data...</p>;

  return (
    <div>
      <Header />
      <div className="main-content">
        <h2 className={styles.heading}>Stage 2 Test #{testData.id} - View Only</h2>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>Question</th>
              <th>Yes (No Issue)</th>
              <th>No (Impairment)</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                Does the patient have more trouble remembering things that happened recently?
              </td>
              <td><input type="radio" checked={testData.rememberQ} disabled /></td>
              <td><input type="radio" checked={!testData.rememberQ} disabled /></td>
            </tr>
            <tr>
              <td>Do they have more trouble recalling conversations a few days later?</td>
              <td><input type="radio" checked={testData.recallQ} disabled /></td>
              <td><input type="radio" checked={!testData.recallQ} disabled /></td>
            </tr>
            <tr>
              <td>
                When speaking, do they have more difficulty finding the right word or tend to use wrong words more often?
              </td>
              <td><input type="radio" checked={testData.speakQ} disabled /></td>
              <td><input type="radio" checked={!testData.speakQ} disabled /></td>
            </tr>
            <tr>
              <td>
                Are they less able to manage money and financial affairs (e.g. paying bills)?
              </td>
              <td><input type="radio" checked={testData.financeQ} disabled /></td>
              <td><input type="radio" checked={!testData.financeQ} disabled /></td>
            </tr>
            <tr>
              <td>Are they less able to manage medication independently?</td>
              <td><input type="radio" checked={testData.medQ} disabled /></td>
              <td><input type="radio" checked={!testData.medQ} disabled /></td>
            </tr>
            <tr>
              <td>
                Do they need more assistance with transport (not due to physical problems)?
              </td>
              <td><input type="radio" checked={testData.mobilityQ} disabled /></td>
              <td><input type="radio" checked={!testData.mobilityQ} disabled /></td>
            </tr>
          </tbody>
        </table>

        <br />
        <button onClick={() => navigate(-1)}>Back</button>
      </div>
      <Footer />
    </div>
  );
};

export default withAuth(ReviewingStage2, ["doctor", "admin"]);
