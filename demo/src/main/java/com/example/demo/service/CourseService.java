package com.example.demo.service;

import com.example.demo.dto.CourseRequest;
import com.example.demo.entity.Course;
import com.example.demo.entity.User;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findByIsActiveTrue();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(CourseRequest courseRequest) {
        if (courseRepository.existsByCourseCode(courseRequest.getCourseCode())) {
            throw new RuntimeException("Course with code " + courseRequest.getCourseCode() + " already exists");
        }

        Course course = new Course(
                courseRequest.getCourseCode(),
                courseRequest.getCourseName(),
                courseRequest.getDescription(),
                courseRequest.getCredits(),
                courseRequest.getDepartment());

        if (courseRequest.getFacultyId() != null) {
            Optional<User> faculty = userRepository.findById(courseRequest.getFacultyId());
            if (faculty.isPresent() && faculty.get().getRole() == User.Role.FACULTY) {
                course.setFaculty(faculty.get());
            } else {
                throw new RuntimeException("Faculty not found or user is not a faculty member");
            }
        }

        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        // Check if course code is being changed and if it already exists
        if (!course.getCourseCode().equals(courseRequest.getCourseCode()) &&
                courseRepository.existsByCourseCode(courseRequest.getCourseCode())) {
            throw new RuntimeException("Course with code " + courseRequest.getCourseCode() + " already exists");
        }

        course.setCourseCode(courseRequest.getCourseCode());
        course.setCourseName(courseRequest.getCourseName());
        course.setDescription(courseRequest.getDescription());
        course.setCredits(courseRequest.getCredits());
        course.setDepartment(courseRequest.getDepartment());

        if (courseRequest.getFacultyId() != null) {
            Optional<User> faculty = userRepository.findById(courseRequest.getFacultyId());
            if (faculty.isPresent() && faculty.get().getRole() == User.Role.FACULTY) {
                course.setFaculty(faculty.get());
            } else {
                throw new RuntimeException("Faculty not found or user is not a faculty member");
            }
        } else {
            course.setFaculty(null);
        }

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        course.setIsActive(false);
        courseRepository.save(course);
    }

    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }

    public List<Course> getCoursesByFaculty(Long facultyId) {
        Optional<User> faculty = userRepository.findById(facultyId);
        if (faculty.isPresent() && faculty.get().getRole() == User.Role.FACULTY) {
            return courseRepository.findByFaculty(faculty.get());
        }
        throw new RuntimeException("Faculty not found");
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByCourseNameContainingIgnoreCase(keyword);
    }
}