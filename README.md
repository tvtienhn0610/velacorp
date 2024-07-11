# demoVelacorp
readme_content_part1 = """
# API Documentation for Product and Order Management

## Table of Contents
- [Authentication](#authentication)
  - [Login](#login)
- [Product Management](#product-management)
  - [Add a Product](#add-a-product)
  - [Get Product by ID](#get-product-by-id)
  - [Update Product](#update-product)
  - [Delete Product](#delete-product)
  - [Get All Products](#get-all-products)
  - [Search Products](#search-products)
- [Order Management](#order-management)
  - [Create an Order](#create-an-order)
  - [Get Order by ID](#get-order-by-id)
  - [Update Order](#update-order)
  - [Add Product to Order](#add-product-to-order)
  - [Remove Product from Order](#remove-product-from-order)
  - [Get All Orders](#get-all-orders)
  - [Search Orders](#search-orders)

## Authentication

### Login
- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Input**:
  ```json
  {
    "username": "happy@example.com",
    "password": "123456"
  }
