import React, { FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../dashboard/header/Header";
import styles from './stage.module.css';
import { withAuth } from "../../utils/withAuth";

const Stage1: React.FC = () => {
  const navigate = useNavigate();
  const [result, setText] = useState("");
  const NoImpairment = () => {
    setText("No significant cognitive impairment");
  };
  const PossibleImpairment = () => {
    setText("More information required");
    navigate("/secondary");
  };
  const Cooked = () => {
    setText("Cognitive impairment is indicated");
  };

  // Handle the form submission
  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    // Prevent the default form submission behavior (page reload)
    e.preventDefault();

    // Get the form data
    const form = e.target as HTMLFormElement;
    const formData = new FormData(form);

    // Convert the form data to an object
    const formJson = Object.fromEntries(formData.entries());

    console.log(formJson)
    
    var count = 0;
    for (const key in formJson) {
        console.log(`${key}: ${formJson[key]}`);
        if(formJson[key] == "pass"){count++}
    }
    console.log(count)
    if(9 == count){NoImpairment()}
    else if(5 <= count){PossibleImpairment()}
    else{Cooked()}
  };

    return(
        <div>
            <Header/>
        <div className="main-content">
        <form method="post" onSubmit={handleSubmit}>
            <p className={styles.inputLabel}>Choose Patient:</p>
            <select id={styles.bottomField} name="patient" className={styles.inputField} value="patient">
            <option value="John Doe">John Doe</option>
            <option value="Adam Apple">Adam Apple</option>
            </select>
                
            <table className={styles.table}>
                <thead><tr>
                <th>Question</th>
                <th>Passed</th>
                <th>Failed</th>
                </tr></thead>
                <tbody>
                <tr>
                    <td>What is the date? (exact only)</td>
                    <td><input type="radio" name="DateQ" value="pass" required/></td>
                    <td><input type="radio" name="DateQ" value="fail"/></td>
                </tr>
                <tr>
                    <td>Please mark in all the numbers to indicate the hours of a clock
                        <br/>(correct spacing required)</td>
                    <td><input type="radio" name="HoursQ" value="pass" required/></td>
                    <td><input type="radio" name="HoursQ" value="fail"/></td>
                </tr>
                <tr>
                    <td>Please mark in hands to show 10 minutes past eleven o’clock</td>
                    <td><input type="radio" name="timeQ" value="pass" required/></td>
                    <td><input type="radio" name="timeQ" value="fail"/></td>
                </tr>
                <tr>
                    <td>Can you tell me something that happened in the news recently? <br/> (In the last week. If a general answer is given, e.g. “war”, “lot of rain”, ask for details. Only specific answer scores.)</td>
                    <td><input type="radio" name="newsQ" value="pass" required/></td>
                    <td><input type="radio" name="newsQ" value="fail"/></td>
                </tr>
                </tbody>
            </table>
                <br/>
            <table className={styles.table}>
                <thead><tr>
                    <th>What was the name and address I asked you to remember?</th>
                    <th>Passed</th>
                    <th>Failed</th>
                </tr></thead>
                <tbody>
                <tr>
                    <td>John</td>
                    <td><input type="radio" name="JohnQ" value="pass" required/></td>
                    <td><input type="radio" name="JohnQ" value="fail"/></td>
                </tr>
                <tr>
                    <td>Brown</td>
                    <td><input type="radio" name="BrownQ" value="pass" required/></td>
                    <td><input type="radio" name="BrownQ" value="fail"/></td>
                </tr>
                <tr>
                    <td>42</td>
                    <td><input type="radio" name="NoQ" value="pass" required/></td>
                    <td><input type="radio" name="NoQ" value="fail"/></td>
                </tr>
                <tr>
                    <td>West (St)</td>
                    <td><input type="radio" name="StQ" value="pass" required/></td>
                    <td><input type="radio" name="StQ" value="fail"/></td>
                </tr>
                <tr>
                    <td>Kensington</td>
                    <td><input type="radio" name="CityQ" value="pass" required/></td>
                    <td><input type="radio" name="CityQ" value="fail"/></td>
                </tr>
               </tbody>
            </table>
             <button
                    type="submit"
                >Calculate result</button>
        </form>
        <p>{result}</p>
        </div>
        </div>
    );
};

export default withAuth(Stage1, ["doctor", "admin"]);