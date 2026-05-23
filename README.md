# RECO

RECO is an immersive, high-performance e-commerce platform featuring a custom-built Hybrid Recommendation Engine. Designed for modern web experiences, RECO offers a seamless, otherworldly shopping interface backed by a robust, scalable Spring Boot architecture.

## Key Features

### Core E-Commerce Experience
* **Immersive UI:** A visually stunning frontend built with Vanilla JS, Tailwind CSS v4, and Vite.
* **Dynamic Cart System:** Real-time, slide-out shopping cart with instant calculations and state management.
* **Secure Authentication:** Stateless JWT-based access with role-based authorization (Customer vs. Admin).
* **Catalog & Filtering:** Interactive product grid with categorical filtering (Ritual, Form, Signal) and dynamic sorting.
* **Integrated Checkout:** Seamless, self-contained checkout flow with simulated payment processing.

### Hybrid Recommendation Engine
RECO goes beyond standard e-commerce by dynamically predicting user preferences using a multi-layer algorithm:
* **Cold Start Strategy:** Dynamically calculates a *Popularity Score* (factoring in reviews, clicks, and cart adds) to serve trending items to new users.
* **Content-Based Filtering:** Recommends products physically or categorically similar to those the user has recently viewed or highly rated.
* **Collaborative Filtering:** Identifies behavioral clusters and recommends products based on what similar users are buying.
* **Hybrid Merge Algorithm:** Blends Content-Based (60%) and Collaborative (40%) scores into a final, highly-tuned ranking for active users.
* **Smart Caching:** Recommendations are cached via an expiration strategy to guarantee instant dashboard load times.
* **Frequently Bought Together:** Leverages historical order data to cross-sell items on product detail pages.

---

## Tech Stack

**Backend:**
* Java 17+
* Spring Boot (WebMVC, Data JPA, Security)
* PostgreSQL (Relational Database)
* Flyway (Database Migrations)
* JJWT (JSON Web Token Authentication)

**Frontend:**
* HTML5 & CSS3
* Vanilla JavaScript (ES6 Modules)
* Tailwind CSS v4
* Vite (Build Tool & Dev Server)

---

## Getting Started

### Prerequisites
* **Java 17+**
* **Node.js 18+**
* **PostgreSQL**

### 1. Database Setup
Create a local PostgreSQL database named `reco_db`.
```sql
CREATE DATABASE reco_db;
```
*(If your local Postgres uses a different username/password than the default `postgres`/`password`, update `backend/src/main/resources/application.properties`)*

### 2. Backend Setup
Navigate to the backend directory and run the Spring Boot application using the Maven wrapper:
```bash
cd backend
./mvnw spring-boot:run
```
The backend will start on `http://localhost:8080`.
*API Documentation (Swagger UI) is automatically generated and available at `http://localhost:8080/swagger-ui/index.html`.*

### 3. Frontend Setup
In a new terminal window, navigate to the frontend directory:
```bash
cd frontend
npm install
npm run dev
```
The frontend will start on `http://localhost:5173`. Open this URL in your browser to experience RECO.

## Project Structure

```text
├── backend/          # Spring Boot REST API
│   ├── src/main/     # Java source code & application.properties
│   └── pom.xml       # Maven dependencies
└── frontend/         # Vite + Vanilla JS application
    ├── src/          # UI components, JS modules, styles
    └── package.json  # NPM dependencies
```

---

## Architecture

RECO is designed as a decoupled full-stack system:
* **Frontend:** A lightweight, highly optimized Single Page Application (SPA) driven by Vanilla JS routing, dynamic DOM injection, and `IntersectionObserver` for scroll-reveal animations.
* **Backend:** A standard Layered Spring Boot REST API (Controller → Service → Repository).
* **Database:** PostgreSQL with Flyway for version-controlled schema migrations.

### Key Backend Modules:
1. **Security & Auth:** Custom JWT implementation with Access and Refresh tokens.
2. **Category & Stock:** Real-time stock control and product lifecycle management.
3. **Order Processing:** Cart lifecycle, order persistence, and mock payment gateway routing.
4. **Recommendation Engine:** Interaction tracking (clicks, adds) and multi-strategy algorithmic product suggestions.
