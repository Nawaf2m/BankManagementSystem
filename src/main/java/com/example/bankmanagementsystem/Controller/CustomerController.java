package com.example.bankmanagementsystem.Controller;

import com.example.bankmanagementsystem.Api.ApiResponse;
import com.example.bankmanagementsystem.DTO.CustomerRegisterDTO;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<?> getCustomers(){
        return ResponseEntity.status(200).body(customerService.getCustomers());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid CustomerRegisterDTO customerRegisterDTO){
        customerService.registerCustomer(customerRegisterDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Customer registered successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(@AuthenticationPrincipal User user, @RequestBody @Valid CustomerRegisterDTO dto){
        customerService.updateCustomer(user.getId(), dto);
        return ResponseEntity.status(200).body(new ApiResponse("Customer updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id){
        customerService.deleteCustomer(id);
        return ResponseEntity.status(200).body(new ApiResponse("Customer deleted successfully"));
    }
}