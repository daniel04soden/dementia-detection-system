import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import styles from './stage.module.css';
import { withAuth } from "../../utils/withAuth";

interface TestDataFromAPI2 {
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
  recallRes: number; // numeric total
  stageTwoStatus: number; // New field to check if Stage 2 is available
}

const ReviewStage1: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<TestDataFromAPI2 | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!testId) return;
      try {
        const res = await fetch(`/api/web/stageone/review?id=${testId}`, {
          credentials: "include",
        });
        if (!res.ok) throw new Error("Failed to fetch stage 1 data");
        const data: TestDataFromAPI2 = await res.json();
        setTestData(data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchData();
  }, [testId]);

  if (!testData) return <p>Loading test data...</p>;

  const handleNextTest = () => {
    // Check if Stage 2 is available (status 1) and navigate to the next test
    if (testData.stageTwoStatus === 1) {
      navigate(`/grading/two/${testData.testID}`);
    } else {
      alert("Stage 2 is not available yet for this test.");
    }
  };

  return (
    <div>
      <Header />
      <div className="main-content">
        <form onSubmit={e => { e.preventDefault(); }}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Question</th>
                <th>Answer</th>
                <th>Correct</th>
                <th>Incorrect</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Date (exact)</td>
                <td>{testData.dateQuestion || '-'}</td>
                <td>
                  <input type="radio" disabled checked={testData.dateQuestionRes} />
                </td>
                <td>
                  <input type="radio" disabled checked={!testData.dateQuestionRes} />
                </td>
              </tr>
              <tr>
                <td>Clock numbers</td>
                <td>{testData.clockID ?? '-'}</td>
                <td>
                  <input type="radio" disabled checked={testData.clockNumberRes} />
                </td>
                <td>
                  <input type="radio" disabled checked={!testData.clockNumberRes} />
                </td>
              </tr>
              <tr>
                <td>Clock hands</td>
                <td>-</td>
                <td>
                  <input type="radio" disabled checked={testData.clockHandsRes} />
                </td>
                <td>
                  <input type="radio" disabled checked={!testData.clockHandsRes} />
                </td>
              </tr>
              <tr>
                <td>News</td>
                <td>{testData.news || '-'}</td>
                <td>
                  <input type="radio" disabled checked={testData.newsRes === true} />
                </td>
                <td>
                  <input type="radio" disabled checked={testData.newsRes === false} />
                </td>
              </tr>
              <tr>
                <td>Recall</td>
                <td>
                  <div>
                    <div>Name: {testData.recallName || '-'}</div>
                    <div>Surname: {testData.recallSurname || '-'}</div>
                    <div>Number: {testData.recallNumber || '-'}</div>
                    <div>Street: {testData.recallStreet || '-'}</div>
                    <div>City: {testData.recallCity || '-'}</div>
                  </div>
                </td>
                <td colSpan={2}>
                  <p>{testData.recallRes}/5</p>
                </td>
              </tr>
            </tbody>
          </table>

          <br />

          <button type="button" onClick={() => navigate(-1)}>Back</button>{" "}
          {testData.stageTwoStatus === 1 && (
            <button type="button" onClick={handleNextTest}>
              Next Test (Stage 2)
            </button>
          )}
        </form>
      </div>
      <Footer />
    </div>
  );
};

export default withAuth(ReviewStage1, ["doctor", "admin"]);
