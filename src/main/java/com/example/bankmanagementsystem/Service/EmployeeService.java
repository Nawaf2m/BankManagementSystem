package com.example.bankmanagementsystem.Service;

import com.example.bankmanagementsystem.Api.ApiException;
import com.example.bankmanagementsystem.DTO.EmployeeRegisterDTO;
import com.example.bankmanagementsystem.Model.Employee;
import com.example.bankmanagementsystem.Model.User;
import com.example.bankmanagementsystem.Repository.AuthRepository;
import com.example.bankmanagementsystem.Repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AuthRepository authRepository;

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    @Transactional
    public void registerEmployee(EmployeeRegisterDTO employeeRegisterDTO){
        User oldUser = authRepository.findUserByUsername(employeeRegisterDTO.getUsername());

        if (oldUser != null){
            throw new ApiException("Username already exists");
        }

        User oldEmail = authRepository.findUserByEmail(employeeRegisterDTO.getEmail());

        if (oldEmail != null){
            throw new ApiException("Email already exists");
        }

        User user = new User();
        user.setUsername(employeeRegisterDTO.getUsername());
        String hash = new BCryptPasswordEncoder().encode(employeeRegisterDTO.getPassword());
        user.setPassword(hash);
        user.setName(employeeRegisterDTO.getName());
        user.setEmail(employeeRegisterDTO.getEmail());
        user.setRole("EMPLOYEE");

        Employee employee = new Employee();
        employee.setPosition(employeeRegisterDTO.getPosition());
        employee.setSalary(employeeRegisterDTO.getSalary());
        employee.setUser(user);

        employeeRepository.save(employee);
    }

    @Transactional
    public void updateEmployee(Integer id, EmployeeRegisterDTO dto){
        Employee oldEmployee = employeeRepository.findEmployeeById(id);

        if (oldEmployee == null){
            throw new ApiException("Employee not found");
        }

        User user = oldEmployee.getUser();

        User oldUsername = authRepository.findUserByUsername(dto.getUsername());

        if (oldUsername != null && !oldUsername.getId().equals(user.getId())){
            throw new ApiException("Username already exists");
        }

        User oldEmail = authRepository.findUserByEmail(dto.getEmail());

        if (oldEmail != null && !oldEmail.getId().equals(user.getId())){
            throw new ApiException("Email already exists");
        }

        user.setUsername(dto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        oldEmployee.setPosition(dto.getPosition());
        oldEmployee.setSalary(dto.getSalary());

        authRepository.save(user);
        employeeRepository.save(oldEmployee);
    }

    @Transactional
    public void deleteEmployee(Integer id){
        Employee employee = employeeRepository.findEmployeeById(id);

        if (employee == null){
            throw new ApiException("Employee not found");
        }

        User user = employee.getUser();

        employeeRepository.delete(employee);
        authRepository.delete(user);
    }
}