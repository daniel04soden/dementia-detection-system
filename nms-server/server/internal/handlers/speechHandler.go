package handlers

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"strconv"
	"strings"
	"time"

	"nms-server/server/internal/auth"
)

var AssemblySecret = os.Getenv("ASSEMBLY_AI_KEY")

func uploadAudio(apiKey string, data []byte) (string, error) {
	req, _ := http.NewRequest("POST", "https://api.assemblyai.com/v2/upload", bytes.NewReader(data))
	req.Header.Set("authorization", apiKey)
	req.Header.Set("Content-Type", "application/octet-stream")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println(err.Error())
		return "", err
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	if resp.StatusCode != 200 {
		fmt.Println(err.Error())
		return "", fmt.Errorf("upload failed: %s", string(body))
	}

	var result map[string]interface{}
	json.Unmarshal(body, &result)
	return result["upload_url"].(string), nil
}

func transcribeAudio(apiKey, uploadURL string, patientID int) (string, error) {
	webhookURL := fmt.Sprintf("https://magestle.dev/api/assembly/webhook?id=%d", patientID)
	data := map[string]interface{}{
		"audio_url":    uploadURL,
		"webhook_url":  webhookURL,
		"disfluencies": true,
	}
	jsonData, _ := json.Marshal(data)

	req, _ := http.NewRequest("POST", "https://api.assemblyai.com/v2/transcript", bytes.NewBuffer(jsonData))
	req.Header.Set("Authorization", apiKey)
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	if resp.StatusCode != 200 {
		return "", fmt.Errorf("transcription request failed: %s", string(body))
	}

	var result map[string]interface{}
	json.Unmarshal(body, &result)
	return result["id"].(string), nil
}

func HandleUpload(w http.ResponseWriter, r *http.Request) {
	authHeader := r.Header.Get("Authorization")
	var token string
	token, ok := strings.CutPrefix(authHeader, "Bearer ")
	if !ok {
		http.Error(w, "Missing or invalid Authorization header", http.StatusUnauthorized)
		return
	}

	claims, err := auth.ValidateJWT(token)
	if err != nil {
		http.Error(w, "Invalid or expired token", http.StatusUnauthorized)
		return
	}

	r.ParseMultipartForm(50 << 20)

	file, _, err := r.FormFile("file")
	if err != nil {
		http.Error(w, "Could not read file", http.StatusBadRequest)
		return
	}
	defer file.Close()

	fileBytes, _ := io.ReadAll(file)

	uploadURL, err := uploadAudio(AssemblySecret, fileBytes)
	if err != nil {
		http.Error(w, "Upload to AssemblyAI failed: "+err.Error(), http.StatusInternalServerError)
		return
	}

	log.Println("uploadURL:", uploadURL)
	_, err = transcribeAudio(AssemblySecret, uploadURL, claims.UserID)
	if err != nil {
		http.Error(w, "Transcription request failed: "+err.Error(), http.StatusInternalServerError)
		return
	}

	_, err = db.Exec(`
		INSERT INTO SpeechTest
		(speechTestStatus, patientID)
		VALUES ($1, $2)
	`, 1, claims.UserID)
	if err != nil {
		http.Error(w, "Failed to add user to speech test"+err.Error(), http.StatusInternalServerError)
		return
	}
}

type AssemblyPayload struct {
	ID     string `json:"transcript_id"`
	Status string `json:"status"`
}

type Transcript struct {
	Text string `json:"text"`
}

type DementiaResult struct {
	Classification int `json:"classification"`
}

func HandleAssemblyWebHook(w http.ResponseWriter, r *http.Request) {
	defer r.Body.Close()
	var payload AssemblyPayload
	var transcript Transcript
	var dementiaResult DementiaResult

	if err := json.NewDecoder(r.Body).Decode(&payload); err != nil {
		fmt.Println("failed to payload" + err.Error())
		http.Error(w, "invalid payload", http.StatusBadRequest)
		return
	}

	idStr := r.URL.Query().Get("id")
	if idStr == "" {
		fmt.Println("Emoty id")
		http.Error(w, "id is required", http.StatusBadRequest)
		return
	}

	id, err := strconv.Atoi(idStr)
	if err != nil {
		fmt.Println("failed to convert to string" + err.Error())
		http.Error(w, "invalid id", http.StatusBadRequest)
		return
	}

	if payload.Status != "completed" {
		w.WriteHeader(http.StatusOK)
		return
	}

	transcriptURL := fmt.Sprintf("https://api.assemblyai.com/v2/transcript/%s", payload.ID)
	fmt.Println(transcriptURL)

	fmt.Println("sending get")
	req, err := http.NewRequest(
		"GET",
		transcriptURL,
		nil,
	)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	req.Header.Set("Authorization", AssemblySecret)
	req.Header.Set("Content-Type", "application/json")

	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		http.Error(w, "Failed to get data", http.StatusBadRequest)
	}

	fmt.Println(resp.Body)
	defer resp.Body.Close()

	if err = json.NewDecoder(resp.Body).Decode(&transcript); err != nil {
		fmt.Println("failed to payload" + err.Error())
		http.Error(w, "invalid payload", http.StatusBadRequest)
		return
	}

	fmt.Println("updating db")
	if _, err = db.Exec(`
        UPDATE SpeechTest
        SET speechTestStatus=$1,llmResponse=$2
        WHERE patientID=$3
    `, 1, transcript.Text, id); err != nil {
		http.Error(w, "Failed to update clinic", 500)
		return
	}

	aiPayload := AiAnalyseRequest{Speech: transcript.Text}
	jsonData, err := json.Marshal(aiPayload)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	aiReq, err := http.NewRequest(
		"POST",
		"https://ai.magestle.dev/classify",
		bytes.NewBuffer(jsonData),
	)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	aiReq.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	aiResp, err := client.Do(aiReq)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadGateway)
		return
	}
	defer aiResp.Body.Close()

	if err := json.NewDecoder(aiResp.Body).Decode(&dementiaResult); err != nil {
		fmt.Println("failed to payload" + err.Error())
		http.Error(w, "invalid payload", http.StatusBadRequest)
		return
	}

	date := time.Now().Format("02/01/2006")

	if _, err := db.Exec(`
		UPDATE SpeechTest
		SET speechTestStatus=$1,testDate=$2
		WHERE patientID=$3
		`, dementiaResult.Classification+2, date, id); err != nil {
		http.Error(w, "Failed to update speech clinic", 500)
		fmt.Printf("failed to add speech")
		return
	}
}

type AiAnalyseRequest struct {
	Speech string `json:"speech"`
}
