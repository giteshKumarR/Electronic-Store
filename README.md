# Electronic-Store

## Setup mysql DB using Docker
- Used a docker-compose.yml file to setup the mysql DB container
    - Go to the directory where `docker-compose.yml` file is present and then run `docker-compose up` command.
- Then to access the DB used MySqlWorkbench
## Added postman collection for User and Category API
- We have following Postman env variables (PORT : 9090 for my local setup)
  - Auth-api-link : http://localhost:PORT/v1/auth-api
  - User-api-link : http://localhost:PORT/v1/user-api
  - Category-api-link : http://localhost:PORT/v1/category-api
  - Product-api-link : http://localhost:PORT/v1/product-api
  - Cart-api-link : http://localhost:PORT/v1/cart-api
  - Order-api-link : http://localhost:PORT/v1/order-api
  
## Database Diagram for Electronic Store
![Alt text]( ./DB_Diagram/Electronic_store_Schema.png "DB diagram")