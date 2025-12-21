package handlers

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
)

var ASSEMBLY_SECRET = os.Getenv("DB_HOST")

func uploadAudio(apiKey string, data []byte) (string, error) {
	req, _ := http.NewRequest("POST", "https://api.assemblyai.com/v2/upload", bytes.NewReader(data))
	req.Header.Set("authorization", apiKey)
	req.Header.Set("Content-Type", "application/octet-stream")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	if resp.StatusCode != 200 {
		return "", fmt.Errorf("upload failed: %s", string(body))
	}

	var result map[string]interface{}
	json.Unmarshal(body, &result)
	return result["upload_url"].(string), nil
}

func transcribeAudio(apiKey, uploadURL string) (string, error) {
	data := map[string]string{"audio_url": uploadURL}
	jsonData, _ := json.Marshal(data)

	req, _ := http.NewRequest("POST", "https://api.assemblyai.com/v2/transcript", bytes.NewBuffer(jsonData))
	req.Header.Set("authorization", apiKey)
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
	r.ParseMultipartForm(50 << 20)

	file, _, err := r.FormFile("file")
	if err != nil {
		http.Error(w, "Could not read file", http.StatusBadRequest)
		return
	}
	defer file.Close()

	fileBytes, _ := io.ReadAll(file)

	uploadURL, err := uploadAudio(ASSEMBLY_SECRET, fileBytes)
	if err != nil {
		http.Error(w, "Upload to AssemblyAI failed: "+err.Error(), http.StatusInternalServerError)
		return
	}

	_, err = transcribeAudio(ASSEMBLY_SECRET, uploadURL)
	if err != nil {
		http.Error(w, "Transcription request failed: "+err.Error(), http.StatusInternalServerError)
		return
	}
}

func HandleAssemblyWebHook(w http.ResponseWriter, r *http.Request) {
}

type AiAnalyseRequest struct {
	Speech string `json:"speech"`
}

func AiAnalyseHandler(w http.ResponseWriter, r *http.Request) {
	payload := AiAnalyseRequest{
		Speech: `well little boy &=clears throat reachin(g) out for the cookie jar . and the stool he's standin(g) on tilts over . 18068_23004 and &um (.) he's handin(g) some cookies down to the little girl . 23004_35439 and the mother's &w dryin(g) dishes (.) and spills water on the and (.) she's lookin(g) out the window . 45819_55900 that's it . [+ exc] 55900_57000`,
	}

	jsonData, err := json.Marshal(payload)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	req, err := http.NewRequest(
		"POST",
		"https://ai.magestle.dev/classify",
		bytes.NewBuffer(jsonData),
	)
	if err != nil {
		log.Println(err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadGateway)
		return
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(resp.StatusCode)
	w.Write(body)
}
