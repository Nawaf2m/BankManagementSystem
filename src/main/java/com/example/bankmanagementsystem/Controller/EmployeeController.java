package com.example.bankmanagementsystem.Controller;

import com.example.bankmanagementsystem.Api.ApiResponse;
import com.example.bankmanagementsystem.DTO.EmployeeRegisterDTO;
import com.example.bankmanagementsystem.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employeeService.getEmployees());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@RequestBody @Valid EmployeeRegisterDTO employeeRegisterDTO){
        employeeService.registerEmployee(employeeRegisterDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Employee registered successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id, @RequestBody @Valid EmployeeRegisterDTO employeeRegisterDTO){
        employeeService.updateEmployee(id, employeeRegisterDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Employee updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully"));
    }
}