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

const Beans1Editable: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<TestDataFromAPI | null>(null);

  const [clockNumberRes, setClockNumberRes] = useState<boolean>(false);
  const [clockHandsRes, setClockHandsRes] = useState<boolean>(false);
  const [dateQuestionRes, setDateQuestionRes] = useState<boolean>(false);
  const [newsRes, setNewsRes] = useState<boolean>(false);
  const [recallRes, setRecallRes] = useState<number>(0);

  useEffect(() => {
    const fetchData = async () => {
      if (!testId) return;

      try {
        const res = await fetch(`/api/web/stageone/review?id=${testId}`, { credentials: "include" });
        if (!res.ok) throw new Error(await res.text());
        const data: TestDataFromAPI = await res.json();
        setTestData(data);

        setClockNumberRes(Boolean(data.clockNumberRes));
        setClockHandsRes(Boolean(data.clockHandsRes));
        setDateQuestionRes(Boolean(data.dateQuestionRes));
        setNewsRes(Boolean(data.newsRes));
        setRecallRes(data.recallRes);
      } catch (err) {
        console.error(err);
        alert("Error loading test data.");
      }
    };
    fetchData();
  }, [testId]);

  if (!testData) return <p>Loading...</p>;

  const handleSubmit = async () => {
    const payload = {
      testID: testData.testID,
      clockNumberRes,
      clockHandsRes,
      dateQuestionRes,
      newsRes,
      recallRes,
    };

    try {
      const res = await fetch(`/api/web/stageone/grade?id=${testId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error(await res.text());
      alert("Grading submitted!");
    } catch (err) {
      console.error(err);
      alert("Error submitting grading.");
    }
    navigate(-1)
  };

  const clockImages: Record<number, string> = { 1: clock1, 2: clock2, 3: clock3, 4: clock4, 5: clock5, 6: clock6 };
  const clock = testData.clockID !== null ? clockImages[testData.clockID + 1] : undefined;

  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <div className={styles.container}>
          <h2 className={styles.heading}>Grading Test #{testData.testID}</h2>

          <table className={styles.table}>
            <thead>
              <tr>
                <th>Question</th>
                <th>Answer</th>
                <th>Pass</th>
                <th>Fail</th>
              </tr>
            </thead>
            <tbody>
              {/* Date Question */}
              <tr>
                <td>What is the date?</td>
                <td>{testData.testDate || '-'} = {testData.dateQuestion || '-'}</td>
                <td>
                  <input type="radio" checked={dateQuestionRes} onChange={() => setDateQuestionRes(true)} />
                </td>
                <td>
                  <input type="radio" checked={!dateQuestionRes} onChange={() => setDateQuestionRes(false)} />
                </td>
              </tr>

              {/* News */}
              <tr>
                <td>News</td>
                <td>{testData.news || '-'}</td>
                <td>
                  <input type="radio" checked={newsRes} onChange={() => setNewsRes(true)} />
                </td>
                <td>
                  <input type="radio" checked={!newsRes} onChange={() => setNewsRes(false)} />
                </td>
              </tr>

              {/* Clock Section */}
              <tr>
                <th colSpan={4} className={styles.subHeader}>Clock</th>
              </tr>
              <tr>
                <td colSpan={4} style={{ textAlign: "center" }}>
                  {clock && <img src={clock} alt="Clock" className={styles.clockImg} />}
                </td>
              </tr>
              <tr>
                <td>Clock numbers correct?</td>
                <td></td>
                <td>
                  <input type="radio" checked={clockNumberRes} onChange={() => setClockNumberRes(true)} />
                </td>
                <td>
                  <input type="radio" checked={!clockNumberRes} onChange={() => setClockNumberRes(false)} />
                </td>
              </tr>
              <tr>
                <td>Clock hands correct?</td>
                <td></td>
                <td>
                  <input type="radio" checked={clockHandsRes} onChange={() => setClockHandsRes(true)} />
                </td>
                <td>
                  <input type="radio" checked={!clockHandsRes} onChange={() => setClockHandsRes(false)} />
                </td>
              </tr>

              {/* Recall */}
              <tr>
                <th colSpan={4} className={styles.subHeader}>Recall</th>
              </tr>
              {["Name","Surname","Number","Street","City"].map((field, i) => (
                <tr key={i}>
                  <td>{field}</td>
                  <td>{["John","Brown","43","West (St)","Kensington"][i]}</td>
                  <td>{[testData.recallName,testData.recallSurname,testData.recallNumber,testData.recallStreet,testData.recallCity][i] || "-"}</td>
                </tr>
              ))}
              <tr>
                <td>Score</td>
                <td></td>
                <td>
                  <select value={recallRes} onChange={(e) => setRecallRes(Number(e.target.value))}>
                    {[0,1,2,3,4,5].map(n => <option key={n} value={n}>{n}</option>)}
                  </select>/5
                </td>
              </tr>
            </tbody>
          </table>

          <div className={styles.buttons}>
            <button className={styles.btn} onClick={() => navigate(-1)}>Back</button>
            <button className={styles.btnPrimary} onClick={handleSubmit}>Submit Grading</button>
          </div>
        </div>
      </main>
    </div>
  );
};

export default withAuth(Beans1Editable, ["doctor"]);
