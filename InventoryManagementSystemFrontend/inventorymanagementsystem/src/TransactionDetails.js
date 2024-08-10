import React , { useEffect, useState } from 'react';
import { MDBContainer, MDBCol, MDBRow, MDBBtn, MDBIcon, MDBInput,MDBDropdown,MDBDropdownToggle,MDBDropdownMenu,MDBDropdownItem } from 'mdb-react-ui-kit';
import Button from 'react-bootstrap/Button';
import Swal from 'sweetalert2';


function TransactionDetails()
{
    const [orderId,setOrderId] = useState('');

    const [emailId,setEmailId] = useState('');

    const [dateOfPurchase,setDateOfPurchase] = useState();

    const validateEmail = (emailId) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if(!emailRegex.test(emailId)){
            return(false);
        }

        return(true);
    }

    const validateOrderId = (orderId) => {
        if(orderId=='' || orderId==null){
            return(false);
        }

        return(true);
    }

    const validateDateOfPurchase  = (dateOfPurchase) => {
        const today = new Date().toISOString().split('T')[0];

        if(dateOfPurchase=='' || dateOfPurchase==null || dateOfPurchase>today){
            return(false);
        }

        return(true);
    }

    const onBlurEvent = async() => {
        if(!validateEmail(emailId)){
          Swal.fire({
            icon: 'error',
            title: 'Invalid Email Address!',
            text: 'Please enter a valid email address',
          });

          return ;
        }
    }

    const fetchOrderDetails = async() => {
        if(!validateEmail(emailId)){
            Swal.fire({
                icon: 'error',
                title: 'Invalid Email',
                text: 'Please enter a valid email address',
            });

            return ;
        }

        if(!validateOrderId(orderId)){
            Swal.fire({
                icon: 'error',
                title: 'Invalid Email',
                text: 'Order Id cannot be empty',
            });

            return ;
        }

        if(!validateDateOfPurchase(dateOfPurchase)){
            Swal.fire({
                icon: 'error',
                title: 'Invalid Date of purchase',
                text: 'Invalid Date of purcahse',
            });

            return ;
        }

        try {
            const response = await fetch("http://localhost:8080/getOrderInfo",{
                method:'POST',
                headers:{
                    'Content-Type': 'application/json',
                },

                body: JSON.stringify({emailId,orderId,dateOfPurchase}),
            });

            const data = await response.json();

            if(response.ok){
                
            }
            else{

            }

        } catch (error) {
            console.error();
        }
    }


    useEffect(() => {
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('date').setAttribute('max', today);
    },[]);


    return(
        <>
            <div className="successfulTransaction">

                <MDBContainer fluid className="p-3 my-5 h-custom">
                    <h1 style={{ marginLeft: '43%' }}>Order Inquiry</h1>
                    <br />

                    <MDBRow>
                        <MDBCol md='6'>
                            <MDBInput wrapperClass='mb-4' label='Enter OrderId' id='orderId' type='text' size="lg" onChange={(e) => setOrderId(e.target.value)} required />
                        </MDBCol>

                        <MDBCol md='6'>
                            <MDBInput wrapperClass='mb-4' label='Enter Email Id' id='emailId' type='text' size="lg" onChange={(e) => setEmailId(e.target.value)} onBlur={onBlurEvent} required />
                        </MDBCol>
                    </MDBRow>

                    <MDBRow>
                        <MDBCol md='6'>
                            <MDBInput wrapperClass='mb-4' label='Enter Date Of Purchase' id='date' type='date' size="lg" onChange={(e) => setDateOfPurchase(e.target.value)} required />
                        </MDBCol>
                    </MDBRow>

                    <Button variant="primary" onClick={fetchOrderDetails}>Submit</Button>
                </MDBContainer>
            </div>
        </>
    );
}


export default TransactionDetails;