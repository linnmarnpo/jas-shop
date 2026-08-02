# JAS Shop — Enterprise E-Commerce Platform 

[![Live Demo](https://img.shields.io/badge/Live%20Demo-jas--shop.vercel.app-cyan?style=for-the-badge&logo=vercel)](https://jas-shop.vercel.app)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-4FC08D?style=for-the-badge&logo=vuedotjs)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.x-4169E1?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)

**JAS Shop** is a high-performance, full-stack e-commerce web application engineered with a reactive **Vue.js 3** frontend and a robust **Spring Boot (Java)** REST API backend. It features dynamic product specification filtering, real-time cart state management, cloud image uploads via Cloudinary, JWT authentication, and containerized cloud deployment on Vercel and Render.

---

## 🌟 Key Features

- nteractive Product Catalog**: Real-time category filtering, search, and specification sorting.
- Slide-out Cart Drawer**: Reactive state management powered by Pinia.
- Spring Security & JWT Authentication**: Secure user registration, authentication headers, and role authorization.
- Cloudinary Image Integration**: Automated cloud image upload and CDN delivery for product assets.
- Relational PostgreSQL Persistence**: Structured database schema managed with Spring Data JPA & Hibernate ORM.
- SPA History Routing**: Configured Vercel rewrite rules (`vercel.json`) ensuring seamless direct route refreshes without 404 errors.

---

## 🛠️ Tech Stack & Architecture

### **Frontend (Client)**
- **Framework**: Vue 3 (Composition API / Options API)
- **Build Tool**: Vite
- **State Management**: Pinia
- **Router**: Vue Router (HTML5 History Mode)
- **Styling**: Tailwind CSS v4
- **Hosting**: Vercel

### **Backend (API Services)**
- **Language & Framework**: Java 17+, Spring Boot 3.x
- **Security**: Spring Security & JWT Token Helper
- **ORM & Database**: Spring Data JPA, Hibernate, PostgreSQL
- **Media Management**: Cloudinary Java SDK
- **Hosting**: Render

---

## 📁 Repository Structure

```text
jas-shop/
├── frontend/                 # Vue 3 Single Page Application
│   ├── src/
│   │   ├── components/       # UI Components (Header, CartDrawer, ProductCard)
│   │   ├── router/           # Vue Router configuration
│   │   ├── store/            # Pinia state stores (cart.js)
│   │   └── views/            # Home, Shop, Info views
│   ├── vercel.json           # Vercel SPA route rewrite rules
│   └── package.json
│
├── shop-backend/             # Spring Boot REST API
│   ├── src/main/java/com/Jas/shop_backend/
│   │   ├── api/              # REST Controllers & JWT Helpers
│   │   ├── model/            # JPA Entities (Product, Category, User)
│   │   ├── repository/       # Data Access Layer
│   │   └── service/          # Business Logic
│   └── src/main/resources/
│       └── application.properties
│
└── vercel.json               # Root Vercel deployment configuration
```

---

## Local Development Setup

### **Prerequisites**
- **Node.js** (v18.x or later)
- **Java JDK** (17 or later)
- **Maven** (3.8+)
- **PostgreSQL** database instance

---

### 1️ Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start Vite development server
npm run dev
```

The frontend client will run locally at `http://localhost:5173`.

---

### 2️ Backend Setup

1. Configure your database & Cloudinary credentials in `shop-backend/src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/jas_shop_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Cloudinary Credentials
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret

# JWT Secret
jwt.secret=your_jwt_secret_key
```

2. Run the Spring Boot application:

```bash
# Navigate to backend directory
cd shop-backend

# Build & Run via Maven
./mvnw spring-boot:run
```

The REST API backend will run locally at `http://localhost:8080`.

---

## 🌐 Production Deployment

- **Frontend (Vercel)**: Deployed automatically from `frontend/` with History Mode rewrites:
  ```json
  {
    "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }]
  }
  ```
- **Backend (Render)**: Deployed as a Maven Web Webservice connected to PostgreSQL.

---

## Developer

**Linn Marn Po** — Full Stack Developer & CS Student  
- **GitHub**: [@linnmarnpo](https://github.com/linnmarnpo)  
- **Telegram**: [@linnmarn_po](https://t.me/linnmarn_po)  
- **Live Portfolio**: [linnmarnpo.dev](https://linnmarnpo.vercel.app/))
