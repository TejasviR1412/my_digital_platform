# 💱 Currency Converter — Spring Boot FinTech Project

A modern currency converter web application built with **Spring Boot**, **Thymeleaf**, and **Bootstrap** — featuring:
- dynamic base currency (passed as request parameter)
- real-time exchange rates from external API
- Swagger UI for API exploration & documentation

> 🎯 Built as a learning project to explore Spring Boot, REST APIs, Swagger, and FinTech data handling.

---

## ✨ Features

- 🌐 Convert amounts between currencies, choosing base currency dynamically
- 📊 View & filter latest exchange rates stored locally
- 🔄 Auto-update exchange rates daily from external API
- 🧰 Swagger UI for easy API testing & documentation
- 🛡 Robust error handling and logging
- 🧪 Unit & web layer tests with MockMvc & Mockito
- 🎨 Responsive front-end with Bootstrap

---

## ⚙️ Tech Stack

| Layer          | Framework / Library                        |
| -------------- | ------------------------------------------- |
| Backend        | Spring Boot, Spring MVC, Spring Data JPA   |
| API Docs       | Springdoc OpenAPI / Swagger                |
| Frontend       | Thymeleaf, Bootstrap                       |
| Database       | H2 (in-memory, dev)                        |
| Build / Test   | Maven, JUnit 5, Mockito, Spring Boot Test  |

---

## 📁 Project Structure
```text
src  
├── main  
│   ├── java/com/projects/currency_converter  
│   │   ├── controller          # CurrencyController (views) & CurrencyApiController (REST API)  
│   │   ├── service             # Business logic: CurrencyService, ExchangeRateUpdaterService  
│   │   ├── model               # Domain models: ExchangeRate, CurrencyConversionRequest  
│   │   ├── repository          # Spring Data JPA repository: ExchangeRateRepository  
│   │   ├── configuration       # External API config & SwaggerConfiguration  
│   │   └── scheduler           # Scheduled task: RateUpdateScheduler  
│   └── resources/templates     # Thymeleaf HTML templates (currency-converter.html, exchange-rates.html)  
└── test  
    └── java/com/projects/currency_converter  
        ├── controller         # Web layer tests: CurrencyApiControllerTest, CurrencyControllerTest  
        └── service            # Unit tests: CurrencyServiceTest, ExchangeRateUpdaterServiceTest  


🧭 Legend:  
controller → Handles HTTP requests: both HTML views (with Thymeleaf) and REST API endpoints

service → Business logic & data fetching from repository / external API

model → Domain data classes and form objects

repository → Interfaces for database access

configuration → External API properties & Swagger config for API docs

scheduler → Scheduled task to auto-update exchange rates daily

templates → Thymeleaf HTML templates for frontend UI

test → Unit & integration tests for controllers and services
```
---

## 🚀 Getting Started

### 📦 Prerequisites
- Java 17+
- Maven 3.x
- Git

---

### ⚙️ Clone & build
bash:  
  git clone https://github.com/YOUR_USERNAME/currency-converter.git  
  cd currency-converter  
  mvn clean install  

### ▶️ Run locally
mvn spring-boot:run

🌐 UI: http://localhost:8081/currency/converter  
📊 Exchange rates: http://localhost:8081/currency/rates  
🧰 Swagger UI: http://localhost:8081/swagger-ui.html  
   OpenAPI JSON: http://localhost:8081/api-docs  
🛠 REST API: POST /api/currency/convert?from=USD&to=EUR&amount=100  

---

📊 External API Configuration

Update application.properties:  
exchange.api.key=YOUR_API_KEY  
exchange.api.url=https://v6.exchangerate-api.com/v6/{key}/latest/{basecurrency}  

Note: {basecurrency} is replaced dynamically based on the request.

---

🧪 Running Tests
bash:
  mvn test
  
---

## ✅ **Extra tips:**
- Replace `YOUR_USERNAME` with your GitHub username.
- Replace `YOUR_API_KEY` with your real API key.
