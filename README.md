# MOAuthoriser

A Spring Boot OAuth2 Authorization Server with client credentials grant type support. This project provides a secure token endpoint for server-to-server authentication.

## Requirements

- **Java**: 17 or higher
- **Gradle**: 8.0 or higher (or use the included `./gradlew` wrapper)
- **Database**: MSSQL (for production) or H2 (for testing)

### Build Dependencies
- Spring Boot 4.1.0
- Spring Security 7.1.0
- Spring Security OAuth2 Authorization Server 1.1.0
- Spring Data JPA
- Lombok (annotation processor)
- Nimbus JOSE+JWT for token signing
- BCrypt for password encoding

## Getting Started

### Build the Project

```bash
./gradlew build
```

### Run Tests

```bash
./gradlew test
```

This will run all 11 integration and unit tests including:
- OAuth2 token endpoint validation
- Client credentials authentication
- JWKS endpoint tests
- Server metadata endpoint tests

### Run the Application

```bash
./gradlew bootRun
```

The server will start on `http://localhost:9000`.

## OAuth2 Endpoints

- **Token Endpoint**: `POST /oauth2/token`
- **JWKS Endpoint**: `GET /oauth2/jwks`
- **Well-Known Configuration**: `GET /.well-known/oauth-authorization-server`

### Example: Get Access Token

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -u client-id:client-secret \
  -d "grant_type=client_credentials"
```

## Configuration

- RSA key pair is auto-generated at startup for JWT signing
- Authorization server issuer: `http://localhost:9000`
- Test profile uses in-memory H2 database
- Client credentials are BCrypt-encoded
