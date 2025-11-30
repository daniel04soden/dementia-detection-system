/* ===================== gradingStage1.tsx ===================== */
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import { withAuth } from "../../utils/withAuth";
import styles from './stage.module.css';

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
  recallRes: number; // numeric count of correct recall items
}

const GradingStage1: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<TestDataFromAPI | null>(null);

  // Always editable in grading mode
  const editable = true;

  // Grading state mirrors backend fields
  const [clockNumberRes, setClockNumberRes] = useState<boolean>(false);
  const [clockHandsRes, setClockHandsRes] = useState<boolean>(false);
  const [dateQuestionRes, setDateQuestionRes] = useState<boolean>(false);
  const [newsRes, setNewsRes] = useState<boolean>(false);

  // Recall individual passed states
  const [recallNamePassed, setRecallNamePassed] = useState<boolean>(false);
  const [recallSurnamePassed, setRecallSurnamePassed] = useState<boolean>(false);
  const [recallNumberPassed, setRecallNumberPassed] = useState<boolean>(false);
  const [recallStreetPassed, setRecallStreetPassed] = useState<boolean>(false);
  const [recallCityPassed, setRecallCityPassed] = useState<boolean>(false);

  useEffect(() => {
    const fetchData = async () => {
      if (!testId) return;
      try {
        const res = await fetch(`/api/web/stageone/review?id=${testId}`, {
          credentials: "include",
        });
        if (!res.ok) throw new Error("Failed to fetch stage 1 data");

        const data: TestDataFromAPI = await res.json();

        setTestData(data);

        setClockNumberRes(Boolean(data.clockNumberRes));
        setClockHandsRes(Boolean(data.clockHandsRes));
        setDateQuestionRes(Boolean(data.dateQuestionRes));
        setNewsRes(Boolean(data.newsRes));

        // Initialize recall passed states to false for grading
        setRecallNamePassed(false);
        setRecallSurnamePassed(false);
        setRecallNumberPassed(false);
        setRecallStreetPassed(false);
        setRecallCityPassed(false);

      } catch (err) {
        console.error(err);
        alert("Error loading test data.");
      }
    };
    fetchData();
  }, [testId]);

  if (!testData) return <p>Loading test data...</p>;

  const computeRecallTotal = () => {
    return [
      recallNamePassed,
      recallSurnamePassed,
      recallNumberPassed,
      recallStreetPassed,
      recallCityPassed,
    ].filter(Boolean).length;
  };

  const handleSubmit = async () => {
    if (!testData) return;

    const recallTotal = computeRecallTotal();

    const payload = {
      testID: testData.testID,
      clockNumberRes,
      clockHandsRes,
      dateQuestionRes,
      newsRes,
      recallRes: recallTotal,
    };

    try {
      const res = await fetch("/api/web/stageone/grade", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
        credentials: "include",
      });
      if (!res.ok) {
        const text = await res.text();
        throw new Error(`Failed to submit grading: ${res.status} ${text}`);
      }

      const data = await res.json();

      if (typeof data.stageOneScore === 'number' && data.stageOneScore < 5) {
        alert("Stage 1 grading complete. Proceeding to Stage 2.");
        navigate(`/stage2/${testData.testID}`);
      } else {
        alert("Stage 1 grading complete, no stage 2 required.");
        navigate(-1);
      }
    } catch (err) {
      console.error(err);
      alert("Error submitting grading.");
    }
  };

  return (
    <div>
      <Header />
      <div className={styles.mainContent}>
        <h2 className={styles.heading}>Grading Test #{testData.testID}</h2>
        <table className={styles.table}>
          <thead>
            <tr className={styles.headerRow}>
              <th className={styles.headerCell}>Question</th>
              <th className={styles.headerCell}>Answer</th>
              <th className={styles.headerCell}>Passed</th>
              <th className={styles.headerCell}>Failed</th>
            </tr>
          </thead>
          <tbody>
            {/* Date Question */}
            <tr>
              <td className={styles.cell}>What is the date? (exact only)</td>
              <td className={styles.cell}>{testData.dateQuestion || '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="dateQuestionRes"
                  checked={dateQuestionRes === true}
                  disabled={!editable}
                  onChange={() => setDateQuestionRes(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="dateQuestionRes"
                  checked={dateQuestionRes === false}
                  disabled={!editable}
                  onChange={() => setDateQuestionRes(false)}
                />
              </td>
            </tr>

            {/* Clock Numbers */}
            <tr>
              <td className={styles.cell}>Clock numbers correct?</td>
              <td className={styles.cell}>{testData.clockID ?? '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="clockNumberRes"
                  checked={clockNumberRes === true}
                  disabled={!editable}
                  onChange={() => setClockNumberRes(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="clockNumberRes"
                  checked={clockNumberRes === false}
                  disabled={!editable}
                  onChange={() => setClockNumberRes(false)}
                />
              </td>
            </tr>

            {/* Clock Hands */}
            <tr>
              <td className={styles.cell}>Clock hands correct?</td>
              <td className={styles.cell}>-</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="clockHandsRes"
                  checked={clockHandsRes === true}
                  disabled={!editable}
                  onChange={() => setClockHandsRes(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="clockHandsRes"
                  checked={clockHandsRes === false}
                  disabled={!editable}
                  onChange={() => setClockHandsRes(false)}
                />
              </td>
            </tr>

            {/* News */}
            <tr>
              <td className={styles.cell}>News</td>
              <td className={styles.cell}>{testData.news || '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="newsRes"
                  checked={newsRes === true}
                  disabled={!editable}
                  onChange={() => setNewsRes(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="newsRes"
                  checked={newsRes === false}
                  disabled={!editable}
                  onChange={() => setNewsRes(false)}
                />
              </td>
            </tr>

            {/* Recall Section Header */}
            <tr className={styles.subHeaderRow}>
              <th className={styles.subHeaderCell}>Recall Question</th>
              <th className={styles.subHeaderCell}>Value</th>
              <th className={styles.subHeaderCell}>Passed</th>
              <th className={styles.subHeaderCell}>Failed</th>
            </tr>

            {/* Recall: Name */}
            <tr>
              <td className={styles.cell}>Name</td>
              <td className={styles.cell}>{testData.recallName || '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallName"
                  checked={recallNamePassed === true}
                  disabled={!editable}
                  onChange={() => setRecallNamePassed(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallName"
                  checked={recallNamePassed === false}
                  disabled={!editable}
                  onChange={() => setRecallNamePassed(false)}
                />
              </td>
            </tr>

            {/* Recall: Surname */}
            <tr>
              <td className={styles.cell}>Surname</td>
              <td className={styles.cell}>{testData.recallSurname || '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallSurname"
                  checked={recallSurnamePassed === true}
                  disabled={!editable}
                  onChange={() => setRecallSurnamePassed(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallSurname"
                  checked={recallSurnamePassed === false}
                  disabled={!editable}
                  onChange={() => setRecallSurnamePassed(false)}
                />
              </td>
            </tr>

            {/* Recall: Number */}
            <tr>
              <td className={styles.cell}>Number</td>
              <td className={styles.cell}>{testData.recallNumber || '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallNumber"
                  checked={recallNumberPassed === true}
                  disabled={!editable}
                  onChange={() => setRecallNumberPassed(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallNumber"
                  checked={recallNumberPassed === false}
                  disabled={!editable}
                  onChange={() => setRecallNumberPassed(false)}
                />
              </td>
            </tr>

            {/* Recall: Street */}
            <tr>
              <td className={styles.cell}>Street</td>
              <td className={styles.cell}>{testData.recallStreet || '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallStreet"
                  checked={recallStreetPassed === true}
                  disabled={!editable}
                  onChange={() => setRecallStreetPassed(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallStreet"
                  checked={recallStreetPassed === false}
                  disabled={!editable}
                  onChange={() => setRecallStreetPassed(false)}
                />
              </td>
            </tr>

            {/* Recall: City */}
            <tr>
              <td className={styles.cell}>City</td>
              <td className={styles.cell}>{testData.recallCity || '-'}</td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallCity"
                  checked={recallCityPassed === true}
                  disabled={!editable}
                  onChange={() => setRecallCityPassed(true)}
                />
              </td>
              <td className={styles.centerCell}>
                <input
                  type="radio"
                  name="recallCity"
                  checked={recallCityPassed === false}
                  disabled={!editable}
                  onChange={() => setRecallCityPassed(false)}
                />
              </td>
            </tr>

          </tbody>
        </table>

        <br />
        <button className={styles.button} onClick={() => navigate(-1)}>Back</button>{" "}
        <button
          className={styles.buttonPrimary}
          onClick={handleSubmit}
          disabled={!editable}
          title={editable ? "" : "Viewing only - no edits allowed"}
        >
          Submit Grading
        </button>
      </div>
      <Footer />
    </div>
  );
};

export default withAuth(GradingStage1, ["doctor"]);
