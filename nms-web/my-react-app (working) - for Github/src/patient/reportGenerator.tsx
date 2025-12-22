export const generatePdf = (user: any) => {
  const personalInfoKeys = ["firstName", "lastName", "age", "gender", "dominantHand", "patientID", "education"];
  const hiddenKeys = ["lifestyleID", "lifestyleStatus"]
  const lifestyleKeys = Object.keys(user).filter(k => !personalInfoKeys.includes(k) && !hiddenKeys.includes(k));

  // Helper to convert 0/1 to "Not Present"/"Present"
  const booleanText = (value: any) => {
    if (value === 0) return "Not Present";
    if (value === 1) return "Present";
    return value;
  };

  const printContents = `
    <div class="report-container">
      <header>
        <h1>Patient Report</h1>
        <p class="subtitle">Detailed information for ${user.firstName} ${user.lastName}</p>
      </header>

      <section class="personal-info">
        <h2>Personal Details</h2>
        <table>
          ${personalInfoKeys.map(key => {
            if (!user[key] && user[key] !== 0) return "";
            const label = key
              .replace(/([A-Z])/g, ' $1')
              .replace(/^./, str => str.toUpperCase());
            let value = user[key];
            if (key === "gender") value = user[key] === 0 ? "Female" : "Male";
            if (key === "dominantHand") value = user[key] === 0 ? "Left" : "Right";
            return `<tr><td>${label}</td><td>${booleanText(value)}</td></tr>`;
          }).join("")}
        </table>
      </section>

      <section class="lifestyle-info">
        <h2>Lifestyle & Health Data</h2>
        <table>
          ${lifestyleKeys.map(key => {
            const label = key
              .replace(/([A-Z])/g, ' $1')
              .replace(/^./, str => str.toUpperCase());
            return `<tr><td>${label}</td><td>${booleanText(user[key])}</td></tr>`;
          }).join("")}
        </table>
      </section>
    </div>
  `;

  const newWindow = window.open("", "_blank", "width=900,height=700");
  if (!newWindow) return;

  newWindow.document.write(`
    <html>
      <head>
        <title>Patient Report</title>
        <style>
          body {
            font-family: 'Arial', sans-serif;
            padding: 20px;
            color: #333;
            background: #fff;
          }
          header {
            text-align: center;
            margin-bottom: 40px;
          }
          h1 {
            font-family: 'Georgia', serif;
            font-size: 28px;
            color: #6a1b9a;
            margin-bottom: 5px;
          }
          .subtitle {
            font-size: 16px;
            color: #555;
          }
          section {
            margin-bottom: 30px;
          }
          section h2 {
            font-family: 'Georgia', serif;
            font-size: 20px;
            color: #4b0082;
            border-bottom: 2px solid #ccc;
            padding-bottom: 5px;
            margin-bottom: 15px;
          }
          table {
            width: 100%;
            border-collapse: collapse;
          }
          table td {
            padding: 8px 10px;
            border-bottom: 1px solid #eee;
          }
          table tr:nth-child(even) {
            background-color: #f9f9f9;
          }
          table td:first-child {
            font-weight: bold;
            width: 35%;
            color: #333;
          }
          .report-container {
            max-width: 900px;
            margin: 0 auto;
          }
        </style>
      </head>
      <body>${printContents}</body>
    </html>
  `);

  newWindow.document.close();
  newWindow.focus();
  newWindow.print();
  newWindow.close();
};
