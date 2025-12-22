import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { withAuth } from "../../utils/withAuth";
import styles from './stageEditable.module.css';

import clock1 from "../assets/clock1.png";
import clock2 from "../assets/clock2.png";
import clock3 from "../assets/clock3.png";
import clock4 from "../assets/clock4.png";
import clock5 from "../assets/clock5.png";
import clock6 from "../assets/clock6.png";
import LoadingScreen from "../reusable/Loading/loadingScreen";

interface TestDataFromAPI {
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
  newsRes: boolean;
  recallRes: number;
}

const Beans1ReadOnly: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<TestDataFromAPI | null>(null);
  const [stageTwoStatus, setStageTwoStatus] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      if (!testId) return;
      try {
        const res = await fetch(`/api/web/stageone/review?id=${testId}`, { credentials: "include" });
        if (!res.ok) throw new Error(await res.text());
        const data: TestDataFromAPI = await res.json();
        setTestData(data);
      } catch (err) {
        console.error(err);
        alert("Error loading test data.");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [testId]);

  useEffect(() => {
    const fetchStageTwoStatus = async () => {
      if (!testId) return;
      try {
        const res = await fetch(`/api/doctor/tests`, { credentials: "include" });
        if (!res.ok) throw new Error(await res.text());
        const tests: { testID: number; stageTwoStatus?: number }[] = await res.json();
        const currentTest = tests.find(t => t.testID === Number(testId));
        setStageTwoStatus(currentTest?.stageTwoStatus ?? null);
      } catch (err) {
        console.error(err);
      }
    };
    fetchStageTwoStatus();
  }, [testId]);

  if (loading || !testData) return <LoadingScreen />;

  const clockImages: Record<number, string> = {
    1: clock1, 2: clock2, 3: clock3, 4: clock4, 5: clock5, 6: clock6
  };
  const clock = testData.clockID !== null ? clockImages[testData.clockID + 1] : undefined;

  const passFail = (value: boolean) => (value ? "Pass" : "Fail");

  const recallFields = ["Name", "Surname", "Number", "Street", "City"];
  const recallValues = [
    testData.recallName,
    testData.recallSurname,
    testData.recallNumber,
    testData.recallStreet,
    testData.recallCity
  ];
  const recallExpected = ["John","Brown","43","West (St)","Kensington"];

  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <div className={styles.container}>
          <h2 className={styles.heading}>Review Test #{testData.testID}</h2>

          <table className={styles.table}>
            <thead>
              <tr>
                <th>Question</th>
                <th>Answer</th>
                <th>Result</th>
              </tr>
            </thead>
            <tbody>
              {/* Date Question */}
              <tr>
                <td>What is the date?</td>
                <td>{testData.testDate || '-'} = {testData.dateQuestion || '-'}</td>
                <td>{passFail(testData.dateQuestionRes)}</td>
              </tr>

              {/* News */}
              <tr>
                <td>News</td>
                <td>{testData.news || '-'}</td>
                <td>{passFail(testData.newsRes)}</td>
              </tr>

              {/* Clock Section */}
              <tr>
                <th colSpan={3} className={styles.subHeader}>Clock</th>
              </tr>
              {clock && (
                <tr className={styles.fullRow}>
                  <td colSpan={3}>
                    <img src={clock} alt="Clock" className={styles.clockImg} />
                  </td>
                </tr>
              )}
              <tr>
                <td>Clock numbers correct?</td>
                <td>-</td>
                <td>{passFail(testData.clockNumberRes)}</td>
              </tr>
              <tr>
                <td>Clock hands correct?</td>
                <td>-</td>
                <td>{passFail(testData.clockHandsRes)}</td>
              </tr>

              {/* Recall Section */}
              <tr>
                <th colSpan={3} className={styles.subHeader}>Recall</th>
              </tr>
              {recallFields.map((field, i) => (
                <tr key={i}>
                  <td>{field}</td>
                  <td>{recallExpected[i]}</td>
                  <td>{recallValues[i] || '-'}</td>
                </tr>
              ))}
              <tr className={styles.fullRow}>
                <td>Score</td>
                <td>-</td>
                <td>{testData.recallRes}/5</td>
              </tr>
            </tbody>
          </table>

          <div className={styles.buttons}>
            <button className={styles.btn} onClick={() => navigate(-1)}>Back</button>
          </div>

          {stageTwoStatus && (stageTwoStatus === 1 || stageTwoStatus > 4) && (
            <div className={styles.buttons}>
              <button className={styles.btn} onClick={() => navigate(`/review/two/${testData.testID}`)}>Next</button>
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default withAuth(Beans1ReadOnly, ["doctor"]);
