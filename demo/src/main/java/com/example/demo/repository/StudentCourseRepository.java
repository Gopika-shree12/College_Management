package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.StudentCourse;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    List<StudentCourse> findByStudent(User student);

    List<StudentCourse> findByCourse(Course course);

    Optional<StudentCourse> findByStudentAndCourse(User student, Course course);

    List<StudentCourse> findByStatus(StudentCourse.EnrollmentStatus status);

    @Query("SELECT sc FROM StudentCourse sc WHERE sc.student.id = :studentId")
    List<StudentCourse> findByStudentId(Long studentId);

    @Query("SELECT sc FROM StudentCourse sc WHERE sc.course.id = :courseId")
    List<StudentCourse> findByCourseId(Long courseId);

    @Query("SELECT sc FROM StudentCourse sc WHERE sc.student.id = :studentId AND sc.grade IS NOT NULL")
    List<StudentCourse> findCompletedCoursesByStudentId(Long studentId);
}