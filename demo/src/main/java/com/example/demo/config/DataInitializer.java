package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.entity.Course;
import com.example.demo.entity.Fee;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.FeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user
        if (!userRepository.existsByEmail("admin@college.com")) {
            User admin = new User(
                    "admin@college.com",
                    passwordEncoder.encode("admin123"),
                    "Admin",
                    "User",
                    User.Role.ADMIN);
            userRepository.save(admin);
        }

        // Create faculty users
        if (!userRepository.existsByEmail("faculty1@college.com")) {
            User faculty1 = new User(
                    "faculty1@college.com",
                    passwordEncoder.encode("faculty123"),
                    "John",
                    "Smith",
                    User.Role.FACULTY);
            userRepository.save(faculty1);
        }

        if (!userRepository.existsByEmail("faculty2@college.com")) {
            User faculty2 = new User(
                    "faculty2@college.com",
                    passwordEncoder.encode("faculty123"),
                    "Jane",
                    "Doe",
                    User.Role.FACULTY);
            userRepository.save(faculty2);
        }

        // Create student users
        if (!userRepository.existsByEmail("student1@college.com")) {
            User student1 = new User(
                    "student1@college.com",
                    passwordEncoder.encode("student123"),
                    "Alice",
                    "Johnson",
                    User.Role.STUDENT);
            userRepository.save(student1);
        }

        if (!userRepository.existsByEmail("student2@college.com")) {
            User student2 = new User(
                    "student2@college.com",
                    passwordEncoder.encode("student123"),
                    "Bob",
                    "Wilson",
                    User.Role.STUDENT);
            userRepository.save(student2);
        }

        // Create sample courses
        if (!courseRepository.existsByCourseCode("CS101")) {
            User faculty1 = userRepository.findByEmail("faculty1@college.com").orElse(null);
            Course course1 = new Course(
                    "CS101",
                    "Introduction to Computer Science",
                    "Basic concepts of computer science and programming",
                    3,
                    "Computer Science");
            if (faculty1 != null) {
                course1.setFaculty(faculty1);
            }
            courseRepository.save(course1);
        }

        if (!courseRepository.existsByCourseCode("MATH101")) {
            User faculty2 = userRepository.findByEmail("faculty2@college.com").orElse(null);
            Course course2 = new Course(
                    "MATH101",
                    "Calculus I",
                    "Differential and integral calculus",
                    4,
                    "Mathematics");
            if (faculty2 != null) {
                course2.setFaculty(faculty2);
            }
            courseRepository.save(course2);
        }

        if (!courseRepository.existsByCourseCode("ENG101")) {
            Course course3 = new Course(
                    "ENG101",
                    "English Composition",
                    "Academic writing and composition",
                    3,
                    "English");
            courseRepository.save(course3);
        }

        // Create sample fees
        User student1 = userRepository.findByEmail("student1@college.com").orElse(null);
        User student2 = userRepository.findByEmail("student2@college.com").orElse(null);

        if (student1 != null) {
            // Check if fee already exists for this student
            if (feeRepository.findByStudentId(student1.getId()).isEmpty()) {
                Fee tuitionFee = new Fee(
                        student1,
                        Fee.FeeType.TUITION,
                        new BigDecimal("5000.00"),
                        "Fall 2024",
                        "2024-25");
                tuitionFee.setDueDate(LocalDateTime.now().plusDays(30));
                tuitionFee.setDescription("Fall semester tuition fee");
                feeRepository.save(tuitionFee);

                Fee libraryFee = new Fee(
                        student1,
                        Fee.FeeType.LIBRARY,
                        new BigDecimal("200.00"),
                        "Fall 2024",
                        "2024-25");
                libraryFee.setDueDate(LocalDateTime.now().plusDays(30));
                libraryFee.setDescription("Library access fee");
                feeRepository.save(libraryFee);
            }
        }

        if (student2 != null) {
            // Check if fee already exists for this student
            if (feeRepository.findByStudentId(student2.getId()).isEmpty()) {
                Fee tuitionFee = new Fee(
                        student2,
                        Fee.FeeType.TUITION,
                        new BigDecimal("5000.00"),
                        "Fall 2024",
                        "2024-25");
                tuitionFee.setDueDate(LocalDateTime.now().plusDays(30));
                tuitionFee.setDescription("Fall semester tuition fee");
                feeRepository.save(tuitionFee);

                Fee labFee = new Fee(
                        student2,
                        Fee.FeeType.LABORATORY,
                        new BigDecimal("300.00"),
                        "Fall 2024",
                        "2024-25");
                labFee.setDueDate(LocalDateTime.now().plusDays(30));
                labFee.setDescription("Laboratory usage fee");
                feeRepository.save(labFee);
            }
        }

        System.out.println("Sample data initialized!");
    }
}