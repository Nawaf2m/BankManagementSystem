package com.example.bankmanagementsystem.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Account number can't be empty")
    @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$", message = "Account number must follow this format (XXXX-XXXX-XXXX-XXXX)")
    @Column(columnDefinition = "varchar(19)", unique = true)
    private String accountNumber;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(columnDefinition = "boolean", nullable = false)
    private Boolean isActive = false;

    @ManyToOne
    @JoinColumn
    private Customer customer;
}
