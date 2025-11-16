import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from './dash.module.css';
import Header from "./header/Header";
import NewsCards from "./newscard/NewsCards";
import Footer from "./footer/Footer";

const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  const handleNewTest = () => {
    navigate("/initial");
  };

  const handleReview = (testId: string) => {
    navigate(`/review/${testId}`);
  };

  const sampleTestData = [
    { id: "001", firstname: "John",		 lastname: "Smith",		 stage1: "Done",	 stage2: "Pending", result: "N/A" },
    { id: "002", firstname: "Mary",		 lastname: "Johnson",	 stage1: "Done",	 stage2: "Done",	 result: "Positive" },
    { id: "003", firstname: "Robert",	 lastname: "Brown",		 stage1: "Pending",	 stage2: "-",		 result: "-" },
    { id: "004", firstname: "Patricia",	 lastname: "Davis",		 stage1: "Done",	 stage2: "Pending",	 result: "N/A" },
    { id: "005", firstname: "Michael",	 lastname: "Miller",	 stage1: "Done",	 stage2: "Done",	 result: "Negative" },
    { id: "006", firstname: "Linda",	 lastname: "Wilson",	 stage1: "Pending",	 stage2: "-",		 result: "-" },
    { id: "007", firstname: "William",	 lastname: "Moore",		 stage1: "Done",	 stage2: "Done",	 result: "Positive" },
    { id: "008", firstname: "Barbara",	 lastname: "Taylor",	 stage1: "Done",	 stage2: "Pending",	 result: "N/A" },
    { id: "009", firstname: "James",	 lastname: "Anderson",	 stage1: "Pending",	 stage2: "-",		 result: "-" },
    { id: "010", firstname: "Elizabeth", lastname: "Thomas",	 stage1: "Done",	 stage2: "Done",	 result: "Negative" },
    { id: "011", firstname: "David",	 lastname: "Jackson",	 stage1: "Done",	 stage2: "Pending",	 result: "N/A" },
    { id: "012", firstname: "Jennifer",	 lastname: "White",		 stage1: "Pending",	 stage2: "-",		 result: "-" },
    { id: "013", firstname: "Richard",	 lastname: "Harris",	 stage1: "Done",	 stage2: "Done",	 result: "Positive" },
    { id: "014", firstname: "Susan",	 lastname: "Martin",	 stage1: "Done",	 stage2: "Done",	 result: "Negative" },
    { id: "015", firstname: "Joseph",	 lastname: "Thompson",	 stage1: "Pending",	 stage2: "-",		 result: "-" }
  ];

  const sampleNews = [
    {
        "headline": "New Blood Biomarker Panel Shows 90% Accuracy in Early Alzheimer's Detection",
        "snippet": "Multicenter study validates a panel of plasma biomarkers (p-tau217, GFAP, NfL) for identifying amyloid pathology in preclinical and prodromal Alzheimer's disease stages.",
        "url": "https://jamanetwork.com/journals/jamaneurology/article-abstract/2814698"
    },
    {
        "headline": "Lecanemab Demonstrates Significant Slowing of Cognitive Decline in Early AD",
        "snippet": "Phase 3 trial results show lecanemab reduces clinical decline on CDR-SB by 27% compared to placebo over 18 months in patients with early Alzheimer's disease.",
        "url": "https://www.nejm.org/doi/full/10.1056/NEJMoa2212948"
    },
    {
        "headline": "Digital Cognitive Assessment Tools Show Strong Correlation With Standardized Tests",
        "snippet": "Validation study demonstrates that tablet-based digital cognitive assessments have 94% concordance with MoCA and MMSE in detecting mild cognitive impairment.",
        "url": "https://alz-journals.onlinelibrary.wiley.com/doi/10.1002/alz.13456"
    },
    {
        "headline": "Sleep Disordered Breathing Linked to Increased Tau Pathology in Older Adults",
        "snippet": "Longitudinal neuroimaging study finds association between sleep apnea severity and accelerated tau accumulation in medial temporal lobe regions vulnerable to Alzheimer's.",
        "url": "https://www.ahajournals.org/doi/10.1161/STROKEAHA.123.044375"
    },
    {
        "headline": "Novel GABAergic Compound Shows Promise for Agitation in Dementia",
        "snippet": "Phase 2 trial demonstrates significant reduction in agitation symptoms without sedation effects in patients with Alzheimer's disease-related agitation.",
        "url": "https://jamanetwork.com/journals/jamapsychiatry/article-abstract/2817022"
    },
    {
        "headline": "Genetic Risk Scores Improve Prediction of Dementia Conversion From MCI",
        "snippet": "Integration of polygenic risk scores with clinical and biomarker data enhances prediction of progression from mild cognitive impairment to dementia by 18%.",
        "url": "https://www.nature.com/articles/s41591-024-02943-6"
    },
    {
        "headline": "Multi-domain Lifestyle Intervention Shows Sustained Cognitive Benefits",
        "snippet": "Two-year follow-up data from FINGER model demonstrates maintained cognitive benefits through combined physical activity, cognitive training, and vascular risk management.",
        "url": "https://www.thelancet.com/journals/lanepe/article/PIIS2666-7762(24)00076-1/fulltext"
    },
    {
        "headline": "PET Imaging Reveals Distinct Tau Spreading Patterns in Different Dementia Subtypes",
        "snippet": "Advanced tau-PET analysis identifies subtype-specific spreading patterns that correlate with clinical symptom profiles in Alzheimer's and primary age-related tauopathy.",
        "url": "https://academic.oup.com/brain/advance-article/doi/10.1093/brain/awae126/7642345"
    },
    {
        "headline": "Remote Monitoring Technology Detects Early Functional Decline in Dementia",
        "snippet": "Smart home sensors and wearable devices identify subtle changes in daily activities 6-12 months before clinical detection of functional impairment.",
        "url": "https://alz-journals.onlinelibrary.wiley.com/doi/10.1002/alz.13892"
    },
    {
        "headline": "Microbiome Modulation Shows Potential for Reducing Neuroinflammation",
        "snippet": "Randomized controlled trial demonstrates that specific probiotic supplementation reduces inflammatory markers and improves cognitive scores in mild Alzheimer's patients.",
        "url": "https://www.sciencedirect.com/science/article/pii/S2666354624000589"
    }
]

