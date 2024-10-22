package com.example.demo.dto.order;
import java.sql.Timestamp;

public interface OrderProjection {
    Long getId();
    String getCompany();
    String getUsername();
    String getLocation();
    String getLocationMap();
    double getTotalWeight();
    String getPhone();
    boolean getDelivered();
    Timestamp getCreatedAt();
}
