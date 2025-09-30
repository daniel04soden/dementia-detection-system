import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function Head(){
  return(
    <>
    <header>
    <nav className="Navbar">
      <ul class="spaced-list">
        <li><a href="#"><img class="logo" src="src/assets/logo.png"></img></a></li>
        <li><a href="#">Home</a></li>
        <li><a href="#">About</a></li>
        <li><a href="#">Services</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
    </nav>
    </header>
    </>
  )

}

function LoginScreen(){
  return(
  <>
  <div class="popup-container">
    <h2>Login</h2>
    <form action="/login" method="post">
      <input type="text" name="username" placeholder="Username" required></input>
      <input type="password" name="password" placeholder="Password" required></input>
      <input type="submit" value="Login"></input>
    </form>
  </div>
  </>
  )
}

function SignUpScreen(){
  return(
  <>
  <div class="popup-container">
    <h2>Sign Up</h2>
    <form action="/SignUp" method="post">
      <input type="text" name="username" placeholder="Username" required></input>
      <input type="password" name="password" placeholder="Password" required></input>
      <input type="password" name="password" placeholder="Confirm Password" required></input>
      <input type="submit" value="Sign Up"></input>
    </form>
  </div>
  </>
  )
}

function DoctorProfile(){
  return(
  <>
  <div class="profile-container">
    <img src="https://imgs.search.brave.com/QOH53kAghtHMEUEmup4r4KaohMexfcdlKBlz-e2h2N4/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YS5nZXR0eWltYWdl/cy5jb20vaWQvMTcx/MjUzNjQwL3Bob3Rv/L2RvY3Rvci5qcGc_/cz02MTJ4NjEyJnc9/MCZrPTIwJmM9ME9D/V0NiTTNabm1vMmwt/bF9pVU5FT3lQSkt1/Ry1xV2R5Q1FnamZB/UmVUOD0"></img>
    <h1>James Noonan, PHD</h1>
    <h2>Specialties: x, y, z</h2>
    <h3>Phone: 0892289569</h3>
    <h3>Location: 1 Cherry Lawn, Thorn Street, Cork, Uganda</h3>
    <p>I love helping people</p>
   
  </div>
  </>
  )
}

function PatientProfile(){
  return(
  <>
  <div class="profile-container">
    <img src="https://imgs.search.brave.com/QOH53kAghtHMEUEmup4r4KaohMexfcdlKBlz-e2h2N4/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YS5nZXR0eWltYWdl/cy5jb20vaWQvMTcx/MjUzNjQwL3Bob3Rv/L2RvY3Rvci5qcGc_/cz02MTJ4NjEyJnc9/MCZrPTIwJmM9ME9D/V0NiTTNabm1vMmwt/bF9pVU5FT3lQSkt1/Ry1xV2R5Q1FnamZB/UmVUOD0"></img>
    <h1>Mae Zing</h1>
    <h2>Diagnosis: Dementia</h2>
    <p>I love helping people</p>
   
  </div>
  </>
  )
}

function App() {
  const signedIn = true;
  const testProfile = "Doctor";

  return (
    <>
    
    <Head/>
    {signedIn === false ? <LoginScreen /> : <SignUpScreen />}
    </>
  )
}

export default App

// npm run dev
// http://localhost:5173/
// {testProfile === "Doctor" ? <DoctorProfile /> : <SignUpScreen />}
// {signedIn === false ? <LoginScreen /> : <SignUpScreen />}
