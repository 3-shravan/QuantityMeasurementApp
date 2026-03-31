# Spring Security & JWT Architecture Diagram

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT APPLICATION                          │
│  (Browser, Mobile App, Desktop App, etc.)                          │
└─────────────────────────────────────────────────────────────────────┘
                                  │
                    HTTP Request / HTTP Response
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SPRING SECURITY FILTER CHAIN                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  1. SecurityContextPersistenceFilter                                │
│  2. WebAsyncManagerIntegrationFilter                               │
│  3. SecurityContextHolderAwareRequestFilter                        │
│  ...                                                                │
│  N. JwtAuthenticationFilter ⭐ (Custom - Validates JWT Tokens)     │
│  M. UsernamePasswordAuthenticationFilter                           │
│  ...                                                                │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
                                  │
                 JWT Token Valid?  │  JWT Token Invalid?
                      │            │           │
                      ▼            ▼           ▼
          ┌──────────────────┐ ┌───────────────────┐
          │ Set Authentication│ │  401 Unauthorized │
          │ in SecurityContext│ │   Reject Request  │
          └──────────────────┘ └───────────────────┘
                      │
                      ▼
          ┌──────────────────────┐
          │  Controller / Handler │
          │  @RequestMapping      │
          └──────────────────────┘
                      │
                      ▼
          ┌──────────────────────┐
          │  Response to Client   │
          └──────────────────────┘
```

## Authentication Workflow

### User Registration Flow

```
┌────────────────────┐
│   User Submits     │
│  Registration Form │
│  (username, email, │
│   password)        │
└────────────────────┘
         │
         ▼
┌────────────────────────────────────────┐
│ POST /api/v1/auth/register             │
│ ───────────────────────────────────────│
│ Request Body:                          │
│ {                                      │
│   "username": "john_doe",              │
│   "email": "john@example.com",         │
│   "password": "SecurePass123"          │
│ }                                      │
└────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     AuthController.register()                   │
│     ──────────────────────────────────────      │
│  1. Validate input fields                       │
│  2. Check username uniqueness                   │
│  3. Check email uniqueness                      │
└─────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     AuthService.register()                      │
│     ──────────────────────────────────────      │
│  1. Hash password with BCrypt                   │
│  2. Create User entity                          │
│  3. Assign ROLE_USER by default                 │
│  4. Save to database                            │
└─────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     JwtTokenProvider.generateToken()            │
│     ──────────────────────────────────────      │
│  1. Create JWT payload with username            │
│  2. Set issue time & expiration (24h)           │
│  3. Sign with secret key (HS256)                │
│  4. Return encoded token                        │
└─────────────────────────────────────────────────┘
         │
         ▼
┌────────────────────────────────────────┐
│ 201 CREATED Response                   │
│ ───────────────────────────────────────│
│ {                                      │
│   "token": "eyJhbGc...",              │
│   "type": "Bearer",                    │
│   "username": "john_doe",              │
│   "userId": 1                          │
│ }                                      │
└────────────────────────────────────────┘
```

### User Login Flow

```
┌────────────────────┐
│   User Submits     │
│  Login Credentials │
│  (username,        │
│   password)        │
└────────────────────┘
         │
         ▼
┌────────────────────────────────────────┐
│ POST /api/v1/auth/login                │
│ ───────────────────────────────────────│
│ Request Body:                          │
│ {                                      │
│   "username": "john_doe",              │
│   "password": "SecurePass123"          │
│ }                                      │
└────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     AuthController.login()                      │
│     ──────────────────────────────────────      │
│  Validate input fields                          │
└─────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     AuthService.login()                         │
│     ──────────────────────────────────────      │
│  1. Call authenticationManager.authenticate()   │
│     (compares provided password with BCrypt     │
│      hash stored in database)                   │
│  2. If match: Continue                          │
│  3. If no match: Throw BadCredentialsException  │
└─────────────────────────────────────────────────┘
         │
         ├─────────────────────┬─────────────────┐
         │ Valid              │ Invalid         │
         ▼                    ▼                 ▼
    ┌─────────┐         ┌──────────────────────┐
    │ Load    │         │  401 Unauthorized    │
    │ User    │         │                      │
    └─────────┘         │  "Bad credentials"   │
         │               └──────────────────────┘
         ▼
