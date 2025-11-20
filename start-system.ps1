Write-Host "Starting College Management System..." -ForegroundColor Green
Write-Host ""

Write-Host "Starting Backend (Spring Boot)..." -ForegroundColor Yellow
$backendPath = "c:\Users\aruns\OneDrive\Desktop\Gopika\Project\demo"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$backendPath'; .\gradlew bootRun"

Write-Host "Waiting for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "Starting Frontend (React)..." -ForegroundColor Yellow
$frontendPath = "c:\Users\aruns\OneDrive\Desktop\Gopika\Project\Frontend"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$frontendPath'; npm run dev"

Write-Host ""
Write-Host "College Management System is starting up!" -ForegroundColor Green
Write-Host "Backend will be available at: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Frontend will be available at: http://localhost:5173" -ForegroundColor Cyan
Write-Host ""
Write-Host "Demo accounts:" -ForegroundColor Magenta
Write-Host "Admin: admin@college.com / admin123" -ForegroundColor White
Write-Host "Faculty: faculty1@college.com / faculty123" -ForegroundColor White
Write-Host "Student: student1@college.com / student123" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")