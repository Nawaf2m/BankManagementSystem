package com.example.bankmanagementsystem.Repository;

import com.example.bankmanagementsystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User findUserById(Integer id);

}
