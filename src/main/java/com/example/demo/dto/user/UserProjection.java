package com.example.demo.dto.user;

import java.sql.Timestamp;

public interface UserProjection {
    Long getId();
    String getFullName();
    String getUsername();
    String getStatus();
    String getDateOfEmployment();
    String getSalary();
    String getPhone();
    String getRoleName();
    Timestamp getCreatedAt();
}
