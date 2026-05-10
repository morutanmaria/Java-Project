# RailApp — Train Ticketing Application

A Spring Boot web application for managing train schedules, routes, and ticket bookings. Features a dark-themed Thymeleaf UI with role-based access (USER / ADMIN), JWT-secured REST API, email notifications, overbooking prevention, and journey search with changeover support.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| UI | Thymeleaf + custom CSS |
| Security | Spring Security (form login) + JWT (REST API) |
| Persistence | Spring Data JPA + MySQL |
| Email | Spring Mail (mock mode by default) |
| Build | Maven |

---

## Setup & Running

### 1. Database

Make sure your MySQL database is running and has the following tables. Run this SQL if needed:

```sql
ALTER TABLE trains ADD COLUMN IF NOT EXISTS is_delayed tinyint(1) DEFAULT 0;
```

### 2. Configure `application.properties`

```properties
spring.application.name=train-ticketing

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/train_app
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# JWT
jwt.secret=TrainAppSuperSecretKeyForJWT2024!!Long
jwt.expiration-ms=86400000

# Email (mock=true prints emails to console, no SMTP needed)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
app.mail.mock=true

server.port=8080
```

### 3. Run

```bash
mvn spring-boot:run
```

Open **http://localhost:8080** in your browser.

### 4. Default Admin Account

| Email | Password |
|-------|----------|
| admin@trainapp.com | admin123 |

---

## Application Pages

