import React, { useState } from 'react';
import Swal from 'sweetalert2';

function CreateProducts()
{
    const [productName,setProductName]         = useState('');

    const [productQuantity,setProductQuantity] = useState('');

    const [productCategory,setProductCategory] = useState('');

    const [subCategory,setSubCategory]         = useState('');

    const [productBrand,setProductBrand]       = useState('');

    const [uploadedImage,setuploadedImage]     = useState('');

    const productCategories = [
        {value:'',label:'Select a product category'},
        {value:'edibles', label:'Edibles'},
        {value:'consumer_durables', label:'Consumer Durables'},
        {value:'clothes', label:'Clothes'},
        {value:'toilet', label:'Toiletries'},
        {value:'toys', label:'Toys'},
        {value:'electronic_goods',label:'Electronic goods'},
        {value:'sports_accessories',label:'Sports accessories'},
        {value:'books_stationeries',label:'Books and stationeries'},
        {value:'music_stores',label:'Music stores'},
    ];

    const edibleSubCategory = [
        {value:'',label:'Select an edible'},
        {value:'fruits',label:'Fruits'},
        {value:'vegetables',label:'Vegetables'},
        {value:'sweets',label:'Sweets'},
    ];

    const consumerDurablesSubCategory = [
      {value:'',label:'Select a consumer durable'},
      {value:'cars',label:'Cars'},
      {value:'bikes',label:'Bikes'},
      {value:'bicycles',label:'Bicycles'},
    ];

    const clothesSubCategory = [
        {value:'',label:'Select a clothes subcategory'},
        {value:'male',label:'Male'},
        {value:'female',label:'Female'},
        {value:'kids',label:'Kids'},
    ];

    const handleImageChange = (selectedImage) => {
        if(!selectedImage || !selectedImage.type.match('image/*')){
          Swal.fire({
            icon: 'error',
            title: 'Image Type Error',
            text: 'Please select a valid image file.',
            didClose : () => {
              document.getElementById('productImage').value=null;
            }
          });

          return ;
        }
    }

    const createProductSubmitRequest = async () =>{

      if(!productName){
        Swal.fire({
          icon: 'error',
          title: "Error",
          text: "Please enter product name",
        });

        return ;
      }

      if(!productQuantity){
        Swal.fire({
          icon: 'error',
          title: "Error",
          text: "Please enter product quantity",
        });

        return ;
      }

      if(!productCategory){
        Swal.fire({
          icon: 'error',
          title: "Error",
          text: "Please enter product category",
        });

        return ;
      }

      if(!subCategory){
        Swal.fire({
          icon: 'error',
          title: "Error",
          text: "Please enter product sub category",
        });

        return ;
      }

      if(!productBrand){
        Swal.fire({
          icon: 'error',
          title: "Error",
          text: "Please enter product brand",
        });

        return ;
      }

      if(!uploadedImage){
        Swal.fire({
          icon: 'error',
          title: "Error",
          text: "Please upload an image",
        });

        return ;
      }



      const response = await fetch("http://localhost:8080/createProduct",{
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${sessionStorage.getItem('token')}`,
            Role:sessionStorage.getItem('role'),
          },

          body:JSON.stringify({productName,productQuantity,productCategory,subCategory,productBrand,uploadedImage}),
      });

      const data = await response.json();
    }
    
    const uploadImage = async () => {

      const formData = new FormData();

      const imageFile = document.getElementById('productImage').files[0];

      formData.append('file',imageFile);

      const response = await fetch("http://localhost:8080/uploadImage",{
          method:'POST',
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${sessionStorage.getItem('token')}`,
            Role:sessionStorage.getItem('role'),
          },

          body:formData,
      });

      const data = await response.json();

      if(response.ok()){
        setuploadedImage(data.imagePath);
      }
    }



    return(
    
    <form id="form" className="text-center" style={{ marginTop: '4.5%', marginLeft: '37%', width: '100%', maxWidth: '400px' }}>
      <h2>Create a Product</h2>
      <br />

      <div className="form-group">
        <label htmlFor="productName">Product Name:</label>
        <input
          type="text"
          className="form-control"
          id="productName"
          name="productName"
          value={productName}
          onChange={(event) => setProductName(event.target.value)}
          required
        />
        {/* <div className="invalid-feedback">Please provide product name.</div> */}
      </div>

      <br />

      <div className="form-group">
        <label htmlFor="productQuantity">Product Quantity:</label>
        <input
          type="number"
          className="form-control"
          id="productQuantity"
          name="productQuantity"
          value={productQuantity}
          onChange={(event) => setProductQuantity(parseInt(event.target.value))}
          required
        />
        {/* <div className="invalid-feedback">Please provide product quantity.</div> */}
      </div>
      <br />

      <div className="form-group">
        <label htmlFor="productCategory">Product Category:</label>
        <select
          className="form-control"
          id="productCategory"
          name="productCategory"
          value={productCategory}
          onChange={(event) => setProductCategory(event.target.value)}
          required
        >
          {productCategories.map((category) => (
            <option key={category.value} value={category.value}>
              {category.label}
            </option>
          ))}
        </select>
        {/* <div className="invalid-feedback">Please select a product category.</div> */}
      </div>

      <br />

      {productCategory==="edibles" &&(
        <>
          <div className="form-group">
            <label htmlFor="productSubCategory">Product Sub Category:</label>
            <select
              className="form-control"
              id="productSubCategory"
              name="productSubCategory"
              value={subCategory}
              onChange={(event) => setSubCategory(event.target.value)}
              required
            >
              {edibleSubCategory.map((category) => (
                <option key={category.value} value={category.value}>
                  {category.label}
                </option>
              ))}
            </select>
          </div>
        </>
      )}

      {productCategory==="consumer_durables" &&(
        <>
          <div className="form-group">
            <label htmlFor="productSubCategory">Product Sub Category:</label>
            <select
              className="form-control"
              id="productSubCategory"
              name="productSubCategory"
              value={subCategory}
              onChange={(event) => setSubCategory(event.target.value)}
              required
            >
              {consumerDurablesSubCategory.map((category) => (
                <option key={category.value} value={category.value}>
                  {category.label}
                </option>
              ))}
            </select>
          </div>
        </>
      )}

      {productCategory==="clothes" &&(
        <>
          <div className="form-group">
            <label htmlFor="productSubCategory">Product Sub Category:</label>
            <select
              className="form-control"
              id="productSubCategory"
              name="productSubCategory"
              value={subCategory}
              onChange={(event) => setSubCategory(event.target.value)}
              required
            >
              {clothesSubCategory.map((category) => (
                <option key={category.value} value={category.value}>
                  {category.label}
                </option>
              ))}
            </select>
          </div>
        </>
      )}

      <br />
      
      <div className="form-group">
        <label htmlFor="productName">Product Brand:</label>
        <input
          type="text"
          className="form-control"
          id="productBrand"
          name="productBrand"
          value={productBrand}
          onChange={(event) => setProductBrand(event.target.value)}
          required
        />
      </div>

      <br />

      <div className="form-group">
        <label htmlFor="productImage">Product Image:</label>
          <div style={{ display:'flex'}}>
              <input
                type="file"
                className="form-control"
                id="productImage"
                name="productImage"
                onChange={(event) => handleImageChange(event.target.files[0])}
                required
              />

              <button type="submit" style={{ marginLeft:'3%' }} onClick={uploadImage}>
                Upload
              </button>
          </div>
      </div>

      <br />

      <button type="submit" className="btn btn-primary" style={{ marginBottom: '5%' }} onClick={createProductSubmitRequest}>
        Submit
      </button>
    </form>
    );    
}





export default CreateProducts;

