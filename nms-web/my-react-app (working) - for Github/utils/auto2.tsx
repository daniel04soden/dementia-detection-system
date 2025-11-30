import { useEffect } from "react";

export const TestAuto2 = () => {
  useEffect(() => {
    const runStageTwo = async () => {
      try {
        const payload = {
          "patientID": 3,
          "memoryScore": 10,
          "recallScore": 8,
          "speakingScore": 9,
          "financialScore": 7,
          "medicineScore": 6,
          "transportScore": 8
        };

        const res = await fetch("/api/mobile/stage2", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify(payload)
        });

        if (!res.ok) {
          const text = await res.text();
          throw new Error(`Server error: ${text}`);
        }

        const data = await res.json();
        console.log("Stage Two response:", data);
      } catch (err) {
        console.error("Stage Two pipeline error:", err);
      }
    };

    runStageTwo();
  }, []);

  return null;
};
