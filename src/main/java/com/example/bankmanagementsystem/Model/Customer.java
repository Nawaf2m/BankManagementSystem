package com.example.bankmanagementsystem.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(10)", nullable = false, unique = true)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    private User user;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private Set<Account> accounts;
}
