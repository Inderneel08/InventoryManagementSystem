import Pagination from 'react-bootstrap/Pagination'; 
import Button from 'react-bootstrap/Button';
import { useEffect, useState } from 'react';



function ShowProducts()
{
    const [products, setProducts] = useState([]);

    useEffect(() => {
        fetchProducts();
    },[]);

    async function fetchProducts(){
        try {
            const response = await fetch("http://localhost:8080/getAllProducts",{
                method:'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            const productsData = await response.json();

            console.log(productsData);

            setProducts(productsData);
        } catch (error) {
            console.log(error);
        }
    }

    return(
        <>
        {/* style={{ border:'5px solid black'}} */}
            <div className="entireContent" style={{ border:'10px solid pink', display:'flex',flexDirection:'column',alignItems:'center' }}>
                <div className="displayItems" style={{ display:'flex',flexDirection:'row',border:'1px solid red',width:'100%' }}>
                    {/* <div className="item" style={{ border:'1px solid green',display:'flex',flexDirection:'column',width:'30%' }}>
                        <img src={require('file:///C:/Users/inder/Desktop/uploads/0becf459-efb4-4f40-b43b-9eaf7f919ee2-banana.jpg')} alt="banana" style={{ width:'85%',border:'1px solid yellow' }} />
                        <div className="details">
                            <h5 style={{ color:'#141516cc' }}>Banana</h5>
                            <h5>Rs 141.45</h5>
                        </div>

                        <Button variant="dark" style={{ padding:'2%' }}>Add to cart</Button>
                    </div> */}

                    {products.map((product,index) => {
                        <div key={index} className="item" style={{ border:'1px solid green',display:'flex',flexDirection:'column',width:'30%' }}>
                            <img src={require(`file:///`.concat(`${product.path}`))} alt={product.productName} style={{ width:'85%',border:'1px solid yellow' }} />
                            <div className="details">
                                <h5 style={{ color:'#141516cc' }}>Banana</h5>
                                <h5>Rs 141.45</h5>
                            </div>

                            <Button variant="dark" style={{ padding:'2%' }}>Add to cart</Button>
                        </div>
                    })}

                </div>

                <br />
                <div className="pagination">
                    <Pagination> 
                        <Pagination.Prev />
                        <Pagination.Next /> 
                    </Pagination>
                </div>
            </div>
        </>
    );
}





export default ShowProducts;