package handlers


type TestOneRequest struct {
    Age         int     `json:"age"`
    DHand       int     `json:"dHand"`
    Weight      float64 `json:"weight"`
    AvgTemp     float64 `json:"avgTemp"`
    RestingHR   int     `json:"restingHR"`
    OxLv        int     `json:"oxLv"`
    History     bool    `json:"history"`
    Smoke       bool    `json:"smoke"`
    Apoe        bool    `json:"apoe"`
    ActivityLv  string  `json:"activityLv"`
    Depressed   bool    `json:"depressed"`
    Diet        string  `json:"diet"`
    GoodSleep   bool    `json:"goodSleep"`
    Edu         string  `json:"edu"`
}



