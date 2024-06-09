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

    console.log(cartItems);

    const closeModal = async() => {
      setshowShoppingCart(false);
    }

    const decrement = (index) => {
      cartItems[index].count-=1;
    };

    const increment = (index) => {
      cartItems[index].count+=1;
    };


    return(
        <>
            <Modal className="cartModal" show={showShoppingCart} fullscreen={true} onHide={closeModal} backdrop="static">
                <Modal.Header closeButton>
                    <Modal.Title>Your Cart({ cartItemsLength }) </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {cartItemsLength===0?(
                        <>
                        </>
                    ):(
                      // "/uploads/0becf459-efb4-4f40-b43b-9eaf7f919ee2-banana.jpg"
                        cartItems.map((item,index) => (
                            <Stack key={index} direction='horizontal' gap={2} style={{ border:'1px solid black' }}>
                              <div style={{ width:'8%',display:'flex',justifyContent:'flex-start' }}>
                                <img src={item.path} alt="Images" style={{ width:'100%' }} />
                              </div>
                              <div className="p-2" style={{width:'90%',display:'flex' }}>
                                <div className="detailsOfCart" style={{ display:'flex',justifyContent:'space-between',width:'100%' }}>
                                  <div className="order" style={{ display:'flex',flexDirection:'column' }}>
                                      <b>{item.productName}</b>
                                      <br />
                                      <div className="increaseDecreaseAndPrice" style={{ display:'flex',flexDirection:'row' }}>
                                        <div className="increaseDecrease" style={{ display:'flex',width:'60%' }}>
                                          <div className="minus" style={{ width:'20%',display:'flex',justifyContent:'center', cursor:'pointer',border:'1px solid rgba(0,0,0,0.1)' }} onClick={decrement(index)}>-</div>
                                          <input type="text" style={{ width:'35%',textAlign:'center', borderTop:'1px solid rgba(0,0,0,0.1)',borderBottom:'1px solid rgba(0,0,0,0.1)',borderLeft:'none',borderRight:'none' }} value={item.count} readOnly/>
                                          <div className="plus" style={{ width:'20%',display:'flex',justifyContent:'center', cursor:'pointer',border:'1px solid rgba(0,0,0,0.1)' }} onClick={increment(index)}>+</div>
                                        </div>

                                        <div className="price">
                                          Rs {(parseFloat(item.count))*(item.pricePerItem)}
                                        </div>
                                      </div>
                                  </div>

                                  <CloseButton />
                                </div>
                              </div>
                            </Stack>
                        ))
                    )}
                </Modal.Body>

                <Modal.Footer>
                  {cartItemsLength===0?(
                    <>
                    </>
                  ):(
                    <>
                      <Button variant="primary" onClick={() => navigate('/checkout')}>
                        Proceed to Checkout
                      </Button>
                    </>
                  )}
                </Modal.Footer>
            </Modal>
       </>
    );
}



export default ShoppingCart;