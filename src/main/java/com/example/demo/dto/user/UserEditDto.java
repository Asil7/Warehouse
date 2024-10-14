package com.example.demo.dto.user;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDto {

    private String fullName;
    private String username;
    private String phone;
    private String salary;
    private LocalDate dateOfEmployment;
    private Long roleId;
}
