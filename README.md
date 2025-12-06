🚕 RideShare Backend (Spring Boot + MongoDB + JWT)

A mini ride-sharing backend application built using Spring Boot, MongoDB, JWT Authentication, DTO Validation, and Global Exception Handling.
This project demonstrates a clean architecture using Controller → Service → Repository with role-based access (ROLE_USER, ROLE_DRIVER).

⸻

🛠️ Tech Stack
	•	Java 17+
	•	Spring Boot 4.x
	•	MongoDB
	•	Spring Security (JWT)
	•	Jakarta Validation
	•	Postman / cURL for API testing
📁 Project Structure
src/main/java/com/example/RideShare/
│── controller/
│── service/
│── repository/
│── model/
│── dto/
│── config/
│── exception/
│── util/
src/main/resources/
│── application.properties
🔐 Authentication Flow (JWT)
	1.	Register user (POST /api/auth/register)
	2.	Login (POST /api/auth/login)
	3.	Receive JWT
	4.	Send token in header for all protected endpoints:
  Authorization: Bearer <token>
  🌐 API Endpoints

🔓 Public

✅ Register User / Driver
POST /api/auth/register
Body:
{
  "username": "john",
  "password": "1234",
  "role": "ROLE_USER"
}
✅ Login
POST /api/auth/login
Body:
{
  "username": "john",
  "password": "1234"
}
Response:
{
  "token": "<JWT>",
  "username": "john",
  "role": "ROLE_USER"
}
👤 USER Endpoints

🚕 Request Ride
POST /api/v1/rides
Headers: Authorization: Bearer <token>
Body:
{
  "pickupLocation": "A",
  "dropLocation": "B"
}
📜 View My Rides
GET /api/v1/user/rides
Headers: Authorization: Bearer <token>
🚗 DRIVER Endpoints

📥 View Pending Ride Requests
GET /api/v1/driver/rides/requests
Headers: Authorization: Bearer <driver-token>
✔ Accept a Ride
POST /api/v1/driver/rides/{rideId}/accept
Headers: Authorization: Bearer <driver-token>
🧪 Postman / cURL Testing

Register
curl -X POST http://localhost:8081/api/auth/register \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234","role":"ROLE_USER"}'
Login
curl -X POST http://localhost:8081/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"john","password":"1234"}'
Create Ride
curl -X POST http://localhost:8081/api/v1/rides \
-H "Authorization: Bearer <token>" \
-H "Content-Type: application/json" \
-d '{"pickupLocation":"A","dropLocation":"B"}'
🚀 Run Application
mvn clean package
mvn spring-boot:run
✔ Features Completed
	•	JWT Login + Role-Based Access
	•	Secure Password Encoding (BCrypt)
	•	MongoDB Repository Pattern
	•	DTO Validation
	•	Global Exception Handler
	•	Passenger Ride Requests
	•	Driver Accept + Complete Ride
	•	Clean Folder Structure
  📌 Notes
	•	If POST works but browser GET shows error → expected (POST only).
	•	Use Postman or cURL for request-based operations.
	•	Make sure MongoDB is running locally.
