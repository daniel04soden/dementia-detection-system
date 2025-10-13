import React from "react";
import { useNavigate } from "react-router-dom";
import styles from './dash.module.css';
import Header from "./header/Header";
import NewsCards from "./newscard/NewsCards";

const Dashboard: React.FC = () => {
  const navigate = useNavigate();

  const handleNewTest = () => {
    navigate("/initial");
  };

  const sampleTestData = [
    { id: "001", name: "John Smith", stage1: "Done", stage2: "Pending", result: "N/A" },
    { id: "002", name: "Mary Johnson", stage1: "Done", stage2: "Done", result: "Positive" },
    { id: "003", name: "Robert Brown", stage1: "Pending", stage2: "-", result: "-" },
    { id: "004", name: "Patricia Davis", stage1: "Done", stage2: "Pending", result: "N/A" },
    { id: "005", name: "Michael Miller", stage1: "Done", stage2: "Done", result: "Negative" },
    { id: "006", name: "Linda Wilson", stage1: "Pending", stage2: "-", result: "-" },
    { id: "007", name: "William Moore", stage1: "Done", stage2: "Done", result: "Positive" },
    { id: "008", name: "Barbara Taylor", stage1: "Done", stage2: "Pending", result: "N/A" },
    { id: "009", name: "James Anderson", stage1: "Pending", stage2: "-", result: "-" },
    { id: "010", name: "Elizabeth Thomas", stage1: "Done", stage2: "Done", result: "Negative" },
    { id: "011", name: "David Jackson", stage1: "Done", stage2: "Pending", result: "N/A" },
    { id: "012", name: "Jennifer White", stage1: "Pending", stage2: "-", result: "-" },
    { id: "013", name: "Richard Harris", stage1: "Done", stage2: "Done", result: "Positive" },
    { id: "014", name: "Susan Martin", stage1: "Done", stage2: "Done", result: "Negative" },
    { id: "015", name: "Joseph Thompson", stage1: "Pending", stage2: "-", result: "-" }
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

  return (
    <div className={styles.dashContainer}>
      <Header />
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
              {sampleTestData.map((test) => (
                <tr key={test.id}>
                  <td>{test.id}</td>
                  <td>{test.name}</td>
                  <td>{test.stage1}</td>
                  <td>{test.stage2}</td>
                  <td>{test.result}</td>
                  <td>
                    <a href="" className={styles.reviewBtn}>Review</a>
                  </td>
                </tr>
              ))}
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

      <footer className={styles.footer} />
    </div>
  );
};

export default Dashboard;
