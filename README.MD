# NutriCheck App - Backend API Server

### ✅ 구성원 기여 내역
```
* **김민우** 
  * 캘린더 기능 구현
  * 레시피 추천 페이지 기능 구현, 발표
* **김병철** 
  * 사료 성분을 통해 하루 영양성분 추천량 기능 구현
* **자하르** 
  * 회원가입 및 보안 기능 구현
  * 동물 정보 처리 및 이미지 처리 기능 구현
  * Frontend와 DB 연동
* **권현우** 
  * DB설계
  * 칼로리 계산 기능 구현
```

This project serves as the Backend API Server for the NutriCheck application, built using Spring Boot and Java.
It is responsible for handling all business logic, data persistence, user authentication, and serving API endpoints for
the frontend application.

### ✨ Key Backend Features & Modules

The backend is responsible for powering the following core functionalities:

* **Secure Authentication:** User registration, login, and JWT management.
* **User & Pet Profile Management:** Creating, retrieving, updating, and deleting profiles for multiple pets associated with a user.
* **Dietary Ration Calculation:** Automatically calculates optimal daily requirements for Calories, Crude Protein, Crude Fat, Crude Fibers, and Calcium based on pet characteristics (weight, age, life stage, activity level) upon registration and profile update.
* **Recipe Service:** Retrieves lists of recipes and detailed, step-by-step instructions (including ingredients and images) for pet meals.
* **Calendar & Journaling:** Allows users to log daily memos and upload/manage photos associated with specific dates.
* **Data Persistence:** Handles all data storage and retrieval.

## 🚀 Getting Started

To successfully launch the server, ensure you have all the necessary tools installed.

### Prerequisites

* **JDK 21+**
* **Maven**

### Run the Application:

```shell
mvn spring-boot:run
```

## ⚙️ Core API Endpoints Overview

The following table summarizes the primary functional endpoints available in the NutriCheck Backend API.

| Module   | Endpoint                   | Method | Description                                                  |
|----------|----------------------------|--------|--------------------------------------------------------------|
| Profile  | /profile                   | GET    | Get the current user's profile and pet list.                 |
| Profile  | /profile                   | PUT    | Update the current user's profile (name, email).             |
| Pet      | /pet                       | POST   | Register a new pet profile.                                  |
| Pet      | /pet                       | GET    | Get all pets associated with the current user.               |
| Pet      | /pet/{id}                  | GET    | Get a specific pet's details.                                |
| Pet      | /pet/{id}                  | PUT    | Update a specific pet's profile.                             |
| Pet      | /pet/{petId}               | DELETE | Delete a pet profile.                                        |
| Recipes  | /recipes                   | GET    | Get a list of recipe summaries.                              |
| Recipes  | /recipes/{recipeId}        | GET    | Get detailed information for a specific recipe.              |
| Calendar | /calendar                  | GET    | Get a calendar entry (memo, photo) for a specific date.      |
| Calendar | /calendar                  | POST   | Update/create a calendar entry, including uploading a photo. |
| Calendar | /calendar/memo             | POST   | Update the memo text for a calendar entry.                   |
| Calendar | /calendar/image/{filename} | DELETE | Delete a photo from a calendar entry.                        |

### Base URL:

```http
http://localhost:8080/api/v1
```

### 📚 API Documentation (Swagger):

Access the full interactive documentation here:
```http
http://localhost:8080/api/v1/swagger-ui/index.html
```

