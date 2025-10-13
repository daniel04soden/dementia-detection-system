import React, { FormEvent, useState } from "react";
import Header from "../dashboard/header/Header";
import styles from './stage.module.css';

const Stage2: React.FC = () => {

  const [result, setText] = useState("");
  const NoImpairment = () => {
    setText("No significant cognitive impairment");
  };
  const Cooked = () => {
    setText("Cognitive impairment is indicated");
  };

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = e.target as HTMLFormElement;
    const formData = new FormData(form);

    const formJson = Object.fromEntries(formData.entries());
    
    var count = 0;
    for (const key in formJson) {
        console.log(`${key}: ${formJson[key]}`);
        if(formJson[key] == "Yes"){count++}
    }
    console.log(count)
    if(4 <= count){NoImpairment()}
    else{Cooked()}
  };

    return(
        <div>
            <Header />
        <form method="post" onSubmit={handleSubmit}>
            <table className={styles.table}>
                <thead>
                    <tr>
                        <th>Compared to 5 or 10 years ago,</th>
                        <th>Yes</th>
                        <th>No</th>
                        <th>Don't know</th>
                        <th>N/A</th>
                    </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Does the patient have more trouble remembering things  
                       that have happened recently than they used to? </td>
                    <td><input type="radio" name="rememberQ" value="Yes"/></td>
                    <td><input type="radio" name="rememberQ" value="No"/></td>
                    <td><input type="radio" name="rememberQ" value="DK"/></td>
                    <td><input type="radio" name="rememberQ" value="NA"/></td>
                </tr>
                <tr>
                    <td>Do they have more trouble recalling conversations 
                        a few days later?</td>
                    <td><input type="radio" name="recallQ" value="Yes"/></td>
                    <td><input type="radio" name="recallQ" value="No"/></td>
                    <td><input type="radio" name="recallQ" value="DK"/></td>
                    <td><input type="radio" name="recallQ" value="NA"/></td>
                </tr>
                <tr>
                    <td>When speaking, do they have more difficulty in 
                        finding the right word or tend to use the wrong words 
                        more often?</td>
                    <td><input type="radio" name="speakQ" value="Yes"/></td>
                    <td><input type="radio" name="speakQ" value="No"/></td>
                    <td><input type="radio" name="speakQ" value="DK"/></td>
                    <td><input type="radio" name="speakQ" value="NA"/></td>
                </tr>
                <tr>
                    <td>Are they less able to manage money and financial 
                        affairs (e.g. paying bills and budgeting)? </td>
                    <td><input type="radio" name="financeQ" value="Yes"/></td>
                    <td><input type="radio" name="financeQ" value="No"/></td>
                    <td><input type="radio" name="financeQ" value="DK"/></td>
                    <td><input type="radio" name="financeQ" value="NA"/></td>
                </tr>
                <tr>
                    <td>Are they less able to manage their medication 
                        independently?</td>
                    <td><input type="radio" name="medQ" value="Yes"/></td>
                    <td><input type="radio" name="medQ" value="No"/></td>
                    <td><input type="radio" name="medQ" value="DK"/></td>
                    <td><input type="radio" name="medQ" value="NA"/></td>
                </tr>
                <tr>
                    <td>Do they need more assistance with transport  
                        (either private or public)?  
                        (If the patient has difficulties only due to physical 
                        problems, e.g. bad leg, tick ‘no’.) </td>
                    <td><input type="radio" name="mobilityQ" value="Yes"/></td>
                    <td><input type="radio" name="mobilityQ" value="No"/></td>
                    <td><input type="radio" name="mobilityQ" value="DK"/></td>
                    <td><input type="radio" name="mobilityQ" value="NA"/></td>
                </tr>
                </tbody>
            </table>
            <button
                    type="submit"
                >Calculate result</button>
        </form>
        <p>{result}</p>
        </div>
    );
};

export default Stage2;