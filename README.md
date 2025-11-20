# College Management System

A full-stack college management system built with React TypeScript (Frontend) and Spring Boot (Backend).

## Features

### User Management

- **Multi-role authentication system** (Admin, Faculty, Student)
- JWT-based authentication
- User registration and login
- Role-based access control

### Course Management

- Create, read, update, and delete courses
- Course enrollment for students
- Faculty assignment to courses
- Department-wise course organization
- Course search functionality

### Fee Management

- Fee structure management
- Payment tracking
- Outstanding fee calculations
- Fee type categorization (Tuition, Library, Laboratory, etc.)
- Payment history

### Student Enrollment

- Course enrollment/unenrollment
- Grade management
- Enrollment status tracking
- Student course history

## Technology Stack

### Backend

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 21
- **Database**: H2 (In-memory)
- **Security**: Spring Security with JWT
- **Build Tool**: Gradle
- **API Documentation**: Swagger/OpenAPI

### Frontend

- **Framework**: React 18.3.1 with TypeScript
- **Build Tool**: Vite
- **Styling**: Tailwind CSS
- **Routing**: React Router DOM
- **HTTP Client**: Axios
- **Icons**: Lucide React

## Project Structure

```
Project/
├── demo/                          # Spring Boot Backend
│   ├── src/main/java/com/example/demo/
│   │   ├── controller/           # REST Controllers
│   │   ├── dto/                  # Data Transfer Objects
│   │   ├── entity/               # JPA Entities
│   │   ├── repository/           # JPA Repositories
│   │   ├── service/              # Business Logic
│   │   ├── security/             # Security Configuration
│   │   ├── util/                 # Utility Classes
│   │   └── config/               # Configuration Classes
│   └── src/main/resources/
│       └── application.properties
└── Frontend/                      # React TypeScript Frontend
    ├── src/
    │   ├── components/           # React Components
    │   ├── context/              # React Context
    │   ├── services/             # API Services
    │   └── App.tsx               # Main App Component
    └── package.json
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Node.js 18 or higher
- npm or yarn

### Backend Setup

1. Navigate to the backend directory:

   ```powershell
   cd "c:\Users\aruns\OneDrive\Desktop\Gopika\Project\demo"
   ```

2. Build and run the Spring Boot application:

   ```powershell
   .\gradlew bootRun
   ```

3. The backend will start on `http://localhost:8080`

4. Access H2 Console at `http://localhost:8080/h2-console` (optional)

   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: `password`

5. API documentation available at `http://localhost:8080/swagger-ui.html`

### Frontend Setup

1. Navigate to the frontend directory:

   ```powershell
   cd "c:\Users\aruns\OneDrive\Desktop\Gopika\Project\Frontend"
   ```

2. Install dependencies:

   ```powershell
   npm install
   ```

3. Start the development server:

   ```powershell
   npm run dev
   ```

4. The frontend will start on `http://localhost:5173`

## Demo Accounts

The system comes with pre-configured demo accounts:

### Admin

- **Email**: `admin@college.com`
- **Password**: `admin123`
- **Capabilities**: Full system access, user management, course management, fee management

### Faculty

- **Email**: `faculty1@college.com` or `faculty2@college.com`
- **Password**: `faculty123`
- **Capabilities**: Course management, grade entry, student enrollment viewing

### Student

- **Email**: `student1@college.com` or `student2@college.com`
- **Password**: `student123`
- **Capabilities**: Course enrollment, grade viewing, fee viewing and payment

## API Endpoints

### Authentication

- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration

### Courses

- `GET /api/courses` - Get all courses
- `GET /api/courses/{id}` - Get course by ID
- `POST /api/courses` - Create course (Admin only)
- `PUT /api/courses/{id}` - Update course (Admin only)
- `DELETE /api/courses/{id}` - Delete course (Admin only)
- `GET /api/courses/department/{department}` - Get courses by department
- `GET /api/courses/faculty/{facultyId}` - Get courses by faculty
- `GET /api/courses/search?keyword={keyword}` - Search courses

### Enrollments

- `POST /api/enrollments/enroll` - Enroll student in course
- `POST /api/enrollments/unenroll` - Unenroll student from course
- `GET /api/enrollments/student/{studentId}` - Get student enrollments
- `GET /api/enrollments/course/{courseId}` - Get course enrollments
- `PUT /api/enrollments/{enrollmentId}/grade` - Update grade

### Fees

- `GET /api/fees` - Get all fees (Admin only)
- `GET /api/fees/student/{studentId}` - Get student fees
- `POST /api/fees` - Create fee (Admin only)
- `POST /api/fees/{feeId}/pay` - Pay fee
- `GET /api/fees/student/{studentId}/total` - Get fee summary

## Database Schema

### User Entity

- ID, Email, Password, First Name, Last Name, Role, Active Status

### Course Entity

- ID, Course Code, Course Name, Description, Credits, Department, Faculty

### Student Course Entity

- ID, Student, Course, Enrollment Date, Grade, Status

### Fee Entity

- ID, Student, Fee Type, Amount, Paid Amount, Due Date, Payment Status

## Security Features

- JWT token-based authentication
- Role-based access control
- Password encryption using BCrypt
- CORS configuration
- Input validation
- SQL injection prevention through JPA

## Development

### Backend Development

- Uses Spring Boot DevTools for hot reloading
- H2 database for development (data resets on restart)
- Comprehensive error handling
- Input validation with Jakarta Bean Validation

### Frontend Development

- Hot module replacement with Vite
- TypeScript for type safety
- Context API for state management
- Responsive design with Tailwind CSS
- Loading states and error handling

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is for educational purposes.

## Support

For support, contact the development team or create an issue in the repository.
