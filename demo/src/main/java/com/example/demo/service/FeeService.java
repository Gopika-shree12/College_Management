package com.example.demo.service;

import com.example.demo.dto.FeeRequest;
import com.example.demo.entity.Fee;
import com.example.demo.entity.User;
import com.example.demo.repository.FeeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeeService {

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    private UserRepository userRepository;

    public Fee createFee(FeeRequest feeRequest) {
        Optional<User> student = userRepository.findById(feeRequest.getStudentId());
        
        if (student.isEmpty() || student.get().getRole() != User.Role.STUDENT) {
            throw new RuntimeException("Student not found");
        }

        Fee fee = new Fee(
                student.get(),
                feeRequest.getFeeType(),
                feeRequest.getAmount(),
                feeRequest.getSemester(),
                feeRequest.getAcademicYear()
        );

        fee.setDueDate(feeRequest.getDueDate());
        fee.setDescription(feeRequest.getDescription());

        return feeRepository.save(fee);
    }

    public List<Fee> getStudentFees(Long studentId) {
        return feeRepository.findByStudentId(studentId);
    }

    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }

    public Optional<Fee> getFeeById(Long id) {
        return feeRepository.findById(id);
    }

    public Fee payFee(Long feeId, BigDecimal amount) {
        Fee fee = feeRepository.findById(feeId)
                .orElseThrow(() -> new RuntimeException("Fee not found"));

        BigDecimal newPaidAmount = fee.getPaidAmount().add(amount);
        
        if (newPaidAmount.compareTo(fee.getAmount()) > 0) {
            throw new RuntimeException("Payment amount exceeds total fee amount");
        }

        fee.setPaidAmount(newPaidAmount);
        fee.setPaidDate(LocalDateTime.now());

        // Update payment status
        if (newPaidAmount.equals(fee.getAmount())) {
            fee.setPaymentStatus(Fee.PaymentStatus.PAID);
        } else if (newPaidAmount.compareTo(BigDecimal.ZERO) > 0) {
            fee.setPaymentStatus(Fee.PaymentStatus.PARTIAL);
        }

        return feeRepository.save(fee);
    }

    public List<Fee> getFeesByStatus(Fee.PaymentStatus status) {
        return feeRepository.findByPaymentStatus(status);
    }

    public List<Fee> getFeesBySemester(String semester, String academicYear) {
        return feeRepository.findBySemesterAndAcademicYear(semester, academicYear);
    }

    public BigDecimal getTotalFeesByStudent(Long studentId) {
        BigDecimal total = feeRepository.findTotalAmountByStudentId(studentId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalPaidByStudent(Long studentId) {
        BigDecimal total = feeRepository.findTotalPaidAmountByStudentId(studentId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getOutstandingFeesByStudent(Long studentId) {
        BigDecimal totalFees = getTotalFeesByStudent(studentId);
        BigDecimal totalPaid = getTotalPaidByStudent(studentId);
        return totalFees.subtract(totalPaid);
    }

    public List<Fee> getPendingFeesByStudent(Long studentId) {
        return feeRepository.findByStudentIdAndPaymentStatus(studentId, Fee.PaymentStatus.PENDING);
    }
}