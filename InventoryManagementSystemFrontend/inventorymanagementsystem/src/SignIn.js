import React , { useState } from 'react';
import { MDBContainer, MDBCol, MDBRow, MDBBtn, MDBIcon, MDBInput } from 'mdb-react-ui-kit';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import '@fortawesome/fontawesome-svg-core/styles.css';
import { faGoogle, faFacebook } from './fontAwesome';
import { Link } from 'react-router-dom';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';



function SignIn() {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const validatePassword = (password) => {
    const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/;

    return passwordRegex.test(password);
  }

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if(!emailRegex.test(email)){
      return(false);
    }

    return(true);
  }

  const handleLogin = async () => {

    if(!validateEmail(email)){
      Swal.fire({
        icon: 'error',
        title: 'Invalid Email',
        text: 'Please enter a valid email address',
      });

      return ;
    }

    if(!validatePassword(password)){
      Swal.fire({
        icon: 'error',
        title: 'Invalid Password!',
        text: 'Password must be at least 8 characters long and contain one special character and one number.',
      });
    }
    else{
      try {

        const response = await fetch("http://localhost:8080/login",{
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },

          body: JSON.stringify({email,password}),
        });

        const data = await response.json();

        if(response.status===400){
          Swal.fire({
            icon: 'error',
            title: "Error",
            text: data.message
          });

          return ;
        }
        else if(response.status===666){
          
        }

        console.log(data);

        const token = data.token;
        const emailrecieved = data.email;
        const role = data.role;

        sessionStorage.setItem('token', token);
        sessionStorage.setItem('emailrecieved', emailrecieved);
        sessionStorage.setItem('role',role);

        Swal.fire({
            icon:'success',
            title:'Success',
            text:'Login Successfull',

            didClose: () => {
              navigate('/');
            }
        });

      } catch (error) {
        console.error('Sign-in error:', error);
      }
    }

  };


  return (
    <MDBContainer fluid className="p-3 my-5 h-custom">
      <h1 style={{ marginLeft: '43%' }}>Sign In</h1>
      <MDBRow>
        <MDBCol col='10' md='6'>
          <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.webp" className="img-fluid" alt="Sample image" />
        </MDBCol>

        <MDBCol col='4' md='6' style={{marginTop: '10vh' }}>
          <MDBInput wrapperClass='mb-4' label='Email address' id='email' type='email' size="lg" onChange={(e) => setEmail(e.target.value)} />
          <MDBInput wrapperClass='mb-4' label='Password' id='passsword' type='password' size="lg" onChange={(e) => setPassword(e.target.value)} />

          <div className='text-center text-md-start mt-4 pt-2'>
            <button className="m-3 btn btn-primary" size='xl' onClick={handleLogin}>Login</button>

            <button className="m-3 btn btn-danger" size='xl' style={{ color: 'white'}}>
            <FontAwesomeIcon icon={faGoogle} /> Login with Google
            </button>

            <button className="m-3 btn btn-primary" size='xl' style={{ color: 'white'}}>
              <FontAwesomeIcon icon={faFacebook} /> Login with Facebook
            </button>

            <p className="small fw-bold mt-2 pt-1 mb-2">Don't have an account? <Link to="/register" className="link-danger">Register</Link></p>
          </div>
        </MDBCol>
      </MDBRow>

    </MDBContainer>
  );
}

export default SignIn;
