import { useEffect,useState } from 'react';
import { Navigate } from 'react-router-dom';


function Logout()
{
    const [loading, setLoading] = useState(true);

    useEffect(()=>{
        console.log("Logging out....")
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('emailrecieved');
        sessionStorage.removeItem('role');

        const delay = 2000;

        setTimeout(() =>{
            console.log("Redirecting....");
        },delay);
    
        setLoading(false);
    },[]);
        
    if(loading){
        return null;
    }

    console.log("Here");

    const newRedirectLink = '/signIn';

    return(<Navigate to={newRedirectLink} replace/>);
}



export default Logout;