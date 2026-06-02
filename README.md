# Product Management Service

A simple Spring Boot product management API. This README explains how to build, run, and test the APIs using Postman or Bruno (an HTTP client similar to Postman).

**Prerequisites**
- Java 11+ or compatible JDK
- Maven 3.6+
- Postman or Bruno CLI (or `curl`)

**Build & Run**

1. Build the project:

```bash
mvn clean package -DskipTests
```

2. Run the application (from project root):

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080` by default. Application properties are in [src/main/resources/application.properties](src/main/resources/application.properties#L1).

**Seed Data**
This project contains a `DataSeeder` that inserts sample users/products on startup. See [src/main/java/com/store/config/DataSeeder.java](src/main/java/com/store/config/DataSeeder.java#L1).

**Authentication**
The API uses JWT. Obtain a token by POSTing credentials to the login endpoint then include the token in the `Authorization` header as `Bearer <token>` for protected endpoints.

- Login: `POST /api/auth/login` (body: JSON with `username` and `password`)
- Register: `POST /api/auth/register`

Example login body:

```json
{
  "username": "admin",
  "password": "admin"
}
```

The response returns a JWT token (typically in a field called `accessToken` or `token`).

**Key API Endpoints**
- `GET /api/products` — list products (supports query parameters: `name`, `status`, `minPrice`, `maxPrice`, `page`, `size`)
- `GET /api/products/{id}` — get product by id
- `POST /api/products` — create product (protected)
- `GET /api/orders` — list orders (protected)
- `POST /api/orders` — create order (protected)

Use these endpoints in Postman or Bruno as shown below.

**Using Postman**

1. Create an environment variable `baseUrl` with value `http://localhost:8080`.
2. To login, create a `POST` request to `{{baseUrl}}/api/auth/login` with header `Content-Type: application/json` and the JSON body shown above.
3. Copy the returned token into an environment variable `authToken`.
4. For protected requests, add the header `Authorization: Bearer {{authToken}}`.

Example: fetch products

- Method: `GET`
- URL: `{{baseUrl}}/api/products?name=shirt&page=0&size=10`
- Headers: `Authorization: Bearer {{authToken}}`

Optionally create a Postman collection with the above requests to reuse.

**Using Bruno (CLI) or curl**

Below are example commands. Replace `{{baseUrl}}` and `{{token}}` accordingly.

Login (get token):

```bash
# Bruno (example)
bruno post http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"admin"}'

# curl equivalent
curl -s -X POST http://localhost:8080/api/auth/login \ 
  -H "Content-Type: application/json" \ 
  -d '{"username":"admin","password":"admin"}'
```

Assuming the login response contains a JSON `accessToken` field, extract and use it for protected calls.

Fetch products with token:

```bash
# Bruno
bruno get http://localhost:8080/api/products -H "Authorization: Bearer <token>"

# curl
curl -s "http://localhost:8080/api/products?name=shirt&page=0&size=10" \
  -H "Authorization: Bearer <token>"
```

Create product (protected):

```bash
# Bruno
bruno post http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"New Product","description":"...","price":19.99,"status":"ACTIVE"}'

# curl
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"New Product","description":"...","price":19.99,"status":"ACTIVE"}'
```

**Troubleshooting**
- If the app fails to start, check logs for port conflicts or missing DB configuration.
- Verify `application.properties` values in [src/main/resources/application.properties](src/main/resources/application.properties#L1).


---

File: [README.md](README.md)
