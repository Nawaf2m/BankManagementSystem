package com.example.bankmanagementsystem.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterDTO {

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

    @NotEmpty(message = "Phone number can't be empty")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and contain exactly 10 digits")
    private String phoneNumber;
}