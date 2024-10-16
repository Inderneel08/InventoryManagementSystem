import ReactDOM from "react-dom/client";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import {Row, Col } from 'react-bootstrap';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { BrowserRouter as Router, Route, Link, Outlet,useLocation } from 'react-router-dom';
import './App.css';
import { useEffect, useState,useRef } from "react";
import AdminLogin from "./adminLogin";
import ShoppingCart from "./ShoppingCart";
import ProtectedRoute from "./ProtectedRoute";
import ShowProducts from "./ShowProducts";
import { Modal, Button } from 'react-bootstrap';
import Badge from 'react-bootstrap/Badge';
import { Toast, ToastContainer } from 'react-bootstrap';


const CustomNavbar = () => {

    const location = useLocation();

    const [userEmail, setUserEmail] = useState('');

    const [showModal,setShowModal] = useState(false);

    const [showShoppingCart,setshowShoppingCart] = useState(false);

    const [showEdiblesDropdown,setshowEdiblesDropdown] = useState(false);

    const [consumerDurables,setconsumerDurables] = useState(false);

    const [clothes,setconsumerClothes] = useState(false);

    const [dropdownDetails,setDropdownDetails] = useState(false);

    const [token,setToken] = useState(sessionStorage.getItem('token'));

    const [role,setRole]  = useState(sessionStorage.getItem('role'));

    const cartItems = JSON.parse(sessionStorage.getItem('cartItems'));

    const [showToast, setShowToast] = useState(false);

    useEffect(() => {
        const getCookie = (name) => {
            const value = `; ${document.cookie}`;

            const parts = value.split(`; ${name}=`);

            if(parts.length === 2){
                return(parts.pop().split(';').shift());
            }

            return(null);
        };

        const error = getCookie('errorMessage');

        const tokenCookie = getCookie('token');

        if(error){
            setShowToast(true);

            document.cookie = "errorMessage=; Max-Age=0; path=/;";
        }

        if(tokenCookie){
            const jwtTokenObject = JSON.parse(atob(tokenCookie));

            // console.log(jwtTokenObject);

            sessionStorage.setItem('token',jwtTokenObject.token);

            sessionStorage.setItem('emailrecieved',jwtTokenObject.email);

            sessionStorage.setItem('role',jwtTokenObject.role);

            setUserEmail(sessionStorage.getItem('emailrecieved'));

            setToken(sessionStorage.getItem('token'));

            setRole(sessionStorage.getItem('role'));

            document.cookie = "token=; Max-Age=0; path=/;";
        }
  },[]);

    useEffect(()=>{

        if(token!=null){
            setUserEmail(sessionStorage.getItem('emailrecieved'));
        }

    },[token]);


    const changeModal = () => setShowModal(true);

    const changeShoppingCartModal = () => setshowShoppingCart(true);

    const [currentImageIndex, setCurrentImageIndex] = useState(0);

    const handleMouseEnterEdibles = () => setshowEdiblesDropdown(true);

    const handleMouseLeaveEdibles = () => setshowEdiblesDropdown(false);

    const handleMouseEnterconsumerDurables = () => setconsumerDurables(true);

    const handleMouseLeaveconsumerDurables = () => setconsumerDurables(false);

    const handleMouseEnterclothes = () => setconsumerClothes(true);

    const handleMouseLeaveclothes = () => setconsumerClothes(false);

    const handleMouseEnterDetailsDropdown = () => setDropdownDetails(true);

    const handleMouseLeaveDetailsDropdown = () => setDropdownDetails(false);

    const handleCloseToast = () => {
        document.cookie = "errorMessage=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        setShowToast(false);
    };



    const backgroundImages = [
        '/Supermarket.jpg',
        '/Vegies.jpg',
        '/Market.jpg'
    ];


    useEffect(() => {

        if(location.pathname === '/' || location.pathname === '/shoppingCart'){
            const intervalId = setInterval(() => {
                setCurrentImageIndex((prevIndex) => (prevIndex+1)%backgroundImages.length);
            },2500);

            return () => clearInterval(intervalId);
        }
    },[]);


    return(
    <>
        <ToastContainer position="top-end" className="p-3">
            <Toast show={showToast} onClose={handleCloseToast} delay={3000} autohide >
                <Toast.Header style={{ backgroundColor:'red' }}>
                    <strong className="me-auto" style={{ color:'white' }}>Account Already Exists</strong>
                </Toast.Header>
            </Toast>
        </ToastContainer>

        <Navbar bg="warning" data-bs-theme="light">
            <Container>
                <Navbar.Brand as={Link} to="/" className="d-flex align-items-center">
                    <img src="/inventoryimage.jpg" alt="inventoryimage"/>
                </Navbar.Brand>

                {role? (
                    <>
                        <Navbar.Brand as={Link} to="products">All Products</Navbar.Brand>

                        {role === "ADMIN" && (
                            <Navbar.Brand as={Link} to="createProducts">Create Products</Navbar.Brand>
                        )}

                        <Navbar.Toggle aria-controls="basic-navbar-nav" />

                        <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <NavDropdown title="Categories" id="basic-nav-dropdown" style={{ fontSize: '20px' }}>

                                <NavDropdown className="custom-dropdown" title="Edibles" style={{ fontSize: '20px' }}  onMouseEnter={handleMouseEnterEdibles} onMouseLeave={handleMouseLeaveEdibles} show={showEdiblesDropdown}>
                                    <NavDropdown.Item as={Link} to="category/fruits">Fruits</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/vegetables">Vegetables</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/sweets">Sweets</NavDropdown.Item>
                                </NavDropdown>

                                <NavDropdown.Divider />

                                <NavDropdown className="custom-dropdown" title="Consumer Durables" style={{ fontSize: '20px' }} onMouseEnter={handleMouseEnterconsumerDurables} onMouseLeave={handleMouseLeaveconsumerDurables} show={consumerDurables}>
                                    <NavDropdown.Item as={Link} to="category/cars">Cars</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/bikes">Bikes</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/bicycles">Bicycles</NavDropdown.Item>
                                </NavDropdown>

                                <NavDropdown.Divider />

                                <NavDropdown className="custom-dropdown" title="Clothes" style={{ fontSize: '20px' }} onMouseEnter={handleMouseEnterclothes} onMouseLeave={handleMouseLeaveclothes} show={clothes}>
                                    <NavDropdown.Item as={Link} to="category/male">Male</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/female">Female</NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item as={Link} to="category/kids">Kids</NavDropdown.Item>
                                </NavDropdown>
                                

                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/toilet">Toiletries</NavDropdown.Item>
                            
                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/toys">Toys</NavDropdown.Item>
                                
                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/electronics">Electronic goods</NavDropdown.Item>
                
                                <NavDropdown.Divider />
                    
                                <NavDropdown.Item as={Link} to="category/sports">Sports accessories</NavDropdown.Item>
                        
                                <NavDropdown.Divider />
                            
                                <NavDropdown.Item as={Link} to="category/stationary">Books and stationeries</NavDropdown.Item>

                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/music">Music stores</NavDropdown.Item>
                            </NavDropdown>

                            {role==="ADMIN"?(
                                <>                                    
                                    <Nav.Link as={Link} to="customerRequests" style={{ fontSize: '20px' }}>Customer Requests</Nav.Link>
                                </>
                            ):(
                                <>
                                    <Nav.Link as={Link} to="contact" style={{ fontSize: '20px' }}>Contact</Nav.Link>

                                    <Nav.Link as={Link} to="shoppingCart" onClick={changeShoppingCartModal}>
                                        <img src="/cart-shopping-solid.svg" alt="shoppingCart"  style={{ height:'30px', width:'30px'}}/>
                                        {/* <a href="" style={{ color:'black' }}>Hello</a> */}
                                    </Nav.Link>
                                </>
                            )}
                        </Nav>
                        </Navbar.Collapse>

                        <Nav className='ms-auto'>
                            {token ? (
                                    <>
                                        <NavDropdown className="userDetails" title={userEmail} style={{ fontSize: '20px'}} onMouseEnter={handleMouseEnterDetailsDropdown} onMouseLeave={handleMouseLeaveDetailsDropdown} show={dropdownDetails}>
                                            <NavDropdown.Item as={Link} to="/history">Order History</NavDropdown.Item>
                                            <NavDropdown.Divider />
                                            <NavDropdown.Item as={Link} to="/logout">Logout</NavDropdown.Item>
                                        </NavDropdown>
                                    </>
                                ) : (
                                    <>
                                        <Nav.Link as={Link} to="signIn" style={{ fontSize: '20px' }}>Sign In</Nav.Link>
                                        <Nav.Link as={Link} to="register" style={{ fontSize: '20px' }}>Register</Nav.Link>
                                        <Nav.Link as={Link} to="adminLogin" style={{ fontSize: '20px' }} onClick={changeModal}>Admin Login</Nav.Link>
                                    </>
                            )}
                        </Nav>

                    </>
                ) : (
                    <>
                        <Navbar.Brand as={Link} to="products">All Products</Navbar.Brand>

                        <Navbar.Brand as={Link} to="inquiry">Product Inquiry</Navbar.Brand>

                        <Nav className="me-auto">
                            <NavDropdown title="Categories" id="basic-nav-dropdown" style={{ fontSize: '20px' }}>
                            
                                <NavDropdown className="custom-dropdown" title="Edibles" style={{ fontSize: '20px' }} onMouseEnter={handleMouseEnterEdibles} onMouseLeave={handleMouseLeaveEdibles} show={showEdiblesDropdown}>
                                    <NavDropdown.Item as={Link} to="category/edibles/fruits">Fruits</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/edibles/vegetables">Vegetables</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/edibles/sweets">Sweets</NavDropdown.Item>
                                </NavDropdown>

                                <NavDropdown.Divider />

                                <NavDropdown className="custom-dropdown" title="Consumer Durables" style={{ fontSize: '20px' }} onMouseEnter={handleMouseEnterconsumerDurables} onMouseLeave={handleMouseLeaveconsumerDurables} show={consumerDurables}>
                                    <NavDropdown.Item as={Link} to="category/cars">Cars</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/bikes">Bikes</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/bicycles">Bicycles</NavDropdown.Item>
                                </NavDropdown>

                                <NavDropdown.Divider />

                                <NavDropdown className="custom-dropdown" title="Clothes" style={{ fontSize: '20px' }} onMouseEnter={handleMouseEnterclothes} onMouseLeave={handleMouseLeaveclothes} show={clothes}>
                                    <NavDropdown.Item as={Link} to="category/male">Male</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="category/female">Female</NavDropdown.Item>
                                    <NavDropdown.Divider />
                                    <NavDropdown.Item as={Link} to="category/kids">Kids</NavDropdown.Item>
                                </NavDropdown>
                                

                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/toilet">Toiletries</NavDropdown.Item>
                            
                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/toys">Toys</NavDropdown.Item>
                                
                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/electronics">Electronic goods</NavDropdown.Item>
                
                                <NavDropdown.Divider />
                    
                                <NavDropdown.Item as={Link} to="category/sports">Sports accessories</NavDropdown.Item>
                        
                                <NavDropdown.Divider />
                            
                                <NavDropdown.Item as={Link} to="category/stationary">Books and stationeries</NavDropdown.Item>

                                <NavDropdown.Divider />

                                <NavDropdown.Item as={Link} to="category/music">Music stores</NavDropdown.Item>
                            </NavDropdown>
                            
                            <Nav.Link as={Link} to="contact" style={{ fontSize: '20px' }}>Contact</Nav.Link>

                            <Nav.Link as={Link} to="shoppingCart" onClick={changeShoppingCartModal} style={{ display:'flex',width:'20vh' }}>
                                <img src="/cart-shopping-solid.svg" alt="shoppingCart"  style={{ height:'30px', width:'30px'}}/>
                                {cartItems && cartItems.length!==0?(
                                    <Badge bg="dark" style={{ borderRadius: '50%', padding: '0.5em 0.75em', fontSize: '1rem',marginLeft:'10%' }}>{cartItems.length}</Badge>
                                ):(
                                    <>
                                    </>
                                )}
                            </Nav.Link>
                        </Nav>

                        <Nav className='ms-auto'>
                            {token ? (
                                    <>
                                        <NavDropdown className="userDetails" title={userEmail} style={{ fontSize: '20px'}} onMouseEnter={handleMouseEnterDetailsDropdown} onMouseLeave={handleMouseLeaveDetailsDropdown} show={dropdownDetails}>
                                            <NavDropdown.Item as={Link} to="/history">Order History</NavDropdown.Item>
                                            <NavDropdown.Divider />
                                            <NavDropdown.Item as={Link} to="/logout">Logout</NavDropdown.Item>
                                        </NavDropdown>
                                    </>
                                ) : (
                                    <>
                                        <Nav.Link as={Link} to="signIn" style={{ fontSize: '20px' }}>Sign In</Nav.Link>
                                        <Nav.Link as={Link} to="register" style={{ fontSize: '20px' }}>Register</Nav.Link>
                                        <Nav.Link as={Link} to="adminLogin" style={{ fontSize: '20px' }} onClick={changeModal}>Admin Login</Nav.Link>
                                    </>
                            )}
                        </Nav>
                    </>
                ) }
                
            </Container>
        </Navbar>

        {/* || location.pathname === '/showOtpForm' */}

        {(location.pathname === '/' || location.pathname==='/shoppingCart' || location.pathname==='/adminLogin') && (
            // Supermarket.jpg, Vegies.jpg and Market.jpg

            <div className="image-container" style={{ backgroundImage: `url(${backgroundImages[currentImageIndex]})`, backgroundSize: 'cover', backgroundPosition: 'center', height: '150vh', backgroundRepeat: 'no-repeat', width:'100%'}}>
            </div>
        )}

        <Outlet />

        <AdminLogin showModal={showModal} setShowModal={setShowModal} />

        <ShoppingCart showShoppingCart={showShoppingCart} setshowShoppingCart={setshowShoppingCart} />
    </>
    );
};


export default CustomNavbar;