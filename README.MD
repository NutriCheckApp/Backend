# Backend

### Run app:

```shell
mvn spring-boot:run
```

## Docs

### Swagger

```http
http://localhost:8080/api/v1/swagger-ui/index.html
```

NutriCheck Backend API Documentation

Base URL:

``` HTTP
http://localhost:8080/api/v1
```

### Authentication & Registration

#### Register User

Register a new user account.  
URL: _/auth/register_

``` 
http://localhost:8080/api/v1/auth/register
```

Method: POST

Request Body:

```json 
{
  "username": "user123",
  "password": "securePassword",
  "email": "user@example.com",
  "petWeight": 10.5,
  "pet_age": 24,
  "gender": "MALE",
  "activity_level": "NORMAL"
}
```

Response (200 OK):

```json 
{
  "jwt": "eyJhbGciOiJIUz...",
  "message": "User registered successfully",
  "username": "user123",
  "dailyCalories": 500.0,
  "dailyCrudeProtein": 40.0,
  "dailyCrudeFat": 20.0,
  "dailyCrudeFibers": 5.0,
  "dailyCalcium": 1.2
}
```

#### Login

Authenticate a user and retrieve a JWT.

URL: /auth/login
``` 
http://localhost:8080/api/v1/auth/login
```
Method: POST

Request Body:

```json 
{
  "username": "user123",
  "password": "securePassword"
}
```

Response (200 OK):

```json
{
  "jwt": "eyJhbGciOiJIUz...",
  "message": "Login successful",
  "username": "user123"
}
```

