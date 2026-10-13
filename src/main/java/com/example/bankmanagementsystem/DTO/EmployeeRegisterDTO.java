package com.example.bankmanagementsystem.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRegisterDTO {

    @NotEmpty(message = "Username can't be empty")
    @Length(min = 4, max = 10, message = "Username length must be between 4 and 10 characters")
    private String username;

    @NotEmpty(message = "Password can't be empty")
    @Length(min = 6, message = "Password length must be at least 6 characters")
    private String password;

    @NotEmpty(message = "Name can't be empty")
    @Length(min = 2, max = 20, message = "Name length must be between 2 and 20 characters")
    private String name;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Wrong email format")
    private String email;

    @NotBlank(message = "Position can't be empty")
    @Length(max = 20, message = "Position can't be more than 20 characters")
    private String position;

    @NotNull(message = "Salary can't be null")
    @PositiveOrZero(message = "Salary can't be negative")
    private BigDecimal salary;
}