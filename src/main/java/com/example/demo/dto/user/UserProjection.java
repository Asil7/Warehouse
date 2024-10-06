package com.example.demo.dto.user;

import java.sql.Timestamp;

public interface UserProjection {
    Long getId();
    String getFullName();
    String getUsername();
    String getStatus();
    String getRoleName();
    Timestamp getCreatedAt();
}
