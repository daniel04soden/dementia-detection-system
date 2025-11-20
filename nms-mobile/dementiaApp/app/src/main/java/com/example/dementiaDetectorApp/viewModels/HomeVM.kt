package com.example.dementiaDetectorApp.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.dementiaDetectorApp.models.NewsPiece

class HomeVM: ViewModel(){
    //News Pieces
    private val _news = mutableStateOf<List<NewsPiece>>(newsList())
    val news: State<List<NewsPiece>> = _news

    fun newsList(): List<NewsPiece>{
        val newsList = listOf(
            NewsPiece(
                headline = "New Oral Treatment Shows Promise Against Dementia",
                description = "Researchers have developed a new oral treatment that has shown promising results in clinical trials for dementia patients.",
                link = "https://medicalxpress.com/news/2025-11-oral-treatment-dementia.html"
            ),
            NewsPiece(
                headline = "Alzheimer's Risk Calculator Developed",
                description = "A new tool can estimate a person's risk of developing Alzheimer's years before symptoms appear.",
                link = "https://medicalxpress.com/news/2025-11-alzheimers-risk-calculator.html"
            ),
            NewsPiece(
                headline = "Heart Health in Midlife Predicts Dementia Risk",
                description = "A study finds heart health during middle age is linked to dementia risk in later life.",
                link = "https://medicalxpress.com/news/2025-11-heart-health-dementia-risk.html"
            ),
            NewsPiece(
                headline = "The Silent Threat: Hearing Loss and Memory Decline",
                description = "New research highlights the connection between hearing loss, loneliness, and memory decline in older adults.",
                link = "https://sciencedaily.com/releases/2025/07/hearing-loss-memory-decline.html"
            ),
            NewsPiece(
                headline = "Faster MRI Scans Offer New Hope for Dementia Diagnosis",
                description = "Innovative MRI techniques could significantly reduce scan times for dementia diagnosis.",
                link = "https://alzheimers.org.uk/news/faster-mri-scans-dementia-hope"
            ),
            NewsPiece(
                headline = "FDA Approves First Digital Therapeutic for Dementia Symptoms",
                description = "Prescription digital therapeutic using cognitive training exercises shows efficacy in improving memory and executive function in mild cognitive impairment.",
                link = "https://www.fda.gov/medical-devices/digital-health/dementia-digital-therapeutic-approval"
            ),
            NewsPiece(
                headline = "Blood Test for Alzheimer's Biomarkers Reaches 95% Accuracy",
                description = "New plasma-based biomarker test detects tau and amyloid proteins with high precision, offering accessible early diagnosis for Alzheimer's disease.",
                link = "https://www.nature.com/articles/s41591-023-02456-8"
            ),
            NewsPiece(
                headline = "New Alzheimer's Drug Shows Promise in Slowing Cognitive Decline",
                description = "Lecanemab demonstrates significant reduction in amyloid plaques and slowing of cognitive decline in early Alzheimer's patients in Phase 3 clinical trial.",
                link = "https://www.alz.org/research/2023/lecanemab-phase3-results"
            )
        )
        return newsList
    }

}