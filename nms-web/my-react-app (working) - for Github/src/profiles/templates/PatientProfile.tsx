import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styles from '../profile.module.css';
import Header from "../../dashboard/header/Header.tsx";
import Footer from "../../dashboard/footer/Footer.tsx";

interface TestData {
  id: number;
  firstname: string
  lastname: string,
  pfp: string,
  BOD: Date;
  Gender: string
  Address: string
  dHand: boolean
  weight: number
  avgTemp: number
  restingHR: number
  boxLv: number
  history: Boolean
  smoke: Boolean
  apoeGene: Boolean
  activityLv: String
  depressed: Boolean
  diet: String
  goodSleep: Boolean
  edu: String
}

const PatientProfile: React.FC = () => {
  const { testId } = useParams<{ testId: string }>();
  const navigate = useNavigate();
  const [userInfo, setTestData] = useState<TestData | null>(null);
  

  
    const users: TestData[] = [
  {
    id: 1,
    firstname: "John",
    lastname: "Smith",
    pfp: "Smith",
    BOD: new Date(1980, 5, 15),  // Assuming birthdate: June 15, 1980
    Gender: "Male",
    Address: "123 Elm St, Springfield, IL",
    dHand: true,  // Right-hand dominant
    weight: 82,   // in kg
    avgTemp: 36.7,  // in Celsius
    restingHR: 70,  // heart rate
    boxLv: 95,  // Blood oxygen level
    history: true,  // Family history of dementia
    smoke: false,  // Doesn't smoke
    apoeGene: false,  // Doesn't carry the APOE_4 gene
    activityLv: "Moderate",  // Regular exercise
    depressed: false,  // No depression
    diet: "Balanced diet",  // Eats a balanced diet
    goodSleep: true,  // Sleeps well
    edu: "Degree",  // Higher education
  },
  {
    id: 2,
    firstname: "Mary",
    lastname: "Johnson",
    pfp: "https://as2.ftcdn.net/v2/jpg/05/14/38/85/1000_F_514388578_WYdWdhaIpcxdqPW6ARbXvRdsh0NHIih3.jpg",
    BOD: new Date(1975, 3, 25),  // April 25, 1975
    Gender: "Female",
    Address: "456 Oak St, Shelbyville, IL",
    dHand: true,  // Right-hand dominant
    weight: 68,
    avgTemp: 36.5,
    restingHR: 72,
    boxLv: 98,
    history: false,
    smoke: true,  // Smokes
    apoeGene: false,
    activityLv: "Sedentary",  // Very little exercise
    depressed: true,  // Has felt depressed
    diet: "Low-carb",  // Diet could be low-carb
    goodSleep: false,  // Poor quality sleep
    edu: "Secondary",  // High school diploma
  },
  {
    id: 3,
    firstname: "Robert",
    lastname: "Brown",
    pfp: "Smith",
    BOD: new Date(1968, 10, 12),  // November 12, 1968
    Gender: "Male",
    Address: "789 Pine St, Capital City, IL",
    dHand: true,  // Right-hand dominant
    weight: 95,
    avgTemp: 37.0,
    restingHR: 75,
    boxLv: 92,
    history: false,
    smoke: true,
    apoeGene: true,  // Carries the APOE_4 gene
    activityLv: "Moderate",
    depressed: true,
    diet: "Mediterranean",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 4,
    firstname: "Patricia",
    lastname: "Davis",
    pfp: "Smith",
    BOD: new Date(1990, 1, 1),  // January 1, 1990
    Gender: "Female",
    Address: "101 Maple St, Rivertown, IL",
    dHand: false,  // Left-hand dominant
    weight: 60,
    avgTemp: 36.6,
    restingHR: 68,
    boxLv: 97,
    history: false,
    smoke: false,
    apoeGene: false,
    activityLv: "Mild",  // Some exercise
    depressed: false,
    diet: "Balanced diet",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 5,
    firstname: "Michael",
    lastname: "Miller",
    pfp: "Smith",
    BOD: new Date(1985, 6, 20),  // July 20, 1985
    Gender: "Male",
    Address: "202 Birch St, Greendale, IL",
    dHand: true,  // Right-hand dominant
    weight: 78,
    avgTemp: 36.8,
    restingHR: 68,
    boxLv: 96,
    history: true,
    smoke: false,
    apoeGene: false,
    activityLv: "Moderate",
    depressed: false,
    diet: "Balanced diet",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 6,
    firstname: "Linda",
    lastname: "Wilson",
    pfp: "Smith",
    BOD: new Date(1995, 8, 30),  // September 30, 1995
    Gender: "Female",
    Address: "303 Cedar St, Brookville, IL",
    dHand: true,  // Right-hand dominant
    weight: 65,
    avgTemp: 36.4,
    restingHR: 74,
    boxLv: 94,
    history: false,
    smoke: false,
    apoeGene: false,
    activityLv: "Mild",
    depressed: false,
    diet: "Mediterranean",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 7,
    firstname: "William",
    lastname: "Moore",
    pfp: "Smith",
    BOD: new Date(1983, 2, 5),  // March 5, 1983
    Gender: "Male",
    Address: "404 Walnut St, Lakedale, IL",
    dHand: true,  // Right-hand dominant
    weight: 80,
    avgTemp: 36.6,
    restingHR: 78,
    boxLv: 95,
    history: false,
    smoke: true,
    apoeGene: true,  // Carries the APOE_4 gene
    activityLv: "Sedentary",
    depressed: false,
    diet: "Balanced diet",
    goodSleep: false,  // Poor sleep
    edu: "Degree",
  },
  {
    id: 8,
    firstname: "Barbara",
    lastname: "Taylor",
    pfp: "Smith",
    BOD: new Date(1963, 11, 10),  // December 10, 1963
    Gender: "Female",
    Address: "505 Spruce St, Wildwood, IL",
    dHand: true,  // Right-hand dominant
    weight: 88,
    avgTemp: 36.7,
    restingHR: 82,
    boxLv: 97,
    history: true,
    smoke: false,
    apoeGene: false,
    activityLv: "Mild",
    depressed: false,
    diet: "Balanced diet",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 9,
    firstname: "James",
    lastname: "Anderson",
    pfp: "Smith",
    BOD: new Date(1970, 7, 17),  // July 17, 1970
    Gender: "Male",
    Address: "606 Chestnut St, Lakeside, IL",
    dHand: false,  // Left-hand dominant
    weight: 72,
    avgTemp: 36.6,
    restingHR: 70,
    boxLv: 98,
    history: true,
    smoke: false,
    apoeGene: false,
    activityLv: "Moderate",
    depressed: false,
    diet: "Balanced diet",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 10,
    firstname: "Elizabeth",
    lastname: "Thomas",
    pfp: "Smith",
    BOD: new Date(1978, 11, 24),  // December 24, 1978
    Gender: "Female",
    Address: "707 Oak St, Sunnyside, IL",
    dHand: true,  // Right-hand dominant
    weight: 58,
    avgTemp: 36.5,
    restingHR: 68,
    boxLv: 99,
    history: false,
    smoke: false,
    apoeGene: false,
    activityLv: "Moderate",
    depressed: false,
    diet: "Mediterranean",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 11,
    firstname: "David",
    lastname: "Jackson",
    pfp: "Smith",
    BOD: new Date(1982, 4, 17),  // May 17, 1982
    Gender: "Male",
    Address: "808 Pine St, Highland, IL",
    dHand: true,  // Right-hand dominant
    weight: 89,
    avgTemp: 36.8,
    restingHR: 75,
    boxLv: 94,
    history: true,
    smoke: true,
    apoeGene: true,  // Carries the APOE_4 gene
    activityLv: "Moderate",
    depressed: true,  // Has depression
    diet: "Low-carb",
    goodSleep: false,  // Poor sleep
    edu: "Secondary",
  },
    {
    id: 12,
    firstname: "Jennifer",
    lastname: "White",
    pfp: "Smith",
    BOD: new Date(1986, 9, 12),  // September 12, 1986
    Gender: "Female",
    Address: "909 Birch St, Lakeview, IL",
    dHand: true,  // Right-hand dominant
    weight: 70,
    avgTemp: 36.7,
    restingHR: 72,
    boxLv: 97,
    history: false,
    smoke: false,
    apoeGene: false,
    activityLv: "Moderate",
    depressed: false,
    diet: "Balanced diet",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 13,
    firstname: "Richard",
    lastname: "Harris",
    pfp: "Smith",
    BOD: new Date(1950, 2, 22),  // February 22, 1950
    Gender: "Male",
    Address: "1010 Birchwood St, Northbay, IL",
    dHand: true,  // Right-hand dominant
    weight: 93,
    avgTemp: 36.5,
    restingHR: 80,
    boxLv: 94,
    history: true,  // Family history of dementia
    smoke: false,
    apoeGene: false,
    activityLv: "Sedentary",
    depressed: false,
    diet: "Low-carb",
    goodSleep: false,  // Poor sleep
    edu: "Primary",
  },
  {
    id: 14,
    firstname: "Susan",
    lastname: "Martin",
    pfp: "Smith",
    BOD: new Date(1972, 11, 10),  // December 10, 1972
    Gender: "Female",
    Address: "1111 Oakwood St, Greenfield, IL",
    dHand: false,  // Left-hand dominant
    weight: 75,
    avgTemp: 36.9,
    restingHR: 74,
    boxLv: 95,
    history: false,
    smoke: false,
    apoeGene: false,
    activityLv: "Mild",
    depressed: false,
    diet: "Mediterranean",
    goodSleep: true,
    edu: "Degree",
  },
  {
    id: 15,
    firstname: "Joseph",
    lastname: "Thompson",
    pfp: "Smith",
    BOD: new Date(1992, 6, 25),  // July 25, 1992
    Gender: "Male",
    Address: "1212 Maple St, Hillcrest, IL",
    dHand: true,  // Right-hand dominant
    weight: 79,
    avgTemp: 36.6,
    restingHR: 71,
    boxLv: 97,
    history: false,
    smoke: false,
    apoeGene: false,
    activityLv: "Moderate",
    depressed: false,
    diet: "Balanced diet",
    goodSleep: true,
    edu: "Degree",
  }
];

const getApplicableRisks = (user: TestData) => {
  const risks: string[] = [];

  // Family History of Dementia
  if (user.history) risks.push("Family history of dementia");

  // APOE_4 Gene (strong correlation with dementia risk)
  if (user.apoeGene) risks.push("Carries the APOE_4 gene");

  // Smoking (can contribute to heart disease, etc.)
  if (user.smoke) risks.push("Smoking (increases heart disease and other risks)");

  // Depression (could contribute to cognitive decline)
  if (user.depressed) risks.push("History of depression (may contribute to cognitive decline)");

  // Poor Sleep (linked to mental health issues)
  if (!user.goodSleep) risks.push("Poor quality sleep (can affect mental health)");

  return risks;
};

  // Fetch data from your backend
      useEffect(() => {
    const fetchData = async () => {
      // Simulate delay (like a network call)
      await new Promise((resolve) => setTimeout(resolve, 300));
      const testid = parseInt(testId!, 10);
  
  
      // Lookup dummy data by testid
      const data = users[testid!];
  
      if (data) {
        setTestData(data);
      }
  };

  fetchData();
}, [testId]);

  if (!userInfo) return <p>Loading test data...</p>;
  
  return (
    <div className={styles.dashContainer}>
      <Header />
      <main className={styles.mainContent}>
        <h1>{userInfo.firstname} {userInfo.lastname}!</h1>
        <img className={styles.headshot} src={userInfo.pfp} />
        <p>Date of Birth: {""+userInfo.BOD.toLocaleDateString('en-GB', {day: '2-digit', month: 'numeric',year: 'numeric'})}</p>
        <p>Gender: {userInfo.Gender}</p>
        <p>Address: {userInfo.Address}</p>        {/* eircode */}

        <h2>Noted Risks</h2>
        <ul>
          {getApplicableRisks(userInfo).length > 0 ? (
            getApplicableRisks(userInfo).map((risk, index) => (
              <li key={index}>{risk}</li>
            ))
          ) : (
            <li>No significant risks.</li>
          )}
        </ul>

      </main>
      <Footer />
    </div>
  );
};

export default PatientProfile;

