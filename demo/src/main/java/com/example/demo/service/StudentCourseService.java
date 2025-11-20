package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.StudentCourse;
import com.example.demo.entity.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentCourseRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class StudentCourseService {

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public StudentCourse enrollStudent(Long studentId, Long courseId) {
        Optional<User> student = userRepository.findById(studentId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (student.isEmpty() || student.get().getRole() != User.Role.STUDENT) {
            throw new RuntimeException("Student not found");
        }

        if (course.isEmpty()) {
            throw new RuntimeException("Course not found");
        }

        // Check if student is already enrolled
        Optional<StudentCourse> existingEnrollment = studentCourseRepository
                .findByStudentAndCourse(student.get(), course.get());

        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        StudentCourse studentCourse = new StudentCourse(student.get(), course.get());
        return studentCourseRepository.save(studentCourse);
    }

    public void unenrollStudent(Long studentId, Long courseId) {
        Optional<User> student = userRepository.findById(studentId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (student.isEmpty() || course.isEmpty()) {
            throw new RuntimeException("Student or Course not found");
        }

        Optional<StudentCourse> enrollment = studentCourseRepository
                .findByStudentAndCourse(student.get(), course.get());

        if (enrollment.isEmpty()) {
            throw new RuntimeException("Student is not enrolled in this course");
        }

        StudentCourse studentCourse = enrollment.get();
        studentCourse.setStatus(StudentCourse.EnrollmentStatus.WITHDRAWN);
        studentCourseRepository.save(studentCourse);
    }

    public List<StudentCourse> getStudentCourses(Long studentId) {
        return studentCourseRepository.findByStudentId(studentId);
    }

    public List<StudentCourse> getCourseStudents(Long courseId) {
        return studentCourseRepository.findByCourseId(courseId);
    }

    public StudentCourse updateGrade(Long enrollmentId, BigDecimal grade) {
        StudentCourse studentCourse = studentCourseRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        studentCourse.setGrade(grade);
        if (grade != null) {
            studentCourse.setStatus(StudentCourse.EnrollmentStatus.COMPLETED);
        }

        return studentCourseRepository.save(studentCourse);
    }

    public List<StudentCourse> getStudentGrades(Long studentId) {
        return studentCourseRepository.findCompletedCoursesByStudentId(studentId);
    }

    public Optional<StudentCourse> getEnrollment(Long studentId, Long courseId) {
        Optional<User> student = userRepository.findById(studentId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (student.isEmpty() || course.isEmpty()) {
            return Optional.empty();
        }

        return studentCourseRepository.findByStudentAndCourse(student.get(), course.get());
    }
}