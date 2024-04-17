import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { useEffect, useState } from 'react';
import Form from 'react-bootstrap/Form';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';



function AdminLogin({showModal,setShowModal})
{

    const validateEmail = (email) => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if(!emailRegex.test(email)){
        return(false);
      }

      return(true);
    }

    const validatePassword = (password) => {
      const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/;

      return passwordRegex.test(password);
    }
    
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const closeModal = async() => {
      setShowModal(false);
    }

    const handleClose = async()  =>  {

        if(!validateEmail(email)){
          Swal.fire({
            icon: 'error',
            title: 'Invalid Email Address!',
            text: 'Please enter a valid email address',
            didClose : () =>{
              document.getElementById('email').focus();
            }
          });

          return ;
        }
      
        if(!validatePassword(password)){
            Swal.fire({
              icon: 'error',
              title: 'Invalid Password!',
              text: 'Password must be at least 8 characters long and contain one special character and one number.',
              didClose : () =>{
                document.getElementById('password').focus();
              }
            });

            return;
          }
          else{
            try {
      
              const response = await fetch("http://localhost:8080/adminLogin",{
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

              console.log(data);
      
              const token = data.token;
              const emailrecieved = data.email;
              const role = data.role;

              sessionStorage.setItem('token', token);
              sessionStorage.setItem('emailrecieved',emailrecieved);
              sessionStorage.setItem('role',role);

              setShowModal(false);

                  Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Login Successfull',
                    didClose: () => {
                      navigate('/');
                    }
                  });
            } catch (error) {
              console.error('Sign-in error:', error);
            }
          }
    };
    
    return(
        <>
            <Modal show={showModal} onHide={closeModal} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>Admin Login</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                <Form>
                    <Form.Group className="mb-3" controlId="email">
                    <Form.Label>Email Address</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="name@example.com"
                        autoFocus required onChange={(e) => setEmail(e.target.value)}
                    />
                    </Form.Group>
                    <Form.Group
                    className="mb-3"
                    controlId="password" required
                    >
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
                    </Form.Group>
                </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleClose}>
                    Login
                    </Button>
                </Modal.Footer>
            </Modal>
       </>
    );
}



export default AdminLogin;