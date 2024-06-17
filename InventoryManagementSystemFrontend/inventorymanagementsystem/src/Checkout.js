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

    const navigate = useNavigate();

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if(!emailRegex.test(email)){
        return(false);
        }

        return(true);
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

    const handleSelect = (e) => {
        setState(e.target.getAttribute('data-value'));
    };

    useEffect(() => {
        fetchStateLists();
    },[]);


    return(
        <>
            <MDBContainer fluid className="p-3 my-5 h-custom">
                <h1 style={{ marginLeft: '43%' }}>Checkout</h1>
                <br />

                <MDBRow>
                    <MDBCol col='10' md='6'>
                        <MDBInput wrapperClass='mb-4' label='Email address' id='email' type='email' size="lg" onChange={(e) => setEmail(e.target.value)} />

                        <select name="state" id="state" className='form-control'>
                            <option value="">---Select State---</option>
                        </select>

                        <label htmlFor="state">State</label>

                        <br />
                        <br />

                        <MDBInput wrapperClass='mb-4' label='Billing Address' id='billing_address' type='text' size="lg" onChange={(e) => setBillingAddress(e.target.value)} value={billingAddress} />

                        <input type="checkbox" id="applyAddress" name="applyAddress" onClick={checkboxClicked} />
                        <label for="applyAddress">Shipping Address same as Billing Address</label>
                        <br />
                        <br />

                        <MDBInput wrapperClass='mb-4' label='Shipping Address' id='shipping_address' type='text' size="lg" onChange={(e) => setShippingAddress(e.target.value)} value={shippingAddress} />
                    </MDBCol>

                    <MDBCol col='4' md='6' style={{marginTop: '10vh' }}>
                        <Modal className="cartDetails">
                            <Modal.Header>
                                <Modal.Title>Price Details</Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                Hello
                            </Modal.Body>
                        </Modal>
                    </MDBCol>
                </MDBRow>

                <Button variant="dark" className='submit'>Place Order</Button>
            </MDBContainer>
        </>
    );
}




export default Checkout;