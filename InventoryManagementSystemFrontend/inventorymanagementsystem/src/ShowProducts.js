import Pagination from 'react-bootstrap/Pagination';
import Button from 'react-bootstrap/Button';
import { useEffect, useState } from 'react';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import ShoppingCart from './ShoppingCart';
// import './App.css';


function ShowProducts()
{
    const [products, setProducts] = useState([]);

    const [back,setback]          = useState(0);

    const [next,setNext]          = useState(1);

    const [showShoppingCart,setshowShoppingCart] = useState(false);

    useEffect(() => {
        fetchProducts();
    },[]);

    useEffect(() => {

        if(products.length>0){
            if(back===0 && next===0){
                document.getElementById('previous').disabled=true;
                document.getElementById('previous').style.pointerEvents='none';
                document.getElementById('previous').style.cursor='default';
                document.getElementById('previous').style.color='#b8b8b8';

                document.getElementById('forward').disabled=true;
                document.getElementById('forward').style.removeProperty('pointer-events');
                document.getElementById('forward').style.cursor='default';
                document.getElementById('forward').style.color='#b8b8b8';
            }
            else if(back===0 && next===1){
                document.getElementById('previous').disabled=true;
                document.getElementById('previous').style.pointerEvents='none';
                document.getElementById('previous').style.cursor='default';
                document.getElementById('previous').style.color='#b8b8b8';

                document.getElementById('forward').disabled=false;
                document.getElementById('forward').style.removeProperty('pointer-events');
                document.getElementById('forward').style.cursor='pointer';
                document.getElementById('forward').style.color='#0D6EFD';
            }
            else if(back===1 && next===0){
                document.getElementById('previous').disabled=false;
                document.getElementById('previous').style.removeProperty('pointer-events');
                document.getElementById('previous').style.cursor='pointer';
                document.getElementById('previous').style.color='#0D6EFD';

                document.getElementById('forward').disabled=true;
                document.getElementById('forward').style.pointerEvents='none';
                document.getElementById('forward').style.cursor='pointer';
                document.getElementById('forward').style.color='#b8b8b8';
            }
            else if(back===1 && next===1){
                document.getElementById('previous').disabled=false;
                document.getElementById('previous').style.removeProperty('pointer-events');
                document.getElementById('previous').style.cursor='pointer';
                document.getElementById('previous').style.color='#0D6EFD';

                document.getElementById('forward').disabled=true;
                document.getElementById('forward').style.removeProperty('pointer-events');
                document.getElementById('forward').style.cursor='pointer';
                document.getElementById('forward').style.color='#0D6EFD';
            }
        }

    },[products]);

    function addToCart(index)
    {
        const clickedProduct = [
            {key:'count',value:1},
            {key:'productId',value:products[index].id},
            {key:'pricePerItem',value:products[index].costPerUnit}
        ];

        if(sessionStorage.getItem('cartItems')==null){
            let cartItems = [];
            cartItems.push(clickedProduct);
            console.log(cartItems);
            sessionStorage.setItem('cartItems',JSON.stringify(cartItems));
        }
        else{
            let cartItems =JSON.parse(sessionStorage.getItem('cartItems'));

            console.log(cartItems);

            let itemFound=false;

            for(let i=0;i<cartItems.length;i++){
                const cartItem = cartItems[i];

                if(cartItem['productId']===clickedProduct['productId']){
                    cartItems[i]['count']+=1;
                    itemFound=true;
                    break;
                }
            }

            if(itemFound===false){
                cartItems.push(clickedProduct);
            }

            sessionStorage.setItem('cartItems',JSON.stringify(cartItems));
        }

        setshowShoppingCart(true);
    }

    async function fetchProducts(){
        try {
            const response = await fetch("http://localhost:8080/getAllProducts",{
                method:'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            const responseData = await response.json();

            const productsData = responseData.products;

            setback(responseData.back);

            productsData.forEach(function(products){
                const startIndex = products.path.indexOf('/uploads/');

                products.path = products.path.substring(startIndex);
            });

            console.log(productsData);

            setProducts(productsData);

        } catch (error) {
            console.log(error);
        }
    }

    async function previous()
    {
        const id = document.getElementById('previous').getAttribute('data-id');

        console.log(id);

        try {
            const response = await fetch("http://localhost:8080/previous",{
                method:'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id:id,
                })
            });

            const responseData = await response.json();

            const productsData = responseData.products;

            setback(responseData.back);

            setNext(responseData.next);

            productsData.forEach(function(products){
                const startIndex = products.path.indexOf('/uploads/');

                products.path = products.path.substring(startIndex);
            });

            setProducts(productsData);

        } catch (error) {
            console.log(error);
        }
    }

    async function forward()
    {
        const id = document.getElementById('forward').getAttribute('data-id');

        console.log(id);

        try {
            const response = await fetch("http://localhost:8080/forward",{
                method:'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id:id,
                })
            });

            const responseData = await response.json();

            const productsData = responseData.products;

            setback(responseData.back);

            setNext(responseData.next);

            productsData.forEach(function(products){
                const startIndex = products.path.indexOf('/uploads/');

                products.path = products.path.substring(startIndex);
            });

            setProducts(productsData);
        } catch (error) {
            console.log(error);
        }
    }

    return(
        <>
            {products.length>0 ? (
                <>
                    <Container fluid>
                        <Row>
                            {products.slice(0, 6).map((product, index) => (
                                <Col key={product.id} style={{ display:'flex',flexDirection:'column' }}>
                                    <img src={`${product.path}`} alt={product.productName} style={{ width: '100%', height: '60%' }} />
                                    <div className="details">
                                        <h5 style={{ color: '#141516cc' }}>{`${product.productName}`}</h5>
                                        <h5>Rs {`${product.costPerUnit}`}</h5>
                                    </div>
                                    {product.count==='0'?(
                                        <Button variant="dark" style={{ padding: '2%' }} className='addtoCart' id={index} disabled>OUT OF STOCK</Button>
                                    ):(
                                        <Button variant="dark" style={{ padding: '2%' }} className='addtoCart' id={index} onClick={() => addToCart(index)}>Add to cart</Button>
                                    )}
                                    <ShoppingCart showShoppingCart={showShoppingCart} setshowShoppingCart={setshowShoppingCart} />
                                </Col>
                            ))}
                        </Row>

                        <Row>
                            {products.slice(6, 12).map((product, index) => (
                                <Col key={product.id} style={{ display:'flex',flexDirection:'column'  }}>
                                    <img src={`${product.path}`} alt={product.productName} style={{ width: '100%', height: '60%' }} />
                                    <div className="details">
                                        <h5 style={{ color: '#141516cc' }}>{`${product.productName}`}</h5>
                                        <h5>Rs {`${product.costPerUnit}`}</h5>
                                    </div>
                                    {product.count==='0'?(
                                        <Button variant="dark" style={{ padding: '2%' }} className='addtoCart' id={index} disabled>OUT OF STOCK</Button>
                                    ):(
                                        <Button variant="dark" style={{ padding: '2%' }} className='addtoCart' id={index} onClick={() => addToCart(index)}>Add to cart</Button>
                                    )}
                                    <ShoppingCart showShoppingCart={showShoppingCart} setshowShoppingCart={setshowShoppingCart} />
                                </Col>
                            ))}
                        </Row>
                    </Container>

                    <br />

                    <div className="pagination_pages" style={{ width:'100%', display:'flex',justifyContent:'center' }}>
                        <Pagination>
                            <Pagination.Prev id='previous' data-id={products[0].id} onClick={previous} />
                            <Pagination.Next id='forward' data-id={products[products.length-1].id} onClick={forward}/>
                        </Pagination>
                    </div>
                </>
            ):(
                <>
                </>
            )}
        </>
    );
}





export default ShowProducts;