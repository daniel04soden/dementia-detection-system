import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from './dash.module.css';
import NewsCards from "./newscard/NewsCards";
import { withAuth } from "../../utils/withAuth";

interface TestOverview {
  testID: number;
  patientID?: number;
  patientName?: string;
  testDate?: string;
  stageOneStatus?: number | boolean;
  stageTwoStatus?: number | boolean;
  result?: string | null;
}

interface NewsOverview {
  headline: string;
  snippet: string;
  url: string;
}

const UpdatedDashboard: React.FC = () => {
  const navigate = useNavigate();
  const [tests, setTests] = useState<TestOverview[] | null>(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [news, setNews] = useState<NewsOverview[]>([]);
  const [loadingNews, setLoadingNews] = useState(true);
  const [newsError, setNewsError] = useState<string | null>(null);

  // Fetch tests from backend
  useEffect(() => {
    const fetchTests = async () => {
      try {
        const res = await fetch("/api/doctor/tests", { credentials: "include" });
        if (!res.ok) throw new Error("Failed to fetch tests");
        const data: TestOverview[] = await res.json();
        setTests(data);
      } catch (err) {
        console.error(err);
        setTests([]);
      }
    };
    fetchTests();
  }, []);

  // Fetch news from backend
  useEffect(() => {
    const fetchNews = async () => {
      try {
        const res = await fetch("/api/web/news", { credentials: "include" });
        if (!res.ok) throw new Error("Failed to fetch news");
        const data: NewsOverview[] = await res.json();
        setNews(data);
      } catch (err) {
        setNewsError((err as Error).message);
      } finally {
        setLoadingNews(false);
      }
    };
    fetchNews();
  }, []);

  // Filter tests based on search term
  const filteredTests = tests?.filter((t) =>
    Object.values(t)
      .join(" ")
      .toLowerCase()
      .includes(searchTerm.toLowerCase())
  );

  const handleReviewClick = (testID: number) => {
    const test = tests?.find((t) => t.testID === testID);
    if (!test) return alert("Test not found");
    if (!test.stageOneStatus || test.stageOneStatus !== 1) {
      navigate(`/review/one/${testID}`);
    } else {
      navigate(`/grading/one/${testID}`);
    }
  };

  const getStageOneText = (status: number | boolean | undefined) => {
  switch (status) {
    case 0:
      return "Not taken";
    case 1:
      return "Needs Grading";
    case 2:
      return "Pass";
    case 3:
      return "Fail";
    case 4:
      return "Needs immediate assistance";
    default:
      return "-";
  }
};

const getStageTwoText = (status: number | boolean | undefined) => {
  switch (status) {
    case 1:
      return "Review";
    case 2:
      return "Doesn't need to happen";
    case 3:
      return "Awaiting Submission";
    case 4:
      return "Needs immediate assistance";
    default:
      return "-";
  }
};

  return (
    <div className={styles.dashContainer}>
      

      <div>
        <p className={styles.searchText}>Enter Name or Test ID: </p>
        <input
          className={styles.search}
          type="text"
          placeholder="Search Patient Name or Test ID"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
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
                {filteredTests && filteredTests.length > 0 ? (
                  filteredTests.map((t) => (
                    <tr key={t.testID}>
                      <td>{t.testID}</td>
                      <td>{t.patientName || "-"}</td>
                      <td>{getStageOneText(t.stageOneStatus)}</td>
                      <td>{getStageTwoText(t.stageTwoStatus)}</td>
                      <td>{getStageTwoText(t.stageTwoStatus) || "-"}</td>
                      <td>
                        <button className={styles.reviewBtn} onClick={() => handleReviewClick(t.testID)}>
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
                )}
              </tbody>
            </table>
          </div>
        </div>

        <aside className={styles.newsSection}>
          <div className={styles.tableSection}>
            <table>
              <tbody>
                {loadingNews ? (
                  <tr>
                    <td>Loading news...</td>
                  </tr>
                ) : newsError ? (
                  <tr>
                    <td>Error loading news: {newsError}</td>
                  </tr>
                ) : (
                  news.map((item, index) => (
                    <tr key={index} className={styles.newsRow}>
                      <td className={styles.newsCell}>
                        <NewsCards news={item} />
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </aside>
      </main>

      
    </div>
  );
};

export default withAuth(UpdatedDashboard, ["doctor", "admin"]);
