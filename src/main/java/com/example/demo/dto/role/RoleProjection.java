package com.example.demo.dto.role;

import java.sql.Timestamp;

public interface RoleProjection {

    Long getId();
            
    String getName();

    String getDescription();

    Timestamp getCreatedAt();
}
