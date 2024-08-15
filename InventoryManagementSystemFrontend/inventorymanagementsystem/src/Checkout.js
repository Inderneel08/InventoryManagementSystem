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
import OtpInput from 'react-otp-input';


function Checkout()
{
    const [email, setEmail] = useState('');
    const [state, setState] = useState('');
    const [billingAddress,setBillingAddress] = useState('');
    const [shippingAddress,setShippingAddress] = useState('');
    const [cartItems,setCartItems] = useState([]);
    const [totalAmount,setTotalAmount] = useState(0);
    const [netAmount,setNetAmount] = useState(0);
    const [pincode,setPincode] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [otp,setOtp] = useState('');
    const [operationId,setOperationId] = useState(0);
    const [operation,setOperation] = useState(0);
    const [token,setToken]  = useState('');
    const [emailrecieved,setEmailRecieved] = useState('');
    const [role,setRole] = useState('');
    const [paymentMethod,setPaymentMethod] = useState('');

    useEffect(() => {
        fetchStateLists();

        setCartItems(JSON.parse(sessionStorage.getItem('cartItems')));

        setTotalAmount(parseFloat(sessionStorage.getItem('totalCost')));

        setNetAmount(parseFloat(sessionStorage.getItem('totalCost'))- parseFloat(sessionStorage.getItem('setDiscount')));

        setToken(sessionStorage.getItem('token'));

        setEmailRecieved(sessionStorage.getItem('emailrecieved'));

        setRole(sessionStorage.getItem('role'));
    },[]);

    const setPaymentType = (e) => {
        setPaymentMethod(e.target.value);
    };

    function clearTransactionRelatedDetails(success)
    {
        setShowModal(false);
        setOtp('');
        setOperation('');
        setOperationId('');

        if(success===true){
            setEmail('');

            setState('');

            setBillingAddress('');

            setShippingAddress('');

            setTotalAmount('');

            setNetAmount('');

            setPincode('');

            setCartItems([]);

            sessionStorage.clear();

            sessionStorage.setItem('token', token);

            sessionStorage.setItem('emailrecieved', emailrecieved);
            
            sessionStorage.setItem('role',role);
        }
    }

    const navigate = useNavigate();

    const validateEmail = (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if(!emailRegex.test(email)){
            return(false);
        }

        return(true);
    }

    const otpInputStyle = {
        width: '50px', // Adjust width and height to make the input square
        height: '50px',
        margin: '0 5px',
        fontSize: '24px',
        borderRadius: '5px',
        border: '1px solid #ccc',
        textAlign: 'center',
    };

    const renderInput = (inputProps, index) => (
        <input {...inputProps} key={index} style={otpInputStyle} />
    );

    const validateState = (state) => {
        if(state===null || state===''){
            return(false);
        }

        return(true);
    }

    const validateBillingAddressAndShippingAddress = (billingAddress,shippingAddress) => {
        if(billingAddress===null || billingAddress==='' || shippingAddress==='' || shippingAddress===null){
            return(false);
        }

        return(true);
    }

    const validatePincode = (pincode) => {
        if(pincode===null || pincode===''){
            return(false);
        }

        return(true);
    }

    const validatePaymentMethod = (paymentMethod) => {
        if((paymentMethod===null || (paymentMethod===''))){
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
            setShippingAddress(billingAddress);
            console.log("Checked");
        }
        else{
            setShippingAddress('');
            console.log("UnChecked");
        }
    }

    const handleClose = async() => {
        setShowModal(false);
    }

    const checkout = async() =>{
        if(!validateEmail(email)){
            Swal.fire({
                icon: 'error',
                title: 'Invalid Email',
                text: 'Please enter a valid email address',
            });

            return ;
        }

        if(!validateState(state)){
            Swal.fire({
                icon: 'error',
                title: 'The state dropdown cannot be empty!',
                text: 'Please enter a valid state',
            });

          return ;
        }

        if(!validateBillingAddressAndShippingAddress(billingAddress,shippingAddress)){
            Swal.fire({
                icon:'error',
                text:'Incorrect Billing or Shipping Address',
            });

            return ;
        }

        if(!validatePincode(pincode)){
            Swal.fire({
                icon:'error',
                text:'Incorrect pincode',
            });

            return ;
        }

        if(!validatePaymentMethod(paymentMethod)){
            Swal.fire({
                icon:'error',
                text:'Payment Method must be specified',
            });

            return ;
        }

        if(paymentMethod==0){
            try {
                const response = await fetch("http://localhost:8080/checkout",{
                    method:'POST',
                    headers:{
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${sessionStorage.getItem('token')}`,
                        Role:sessionStorage.getItem('role'),
                    },

                    body: JSON.stringify({email}),
                });

                const data = await response.json();

                console.log(data);

                if(response.ok){
                    Swal.fire({
                        icon:'info',
                        text:data.responseOtp.message,
                        didClose:()=>{
                            setShowModal(true);
                            setOperationId(data.responseOtp.operationId);
                            setOperation(data.responseOtp.operation);
                        }
                    });
                }

            } catch (error) {
                console.error('Otp confirmation error:', error);
            }
        }
        else{
            try {
                const response = await fetch("http://localhost:8080/onlinePayment",{
                    method:'POST',
                    headers:{
                        'Content-Type': 'application/json',
                    },

                    body: JSON.stringify({email,netAmount,totalAmount}),
                });
            } catch (error) {
                console.error('Request failed...',error);
            }
        }
    }

    const confirmOrder = async() =>{

        try {
            const response = await fetch("http://localhost:8080/confirmOrder",{
                method:'POST',
                headers:{
                    'Content-Type': 'application/json',
                },

                body: JSON.stringify({email,state,billingAddress,shippingAddress,cartItems,totalAmount,netAmount,pincode,operation:operation.toString(),operationId:operationId.toString(),otp}),
            });

            const data = await response.json();

            if(response.ok){
                Swal.fire({
                    icon:'success',
                    text:data.message,

                    didClose : () => {
                        clearTransactionRelatedDetails(true);

                        navigate('/');
                    }
                });

                return ;
            }
            else{
                Swal.fire({
                    icon:'error',
                    text:data.message,

                    didClose : ()=>{
                        clearTransactionRelatedDetails(false);

                        navigate('/');
                    }
                });

                return ;
            }

        } catch (error) {
            console.error('Payment Error:', error);
        }
    }

    return(
        <>
            <MDBContainer fluid className="p-3 my-5 h-custom">
                <h1 style={{ marginLeft: '43%' }}>Checkout</h1>
                <br />

                <MDBRow>
                    <MDBCol col='10' md='6'>
                        <MDBInput wrapperClass='mb-4' label='Email address' id='email' type='email' size="lg" onChange={(e) => setEmail(e.target.value)} onBlur={onBlurEvent} required />

                        <select name="state" id="state" className='form-control' onChange={(e) =>setState(e.target.value)} required>
                            <option value="">---Select State---</option>
                        </select>
            
                        {/* <p>Selected State: {state}</p> */}

                        <label htmlFor="state">State</label>

                        <br />
                        <br />

                        <MDBInput wrapperClass='mb-4' label='Billing Address' id='billing_address' type='text' size="lg" onChange={(e) => setBillingAddress(e.target.value)} value={billingAddress} required />

                        <input type="checkbox" id="applyAddress" name="applyAddress" onClick={checkboxClicked} />
                        <label htmlFor="applyAddress">Shipping Address same as Billing Address</label>

                        <br />
                        <br />

                        <MDBInput wrapperClass='mb-4' label='Shipping Address' id='shipping_address' type='text' size="lg" onChange={(e) => setShippingAddress(e.target.value)} value={shippingAddress} required />

                        <MDBInput wrapperClass='mb-4' label='Pincode' id='pincode' type='text' size="lg" onChange={(e) => setPincode(e.target.value)} value={pincode} required />
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

                        <h5>Total Amount  Rs({totalAmount})</h5>
                        
                        <br />

                        <h5>Discount -Rs({sessionStorage.getItem('setDiscount')})</h5>
                        
                        <hr />

                        <h5>Net Amount Rs({netAmount})</h5>

                    </MDBCol>
                </MDBRow>

                <Modal show={showModal} onHide={handleClose} backdrop="static" keyboard={false} size='lg'>
                    <Modal.Header closeButton>
                        <Modal.Title>Enter OTP</Modal.Title>
                    </Modal.Header>

                    <Modal.Body>
                        <div style={{ display: 'flex', justifyContent: 'center', padding: '20px' }}>
                            <OtpInput
                            value={otp}
                            onChange={setOtp}
                            numInputs={9}
                            separator={<span />} // Use an empty span for separator
                            isInputNum={true} // If your OTP consists only of numbers
                            inputStyle={otpInputStyle}
                            renderInput={renderInput}
                            />
                        </div>
                    </Modal.Body>

                    <Modal.Footer>
                        <div className="d-flex justify-content-between w-100">
                            <Button variant="primary" onClick={confirmOrder}>Submit</Button>
                            <Button variant="secondary" onClick={handleClose}>Close</Button>
                        </div>
                    </Modal.Footer>

                </Modal>

                <b>Select Payment Delivery Method:</b>
                <br />
                <br />

                <input type="radio" id="cash_dilvery" name="age" value="0" onClick={setPaymentType} />
                <label for="cash_dilvery" style={{ padding:'1%' }}>Cash on Delivery</label>
                <input type="radio" id="online" name="age" value="1" onClick={setPaymentType} />
                <label for="online" style={{ padding:'1%' }}>Pay Online</label>
                <br />
                <br />
                <br />

                <Button variant="dark" className='submit' onClick={checkout}>Place Order</Button>
            </MDBContainer>
        </>
    );
}




export default Checkout;