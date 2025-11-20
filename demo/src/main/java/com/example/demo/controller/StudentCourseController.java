package com.example.demo.controller;

import com.example.demo.entity.StudentCourse;
import com.example.demo.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enrollments")
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    @PostMapping("/enroll")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> enrollStudent(@RequestParam Long studentId, @RequestParam Long courseId) {
        try {
            StudentCourse enrollment = studentCourseService.enrollStudent(studentId, courseId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/unenroll")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> unenrollStudent(@RequestParam Long studentId, @RequestParam Long courseId) {
        try {
            studentCourseService.unenrollStudent(studentId, courseId);
            return ResponseEntity.ok().body("Student unenrolled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<List<StudentCourse>> getStudentCourses(@PathVariable Long studentId) {
        List<StudentCourse> enrollments = studentCourseService.getStudentCourses(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<List<StudentCourse>> getCourseStudents(@PathVariable Long courseId) {
        List<StudentCourse> enrollments = studentCourseService.getCourseStudents(courseId);
        return ResponseEntity.ok(enrollments);
    }

    @PutMapping("/{enrollmentId}/grade")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY')")
    public ResponseEntity<?> updateGrade(@PathVariable Long enrollmentId, @RequestParam BigDecimal grade) {
        try {
            StudentCourse enrollment = studentCourseService.updateGrade(enrollmentId, grade);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/grades")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<List<StudentCourse>> getStudentGrades(@PathVariable Long studentId) {
        List<StudentCourse> grades = studentCourseService.getStudentGrades(studentId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FACULTY') or hasRole('STUDENT')")
    public ResponseEntity<?> getEnrollment(@PathVariable Long studentId, @PathVariable Long courseId) {
        Optional<StudentCourse> enrollment = studentCourseService.getEnrollment(studentId, courseId);
        if (enrollment.isPresent()) {
            return ResponseEntity.ok(enrollment.get());
        }
        return ResponseEntity.notFound().build();
    }
}