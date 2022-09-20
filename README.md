## Description
This API demonstrates the usage of spring rest.

## Setup
```
./mvn clean install
./mvnw spring-boot:run
```

## Usage

Use phone: `1234567890` and password: `12345` to login as an admin

## Endpoints

### User Service
- Login: `/api/public/user/login`
- Sign up: `/api/public/user/sign-up`

### Product Service
- Get all products: `/api/private/product/`
- Get product by id: `/api/private/product/{id}`
- Create product: `/api/admin/product/`
- Update product: `/api/admin/product/`
- Delete product: `/api/admin/product/{id}`

### Category Service
- Get all categories: `/api/private/category/`
- Get category by id: `/api/private/category/{id}`
- Create category: `/api/admin/category/`
- Update category: `/api/admin/category/`
- Delete category: `/api/admin/category/{id}`
- Get all products by category: `/api/private/category/{id}/products`