package com.example.bankmanagementsystem.Service;

import com.example.bankmanagementsystem.Api.ApiException;
import com.example.bankmanagementsystem.DTO.CustomerRegisterDTO;
import com.example.bankmanagementsystem.Model.Customer;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Repository.AuthRepository;
import com.example.bankmanagementsystem.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;

    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    @Transactional
    public void registerCustomer(CustomerRegisterDTO customerRegisterDTO){
        User oldUser = authRepository.findUserByUsername(customerRegisterDTO.getUsername());

        if (oldUser != null){
            throw new ApiException("Username already exists");
        }

        User oldEmail = authRepository.findUserByEmail(customerRegisterDTO.getEmail());

        if (oldEmail != null){
            throw new ApiException("Email already exists");
        }

        User user = new User();
        user.setUsername(customerRegisterDTO.getUsername());
        String hash = new BCryptPasswordEncoder().encode(customerRegisterDTO.getPassword());
        user.setPassword(hash);
        user.setName(customerRegisterDTO.getName());
        user.setEmail(customerRegisterDTO.getEmail());
        user.setRole("CUSTOMER");

        Customer customer = new Customer();
        customer.setPhoneNumber(customerRegisterDTO.getPhoneNumber());
        customer.setUser(user);

        customerRepository.save(customer);
    }

    @Transactional
    public void updateCustomer(Integer userId, CustomerRegisterDTO dto){
        Customer oldCustomer = customerRepository.findCustomerByUserId(userId);

        if (oldCustomer == null){
            throw new ApiException("Customer not found");
        }

        User user = oldCustomer.getUser();

        User oldUsername = authRepository.findUserByUsername(dto.getUsername());

        if (oldUsername != null && !oldUsername.getId().equals(userId)){
            throw new ApiException("Username already exists");
        }

        User oldEmail = authRepository.findUserByEmail(dto.getEmail());

        if (oldEmail != null && !oldEmail.getId().equals(userId)){
            throw new ApiException("Email already exists");
        }

        user.setUsername(dto.getUsername());
        String hash = new BCryptPasswordEncoder().encode(dto.getPassword());
        user.setPassword(hash);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        oldCustomer.setPhoneNumber(dto.getPhoneNumber());

        authRepository.save(user);
        customerRepository.save(oldCustomer);
    }

    @Transactional
    public void deleteCustomer(Integer id){
        Customer customer = customerRepository.findCustomerById(id);

        if (customer == null){
            throw new ApiException("Customer not found");
        }

        if (!customer.getAccounts().isEmpty()){
            throw new ApiException("Cannot delete customer with existing accounts");
        }

        User user = customer.getUser();

        customerRepository.delete(customer);
        authRepository.delete(user);
    }
}