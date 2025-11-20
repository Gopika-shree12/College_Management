@echo off
echo Starting College Management System...
echo.

echo Starting Backend (Spring Boot)...
cd "c:\Users\aruns\OneDrive\Desktop\Gopika\Project\demo"
start "Backend" cmd /c ".\gradlew bootRun"

echo Waiting for backend to start...
timeout /t 10 /nobreak >nul

echo Starting Frontend (React)...
cd "c:\Users\aruns\OneDrive\Desktop\Gopika\Project\Frontend"
start "Frontend" cmd /c "npm run dev"

echo.
echo College Management System is starting up!
echo Backend will be available at: http://localhost:8080
echo Frontend will be available at: http://localhost:5173
echo.
echo Demo accounts:
echo Admin: admin@college.com / admin123
echo Faculty: faculty1@college.com / faculty123  
echo Student: student1@college.com / student123
echo.
pause