┌─────────────────────────────────────────────────┐
│     JwtTokenProvider.generateToken()            │
│     ──────────────────────────────────────      │
│  Create JWT token (see registration flow above) │
└─────────────────────────────────────────────────┘
         │
         ▼
┌────────────────────────────────────────┐
│ 200 OK Response                        │
│ ───────────────────────────────────────│
│ {                                      │
│   "token": "eyJhbGc...",              │
│   "type": "Bearer",                    │
│   "username": "john_doe",              │
│   "userId": 1                          │
│ }                                      │
└────────────────────────────────────────┘
```

### Protected Request Flow

```
┌─────────────────────────────────────────┐
│ Authenticated Client Request            │
│ ─────────────────────────────────────   │
│ GET /api/v1/measurement                 │
│ Authorization: Bearer eyJhbGc...        │
└─────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     JwtAuthenticationFilter.doFilterInternal()  │
│     ──────────────────────────────────────────  │
│  1. Extract Authorization header                │
│  2. Parse "Bearer <token>" format               │
│  3. Extract token string                        │
└─────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     JwtTokenProvider.validateToken()            │
│     ──────────────────────────────────────────  │
│  1. Verify token signature with secret key      │
│  2. Check expiration time                       │
│  3. Return true/false                           │
└─────────────────────────────────────────────────┘
         │
         ├──────────────────┬──────────────────┐
         │ Valid Token      │ Invalid Token    │
         ▼                  ▼                  ▼
    ┌─────────────┐   ┌──────────────────────┐
    │ Get Username│   │  401 Unauthorized    │
    │ from Claims │   │                      │
    └─────────────┘   │  Skip to next filter │
         │             └──────────────────────┘
         ▼
┌─────────────────────────────────────────────────┐
│     CustomUserDetailsService.                   │
│     loadUserByUsername()                        │
│     ──────────────────────────────────────────  │
│  1. Query database for user                     │
│  2. Load user details                           │
│  3. Load authorities/roles                      │
│  4. Return UserDetails object                   │
└─────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│     SecurityContext.setAuthentication()         │
│     ──────────────────────────────────────────  │
│  Create UsernamePasswordAuthenticationToken     │
│  with principal, credentials, authorities      │
└─────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│  Request proceeds to Controller/Handler         │
│  SecurityContextHolder.getContext().             │
│  getAuthentication() available                  │
└─────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────┐
│  Process Request                                │
│  Return Response (200, 400, 500, etc.)          │
└─────────────────────────────────────────────────┘
```

## JWT Token Structure

```
┌──────────────────────────────────────────────────────┐
│         JWT TOKEN ANATOMY                            │
├──────────────────────────────────────────────────────┤
│                                                      │
│  Header.Payload.Signature                          │
│  └─────┬─────┘ └────┬─────┘ └──────┬──────┘        │
│        │           │               │               │
│        ▼           ▼               ▼               │
│   ┌─────────┐ ┌────────┐    ┌──────────┐          │
│   │ Header  │ │ Payload│    │Signature │          │
│   ├─────────┤ ├────────┤    ├──────────┤          │
│   │         │ │        │    │          │          │
│   │ {       │ │ {      │    │ HMACSHA  │          │
│   │ "alg":  │ │ "sub": │    │ 256(     │          │
│   │ "HS256",│ │"user1",│    │ base64   │          │
│   │ "typ":  │ │ "iat": │    │ UrlEncode│          │
│   │ "JWT"   │ │ 172000,│    │ (header) │          │
│   │ }       │ │ "exp": │    │ +        │          │
│   │         │ │ 172000 │    │ "."      │          │
│   │         │ │ }      │    │ +        │          │
│   │         │ │        │    │ base64   │          │
│   │         │ │        │    │ UrlEncode│          │
│   │         │ │        │    │ (payload)│          │
│   │         │ │        │    │ ,secret  │          │
│   │         │ │        │    │ )        │          │
│   │         │ │        │    │          │          │
│   └─────────┘ └────────┘    └──────────┘          │
│                                                    │
│  Base64 Encoded        Base64 Encoded             │
│  {"alg":"HS256",       {"sub":"user1",            │
│   "typ":"JWT"}         "iat":1711872000,          │
│                        "exp":1711958400}          │
│                                                    │
│  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9            │
│  │                                                │
│  └─ Combined: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9│
│               .eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcxMTg3│
│               MjAwMCwiZXhwIjoxNzExOTU4NDAwfQ        │
│               .4nB2zYKqzV5mN0pL3xQ9tRsUwVxA2bDfG3h│
│               jKmP8qO5sT7...                        │
│                                                    │
└──────────────────────────────────────────────────────┘
```

## Database Relationships

```
┌──────────────────────────┐
│       USERS              │
├──────────────────────────┤
│ id (PK)                  │
│ username (UNIQUE)        │
│ email (UNIQUE)           │
│ password (BCrypt hash)   │
│ enabled                  │
└──────────────────────────┘
           │
           │ ManyToMany
           │ Join Table: user_roles
           │
           ▼
