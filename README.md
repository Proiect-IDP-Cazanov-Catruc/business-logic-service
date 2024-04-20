# Business Logic Service Documentation
***
### Proiect IDP 2024
### Catruc Ionel 343C3 & Veaceslav Cazanov 343C3
***

## Description

Business logic service. On this service, all exposed endpoint are secured and require authorization.
In order to be authorized, user should claim the access and refresh token from **AuthService**.
Auth service issues access and refresh token either at login(`{auth-service-url}/api/v1/auth/authenticate`) or register(`{auth-service-url}/api/v1/auth/register`).
Also, refresh token could be used in order to refresh an expired access token, which will result in issuing another pair of access and refresh token (`{auth-service-url}/api/v1/auth/refresh-token`).

After receiving the token, access token should be placed inside `Authorization` HTTP header, with value `Bearer <token_value>`.
In this way, business layer will extract the access token and sending a request to `{auth-service-url}/api/v1/users/me` private endpoint to verify user identity.

In order to place order, get orders, get products, get categories and create managers, this service will comunicate with IO service, because it does not have direct access to the database.


**_NOTE:_** All communication is done via HTTP protocol, using application/json as Content-Type.
***
## Default users:
- USER (password: 123)
    - johndoe@example.com
    - janesmith@example.com
    - michaeljohnson@example.com
    - emilybrown@example.com
    - williamjones@example.com
    - sarahdavis@example.com
    - davidmiller@example.com
    - emmawilson@example.com
    - alexandertaylor@example.com
    - samanthaanderson@example.com
- MANAGER (password: manager)
    - manager@manager.com
- ADMIN (password: admin)
    - admin@admin.com
***
## Required environmental variables for application to run
You can look at .env file.

- IO_SERVICE_URL - the URL to the **Auth service**
- AUTH_SERVICE_URL - the URL to the **Auth service**

- BUSINESS_LOGIC_SERVICE_PORT - the port at which application will run and accept HTTP requests

- IO_SERVICE_USERS_ENDPOINT=`/api/v1/users`
- IO_SERVICE_USERS_FIND_BY_EMAIL=`/email`
- IO_SERVICE_USERS_MANAGER=`/manager`

- IO_SERVICE_PRODUCTS_ENDPOINT=`/api/v1/products`

- IO_SERVICE_CATEGORIES_ENDPOINT=`/api/v1/categories`

- IO_SERVICE_ORDERS_ENDPOINT=`/api/v1/orders`

- AUTH_SERVICE_USERS_ME_ENDPOINT=`/api/v1/users/me`

## Errors handling
In case of an error occurred on the service, a message of type ErrorMessage will be returned, which contains:
- status HTTP status
- timestamp
- errorCode (of type `ro.idp.upb.ioservice.exception.handle.ErrorMessage`)
- debugMessage
- validationErrors - list of validation errors (in case provided request DTO is invalid)
- path - API path at which error occurred

***
## Exposed endpoints
- Admin: base `/api/v1/admin` (actions available only to users with role ADMIN)
  - POST `/manager` - create a manager for the STORE
    - required valid body with these fields:
      - email - manager email
      - firstName - manager first name
      - lastName - manager last name
    - Response:
      - UserDto - user info (with ID and role, usually entity info after saving it by IO service)
- Manager: base `/api/v1/manager` (actions available only to users with role MANAGER or higher (ADMIN))
  - POST `/product` - create/add product to STORE
    - required valid body with these fields:
      - name - product name
      - description - product description
      - price - price of the product (Double)
      - quantity - product quantity (Integer)
      - categoryId - category this product is related (UUID)
    - Response:
      - ProductGetDto, filled with product details (ID after saving), and category details
- Store: base `/api/v1/store` - actions available to all users
  - GET `/categories` - get the list of the categories available on store
    - Response:
      - List of CategoryGetDto[id, name]
  - GET `/products` - get the list of the products available on store
    - Optional query param `categoryId`, if specified, list the products of specified category id
    - Response:
      - List of ProductGetDto[id, name, description, price, quantity, CategoryGetDto]
  - POST `/orders` - place an order
    - required valid body with these fields:
      - productsIds - list of products UUID that will be added to the order. Before sending to IO service, business logic will place userId inside DTO for the IO service to know to which user to associate this order.
    - Response:
      - GetOrderDto[orderId, user, List<ProductGetDto>]
  - GET `/orders` - get all orders
    - Optional query param `byUserId` - user id (UUID) whose orders we want to list, ignored if this param is specified by a USER role user. USER can only list its orders.
    - Optional query param `ownOrders` - boolean to specify if we want to list our own orders. Will be used usually by MANAGER or ADMIN role users.
    - Response: List<GetOrderDTO[orderId, user, List<ProductGetDto>]>


