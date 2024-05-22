import React from 'react';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import Container from 'react-bootstrap/Container';
import Stack from 'react-bootstrap/Stack';
import CloseButton from 'react-bootstrap/CloseButton';


function ShoppingCart({showShoppingCart,setshowShoppingCart})
{
    const navigate = useNavigate();
    // const [email, setEmail] = useState('');
    // const [password, setPassword] = useState('');

    const cartItems = JSON.parse(sessionStorage.getItem('cartItems'));

    const cartItemsLength = (cartItems? cartItems.length : 0);

    // const validateEmail = (email) => {
    //   const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    //   if(!emailRegex.test(email)){
    //     return(false);
    //   }

    //   return(true);
    // }

    // const validatePassword = (password) => {
    //   const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/;

    //   return passwordRegex.test(password);
    // }

    const closeModal = async() => {
      setshowShoppingCart(false);
    }

    return(
        <>
            <Modal className="cartModal" show={showShoppingCart} fullscreen={true} onHide={closeModal} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>Your Cart({ cartItemsLength }) </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {cartItemsLength===0?(
                        <>
                            <Stack direction='horizontal' gap={2} style={{ border:'1px solid red' }}>
                              <div style={{ width:'8%',display:'flex',justifyContent:'flex-start' }}>
                                <img src="/uploads/0becf459-efb4-4f40-b43b-9eaf7f919ee2-banana.jpg" alt="Images" style={{ width:'100%' }} />
                              </div>
                              <div className="p-2" style={{width:'90%',display:'flex' }}>
                                <div className="detailsOfCart" style={{ display:'flex',justifyContent:'space-between',width:'100%' }}>
                                  <div className="order" style={{ display:'flex',flexDirection:'column' }}>
                                      <b>Bananas</b>
                                      <br />
                                      <div className="increaseDecreaseAndPrice" style={{ display:'flex',flexDirection:'row' }}>
                                        <div className="increaseDecrease" style={{ display:'flex',width:'60%' }}>
                                          <div className="minus" style={{ width:'20%',display:'flex',justifyContent:'center', cursor:'pointer',border:'1px solid rgba(0,0,0,0.1)' }}>-</div>
                                          <input type="text" style={{ width:'35%',textAlign:'center', borderTop:'1px solid rgba(0,0,0,0.1)',borderBottom:'1px solid rgba(0,0,0,0.1)',borderLeft:'none',borderRight:'none' }} />
                                          <div className="plus" style={{ width:'20%',display:'flex',justifyContent:'center', cursor:'pointer',border:'1px solid rgba(0,0,0,0.1)' }}>+</div>
                                        </div>

                                        <div className="price">
                                            Rs 1,499.00
                                        </div>
                                      </div>
                                  </div>

                                  <CloseButton />
                                </div>
                                <div className="priceAndClose" style={{ display:'flex' }}>
                                </div>
                              </div>
                            </Stack>
                        </>
                    ):(
                        <>
                            {/* <Stack direction='horizontal' gap={3}>
                              Hello
                            </Stack> */}
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