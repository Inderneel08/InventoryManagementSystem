import React, { useEffect, useState } from 'react';

function History()
{
    const [orderHistory,setOrderHistory] = useState([]);

    const [email,setEmail] = useState('');

    useEffect(() => {
        fetchOrderHistory();
        setEmail(sessionStorage.getItem('emailrecieved'));
    },[]);

    async function fetchOrderHistory()
    {
        try {
            const response = await fetch("http://localhost:8080/fetchOrderHistory",{
                method:'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${sessionStorage.getItem('token')}`,
                    Role:sessionStorage.getItem('role'),
                },
                body:JSON.stringify({email}),
            });

            const responseData = await response.json();

        } catch (error) {
            console.log(error);
        }
    }

}


export default History;