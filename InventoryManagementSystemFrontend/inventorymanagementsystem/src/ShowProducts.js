import Pagination from 'react-bootstrap/Pagination'; 
import Button from 'react-bootstrap/Button';
import { useEffect, useState } from 'react';
// import './App.css';


function ShowProducts()
{
    const [products, setProducts] = useState([]);

    const [back,setback]          = useState(0);

    const [next,setNext]          = useState(1);

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
                document.getElementById('forward').style.pointerEvents='none';
                document.getElementById('forward').style.cursor='default';
                document.getElementById('forward').style.color='#b8b8b8';
            }
            else if(back===0 && next===1){
                document.getElementById('previous').disabled=true;
                document.getElementById('previous').style.pointerEvents='none';
                document.getElementById('previous').style.cursor='default';
                document.getElementById('previous').style.color='#b8b8b8';

                document.getElementById('forward').disabled=false;
                document.getElementById('forward').style.pointerEvents='none';
                document.getElementById('forward').style.cursor='pointer';
                document.getElementById('forward').style.color='#0D6EFD';
            }
            else if(back===1 && next===0){
                document.getElementById('previous').disabled=false;
                document.getElementById('previous').style.pointerEvents='none';
                document.getElementById('previous').style.cursor='pointer';
                document.getElementById('previous').style.color='#0D6EFD';

                document.getElementById('forward').disabled=true;
                document.getElementById('forward').style.removeProperty('pointer-events');
                document.getElementById('forward').style.cursor='pointer';
                document.getElementById('forward').style.color='#0D6EFD';
            }
            else if(back===1 && next===1){

            }

        }

        // if(products.length>0 && back===0){
        //     document.getElementById('previous').disabled=true;
        //     document.getElementById('previous').style.pointerEvents='none';
        //     document.getElementById('previous').style.cursor='default';
        //     document.getElementById('previous').style.color='#b8b8b8';
        // }
        // else if(products.length>0 && back===1 && next===1){
        //     document.getElementById('previous').disabled=false;
        //     document.getElementById('previous').style.removeProperty('pointer-events');
        //     document.getElementById('previous').style.cursor='pointer';
        //     document.getElementById('previous').style.color='#0D6EFD';
        // }
        // else if(products.length>0 && next===0){
        //     document.getElementById('forward').disabled=false;
        //     document.getElementById('previous').style.pointerEvents='none';
        //     document.getElementById('forward').style.cursor='pointer';
        //     document.getElementById('forward').style.color='#0D6EFD';
        // }

    },[products]);

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
                body: {
                    id:id,
                }
            });

            const responseData = await response.json();

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
                <div className="entireContent" style={{display:'flex',flexDirection:'column',alignItems:'center' }}>
                    <div className="displayItems" style={{ display:'flex',flexDirection:'row',width:'100%' }}>

                        {products.map((product,index) => (
                            <div key={index} className="item" style={{display:'flex',flexDirection:'column',width:'30%' }}>
                                <img src={`${product.path}`}  alt={product.productName} style={{ width:'100%', height: '100%', objectFit: 'cover' }} />
                                <div className="details">
                                    <h5 style={{ color:'#141516cc' }}>{`${product.productName}`}</h5>
                                    <h5>Rs {`${product.costPerUnit}`}</h5>
                                </div>

                                <br />

                                <Button variant="dark" style={{ padding:'2%' }} className='addtoCart'>Add to cart</Button>
                            </div>
                        ))}

                    </div>

                    <br />
                    <div className="pagination_pages" style={{ width:'100%', display:'flex',justifyContent:'center' }}>
                        <Pagination>
                            <Pagination.Prev id='previous' data-id={products[0].id} onClick={previous} />
                            <Pagination.Next id='forward' data-id={products[products.length-1].id} onClick={forward}/>
                        </Pagination>
                    </div>
                </div>
            ):(
                <>
                </>
            )}
        </>
    );
}





export default ShowProducts;