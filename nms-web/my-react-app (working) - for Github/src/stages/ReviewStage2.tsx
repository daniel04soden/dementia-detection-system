import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import styles from "./stage.module.css";
import Footer from "../dashboard/footer/Footer";

interface Stage2Data {
  id: number;
  rememberQ: boolean;
  recallQ: boolean;
  speakQ: boolean;
  financeQ: boolean;
  medQ: boolean;
  mobilityQ: boolean;
}

const dummyStage2Tests: Record<string, Stage2Data> = {
  "1": {
    id: 1,
    rememberQ: true,
    recallQ: true,
    speakQ: false,
    financeQ: true,
    medQ: true,
    mobilityQ: true,
  },
  "2": {
    id: 2,
    rememberQ: false,
    recallQ: true,
    speakQ: true,
    financeQ: true,
    medQ: false,
    mobilityQ: true,
  },
  "4": {
    id: 4,
    rememberQ: true,
    recallQ: true,
    speakQ: true,
    financeQ: false,
    medQ: true,
    mobilityQ: false,
  },
  "5": {
    id: 5,
    rememberQ: true,
    recallQ: true,
    speakQ: true,
    financeQ: true,
    medQ: true,
    mobilityQ: false,
  },
  "7": {
    id: 7,
    rememberQ: false,
    recallQ: false,
    speakQ: true,
    financeQ: true,
    medQ: true,
    mobilityQ: true,
  },
  "8": {
    id: 8,
    rememberQ: true,
    recallQ: false,
    speakQ: true,
    financeQ: true,
    medQ: false,
    mobilityQ: true,
  },
  "10": {
    id: 10,
    rememberQ: true,
    recallQ: true,
    speakQ: true,
    financeQ: true,
    medQ: false,
    mobilityQ: true,
  },
  "11": {
    id: 11,
    rememberQ: false,
    recallQ: true,
    speakQ: true,
    financeQ: true,
    medQ: true,
    mobilityQ: false,
  },
  "13": {
    id: 13,
    rememberQ: true,
    recallQ: true,
    speakQ: false,
    financeQ: true,
    medQ: true,
    mobilityQ: true,
  },
  "14": {
    id: 14,
    rememberQ: true,
    recallQ: true,
    speakQ: true,
    financeQ: false,
    medQ: true,
    mobilityQ: true,
  },
};

const ReviewStage2: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<Stage2Data | null>(null);

  useEffect(() => {
    const testid = parseInt(testId!, 10);
    // Simulate async load
    setTimeout(() => {
      const data = dummyStage2Tests[testid!];
      
      setTestData(data || null);
    }, 400);
  }, [testId]);

  if (!testData) return <p>Loading stage 2 data...</p>;

  return (
    <div>
      <Header />
      <div className="main-content">
        <form>
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
                  Does the patient have more trouble remembering things that
                  happened recently?
                </td>
                <td><input type="radio" checked={testData.rememberQ} readOnly /></td>
                <td><input type="radio" checked={!testData.rememberQ} readOnly /></td>
              </tr>
              <tr>
                <td>Do they have more trouble recalling conversations a few days later?</td>
                <td><input type="radio" checked={testData.recallQ} readOnly /></td>
                <td><input type="radio" checked={!testData.recallQ} readOnly /></td>
              </tr>
              <tr>
                <td>
                  When speaking, do they have more difficulty finding the right word
                  or tend to use wrong words more often?
                </td>
                <td><input type="radio" checked={testData.speakQ} readOnly /></td>
                <td><input type="radio" checked={!testData.speakQ} readOnly /></td>
              </tr>
              <tr>
                <td>
                  Are they less able to manage money and financial affairs (e.g. paying bills)?
                </td>
                <td><input type="radio" checked={testData.financeQ} readOnly /></td>
                <td><input type="radio" checked={!testData.financeQ} readOnly /></td>
              </tr>
              <tr>
                <td>Are they less able to manage medication independently?</td>
                <td><input type="radio" checked={testData.medQ} readOnly /></td>
                <td><input type="radio" checked={!testData.medQ} readOnly /></td>
              </tr>
              <tr>
                <td>
                  Do they need more assistance with transport (not due to physical problems)?
                </td>
                <td><input type="radio" checked={testData.mobilityQ} readOnly /></td>
                <td><input type="radio" checked={!testData.mobilityQ} readOnly /></td>
              </tr>
            </tbody>
          </table>

            <button
              type="button"
              className={styles.buttonBack}
              onClick={() => navigate(-1)}
            >
              ← Back
            </button>
        </form>
      </div>
      <Footer/>
    </div>
  );
};

export default ReviewStage2;