┌──────────────────────────┐
│       ROLES              │
├──────────────────────────┤
│ id (PK)                  │
│ name (ENUM)              │
│  ├─ ROLE_USER            │
│  └─ ROLE_ADMIN           │
└──────────────────────────┘

┌──────────────────────────┐
│    USER_ROLES (JT)       │
├──────────────────────────┤
│ user_id (FK)             │
│ role_id (FK)             │
│ Primary Key: (user_id,   │
│              role_id)    │
└──────────────────────────┘
```

## Key Classes Interaction

```
┌─────────────────────────────────────────────────────────┐
│                 HTTP Request                            │
└─────────────────────────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────┐
│ JwtAuthenticationFilter      │
│  (Security Filter Chain)     │
└──────────────────────────────┘
         │
         ├──► JwtTokenProvider
         │    ├─ validateToken()
         │    └─ getUsernameFromToken()
         │
         ├──► CustomUserDetailsService
         │    └─ loadUserByUsername()
         │
         └──► SecurityContextHolder
              └─ setAuthentication()
                   │
                   ▼
         ┌──────────────────────┐
         │ Controller/Handler   │
         │                      │
         ├──► AuthController    │
         │    ├─ login()        │
         │    └─ register()     │
         │                      │
         ├──► AuthService       │
         │    ├─ login()        │
         │    └─ register()     │
         │                      │
         ├──► UserRepository    │
         ├──► RoleRepository    │
         └──► PasswordEncoder
                   │
                   ▼
         ┌──────────────────────┐
         │ Database             │
         │                      │
         ├─ users              │
         ├─ roles              │
         └─ user_roles         │
                   │
                   ▼
         ┌──────────────────────┐
         │ HTTP Response        │
         └──────────────────────┘
```

## Security Filter Chain Order

```
Request comes in
       │
       ▼
┌─────────────────────────────────────────────┐
│ 1. SecurityContextPersistenceFilter         │
│    (Load security context from session)     │
└─────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────┐
│ 2. WebAsyncManagerIntegrationFilter         │
│    (Async request support)                  │
└─────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────┐
│ 3. SecurityContextHolderAwareRequestFilter  │
│    (Wrap request)                           │
└─────────────────────────────────────────────┘
       │
       ▼
        ... other filters ...
       │
       ▼
┌─────────────────────────────────────────────┐
│ N. JwtAuthenticationFilter ⭐ CUSTOM        │
│    (Validate JWT tokens)                    │
└─────────────────────────────────────────────┘
       │ Token valid?
       ├─ YES ──► Set Authentication
       │          Continue
       │
       └─ NO ──► Reject (401)
                 Continue
       │
       ▼
┌─────────────────────────────────────────────┐
│ M. UsernamePasswordAuthenticationFilter     │
│    (For form login - bypassed for JWT)      │
└─────────────────────────────────────────────┘
       │
       ▼
        ... other filters ...
       │
       ▼
┌─────────────────────────────────────────────┐
│ Controller / Handler                        │
│ @RequestMapping, etc.                       │
└─────────────────────────────────────────────┘
       │
       ▼
    Response
```

---

This architecture provides:

- **Stateless Authentication** (no server-side sessions)
- **Scalable** (can be deployed across multiple servers)
- **Secure** (JWT signature + expiration verification)
- **Role-Based** (support for multiple roles)
- **RESTful** (follows REST principles)
