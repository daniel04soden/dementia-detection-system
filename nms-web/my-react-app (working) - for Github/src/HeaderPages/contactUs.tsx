import styles from "./contactUs.module.css";

const ContactUs: React.FC = () => {
  return (
    <div className={styles.page}>

      {/* Hero */}
      <section className={styles.hero}>
        <div className={styles.heroOverlay} />
        <div className={styles.heroContent}>
          <h1>Get in Touch</h1>
          <p>
            Questions, collaboration requests, or technical support — we are
            here to help.
          </p>
        </div>
      </section>

      <main className={styles.main}>
        <section className={styles.content}>
          {/* Left Column */}
          <div className={styles.left}>
            <span className={styles.label}>Contact Information</span>

            <h2>We'd Like to Hear From You</h2>
            <p>
              Whether you are a patient, clinician, researcher, or system
              administrator, our team is available to provide guidance and
              support related to the Dementia Association Foundation System.
            </p>

            <div className={styles.contactBlock}>
              <div>
                <h4>Email</h4>
                <p>support@DementiaAssociationFoundation.com</p>
              </div>

              <div>
                <h4>Phone</h4>
                <p>+353 (089) 215-4632</p>
              </div>

              <div>
                <h4>Office</h4>
                <p>
                  Study Hub 3, Munster Technological University, Bishopstown,<br />
                  Cork City, Ireland. T12 P928
                </p>
              </div>
            </div>
          </div>

          {/* Right Column */}
          <div className={styles.right}>
            <div className={styles.infoCard}>
              <h3>Support Hours</h3>
              <p>
                Monday – Friday<br />
                9:00 AM – 5:00 PM (GMT)
              </p>
              <p>
                Messages received outside these hours will be reviewed on the
                next business day.
              </p>
            </div>

            <div className={styles.infoCard}>
              <h3>Clinical & Research Use</h3>
              <p>
                For clinical integration, academic research, or pilot studies,
                please contact us with a brief description of your intended use.
              </p>
              <p>
                This platform supports — but does not replace — professional
                medical judgment.
              </p>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

export default ContactUs;
