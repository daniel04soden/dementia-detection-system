import styles from "./aboutUs.module.css";

const AboutUs: React.FC = () => {
  return (
    <div className={styles.page}>
      {/* Hero */}
      <section className={styles.hero}>
        <div className={styles.heroOverlay} />
        <div className={styles.heroContent}>
        <h1>Early Dementia Risk Detection</h1>
        <p>
            A smart platform that calculates dementia risk scores from lifestyle, health, and medical data, using speech recognition for quick insights.
        </p>
        </div>
        </section>

        <main className={styles.main}>
        <section className={styles.narrative}>
            <span className={styles.sectionLabel}>Our Purpose</span>
            <h2>Insight Before Symptoms Appear</h2>
            <p>
            Dementia risk develops gradually. Our platform detects early warning signs from speech and health factors, helping users understand their risk and act sooner.
            </p>
        </section>

        <section className={styles.split}>
            <div className={styles.splitText}>
            <h3>What We Deliver</h3>
            <p>
                Risk assessments that combine medical, lifestyle, and cognitive data with speech analysis, designed to deliver faster, actionable results than a traditional appointment.
            </p>
            </div>

            <div className={styles.splitHighlight}>
            <h3>How It Works</h3>
            <p className={styles.onPurp}>
                Advanced algorithms analyze speech patterns and medical information, automatically generating risk scores. Inbuilt tests are scored instantly, supported by secure, user-friendly interfaces.
            </p>
            </div>
        </section>

        <section className={styles.featureStrip}>
            <div>
            <h4>Speech Recognition</h4>
            <p>Analyzes verbal responses to detect early cognitive changes.</p>
            </div>
            <div>
            <h4>Risk Scoring</h4>
            <p>Integrates lifestyle, health, and medical factors into a personalized risk profile.</p>
            </div>
            <div>
            <h4>Fast, Inbuilt Testing</h4>
            <p>Automatic test taking and correction for quicker results than standard doctor visits.</p>
            </div>
        </section>

        <section className={styles.vision}>
            <h2>Shaping Proactive Dementia Care</h2>
            <p>
            Our vision is to empower individuals and clinicians with early insights, actionable risk scores, and faster assessments—supporting smarter decisions and better outcomes.
            </p>
        </section>
      </main>
    </div>
  );
};

export default AboutUs;
