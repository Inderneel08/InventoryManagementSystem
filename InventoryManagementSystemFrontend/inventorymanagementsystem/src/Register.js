import React, { useState } from 'react';
import { MDBContainer, MDBCol, MDBRow, MDBBtn, MDBIcon, MDBInput } from 'mdb-react-ui-kit';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import '@fortawesome/fontawesome-svg-core/styles.css';
import { faGoogle, faFacebook } from './fontAwesome';
import { Link } from 'react-router-dom';
import Swal from 'sweetalert2';


function Register() {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if(!emailRegex.test(email)){
      return(false);
    }

    return(true);
  }

  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/;

    return passwordRegex.test(password);
  }




  const handleRegister = async () => {

    if(!validateEmail(email)){
      Swal.fire({
        icon: 'error',
        title: 'Invalid Email',
        text: 'Please enter a valid email address',
        didClose: () =>{
          document.getElementById('email').focus();
        },
      });

      return ;
    }

    if(password!==confirmPassword){
      Swal.fire({
        icon: 'error',
        title:'Passwords do not match!',
        text: 'Please make sure both passwords are the same.',
      });
    }
    else if(!validatePassword(password)){
      Swal.fire({
        icon: 'error',
        title: 'Invalid Password!',
        text: 'Password must be at least 8 characters long and contain one special character and one number.',
        didClose : () => {
          document.getElementById('password').focus();
        }
      });
    }
    else if(!validatePassword(confirmPassword)){
      Swal.fire({
        icon: 'error',
        title: 'Invalid Password!',
        text: 'Password must be at least 8 characters long and contain one special character and one number.',
        didClose : () => {
          document.getElementById('confirmPassword').focus();
        }
      });
    }
    else{
      try {
        const response = await fetch('http://localhost:8080/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ email, password }),
        });
  
        const data = await response.json();

        if(response.status===400){
          Swal.fire({
            icon: 'error',
            title: data.message,
            text: 'Email id already exists...'
          });
        }
        else{
          Swal.fire({
            icon: 'success',
            title: data.message,
            text: 'Your account has been successfully registered and a confirmation email has been sent to you.'
          });
        }

        document.getElementById('email').value='';
        document.getElementById('password').value='';
        document.getElementById('confirmPasssword').value='';
      } catch (error) {
        console.error('Registration failed', error);
        
        Swal.fire({
          icon: 'error',
          title: error.message,
          text: 'Account registration failed.'
        });
      }
    }
  };


  return (
    
    <MDBContainer fluid className="p-3 my-5 h-custom">
      <h1 style={{ marginLeft: '43%' }}>Register</h1>
      <MDBRow>
        <MDBCol col='10' md='6'>
          <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.webp" className="img-fluid" alt="Sample image" />
        </MDBCol>

        <MDBCol col='4' md='6' style={{ marginTop: '10vh' }}>
          <MDBInput  wrapperClass='mb-4' label='Email address' id='email' type='email' size="lg" onChange={(e) => setEmail(e.target.value)}/>
          <MDBInput  wrapperClass='mb-4' label='Password' id='password' type='password' size="lg" onChange={(e) => setPassword(e.target.value)}/>
          <MDBInput  wrapperClass='mb-4' label='Confirm Password' id='confirmPasssword' type='password' size="lg" onChange={(e) => setConfirmPassword(e.target.value)}/>
        

          <div className='text-center text-md-start mt-4 pt-2'>
            <button className="m-3 btn btn-primary" size='xl' onClick={handleRegister}>Register</button>

            <button className="m-3 btn btn-danger" size='xl' style={{ color: 'white'}}>
            <FontAwesomeIcon icon={faGoogle} /> Register with Google
            </button>

            <button className="m-3 btn btn-primary" size='xl' style={{ color: 'white'}}>
              <FontAwesomeIcon icon={faFacebook} /> Register with Facebook
            </button>

            <p className="small fw-bold mt-2 pt-1 mb-2">Already have an account? <Link to="/signIn" className="link-danger">Sign In</Link></p>
          </div>
        </MDBCol>
      </MDBRow>


    </MDBContainer>
  );
}

export default Register;
