package com.example.demo.repository;

import com.example.demo.entity.Fee;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    List<Fee> findByStudent(User student);

    List<Fee> findByStudentId(Long studentId);

    List<Fee> findByPaymentStatus(Fee.PaymentStatus paymentStatus);

    List<Fee> findByFeeType(Fee.FeeType feeType);

    List<Fee> findBySemesterAndAcademicYear(String semester, String academicYear);

    @Query("SELECT SUM(f.amount) FROM Fee f WHERE f.student.id = :studentId")
    BigDecimal findTotalAmountByStudentId(Long studentId);

    @Query("SELECT SUM(f.paidAmount) FROM Fee f WHERE f.student.id = :studentId")
    BigDecimal findTotalPaidAmountByStudentId(Long studentId);

    @Query("SELECT f FROM Fee f WHERE f.student.id = :studentId AND f.paymentStatus = :status")
    List<Fee> findByStudentIdAndPaymentStatus(Long studentId, Fee.PaymentStatus status);
}