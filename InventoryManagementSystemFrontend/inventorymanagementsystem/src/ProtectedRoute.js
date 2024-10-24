import React, { useEffect, useState } from 'react';
import { Route, Navigate, useLocation } from 'react-router-dom';

const checkAuthentication = async () => {
  try {
    const response = await fetch('http://localhost:8080/validate', {
      headers: {
        Authorization: `Bearer ${sessionStorage.getItem('token')}`,
      },
    });

    if (response.ok) {
      return true;
    } else {
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('role');
      return null;
    }
  } catch (error) {
    console.error('Authentication error:', error);
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('role');
    return null;
  }
};

// const oAuth2LoginGoogleCredentials = async () => {
//   try {
//     const response = await fetch("http://localhost:8080/",{
//       method:'GET',
//     });

//     const responseData = await response.json();

//     console.log(responseData);

//   } catch (error) {
//     console.error(error);
//   }

// }


const ProtectedRoute = ({ children }) => {

  const [isAuthenticated, setIsAuthenticated] = useState(null);
  
  const [loading, setLoading] = useState(true);
  
  let redirect = '/signIn';
  
  const location = useLocation();

  const jwtToken = sessionStorage.getItem('token');

  const role     = sessionStorage.getItem('role');

  console.log(jwtToken);
  console.log(isAuthenticated);
  console.log(role);

  // useEffect(() => {
  //   oAuth2LoginGoogleCredentials();
  // },[])



  useEffect(() => {
    if (jwtToken != null) {
        checkAuthentication().then((result) => {
        setIsAuthenticated(result);
        setLoading(false);
      });
    }
    else{
      setIsAuthenticated(false);
      sessionStorage.removeItem('role');
      setLoading(false);
    }

  }, [jwtToken]);


  if(loading){
    return null;
  }


  console.log("Authenticated:"+ isAuthenticated);

  if(isAuthenticated === true){

    if(location.pathname === '/signIn' || location.pathname === '/register' || location.pathname === '/adminLogin'){
      let newRedirectLink = '/';

      if(jwtToken==null){
        newRedirectLink=location.pathname;
      }

      console.log("This here");

      return(<Navigate to={newRedirectLink} replace/>);
    }
    else if(location.pathname === '/inquiry'){
      let newRedirectLink = '/';

      if(jwtToken!=null){
        newRedirectLink=location.pathname;
      }

      return(<Navigate to={newRedirectLink} />);
    }

    console.log("This one");

    return children;
  }
  else{

    if(location.pathname === '/' || location.pathname === '/products' || location.pathname === '/signIn' || location.pathname === '/register' || location.pathname === '/adminLogin' || location.pathname === '/contact' || location.pathname === '/shoppingCart' || location.pathname === '/checkout' || location.pathname === '/showOtpForm' || location.pathname === '/inquiry'){
      return children;
    }

    console.log(2);
    return <Navigate to={redirect} replace />;
  }

};

export default ProtectedRoute;
