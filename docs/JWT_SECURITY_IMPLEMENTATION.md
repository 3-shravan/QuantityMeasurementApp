# Spring Security & JWT Implementation Guide

## Overview

This document describes the Spring Security and JWT (JSON Web Token) implementation in the Quantity Measurement Application.

## Architecture Components

### 1. **User & Role Management**

#### Entities

- **User** (`domain/User.java`)
  - Represents application users
  - Fields: id, username, email, password, enabled
  - ManyToMany relationship with Role

- **Role** (`domain/Role.java`)
  - Represents user roles (ROLE_USER, ROLE_ADMIN)
  - Enum-based role names for type safety

#### Repositories

- **UserRepository** (`repository/UserRepository.java`)
  - Find user by username/email
  - Check username/email existence

- **RoleRepository** (`repository/RoleRepository.java`)
  - Find role by ERole enum

### 2. **JWT Token Management**

#### JwtTokenProvider (`security/JwtTokenProvider.java`)

Handles all JWT operations:

- `generateToken(username)` - Creates JWT token
- `getUsernameFromToken(token)` - Extracts username from token
- `getExpirationDateFromToken(token)` - Gets token expiration
- `validateToken(token)` - Validates token signature and expiration

**Configuration:**

- Secret: `jwt.secret` (default: 256-bit key in application.properties)
- Expiration: `jwt.expiration` (default: 86400000 ms = 24 hours)

### 3. **Authentication & Authorization**

#### CustomUserDetailsService (`security/CustomUserDetailsService.java`)

- Implements Spring's UserDetailsService
- Loads user details from database
- Maps roles to authorities for Spring Security

#### JwtAuthenticationFilter (`security/JwtAuthenticationFilter.java`)

- Extends OncePerRequestFilter
- Intercepts requests to extract JWT from Authorization header
- Validates token and sets authentication context
- Header format: `Authorization: Bearer <token>`

#### AuthService (`service/AuthService.java`)

Business logic for:

- **login(LoginRequest)** - Authenticates user and returns JWT
- **register(SignUpRequest)** - Creates new user and returns JWT

#### AuthController (`controller/AuthController.java`)

REST endpoints:

- `POST /api/v1/auth/login` - Login endpoint
- `POST /api/v1/auth/register` - Registration endpoint

### 4. **Security Configuration**

#### SecurityConfig (`config/SecurityConfig.java`)

Configures Spring Security with:

- **Password Encoding**: BCryptPasswordEncoder
- **Authentication Manager**: For user authentication
- **Security Filter Chain**:
  - Disables CSRF (safe for JWT)
  - Stateless session management
  - Public endpoints: `/api/v1/auth/**`, Swagger, actuator endpoints
  - All other endpoints require authentication
  - JWT filter added before UsernamePasswordAuthenticationFilter

### 5. **DTOs**

#### LoginRequest (`model/LoginRequest.java`)

```java
{
  "username": "user@example.com",
  "password": "securepassword"
}
```

#### SignUpRequest (`model/SignUpRequest.java`)

```java
{
  "username": "newuser",
  "email": "user@example.com",
  "password": "securepassword"
}
```

#### JwtAuthenticationResponse (`model/JwtAuthenticationResponse.java`)

```java
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "user@example.com",
  "userId": 1
}
```

## Usage Flow

### User Registration

1. Client sends POST request to `/api/v1/auth/register` with SignUpRequest
2. AuthService validates username/email uniqueness
3. Password encoded with BCrypt
4. User created with ROLE_USER by default
5. JWT token generated and returned

### User Login

1. Client sends POST request to `/api/v1/auth/login` with LoginRequest
2. AuthService authenticates credentials
3. JWT token generated using username
4. Token + user info returned to client

### Protected Requests

1. Client includes token in Authorization header: `Authorization: Bearer <token>`
2. JwtAuthenticationFilter extracts and validates token
3. If valid, user authentication set in SecurityContext
4. Request proceeds to protected endpoint
5. If invalid/expired, request rejected with 401 Unauthorized

## Database Schema

### Tables Created

- **users**: Stores user account information
- **roles**: Stores role definitions (ROLE_USER, ROLE_ADMIN)
- **user_roles**: Junction table for ManyToMany relationship

Default roles inserted:

- ROLE_USER (id: 1)
- ROLE_ADMIN (id: 2)

## Configuration Properties

```properties
# JWT Configuration
jwt.secret=${JWT_SECRET:mySecretKeyForJWTThatIsAtLeast256BitsLongForHS256Algorithm}
jwt.expiration=${JWT_EXPIRATION:86400000}  # 24 hours in milliseconds
```

## Security Features

1. **Password Security**: BCryptPasswordEncoder
2. **JWT Validation**: Signature + expiration verification
3. **Stateless Authentication**: No session cookies needed
4. **Role-Based Access**: User roles mapped to authorities
5. **CORS**: Can be configured for cross-origin requests
6. **Unique Constraints**: Username and email uniqueness

## Error Handling

Exception handlers in GlobalExceptionHandler:

- `ValidationException` - Returns 400 Bad Request
- `ResourceNotFoundException` - Returns 404 Not Found
- `QuantityMeasurementException` - Returns 400 Bad Request

## Dependencies Added

```xml
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.12.1</version>
</dependency>
```

## Example API Calls

### Register User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### Access Protected Endpoint

```bash
curl -X GET http://localhost:8080/api/v1/measurement \
  -H "Authorization: Bearer <jwt-token>"
```

## Future Enhancements

1. Token Refresh mechanism
2. Role-based authorization on endpoints (@PreAuthorize)
3. OAuth2/OpenID Connect integration
4. Multi-factor authentication
5. Token blacklist for logout
6. Rate limiting for auth endpoints
