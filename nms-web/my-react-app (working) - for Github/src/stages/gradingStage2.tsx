import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import Footer from "../dashboard/footer/Footer";
import { withAuth } from "../../utils/withAuth";
import styles from "./stage.module.css";

interface Stage2Data {
  id: number;
  memoryScore: number;  // 1 for Yes, 2 for No, 3 for Don't Know
  conversationScore: number;
  speakingScore: number;
  financialScore: number;
  medicineScore: number;
  transportScore: number;
  totalScore: number;
}

type TestOverview = {
  TestID: number;
  PatientID: number;
  PatientName: string;
  TestDate: string;
  StageOneStatus: number;
  StageTwoStatus: number;
};


const GradingStage2: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<Stage2Data | null>(null);

  const [rememberQPassed, setRememberQPassed] = useState<number | null>(null);
  const [recallQPassed, setRecallQPassed] = useState<number | null>(null);
  const [speakQPassed, setSpeakQPassed] = useState<number | null>(null);
  const [financeQPassed, setFinanceQPassed] = useState<number | null>(null);
  const [medQPassed, setMedQPassed] = useState<number | null>(null);
  const [mobilityQPassed, setMobilityQPassed] = useState<number | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(`/api/web/stagetwo/review?id=${testId}`);
        if (!res.ok) {
          throw new Error("Failed to fetch test data");
        }
        const data: Stage2Data = await res.json();
        setTestData(data);
        // Set the initial states based on the fetched data
        setRememberQPassed(data.memoryScore);
        setRecallQPassed(data.conversationScore);
        setSpeakQPassed(data.speakingScore);
        setFinanceQPassed(data.financialScore);
        setMedQPassed(data.medicineScore);
        setMobilityQPassed(data.transportScore);
      } catch (err) {
        console.error(err);
        alert("Error loading test data.");
      }
    };

    fetchData();
  }, [testId]);

  if (!testData) return <p>Loading stage 2 data...</p>;

  const handleRadioChange = (question: string, value: number) => {
    switch (question) {
      case "rememberQ":
        setRememberQPassed(value);
        break;
      case "recallQ":
        setRecallQPassed(value);
        break;
      case "speakQ":
        setSpeakQPassed(value);
        break;
      case "financeQ":
        setFinanceQPassed(value);
        break;
      case "medQ":
        setMedQPassed(value);
        break;
      case "mobilityQ":
        setMobilityQPassed(value);
        break;
      default:
        break;
    }
  };

  const handleSubmit = async () => {
  if (!testData) return;

  // Step 3: Use the patientID to create the payload for stage two grading
  const payload = {
    patientID: testData.id,  // Correctly using testData.id as the patientID
    memoryScore: rememberQPassed || 3, // Defaulting to "Don't Know" (3) if no answer
    conversationScore: recallQPassed || 3,
    speakingScore: speakQPassed || 3,
    financialScore: financeQPassed || 3,
    medicineScore: medQPassed || 3,
    transportScore: mobilityQPassed || 3,
  };

  console.log(testData.id)

  try {
    const res = await fetch("/api/web/stagetwo/grade", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
      credentials: "include",
    });

    if (!res.ok) {
      const text = await res.text();
      throw new Error(`Failed to submit grading: ${res.status} ${text}`);
    }

    alert("Stage 2 grading complete.");
    navigate(`/nextStage/${testData.id}`);
  } catch (err) {
    console.error(err);
    alert("Error submitting grading.");
  }
};



  return (
    <div>
      <Header />
      <div className="main-content">
        <h2 className={styles.heading}>Grading Stage 2 Test #{testData.id}</h2>
        <form>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Question</th>
                <th>Yes (No Issue)</th>
                <th>No (Impairment)</th>
                <th>Don't Know</th>
              </tr>
            </thead>
            <tbody>
              {/* Remembering Recent Events */}
              <tr>
                <td>Does the patient have more trouble remembering things that happened recently?</td>
                <td>
                  <input
                    type="radio"
                    value="1"
                    checked={rememberQPassed === 1}
                    onChange={() => handleRadioChange("rememberQ", 1)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="2"
                    checked={rememberQPassed === 2}
                    onChange={() => handleRadioChange("rememberQ", 2)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="3"
                    checked={rememberQPassed === 3}
                    onChange={() => handleRadioChange("rememberQ", 3)}
                  />
                </td>
              </tr>

              {/* Recall Conversations */}
              <tr>
                <td>Do they have more trouble recalling conversations a few days later?</td>
                <td>
                  <input
                    type="radio"
                    value="1"
                    checked={recallQPassed === 1}
                    onChange={() => handleRadioChange("recallQ", 1)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="2"
                    checked={recallQPassed === 2}
                    onChange={() => handleRadioChange("recallQ", 2)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="3"
                    checked={recallQPassed === 3}
                    onChange={() => handleRadioChange("recallQ", 3)}
                  />
                </td>
              </tr>

              {/* Speaking Difficulty */}
              <tr>
                <td>When speaking, do they have more difficulty finding the right word or tend to use wrong words more often?</td>
                <td>
                  <input
                    type="radio"
                    value="1"
                    checked={speakQPassed === 1}
                    onChange={() => handleRadioChange("speakQ", 1)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="2"
                    checked={speakQPassed === 2}
                    onChange={() => handleRadioChange("speakQ", 2)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="3"
                    checked={speakQPassed === 3}
                    onChange={() => handleRadioChange("speakQ", 3)}
                  />
                </td>
              </tr>

              {/* Managing Finances */}
              <tr>
                <td>Are they less able to manage money and financial affairs (e.g. paying bills)?</td>
                <td>
                  <input
                    type="radio"
                    value="1"
                    checked={financeQPassed === 1}
                    onChange={() => handleRadioChange("financeQ", 1)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="2"
                    checked={financeQPassed === 2}
                    onChange={() => handleRadioChange("financeQ", 2)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="3"
                    checked={financeQPassed === 3}
                    onChange={() => handleRadioChange("financeQ", 3)}
                  />
                </td>
              </tr>

              {/* Medication Management */}
              <tr>
                <td>Are they less able to manage medication independently?</td>
                <td>
                  <input
                    type="radio"
                    value="1"
                    checked={medQPassed === 1}
                    onChange={() => handleRadioChange("medQ", 1)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="2"
                    checked={medQPassed === 2}
                    onChange={() => handleRadioChange("medQ", 2)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="3"
                    checked={medQPassed === 3}
                    onChange={() => handleRadioChange("medQ", 3)}
                  />
                </td>
              </tr>

              {/* Mobility Assistance */}
              <tr>
                <td>Do they need more assistance with transport (not due to physical problems)?</td>
                <td>
                  <input
                    type="radio"
                    value="1"
                    checked={mobilityQPassed === 1}
                    onChange={() => handleRadioChange("mobilityQ", 1)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="2"
                    checked={mobilityQPassed === 2}
                    onChange={() => handleRadioChange("mobilityQ", 2)}
                  />
                </td>
                <td>
                  <input
                    type="radio"
                    value="3"
                    checked={mobilityQPassed === 3}
                    onChange={() => handleRadioChange("mobilityQ", 3)}
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </form>
        <button onClick={() => navigate(-1)}>Back</button>
        <button onClick={handleSubmit}>Submit Grading</button>
      </div>
      <Footer />
    </div>
  );
};

export default withAuth(GradingStage2, ["doctor", "admin"]);
