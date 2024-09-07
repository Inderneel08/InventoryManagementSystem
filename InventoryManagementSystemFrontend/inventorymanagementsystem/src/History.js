import React, { useEffect, useState } from 'react';

import './App.css';

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

            console.log(responseData);

            setOrderHistory(responseData.content)

        } catch (error) {
            console.log(error);
        }
    }

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
            <div>
                <h1>Order History</h1>
                <br />
                <br />

                <table>
                    <thead>
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
                        <tr>
                            <td>Hello</td>
                        </tr>
                    </tbody>
                </table>

                {/* <ul>
                    {orderHistory.map((order, index) => (
                        <li key={index}>{order.billing_address}</li> // Customize based on your response structure
                    ))}
                </ul> */}

                {!hasMore && <p>No more records to display</p>}

            </div>
        </>
    );

}


export default History;