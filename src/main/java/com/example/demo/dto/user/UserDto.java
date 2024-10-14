package com.example.demo.dto.user;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String fullName;
    private String username;
    private String password;
    private String salary;
    private String phone;
    private LocalDate dateOfEmployment;
    private Long roleId;
}
