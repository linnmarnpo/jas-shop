# JAS Shop — Full-Stack E-Commerce Platform

[![Live Demo](https://img.shields.io/badge/Live%20Demo-jas--shop.vercel.app-cyan?style=for-the-badge&logo=vercel)](https://jas-shop.vercel.app)
[![Vue.js](https://img.shields.io/badge/Vue.js-3.x-4FC08D?style=for-the-badge&logo=vuedotjs)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Supabase-4169E1?style=for-the-badge&logo=postgresql)](https://supabase.com/)
[![ImageKit](https://img.shields.io/badge/Image%20Storage-ImageKit-FF6B35?style=for-the-badge)](https://imagekit.io/)

**JAS Shop** is a full-stack e-commerce web application built with a reactive **Vue 3** frontend and a **Spring Boot (Java)** REST API backend. It features dynamic product catalog filtering, real-time cart state management, cloud image uploads via **ImageKit**, email verification, JWT authentication, and cloud deployment on **Vercel** (frontend) and **Render** (backend).

---

## Key Features

- **Interactive Product Catalog** — Real-time category filtering, search, and specification-based sorting
- **Slide-out Cart Drawer** — Reactive cart state management powered by Pinia
- **JWT Authentication & Spring Security** — Secure user registration, login, and role-based authorization
- **Email Verification** — Account verification emails sent via Brevo (or SMTP / Resend as fallback)
- **ImageKit Image Storage** — Cloud image upload and CDN delivery for product and category assets
- **Admin Dashboard** — Product and category CRUD management with image upload support
- **PostgreSQL via Supabase** — Managed cloud database with Hibernate ORM and HikariCP connection pooling
- **SPA History Routing** — Vercel rewrite rules ensuring seamless direct URL access without 404 errors

---

## Tech Stack & Architecture

### **Frontend (Client)**
| Technology | Purpose |
|---|---|
| Vue 3 (Composition API) | UI Framework |
| Vite | Build Tool |
| Pinia | State Management |
| Vue Router (HTML5 History Mode) | Client-side Routing |
| Tailwind CSS v4 | Styling |
| Axios | HTTP Client |
| Vercel | Hosting & Deployment |

### **Backend (API Services)**
| Technology | Purpose |
|---|---|
| Java 17 + Spring Boot 4.x | REST API Framework |
| Spring Security + JWT | Authentication & Authorization |
| Spring Data JPA + Hibernate | ORM & Database Layer |
| PostgreSQL (via Supabase) | Cloud Relational Database |
| HikariCP | Connection Pooling |
| ImageKit | Cloud Image Storage & CDN |
| Brevo / SMTP / Resend | Transactional Email |
| Render | Backend Hosting & Deployment |

---

## Repository Structure

```text
jas-shop/
├── frontend/                     # Vue 3 Single Page Application
│   ├── src/
│   │   ├── admin/                # Admin pages (Products, Categories CRUD)
│   │   ├── components/           # Shared UI Components
│   │   ├── pages/                # Public pages (Home, Shop, Product Detail)
│   │   ├── router/               # Vue Router configuration
│   │   ├── service/              # Axios API client & image upload service
│   │   └── stores/               # Pinia state stores (cart, auth)
│   └── package.json
│
├── shop-backend/                 # Spring Boot REST API
│   └── src/main/java/com/Jas/shop_backend/
│       ├── Service/              # Business Logic (Email, ImageKit, User, etc.)
│       ├── api/                  # REST Controllers & JWT Helpers
│       ├── config/               # Spring Security & CORS Configuration
│       ├── enums/                # Enumerations
│       ├── exception/            # Global Exception Handling
│       ├── model/                # JPA Entities (Product, Category, User)
│       └── specification/        # JPA Specifications for dynamic filtering
│
└── vercel.json                   # Root Vercel SPA rewrite rules
```

---

## Environment Variables

The application reads all sensitive values from environment variables. No secrets are hardcoded.

### Backend (Render Environment)

| Variable | Description |
|---|---|
| `DB_URL` | Supabase Pooler JDBC URL — e.g. `jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres` |
| `DB_USERNAME` | Supabase pooler username — format: `postgres.<project-ref>` |
| `DB_PASSWORD` | Supabase database password |
| `MAIL_USERNAME` | Gmail address used as email sender |
| `MAIL_PASSWORD` | Gmail App Password (16 characters) |
| `IMAGEKIT_PRIVATE_KEY` | ImageKit private API key for backend uploads |
| `IMAGEKIT_URL_ENDPOINT` | ImageKit URL endpoint — e.g. `https://ik.imagekit.io/your_id` |
| `JWT_KEY` | Secret key for signing JWT tokens |
| `BREVO_API_KEY` | *(Optional)* Brevo API key for transactional email on cloud |
| `RESEND_API_KEY` | *(Optional)* Resend API key as email fallback |

> **Note:** Render automatically injects the `PORT` variable. The backend is already configured to read `${PORT:8080}`.

---

## Local Development Setup

### Prerequisites
- **Node.js** v18+
- **Java JDK** 17+
- **Maven** 3.8+

---

### 1. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start Vite development server
npm run dev
```

Frontend runs at `http://localhost:5173`

---

### 2. Backend Setup

Create a local config file at `shop-backend/src/main/resources/application-local.properties` (this file is in `.gitignore` and will never be committed):

```properties
# Local database (or your Supabase connection)
spring.datasource.url=jdbc:postgresql://your-db-host:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=your_password

# Mail
spring.mail.username=your_email@gmail.com
spring.mail.password=your_gmail_app_password

# ImageKit
imagekit.private-key=your_imagekit_private_key
imagekit.url-endpoint=https://ik.imagekit.io/your_id
```

Then run the backend:

```bash
cd shop-backend

# Build & Run via Maven
./mvnw spring-boot:run
```

Backend REST API runs at `http://localhost:8080`

---

## Production Deployment

### Frontend — Vercel
- Root directory: `frontend/`
- Build command: `npm run build`
- Output directory: `dist`
- The root `vercel.json` configures SPA history mode rewrites:
  ```json
  {
    "rewrites": [{ "source": "/(.*)", "destination": "/index.html" }]
  }
  ```

### Backend — Render
- Service type: **Web Service**
- Build command: `mvn clean package -DskipTests`
- Start command: `java -jar target/*.jar`
- Set all environment variables listed above in the Render dashboard under **Environment**

> **Important:** Use the **Supabase Connection Pooler** URL (not the direct DB URL) to avoid IPv6 connectivity issues on Render.

---

## Developer

**Linn Marn Po** — Full Stack Developer & CS Student
- **GitHub**: [@linnmarnpo](https://github.com/linnmarnpo)
- **Telegram**: [@linnmarn_po](https://t.me/linnmarn_po)
- **Portfolio**: [linnmarnpo.vercel.app](https://linnmarnpo.vercel.app/)
