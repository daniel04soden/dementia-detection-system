curl -i -X POST \
	-H "Content-Type: application/json" \
	-H "Accept: application/json" \
	-d '{"speech":"the boy is falling off the stool stealing the cookies. the mother is doing the dishes. the water is running out of the sink overflowing. the young lady is holding her hand up ready to receive one of the cookies. there is a cup and a saucer on the sink board. the drapes on the kitchen window are blowing. the top cabinets door is open and the cookie jar is there. the boy is reaching for it and it looks like he is gonna fall. outside is a nice garden with a path leading around the house."}' \
	http://127.0.0.1:5000/classify

curl -i -X GET \
	-H "Accept: application/json" \
	http://127.0.0.1:5000/articles?query=patient

curl -i -X GET \
	-H "Accept: application/json" \
	http://127.0.0.1:5000/articles?query=doctor

curl -i -X POST \
	-H "Content-Type: application/json" \
	-H "Accept: application/json" \
	-d '{"speech":"the boy is falling off the stool stealing the cookies. the mother is doing the dishes. the water is running out of the sink overflowing. the young lady is holding her hand up ready to receive one of the cookies. there is a cup and a saucer on the sink board. the drapes on the kitchen window are blowing. the top cabinets door is open and the cookie jar is there. the boy is reaching for it and it looks like he is gonna fall. outside is a nice garden with a path leading around the house."}' \
	http://127.0.0.1:5000/classify

curl -i -X POST \
	-H "Content-Type: application/json" \
	-H "Accept: application/json" \
	-d '{"answers":[{"Diabetic":1,"AlcoholLevel":0.15,"HeartRate":95,"BloodOxygen":92.5,"BodyTemperature":37.5,"Weight":95.0,"MRI_Delay":1.5,"Age":85,"Dominant_Hand":1,"Gender":1,"Family_History":1,"Smoked":1,"Dementia_gene":1,"Physical_Activity":"Sedentary","Depression_Status":1,"Cognitive_Test_Scores":2,"Medication_History":1,"Nutrition_Diet":"Low-Carb Diet","Sleep_Quality":0,"Chronic_Health_Conditions":"Diabetes","Cumulative_Primary":"TRUE","Cumulative_Secondary":"TRUE","Cumulative_Degree":"TRUE"}]}' \
	http://127.0.0.1:5000/lifestyle

curl -i -X POST \
	-H "Content-Type: application/json" \
	-H "Accept: application/json" \
	-d '{"answers":[{"Diabetic":0,"AlcoholLevel":0.0,"HeartRate":60,"BloodOxygen":98.5,"BodyTemperature":36.5,"Weight":65.0,"MRI_Delay":55.0,"Age":60,"Dominant_Hand":0,"Gender":0,"Family_History":0,"Smoked":0,"Dementia_gene":0,"Physical_Activity":"Moderate Activity","Depression_Status":0,"Cognitive_Test_Scores":10,"Medication_History":0,"Nutrition_Diet":"Mediterranean Diet","Sleep_Quality":1,"Chronic_Health_Conditions":"N/A","Cumulative_Primary":"FALSE","Cumulative_Secondary":"FALSE","Cumulative_Degree":"FALSE"}]}' \
	http://127.0.0.1:5000/lifestyle

curl -i -X POST \
	-H "Content-Type: application/json" \
	-H "Accept: application/json" \
	-d '{"answers":[{"Diabetic":1,"AlcoholLevel":0.08,"HeartRate":75,"BloodOxygen":96.0,"BodyTemperature":37.0,"Weight":80.0,"MRI_Delay":25.0,"Age":70,"Dominant_Hand":1,"Gender":1,"Family_History":1,"Smoked":0,"Dementia_gene":1,"Physical_Activity":"Mild Activity","Depression_Status":0,"Cognitive_Test_Scores":7,"Medication_History":1,"Nutrition_Diet":"Balanced Diet","Sleep_Quality":1,"Chronic_Health_Conditions":"Hypertension","Cumulative_Primary":"TRUE","Cumulative_Secondary":"FALSE","Cumulative_Degree":"FALSE"}]}' \
	http://127.0.0.1:5000/lifestyle

curl -i -X POST \
	-H "Content-Type: application/json" \
	-H "Accept: application/json" \
	-d '{"answers":[{"Diabetic":0,"AlcoholLevel":0.05,"HeartRate":80,"BloodOxygen":97.5,"BodyTemperature":36.8,"Weight":70.0,"MRI_Delay":40.0,"Age":65,"Dominant_Hand":0,"Gender":0,"Family_History":0,"Smoked":1,"Dementia_gene":0,"Physical_Activity":"Moderate Activity","Depression_Status":0,"Cognitive_Test_Scores":9,"Medication_History":0,"Nutrition_Diet":"Mediterranean Diet","Sleep_Quality":0,"Chronic_Health_Conditions":"N/A","Cumulative_Primary":"FALSE","Cumulative_Secondary":"FALSE","Cumulative_Degree":"FALSE"}]}' \
	http://127.0.0.1:5000/lifestyle
