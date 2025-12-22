import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { withAuth } from "../../utils/withAuth";
import styles from './stageEditable.module.css';
import LoadingScreen from "../reusable/Loading/loadingScreen";

interface Stage2Data {
  testID: number;
  rememberQ: boolean;
  recallQ: boolean;
  speakQ: boolean;
  financeQ: boolean;
  medQ: boolean;
  mobilityQ: boolean;
}

// Fallback data in case fetch fails
const fallbackData: Stage2Data = {
  testID: 0,
  rememberQ: false,
  recallQ: false,
  speakQ: false,
  financeQ: false,
  medQ: false,
  mobilityQ: false,
};

const beans2readonly: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<Stage2Data | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      if (!testId) return;

      try {
        const res = await fetch(`/api/web/stagetwo/review?id=${testId}`, { credentials: "include" });
        if (!res.ok) throw new Error(await res.text());
        const data: Stage2Data = await res.json();
        setTestData(data);
      } catch (err) {
        console.error(err);
        alert("Error loading stage 2 test data. Using fallback data.");
        setTestData(fallbackData);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [testId]);

  if (loading || !testData) return <LoadingScreen />;

  const passFail = (value: boolean) => (value ? "Yes (No Issue)" : "No (Impairment)");

  const questions: { label: string; key: Exclude<keyof Stage2Data, "testID"> }[] = [
    { label: "Does the patient have more trouble remembering things that happened recently?", key: "rememberQ" },
    { label: "Do they have more trouble recalling conversations a few days later?", key: "recallQ" },
    { label: "When speaking, do they have more difficulty finding the right word or tend to use wrong words more often?", key: "speakQ" },
    { label: "Are they less able to manage money and financial affairs (e.g. paying bills)?", key: "financeQ" },
    { label: "Are they less able to manage medication independently?", key: "medQ" },
    { label: "Do they need more assistance with transport (not due to physical problems)?", key: "mobilityQ" },
  ];

  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <div className={styles.container}>
          <h2 className={styles.heading}>
            Stage 2 Test #{testData.testID > 0 ? testData.testID : "N/A"} - Review
          </h2>

          <table className={styles.table}>
            <thead>
              <tr>
                <th>Question</th>
                <th>Result</th>
              </tr>
            </thead>
            <tbody>
              {questions.map((q, i) => (
                <tr key={i}>
                  <td>{q.label}</td>
                  <td>{passFail(testData[q.key])}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className={styles.buttons}>
            <button className={styles.btn} onClick={() => navigate(-1)}>Back</button>
          </div>
        </div>
      </main>
    </div>
  );
};

export default withAuth(beans2readonly, ["doctor"]);
