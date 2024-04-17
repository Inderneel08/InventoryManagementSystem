import React from 'react';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';



function ShoppingCart({showShoppingCart,setshowShoppingCart})
{
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const cart = sessionStorage.getItem('cart');

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

    const closeModal = async() => {
      setshowShoppingCart(false);
    }

    // const handleClose = async()  =>  {

    //     if(!validateEmail(email)){
    //       Swal.fire({
    //         icon: 'error',
    //         title: 'Invalid Email Address!',
    //         text: 'Please enter a valid email address',
    //         didClose : () =>{
    //           document.getElementById('email').focus();
    //         }
    //       });

    //       return ;
    //     }
      
    //     if(!validatePassword(password)){
    //         Swal.fire({
    //           icon: 'error',
    //           title: 'Invalid Password!',
    //           text: 'Password must be at least 8 characters long and contain one special character and one number.',
    //           didClose : () =>{
    //             document.getElementById('password').focus();
    //           }
    //         });

    //         return;
    //       }
    //       else{
    //         try {
      
    //           const response = await fetch("http://localhost:8080/adminLogin",{
    //             method: 'POST',
    //             headers: {
    //               'Content-Type': 'application/json',
    //             },
      
    //             body: JSON.stringify({email,password}),
    //           });
      
    //           const data = await response.json();
              
    //           if(response.status===400){
    //             Swal.fire({
    //               icon: 'error',
    //               title: "Error",
    //               text: data.message
    //             });

    //             return ;
    //           }

    //           console.log(data);
      
    //           const token = data.token;
    //           const emailrecieved = data.email;
    //           const role = data.role;

    //           sessionStorage.setItem('token', token);
    //           sessionStorage.setItem('emailrecieved', emailrecieved);
    //           sessionStorage.setItem('role',role);

    //           setshowShoppingCart(false);

    //           navigate('/');           
    //         } catch (error) {
    //           console.error('Sign-in error:', error);
    //         }
    //       }
    // };

    console.log(cart);

    return(
        <>
            <Modal className="cartModal" show={showShoppingCart} fullscreen={true} onHide={closeModal} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>Your Cart ({cart}) </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {cart===0?(
                        <>
                            <h1>Your cart is empty</h1>
                            <span>You don’t have any items in your cart.</span>
                        </>
                    ):(
                        <>
                        </>
                    )}
                    
                <Form>
                </Form>
                </Modal.Body>
            </Modal>
       </>
    );
}



export default ShoppingCart;