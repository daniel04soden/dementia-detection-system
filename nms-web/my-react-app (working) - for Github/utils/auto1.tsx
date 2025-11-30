import { useEffect } from "react";

export const TestAuto1 = () => {
  useEffect(() => {
  const runPipeline = async () => {
    try {
      const payload = {
          patientID: 3,
          doctorID: 1,
          testDate: "2025-11-27",
          clockNumber: "12",
          clockHands: "3:00",
          dateQuestion: "2025-11-27",
          news: "I REMEMBER",
          recall: {
              name: "John",
              surname: "Lennon",
              number: "42",
              street: "West St",
              city: "Ohio"
          }
      };


      const res = await fetch("/api/mobile/stage1", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include", // <- if your backend needs session
        body: JSON.stringify(payload)
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(`Server error: ${text}`);
      }

      const data = await res.json();
      console.log("Stage 1 created with testID:", data.testID);
    } catch (err) {
      console.error("Pipeline error:", err);
    }
  };

  runPipeline();
}, []);

  return null;
};