package com.example.demo.dto;

import com.example.demo.entity.Fee;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FeeRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Fee type is required")
    private Fee.FeeType feeType;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", message = "Amount must be positive")
    private BigDecimal amount;

    private LocalDateTime dueDate;
    private String semester;
    private String academicYear;
    private String description;

    public FeeRequest() {
    }

    public FeeRequest(Long studentId, Fee.FeeType feeType, BigDecimal amount,
            String semester, String academicYear) {
        this.studentId = studentId;
        this.feeType = feeType;
        this.amount = amount;
        this.semester = semester;
        this.academicYear = academicYear;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Fee.FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(Fee.FeeType feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}