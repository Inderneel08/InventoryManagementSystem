import React , { useEffect, useState } from 'react';
import { MDBContainer, MDBCol, MDBRow, MDBBtn, MDBIcon, MDBInput,MDBDropdown,MDBDropdownToggle,MDBDropdownMenu,MDBDropdownItem } from 'mdb-react-ui-kit';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import '@fortawesome/fontawesome-svg-core/styles.css';
import { faGoogle, faFacebook } from './fontAwesome';
import { Link } from 'react-router-dom';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import Modal from 'react-bootstrap/Modal';
import { Dropdown, DropdownButton } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import Button from 'react-bootstrap/Button';


function Checkout()
{
    const [email, setEmail] = useState('');
    const [state, setState] = useState('');
    const [billingAddress,setBillingAddress] = useState('');
    const [shippingAddress,setShippingAddress] = useState('');
    const [cartItems,setCartItems] = useState([]);
    const [netAmount,setNetAmount] = useState(0);
    const [pincode,setPincode] = useState('');

    const navigate = useNavigate();

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if(!emailRegex.test(email)){
            return(false);
        }

        return(true);
    }

    const onBlurEvent = async() => {
        if(!validateEmail(email)){
          Swal.fire({
            icon: 'error',
            title: 'Invalid Email Address!',
            text: 'Please enter a valid email address',
          });

          return ;
        }
    }

    async function fetchStateLists(){
        try {
            const response = await fetch("http://localhost:8080/getAllStates",{
                method:'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            const responseData = await response.json();

            const allStates = responseData.states;

            allStates.forEach(state => {
                const option = document.createElement('option');

                option.value=state.id;

                option.textContent=state.states;

                document.getElementById('state').appendChild(option);
            });

            // const productsData = responseData.products;

            // productsData.forEach(function(products){
            //     const startIndex = products.path.indexOf('/uploads/');

            //     products.path = products.path.substring(startIndex);
            // });

            // console.log(productsData);

        } catch (error) {
            console.log(error);
        }
    }

    function checkboxClicked()
    {
        const checkbox = document.getElementById('applyAddress');

        if(checkbox.checked){
            document.getElementById('shipping_address').value=document.getElementById('billing_address').value;
        }
        else{
            document.getElementById('shipping_address').value='';
        }
    }

    const checkout = async() =>{
        try {
            const response = await fetch("http://localhost:8080/adminLogin",{
                method:'POST',
                headers:{
                    'Content-Type': 'application/json',
                },

                body: JSON.stringify({email,state,billingAddress,shippingAddress,cartItems,netAmount,pincode}),
            });
        } catch (error) {
            console.error('Payment Error:', error);
        }
        // try {
        //     const response = await fetch("http://localhost:8080/adminLogin",{
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json',
        //     },
    
        //     body: JSON.stringify({email,state,billingAddress,shippingAddress}),
        //     });
    
        //     const data = await response.json();
            
        //     if(response.status===400){
        //     Swal.fire({
        //         icon: 'error',
        //         title: "Error",
        //         text: data.message
        //     });

        //     return ;
        //     }

        //     console.log(data);
    
        //     const token = data.token;
        //     const emailrecieved = data.email;
        //     const role = data.role;

        //     sessionStorage.setItem('token', token);
        //     sessionStorage.setItem('emailrecieved',emailrecieved);
        //     sessionStorage.setItem('role',role);

        //     setShowModal(false);

        //         Swal.fire({
        //         icon: 'success',
        //         title: 'Success',
        //         text: 'Login Successfull',
        //         didClose: () => {
        //             navigate('/');
        //         }
        //         });
        // } catch (error) {
        //     console.error('Sign-in error:', error);
        // }
    }

    const handleSelect = (e) => {
        setState(e.target.getAttribute('data-value'));
    };

    useEffect(() => {
        fetchStateLists();

        setCartItems(JSON.parse(sessionStorage.getItem('cartItems')));

        setNetAmount(sessionStorage.getItem('totalCost')-sessionStorage.getItem('setDiscount'));
    },[]);


    return(
        <>
            <MDBContainer fluid className="p-3 my-5 h-custom">
                <h1 style={{ marginLeft: '43%' }}>Checkout</h1>
                <br />

                <MDBRow>
                    <MDBCol col='10' md='6'>
                        <MDBInput wrapperClass='mb-4' label='Email address' id='email' type='email' size="lg" onChange={(e) => setEmail(e.target.value)} onBlur={onBlurEvent} />

                        <select name="state" id="state" className='form-control' onChange={(e) =>setState(e.target.value)}>
                            <option value="">---Select State---</option>
                        </select>
            
                        {/* <p>Selected State: {state}</p> */}

                        <label htmlFor="state">State</label>

                        <br />
                        <br />

                        <MDBInput wrapperClass='mb-4' label='Billing Address' id='billing_address' type='text' size="lg" onChange={(e) => setBillingAddress(e.target.value)} value={billingAddress} />

                        <input type="checkbox" id="applyAddress" name="applyAddress" onClick={checkboxClicked} />
                        <label htmlFor="applyAddress">Shipping Address same as Billing Address</label>
                        <br />
                        <br />

                        <MDBInput wrapperClass='mb-4' label='Shipping Address' id='shipping_address' type='text' size="lg" onChange={(e) => setShippingAddress(e.target.value)} value={shippingAddress} />

                        <MDBInput wrapperClass='mb-4' label='Pincode' id='pincode' type='text' size="lg" onChange={(e) => setPincode(e.target.value)} value={pincode} />
                    </MDBCol>

                    <MDBCol col='4' md='6'>
                        <h6 style={{ color:'#878787' }}>PRICE DETAILS</h6>
                        <hr />
                        <h5>Price ( {cartItems.length} {cartItems.length>1?(
                            <>
                                items
                            </>
                        ):(
                            <>
                                item
                            </>
                        )})</h5>

                        <br />

                        <h5>Total Amount  Rs({sessionStorage.getItem('totalCost')})</h5>
                        
                        <br />

                        <h5>Discount -Rs({sessionStorage.getItem('setDiscount')})</h5>
                        
                        <hr />

                        <h5>Net Amount Rs({netAmount})</h5>

                    </MDBCol>
                </MDBRow>

                <Button variant="dark" className='submit' onClick={checkout}>Place Order</Button>
            </MDBContainer>
        </>
    );
}




export default Checkout;