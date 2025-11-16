import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import styles from './stage.module.css';
import Footer from "../dashboard/footer/Footer";

interface TestData {
  id: number;
  reviewDate: boolean;
  clockHands: boolean;
  clockNumber: boolean;
  dateQuestion: boolean;
  firstRecall: boolean;
  lastRecall: boolean;
  numberRecall: boolean;
  streetRecall: boolean;
  CityRecall: boolean;
}


const ReviewStage1: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [testData, setTestData] = useState<TestData | null>(null);

  const handleNext = (testId: string) => {
    navigate(`/next/${testId}`);
  };

  const dummyTests: Record<string, TestData> = {
  "1": {
    id: 1,
    reviewDate: true,
    clockHands: true,
    clockNumber: false,
    dateQuestion: true,
    firstRecall: true,
    lastRecall: false,
    numberRecall: true,
    streetRecall: true,
    CityRecall: true,
  },
  "2": {
    id: 2,
    reviewDate: false,
    clockHands: true,
    clockNumber: true,
    dateQuestion: true,
    firstRecall: true,
    lastRecall: true,
    numberRecall: false,
    streetRecall: true,
    CityRecall: true,
  },
  "4": {
    id: 4,
    reviewDate: true,
    clockHands: false,
    clockNumber: true,
    dateQuestion: false,
    firstRecall: true,
    lastRecall: true,
    numberRecall: true,
    streetRecall: false,
    CityRecall: true,
  },
  "5": {
    id: 5,
    reviewDate: true,
    clockHands: true,
    clockNumber: true,
    dateQuestion: true,
    firstRecall: false,
    lastRecall: false,
    numberRecall: true,
    streetRecall: true,
    CityRecall: true,
  },
  "7": {
    id: 7,
    reviewDate: false,
    clockHands: false,
    clockNumber: true,
    dateQuestion: true,
    firstRecall: true,
    lastRecall: true,
    numberRecall: true,
    streetRecall: false,
    CityRecall: true,
  },
  "8": {
    id: 8,
    reviewDate: true,
    clockHands: true,
    clockNumber: true,
    dateQuestion: false,
    firstRecall: true,
    lastRecall: true,
    numberRecall: false,
    streetRecall: true,
    CityRecall: true,
  },
  "10": {
    id: 10,
    reviewDate: true,
    clockHands: false,
    clockNumber: true,
    dateQuestion: true,
    firstRecall: true,
    lastRecall: false,
    numberRecall: false,
    streetRecall: true,
    CityRecall: true,
  },
  "11": {
    id: 11,
    reviewDate: false,
    clockHands: true,
    clockNumber: true,
    dateQuestion: true,
    firstRecall: false,
    lastRecall: true,
    numberRecall: true,
    streetRecall: true,
    CityRecall: true,
  },
  "13": {
    id: 13,
    reviewDate: true,
    clockHands: true,
    clockNumber: true,
    dateQuestion: false,
    firstRecall: true,
    lastRecall: true,
    numberRecall: true,
    streetRecall: false,
    CityRecall: true,
  },
  "14": {
    id: 14,
    reviewDate: true,
    clockHands: false,
    clockNumber: false,
    dateQuestion: true,
    firstRecall: true,
    lastRecall: true,
    numberRecall: true,
    streetRecall: true,
    CityRecall: false,
  },
};


    // Fetch data from your backend
    useEffect(() => {
  const fetchData = async () => {
    // Simulate delay (like a network call)
    await new Promise((resolve) => setTimeout(resolve, 300));
    const testid = parseInt(testId!, 10);


    // Lookup dummy data by testid
    const data = dummyTests[testid!];

    if (data) {
      setTestData(data);
    } else {
      // fallback if testid is not found
      setTestData({
        id: 0,
        reviewDate: true,
        clockHands: true,
        clockNumber: true,
        dateQuestion: true,
        firstRecall: true,
        lastRecall: true,
        numberRecall: true,
        streetRecall: true,
        CityRecall: true,
      });
    }
  };

  fetchData();
}, [testId]);

  if (!testData) return <p>Loading test data...</p>;
  const hasAnyFailure = Object.values(testData).includes(false);

  return (
    <div>
      <Header />
      <div className="main-content">
        <form>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Question</th>
                <th>Passed</th>
                <th>Failed</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>What is the date? (exact only)</td>
                <td><input type="radio" checked={testData.dateQuestion} readOnly /></td>
                <td><input type="radio" checked={!testData.dateQuestion} readOnly /></td>
              </tr>
              <tr>
                <td>Clock numbers correct?</td>
                <td><input type="radio" checked={testData.clockNumber} readOnly /></td>
                <td><input type="radio" checked={!testData.clockNumber} readOnly /></td>
              </tr>
              <tr>
                <td>Clock hands correct?</td>
                <td><input type="radio" checked={testData.clockHands} readOnly /></td>
                <td><input type="radio" checked={!testData.clockHands} readOnly /></td>
              </tr>
            </tbody>
          </table>

          <br />

          <table className={styles.table}>
            <thead>
              <tr>
                <th>Recall Question</th>
                <th>Passed</th>
                <th>Failed</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>John</td>
                <td><input type="radio" checked={testData.firstRecall} readOnly /></td>
                <td><input type="radio" checked={!testData.firstRecall} readOnly /></td>
              </tr>
              <tr>
                <td>Brown</td>
                <td><input type="radio" checked={testData.lastRecall} readOnly /></td>
                <td><input type="radio" checked={!testData.lastRecall} readOnly /></td>
              </tr>
              <tr>
                <td>42</td>
                <td><input type="radio" checked={testData.numberRecall} readOnly /></td>
                <td><input type="radio" checked={!testData.numberRecall} readOnly /></td>
              </tr>
              <tr>
                <td>West St</td>
                <td><input type="radio" checked={testData.streetRecall} readOnly /></td>
                <td><input type="radio" checked={!testData.streetRecall} readOnly /></td>
              </tr>
              <tr>
                <td>Kensington</td>
                <td><input type="radio" checked={testData.CityRecall} readOnly /></td>
                <td><input type="radio" checked={!testData.CityRecall} readOnly /></td>
              </tr>
            </tbody>
          </table>

          
          <button type="button" onClick={() => navigate(-1)}>
            Back
          </button>
          
          {hasAnyFailure && (<button type="button" onClick={() => handleNext(""+testData.id)}>
            Next
          </button>
          )}
        </form>
      </div>
      <Footer/>
    </div>
  );
};

export default ReviewStage1;