const [searchTerm, setSearchTerm] = useState("");

 // Filter patients based on the search term (case-insensitive)
  const filteredPatients = sampleTestData.filter((p) =>
    Object.values(p)
      .join(" ")
      .toLowerCase()
      .includes(searchTerm.toLowerCase())
  );

  return (
    <div className={styles.dashContainer}>
      <Header />
      <div>
      <p className={styles.searchText}>Enter Name or Test ID: </p>
      <input className={styles.search}
            type="text"
            placeholder="Search Patient Name or Test ID"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)} />
      </div>
      <main className={styles.mainContent}>
        <div className={styles.leftSection}>
        <div className={styles.tableSection}>
          <table>
            <thead>
              <tr>
                <th>Test ID</th>
                <th>Patient Name</th>
                <th>Stage One</th>
                <th>Stage Two</th>
                <th>Result</th>
                <th>View</th>
              </tr>
            </thead>
            <tbody>
               {filteredPatients.length > 0 ? (
                  filteredPatients.map((test, index) => (
                    <tr key={test.id}>
                      <td>{test.id}</td>
                      <td>{test.firstname} {test.lastname}</td>
                      <td>{test.stage1}</td>
                      <td>{test.stage2}</td>
                      <td>{test.result}</td>
                      <td>
                        <button className={styles.reviewBtn} onClick={() => handleReview(test.id)}>
                          Review
                        </button>
                      </td>
                    </tr>
                    ))
                  ) : (
                  <tr>
                    <td className={styles.EmptyTable} colSpan={6}>
                      No matching patients found.
                    </td>
                  </tr>
                  )
                }
            </tbody>
          </table>
        </div>
        <button className={styles.newTestBtn} onClick={handleNewTest}>
          New Test
        </button>
        </div>

        <aside className={styles.newsSection}>
          <div className={styles.tableSection}>
          <table>
            <thead></thead>
            <tbody>{sampleNews.map((item, index) => (
            <NewsCards key={index} news={item} />
            ))}
            </tbody>
          </table>
          </div>
        </aside>
      </main>

      <Footer />
    </div>
  );
};

export default Dashboard;
