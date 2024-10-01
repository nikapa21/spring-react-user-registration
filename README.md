# spring-react-user-registration

Backend and Frontend implementation of a user registration service

## Technology Stack

- **Backend**: Spring Boot 3.2.4, Java 21, PostgreSQL 12.20
- **Frontend**: React
- **Other Tools**: Docker, MailHog SMTP Server, Swagger/OpenAPI

### Installation Steps

1. **Clone the Repository**:

   ```
   git clone https://github.com/nikapa21/spring-react-user-registration.git
   ```

   ```
   cd spring-react-user-registration
   ```

2. **Run with Docker**:

   ```
   docker-compose up --build
   ```

## Testing

### Running Tests

Navigate to backend directory

```
cd backend
```

and run the following command:

```
mvn test
```

## API Documentation

- **Swagger UI**: Access the API documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Endpoints

### UserController

---

### `GET /api/users`

- **Summary**: Get all users
- **Description**: Retrieve a list of all registered users
- **Operation ID**: `findAll`
- **Responses**:
   - **200 OK**: A list of users
      - **Schema**: `array` of [User](#user-schema)

---

### `PUT /api/users`

- **Summary**: Update multiple users
- **Description**: Update information for multiple users
- **Operation ID**: `updateMultiple`
- **Request Body**:
   - **Required**: Yes
   - **Content Type**: `application/json`
   - **Schema**: `array` of [User](#user-schema)
- **Responses**:
   - **200 OK**: A list of updated users
      - **Schema**: `array` of [User](#user-schema)

---

### `POST /api/users`

- **Summary**: Create a new user
- **Description**: Register a new user and send a welcome email
- **Operation ID**: `create`
- **Request Body**:
   - **Required**: Yes
   - **Content Type**: `application/json`
   - **Schema**: [User](#user-schema)
- **Responses**:
   - **200 OK**: The created user
      - **Schema**: [User](#user-schema)

---

### `GET /api/users/{id}`

- **Summary**: Get user by ID
- **Description**: Retrieve a user by their unique ID
- **Operation ID**: `findById`
- **Parameters**:
   - **id** (path parameter, required): User ID
      - **Type**: `integer`
      - **Format**: `int64`
- **Responses**:
   - **200 OK**: The user with the given ID
      - **Schema**: [User](#user-schema)

---

### `PUT /api/users/{id}`

- **Summary**: Update user
- **Description**: Update an existing user's information
- **Operation ID**: `update`
- **Parameters**:
   - **id** (path parameter, required): User ID
      - **Type**: `integer`
      - **Format**: `int64`
- **Request Body**:
   - **Required**: Yes
   - **Content Type**: `application/json`
   - **Schema**: [User](#user-schema)
- **Responses**:
   - **200 OK**: The updated user
      - **Schema**: [User](#user-schema)

---

### `PUT /api/users/{id}/deactivate`

- **Summary**: Deactivate user
- **Description**: Deactivate a user by their ID
- **Operation ID**: `deactivate`
- **Parameters**:
   - **id** (path parameter, required): User ID
      - **Type**: `integer`
      - **Format**: `int64`
- **Responses**:
   - **200 OK**: The deactivated user
      - **Schema**: [User](#user-schema)

---

### `PUT /api/users/{id}/activate`

- **Summary**: Activate user
- **Description**: Activate a user by their ID
- **Operation ID**: `activateUser`
- **Parameters**:
   - **id** (path parameter, required): User ID
      - **Type**: `integer`
      - **Format**: `int64`
- **Responses**:
   - **200 OK**

---

### `PUT /api/users/deactivate`

- **Summary**: Deactivate multiple users
- **Description**: Deactivate multiple users by their IDs
- **Operation ID**: `deactivateMultiple`
- **Request Body**:
   - **Required**: Yes
   - **Content Type**: `application/json`
   - **Schema**: `array` of `integer` (int64)
- **Responses**:
   - **200 OK**: A list of deactivated users
      - **Schema**: `array` of [User](#user-schema)

---

### `GET /api/users/initData`

- **Operation ID**: `initData`
- **Responses**:
   - **200 OK**: A list of initialized data
      - **Schema**: `array` of [User](#user-schema)

---

### EmailController

---

### `GET /api/emails`

- **Summary**: Get sent emails
- **Description**: Retrieve a list of all sent emails
- **Operation ID**: `getEmails`
- **Responses**:
   - **200 OK**: List of sent emails
      - **Schema**: `string`

---

## Components

### Schemas

---

#### User Schema

- **Type**: `object`
- **Required**:
   - `email`
   - `firstName`
   - `lastName`
- **Properties**:
   - **id** (`integer`, `int64`): Unique user ID
   - **firstName** (`string`): User's first name
   - **lastName** (`string`): User's last name
   - **email** (`string`): User's email address
   - **isDeactivated** (`boolean`): Whether the user is deactivated

### TODOs

The exercise requires a "backend API to register a user. It can also
edit/read/(soft) delete single or multiple user(s)".

In the backend multiple editing (updating), reading (findAll user list) and soft deleting (deactivating) is implemented. In the frontend it is not.
