package com.platformcommons.employeemanagement.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the employee", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Full name of the employee", example = "Mriganka Mondal")
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    @Schema(description = "Date of birth of the employee", example = "2000-10-18")
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Gender of the employee", example = "MALE")
    private Gender gender;

    @Column(name = "employee_code", unique = true, nullable = false)
    @Schema(description = "Unique employee code", example = "EMP001")
    private String employeeCode;

    @Schema(description = "Email address of the employee", example = "mrigankamondal10@gmail.com")
    private String email;

    @Column(name = "mobile_number")
    @Schema(description = "Mobile number of the employee", example = "+917596943998")
    private String mobileNumber;

    @Column(name = "emergency_contact")
    @Schema(description = "Emergency contact information", example = "Mriganka Mondal +9876543210")
    private String emergencyContact;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of addresses associated with the employee")
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "employee_department",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    @Schema(description = "Departments the employee belongs to")
    private Set<Department> departments = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods to manage bidirectional relationships
    public void addAddress(Address address) {
        addresses.add(address);
        address.setEmployee(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setEmployee(null);
    }

    public void addDepartment(Department department) {
        departments.add(department);
        department.getEmployees().add(this);
    }

    public void removeDepartment(Department department) {
        departments.remove(department);
        department.getEmployees().remove(this);
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
