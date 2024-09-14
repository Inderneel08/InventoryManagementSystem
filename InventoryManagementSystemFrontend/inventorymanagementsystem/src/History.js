import React, { useEffect, useState } from 'react';

import './App.css';

import Button from 'react-bootstrap/Button';

function History()
{
    const [orderHistory,setOrderHistory] = useState([]);

    const [email,setEmail] = useState('');

    const [hasMore, setHasMore] = useState(true); // To track if there's more data to load

    const [page,setPage] = useState(0);

    const size=10;

    useEffect(() => {
        setEmail(sessionStorage.getItem('emailrecieved'));
    },[]);

    useEffect(() => {
        if(email!==null && email!=='' && hasMore){
            fetchOrderHistory(page,size);
        }
    },[email,page]);

    async function fetchOrderUsingOrderId(orderId)
    {
        try {
            const response = await fetch(`http://localhost:8080/fetchOrders`,{
                method:'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${sessionStorage.getItem('token')}`,
                    Role:sessionStorage.getItem('role'),
                },

                body:JSON.stringify({orderId}),
            });

            const orders = await response.json();

            console.log(orders);

        } catch (error) {
            console.log(error);
        }
    }

    function showDetailsViaOrderId(orderId)
    {
        fetchOrderUsingOrderId(orderId);
    }

    async function fetchOrderHistory(page,size)
    {
        try {
            const response = await fetch(`http://localhost:8080/fetchOrderHistory?email=${email}&page=${page}&size=${size}`,{
                method:'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${sessionStorage.getItem('token')}`,
                    Role:sessionStorage.getItem('role'),
                },
            });

            const responseData = await response.json();

            // console.log(responseData.content);

            console.log(responseData.content.length);

            if(responseData.content.length===0){
                setHasMore(false);
            }
            else{
                const pastHistory = responseData.content.map(item => ({
                    orderId: item[0],
                    billingAddress: item[1],
                    totalAmount: item[2],
                    shippingAddress: item[3],
                    pincode: item[4],
                    date: item[5],
                    amountPaid: item[6],
                    email: item[7],
                    contactNumber: item[8],
                    state: item[10]
                }));

                setOrderHistory(prevhistory => [...prevhistory,...pastHistory]);

                setPage(page+1);
            }
        } catch (error) {
            console.log(error);
        }
    }

    console.log(orderHistory);

    useEffect(() => {
        function handleScroll()
        {
            if(window.innerHeight+document.documentElement.scrollTop === document.documentElement.offsetHeight){
                setPage(page+1);
            }
        }

        window.addEventListener('scroll',handleScroll);

        return () => window.removeEventListener('scroll',handleScroll);
    },[]);

    return(
        <>
            <br />
            <div className='container-fluid'>
                <div className="row">
                    <h1 style={{ width: 'fit-content',marginLeft:'40%' }}>Order History</h1>
                </div>

                <br />
                <br />

                <div className="row">
                    <table className='orderTable'>
                        <thead id='tableHead'>
                            <tr>
                                <th>OrderId</th>
                                <th>Total Amount</th>
                                <th>Amount Paid</th>
                                <th>State</th>
                                <th>Billing Address</th>
                                <th>Shipping Address</th>
                                <th>Pincode</th>
                                <th>Date</th>
                                <th>Email</th>
                                <th>Contact Number</th>
                            </tr>
                        </thead>
                        <tbody>
                            {orderHistory.map((order, index) => (
                                <tr key={index}>
                                    <td>{order.orderId}</td>
                                    <td>{order.totalAmount}</td>
                                    <td>{order.amountPaid}</td>
                                    <td>{order.state}</td>
                                    <td>{order.billingAddress}</td>
                                    <td>{order.shippingAddress}</td>
                                    <td>{order.pincode}</td>
                                    <td>{order.date}</td>
                                    <td>{order.email}</td>
                                    <td>{order.contactNumber}</td>
                                    <td>
                                        <Button variant='success' onClick={ () => showDetailsViaOrderId(order.orderId) }>View</Button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>


                {/* <ul>
                    {orderHistory.map((order, index) => (
                        <li key={index}>{order.billing_address}</li> // Customize based on your response structure
                    ))}
                </ul> */}

                {(!hasMore && orderHistory.length===0) && <p>No more records to display</p>}

            </div>
        </>
    );

}


export default History;