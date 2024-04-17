import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Routes, Route,Switch } from "react-router-dom";
import MyNavbar from './MyNavbar';
import Home from './Home';
import SignIn from './SignIn';
import Register from './Register';
import Contact from './Contact';
import AdminLogin from './adminLogin';
import ProtectedRoute from './ProtectedRoute';
import ProtectedHome from './ProtectedHome';
import { useEffect } from 'react';
import Logout from './logout_user';
import ShoppingCart from './ShoppingCart';
import CreateProducts from './CreateProducts';

function App() {

  window.onunload = function() {
    if(sessionStorage.getItem('reloaded')==null){
      sessionStorage.clear();
    }
  };

  if(sessionStorage.getItem('reloaded')==null){
    sessionStorage.setItem('reloaded',true);
  }


  return (
    <BrowserRouter>
        <Routes>
          <Route path="/"               element={<ProtectedRoute> <MyNavbar/> </ProtectedRoute>} >
              <Route path="products"    element={<Home />}></Route>
              <Route path="signIn"      element={<SignIn />}></Route>
              <Route path="register"    element={<Register />}></Route>
              <Route path="contact"     element={<Contact />}></Route>
              <Route path="adminLogin"  element={<AdminLogin />}></Route>
              <Route path="logout"      element={<Logout />} ></Route>
              <Route path="shoppingCart" element={<ShoppingCart />}></Route>
              <Route path="createProducts" element={<CreateProducts />}></Route>

              <Route path="category">
                <Route path="edibles" element={<Home />}></Route>
                <Route path="consumerDurables" element={<Home />}></Route>
                <Route path="garmentsAndOthers" element={<Home />}></Route>
                <Route path="toilet" element={<Home />}></Route>
                <Route path="toys" element={<Home />}></Route>
                <Route path="electronics" element={<Home />}></Route>
                <Route path="sports" element={<Home />}></Route>
                <Route path="stationary" element={<Home />}></Route>
                <Route path="music" element={<Home />}></Route>
              </Route>

              <Route path="protectedHome" element={<ProtectedRoute> <ProtectedHome /> </ProtectedRoute>} />

          </Route>
        </Routes>
    </BrowserRouter>

  );
}

export default App;
