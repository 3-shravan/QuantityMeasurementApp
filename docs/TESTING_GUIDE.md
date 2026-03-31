# Spring Security & JWT Testing Guide

## Quick Start

### 1. Run the Application

```bash
cd d:\Training\QuantityMeasurementApp
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 2. Test the Auth Endpoints

#### Register a New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "Test@123456"
  }'
```

**Success Response (201 Created):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "testuser",
  "userId": 1
}
```

#### Login with Credentials

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test@123456"
  }'
```

**Success Response (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "testuser",
  "userId": 1
}
```

### 3. Use the JWT Token

#### Access Protected Endpoints

```bash
# Replace TOKEN with the JWT token received from login/register
curl -X GET http://localhost:8080/api/v1/measurement \
  -H "Authorization: Bearer TOKEN"
```

#### Invalid Token Response

```bash
# This will return 401 Unauthorized if token is invalid or missing
curl -X GET http://localhost:8080/api/v1/measurement
```

## Testing via Swagger UI

1. Open browser and navigate to: `http://localhost:8080/swagger-ui.html`

2. Register a user:
   - Click on **POST /api/v1/auth/register**
   - Click **Try it out**
   - Enter the following JSON:
     ```json
     {
       "username": "testuser",
       "email": "test@example.com",
       "password": "Test@123456"
     }
     ```
   - Click **Execute**
   - Copy the `token` from response

3. Authorize Swagger UI:
   - Click **Authorize** button at top right
   - Enter: `Bearer <paste-token-here>`
   - Click **Authorize**
   - Now all protected endpoints can be tested

## Error Cases to Test

### 1. Duplicate Username

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "another@example.com",
    "password": "Password123"
  }'
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-03-31T15:30:45",
  "status": 400,
  "error": "Validation Error",
  "message": "Username is already taken",
  "path": "/api/v1/auth/register"
}
```

### 2. Invalid Login Credentials

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "WrongPassword"
  }'
```

**Response (401 Unauthorized):**

```json
{
  "timestamp": "2026-03-31T15:31:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Bad credentials",
  "path": "/api/v1/auth/login"
}
```

### 3. Missing Required Fields

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser"
  }'
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-03-31T15:31:30",
  "status": 400,
  "error": "Quantity Measurement Error",
  "message": "Email is required, Password is required",
  "path": "/api/v1/auth/register"
}
```

### 4. Invalid Email Format

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "invalid-email",
    "password": "Password123"
  }'
```

**Response (400 Bad Request):**

```json
{
  "timestamp": "2026-03-31T15:32:00",
  "status": 400,
  "error": "Quantity Measurement Error",
  "message": "Email should be valid",
  "path": "/api/v1/auth/register"
}
```

## JWT Token Structure

A typical JWT token has 3 parts separated by dots:

**Header.Payload.Signature**

Example decoded payload:

```json
{
  "sub": "testuser",
  "iat": 1711872000,
  "exp": 1711958400
}
```

- `sub`: Subject (username)
- `iat`: Issued at time
- `exp`: Expiration time

## Database Queries

### View All Users

```sql
SELECT * FROM users;
```

### View User Roles

```sql
SELECT u.username, r.name
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id;
```

### View All Roles

```sql
SELECT * FROM roles;
```

## Security Notes

1. **JWT Secret**: Change the default secret in production

   ```properties
   jwt.secret=${JWT_SECRET:your-secure-256-bit-key}
   ```

2. **Token Expiration**: Default is 24 hours. Customize in application.properties

   ```properties
   jwt.expiration=86400000  # milliseconds
   ```

3. **Password Security**: Passwords are hashed with BCrypt
   - Never store plain-text passwords
   - Always transmit over HTTPS in production

4. **CORS**: For cross-origin requests, configure in SecurityConfig

## Postman Collection

Import the provided Postman collection:

- Location: `docs/QuantityMeasurement.postman_collection.json`
- Contains pre-configured auth endpoints

## Troubleshooting

### Token Not Working

- Ensure token is prefixed with `Bearer `
- Check if token has expired (24 hours default)
- Verify JWT secret matches across app and config

### Database Error on First Run

- Schema is auto-created with default roles
- Check MySQL connection in application.properties
- Ensure database user has CREATE TABLE permissions

### CORS Issues

- Configure CORS in SecurityConfig for frontend apps
- Add Origin headers in requests

## Next Steps

1. **Add Role-Based Access Control**

   ```java
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<?> adminEndpoint() { ... }
   ```

2. **Implement Token Refresh**
   - Create refresh token endpoint
   - Return new token with extended expiration

3. **Add Logout with Token Blacklist**
   - Store revoked tokens in cache/database
   - Check against blacklist in JwtAuthenticationFilter

4. **Integrate Frontend**
   - Store JWT in localStorage/sessionStorage
   - Send with every API request
   - Handle 401 responses by redirecting to login