| Page | URL | Access |
|------|-----|--------|
| Login | `/login` | Public |
| Register | `/register` | Public |
| Trains | `/trains` | All logged-in users |
| Routes | `/routes` | All logged-in users |
| Schedules | `/schedules` | All logged-in users |
| Journey Search | `/journey` | All logged-in users |
| Book Tickets | `/bookings/new` | All logged-in users |
| My Bookings | `/bookings` | All logged-in users |
| All Bookings | `/bookings` | Admin (sees everyone's) |
| Users | `/users` | Admin only |

---

## Functionality

### a) Book Tickets

Any logged-in user can book one or multiple seats on a scheduled train.

**Steps:**
1. Go to **Bookings → Book Tickets**
2. Select a schedule from the dropdown (shows train, route, time, available seats)
3. Enter number of seats
4. Click **Confirm Booking**

**Example input:**
- Schedule: `InterCity 100 | Cluj-Napoca → Bucharest | 10 May 06:00 | 200 seats`
- Seats: `2`

**Example output (success):**
> Booking confirmed! A confirmation email has been sent.

**Example output (overbooking):**
> Not enough seats. Requested: 5, Available: 2

**Confirmation email (printed to console in mock mode):**
```
===== [MOCK EMAIL] =====
TO      : alice@example.com
SUBJECT : Booking Confirmed – Ticket #1
BODY    :
Dear Alice Smith,

Your booking has been CONFIRMED.

Booking ID   : 1
Train        : InterCity 100 (IC100)
Route        : Cluj-Napoca → Bucharest
Departure    : 2026-05-10T06:00
Arrival      : 2026-05-10T13:00
Seats booked : 2
Status       : CONFIRMED

Have a great journey!
Train Ticketing App
========================
```

---

### b) Journey Search

Find possible routes between any two stations, including connections requiring a train change.

**URL:** `/journey?from=Cluj-Napoca&to=Constanta`

**Example — Direct route:**
```
✦ Direct Route
Cluj-Napoca → Bucharest
Dep: 10 May 06:00    Arr: 10 May 13:00
Train: InterCity 100 (IC100) | 200 seats
[Book This Journey]
```

**Example — Changeover route:**
```
⇄ Changeover Route
Cluj-Napoca → Bucharest
Dep: 10 May 06:00    Arr: 10 May 13:00
Train: InterCity 100 (IC100)

🔄 Change trains at Bucharest

Bucharest → Constanta
Dep: 10 May 14:00    Arr: 10 May 17:00
Train: Express 300 (EX300)
[Book This Journey]
```

**Example — No connection found:**
> No routes found between 'Cluj-Napoca' and 'Rome'. No direct or changeover connections available.

The **Book This Journey** button pre-selects the schedule in the booking form.

---

### c) Admin Operations

Log in as `admin@trainapp.com` to access admin features. Admin-only elements (buttons, columns, nav links) are hidden from regular users.

#### Manage Trains (`/trains`)

| Action | How |
|--------|-----|
| View all trains | Page loads automatically |
| Add train | Click **+ Add Train**, fill modal form |
| Edit train | Click **Edit** on any row |
| Delete train | Click **Delete** on any row |
| Report delay | Click **Report Delay**, enter minutes |

**Add train example input:**
- Name: `Speed Rail 400`
- Train Number: `SR400`
- Total Seats: `300`

**Report delay example input:** `45` minutes

**Delay email sent to all affected passengers:**
```
===== [MOCK EMAIL] =====
TO      : alice@example.com
SUBJECT : ⚠ Train Delay Notice – InterCity 100
BODY    :
Dear Alice Smith,

We regret to inform you that train InterCity 100 (IC100) is delayed by 45 minutes.

We apologise for the inconvenience.
Train Ticketing App
========================
```

#### Manage Routes (`/routes`)

| Action | How |
|--------|-----|
| View all routes | Page loads automatically |
| Add route | Click **+ Add Route**, fill modal |
| Edit route | Click **Edit** on any row |
| Delete route | Click **Delete** on any row |

**Add route example input:**
- From: `Iasi`
- To: `Bucharest`
- Distance: `380 km`

#### Manage Schedules (`/schedules`)

| Action | How |
|--------|-----|
| View all schedules | Page loads automatically |
| Add schedule | Click **+ Add Schedule**, select train, route, times |
| Edit schedule | Click **Edit** on any row |
| Delete schedule | Click **Delete** on any row |

**Add schedule example input:**
- Train: `InterCity 100 (IC100)`
- Route: `Cluj-Napoca → Bucharest`
- Departure: `2026-05-11 08:00`
- Arrival: `2026-05-11 15:00`

#### View Bookings (`/bookings`)

Admins see all bookings across all passengers. Use the **Filter by Train** dropdown to see bookings for a specific train.

**Example output:**

| ID | Passenger | Train | Route | Departure | Seats | Status |
|----|-----------|-------|-------|-----------|-------|--------|
| 1 | Alice Smith | InterCity 100 | Cluj-Napoca → Bucharest | 10 May 06:00 | 2 | CONFIRMED |

#### View Users (`/users`)

Admin-only page showing all registered accounts with name, email, role, and registration date.

---

## Email Configuration

By default `app.mail.mock=true` — emails are printed to the console instead of being sent. To send real emails:

```properties
spring.mail.username=your-gmail@gmail.com
spring.mail.password=your-app-password   # Gmail App Password
app.mail.mock=false
```

---

## REST API

The app also exposes a JSON REST API (JWT-authenticated). Get a token by calling `/api/auth/login`.

### Authentication

```
POST /api/auth/register
Content-Type: application/json

{
  "name": "Alice Smith",
  "email": "alice@example.com",
  "password": "secret123"
}
```

```
POST /api/auth/login
Content-Type: application/json

{
  "email": "alice@example.com",
  "password": "secret123"
}
```

Response:
```json
{
  "userId": 2,
  "email": "alice@example.com",
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Use the token in all subsequent requests:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Endpoints

| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/register` | Public | Register |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| GET | `/api/trains` | Any | List all trains |
| POST | `/api/trains` | Admin | Create train |
| PUT | `/api/trains/{id}` | Admin | Update train |
| DELETE | `/api/trains/{id}` | Admin | Delete train |
| POST | `/api/trains/{id}/delay` | Admin | Report delay |
| GET | `/api/routes` | Any | List all routes |
| POST | `/api/routes` | Admin | Create route |
| PUT | `/api/routes/{id}` | Admin | Update route |
| DELETE | `/api/routes/{id}` | Admin | Delete route |
| GET | `/api/schedules` | Any | List all schedules |
| GET | `/api/schedules/journey?from=X&to=Y` | Any | Journey search |
| POST | `/api/schedules` | Admin | Create schedule |
| PUT | `/api/schedules/{id}` | Admin | Update schedule |
| DELETE | `/api/schedules/{id}` | Admin | Delete schedule |
| POST | `/api/bookings` | Any | Book tickets |
| GET | `/api/bookings/my` | Any | My bookings |
| GET | `/api/bookings` | Admin | All bookings |
| GET | `/api/bookings/train/{id}` | Admin | Bookings by train |

### Book Tickets (REST)

```
POST /api/bookings
Authorization: Bearer <token>
Content-Type: application/json

{
  "scheduleId": 1,
  "seatsBooked": 2
}
```

### Report Delay (REST)

```
POST /api/trains/1/delay
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "delayMinutes": 45
}
```

---

## Project Structure

```
src/main/java/trainapp/
├── TrainTicketingApplication.java
├── configuration/
│   ├── DataInitializer.java       # Seeds default trains, routes, schedules, admin user
│   └── SecurityConfig.java        # Form login + JWT filter, role-based access rules
├── controller/
│   ├── PageController.java        # All Thymeleaf page routes (UI)
│   ├── AuthController.java        # REST: /api/auth/*
│   ├── BookingController.java     # REST: /api/bookings/*
│   ├── RouteController.java       # REST: /api/routes/*
│   ├── ScheduleController.java    # REST: /api/schedules/*
│   ├── TrainController.java       # REST: /api/trains/*
│   └── UserController.java        # REST: /api/users/*
├── dto/                           # Request/Response data transfer objects
├── exception/                     # Custom exceptions + global error handler
├── model/                         # JPA entities (User, Train, Route, Schedule, Booking)
├── repository/                    # Spring Data JPA repositories
├── security/
│   ├── JwtAuthFilter.java         # Reads JWT from Authorization header
│   ├── JwtUtil.java               # Token generation and validation
│   └── UserDetailsServiceImpl.java # Loads users from DB for Spring Security
└── service/
    ├── AuthService.java
    ├── BookingService.java        # Booking logic + overbooking check
    ├── EmailService.java          # Mock + real SMTP email sending
    ├── RouteService.java
    ├── ScheduleService.java       # Journey search (direct + changeover)
    ├── TrainService.java          # Delay reporting + passenger notifications
    └── UserService.java

src/main/resources/templates/
├── fragments.html   # Shared nav + CSS design system
├── login.html
├── register.html
├── trains.html      # Train list + admin CRUD + delay reporting
├── routes.html      # Route list + admin CRUD
├── schedules.html   # Schedule list + admin CRUD
├── journey.html     # Journey search with direct/changeover results
├── bookings.html    # Booking list (user: own; admin: all + filter by train)
├── book.html        # Book tickets form
└── users.html       # Admin: registered users list
```

---

## Role Differences

| Feature | Regular User | Admin |
|---------|-------------|-------|
| View trains / routes / schedules | ✅ | ✅ |
| Search journeys | ✅ | ✅ |
| Book tickets | ✅ | ✅ |
| View own bookings | ✅ | ✅ |
| Add / Edit / Delete trains | ❌ | ✅ |
| Add / Edit / Delete routes | ❌ | ✅ |
| Add / Edit / Delete schedules | ❌ | ✅ |
| Report train delay (notifies passengers) | ❌ | ✅ |
| View all bookings + filter by train | ❌ | ✅ |
| View all users | ❌ | ✅ |

---

## Database Schema

```
users         — id, name, email, password, role, created_at
trains        — id, name, train_number, total_seats, delay_minutes, is_delayed
routes        — id, source, destination, distance_km
schedules     — id, train_id, route_id, departure_time, arrival_time, available_seats
bookings      — id, user_id, schedule_id, seats_booked, booking_status, booking_time
stations      — id, name
```
