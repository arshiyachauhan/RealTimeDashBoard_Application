## 📊 RealTime Dashboard Application

A full-stack web application for real-time personal productivity and finance management, featuring a Java Spring Boot backend and a React + Bootstrap frontend.

---

## Features

- **JWT Authentication:** Secure login and signup with token-based authentication.
- **User Dashboard:** Overview of metrics, tasks, notes, finances, and reminders.
- **Metrics Management:** CRUD for real-time metrics (e.g., system stats, stocks).
- **Task Management:** Create, update, and track tasks.
- **Notes:** Personal note-taking with CRUD operations.
- **Finance Tracking:** Record income/expenses and visualize with charts.
- **Reminders:** Set reminders, optionally linked to tasks.
- **Responsive UI:** Modern, mobile-friendly interface with sidebar navigation and top bar.



## Backend: Java Spring Boot

### **Tech Stack**
- Java 17+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- H2/MySQL (configurable)
- Lombok

### **Setup**
1. **Install Java 17+ and Maven**
2. **Clone the repository:**
   ```sh
   git clone https://github.com/arshiyachauhan/RealTimeDashBoard_Application.git
   cd RealTimeDashBoard_Application
   ```
3. **Configure database:**
   - Edit `src/main/resources/application.properties` for your DB settings.
4. **Build and run:**
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
5. **API Base URL:**
   - `http://localhost:8080/api`

### **Key Endpoints**
| Resource      | Endpoint                        | Methods         |
|--------------|----------------------------------|-----------------|
| Auth         | `/signup`, `/api/login`          | POST            |
| Metrics      | `/api/metrics`                   | GET, POST, PUT, DELETE |
| Tasks        | `/api/tasks`                     | GET, POST, PUT, DELETE |
| Notes        | `/api/notes`                     | GET, POST, PUT, DELETE |
| Transactions | `/api/transactions`              | GET, POST, PUT, DELETE |
| Reminders    | `/api/reminders`                 | GET, POST, PUT, DELETE |

- All endpoints (except signup/login) require a JWT token in the `Authorization: Bearer <token>` header.


## Frontend: React + Bootstrap

### **Tech Stack**
- React 18+
- Bootstrap 5
- Axios
- React Router DOM
- Recharts (for charts)

### **Setup**
1. **Navigate to the frontend directory:**
   ```sh
   cd frontend
   ```
2. **Install dependencies:**
   ```sh
   npm install
   ```
3. **Start the development server:**
   ```sh
   npm start
   ```
4. **Frontend URL:**
   - `http://localhost:3000`

### **Main Pages & Components**
- **Login / Signup:** User authentication
- **Dashboard:** Overview of all modules
- **Metrics:** Real-time metrics CRUD
- **Tasks:** Task management
- **Notes:** Notes CRUD
- **Finance:** Income/expense tracking, summary chart
- **Reminders:** Set and manage reminders
- **Sidebar & Header:** Navigation and user info/logout



## Usage

1. **Sign up** for a new account or log in.
2. **Navigate** using the sidebar to manage metrics, tasks, notes, finances, and reminders.
3. **All data is scoped to your user account.**
4. **Logout** via the top bar to clear your session.



## API Authentication
- Obtain a JWT token via `/api/login`.
- Add the token to all protected requests:
  ```
  Authorization: Bearer <your_token>
  ```



## Development & Contribution
- Fork the repo and create a feature branch.
- Open a pull request with your changes.
- Please follow code style and add documentation/comments where needed.



## License
This project is licensed under the MIT License.
