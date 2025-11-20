package com.example.demo.controller;

import com.example.demo.dto.FeeRequest;
import com.example.demo.entity.Fee;
import com.example.demo.service.FeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fees")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFee(@Valid @RequestBody FeeRequest feeRequest) {
        try {
            Fee fee = feeService.createFee(feeRequest);
            return ResponseEntity.ok(fee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Fee>> getAllFees() {
        List<Fee> fees = feeService.getAllFees();
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> getFeeById(@PathVariable Long id) {
        Optional<Fee> fee = feeService.getFeeById(id);
        if (fee.isPresent()) {
            return ResponseEntity.ok(fee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<List<Fee>> getStudentFees(@PathVariable Long studentId) {
        List<Fee> fees = feeService.getStudentFees(studentId);
        return ResponseEntity.ok(fees);
    }

    @PostMapping("/{feeId}/pay")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> payFee(@PathVariable Long feeId, @RequestParam BigDecimal amount) {
        try {
            Fee fee = feeService.payFee(feeId, amount);
            return ResponseEntity.ok(fee);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Fee>> getFeesByStatus(@PathVariable Fee.PaymentStatus status) {
        List<Fee> fees = feeService.getFeesByStatus(status);
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/semester/{semester}/year/{academicYear}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Fee>> getFeesBySemester(@PathVariable String semester,
            @PathVariable String academicYear) {
        List<Fee> fees = feeService.getFeesBySemester(semester, academicYear);
        return ResponseEntity.ok(fees);
    }

    @GetMapping("/student/{studentId}/total")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<?> getStudentFeesSummary(@PathVariable Long studentId) {
        try {
            BigDecimal totalFees = feeService.getTotalFeesByStudent(studentId);
            BigDecimal totalPaid = feeService.getTotalPaidByStudent(studentId);
            BigDecimal outstanding = feeService.getOutstandingFeesByStudent(studentId);

            return ResponseEntity.ok(new FeesSummary(totalFees, totalPaid, outstanding));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/pending")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<List<Fee>> getPendingFeesByStudent(@PathVariable Long studentId) {
        List<Fee> fees = feeService.getPendingFeesByStudent(studentId);
        return ResponseEntity.ok(fees);
    }

    // Helper class for fees summary
    public static class FeesSummary {
        private BigDecimal totalFees;
        private BigDecimal totalPaid;
        private BigDecimal outstanding;

        public FeesSummary(BigDecimal totalFees, BigDecimal totalPaid, BigDecimal outstanding) {
            this.totalFees = totalFees;
            this.totalPaid = totalPaid;
            this.outstanding = outstanding;
        }

        public BigDecimal getTotalFees() {
            return totalFees;
        }

        public BigDecimal getTotalPaid() {
            return totalPaid;
        }

        public BigDecimal getOutstanding() {
            return outstanding;
        }
    }
}