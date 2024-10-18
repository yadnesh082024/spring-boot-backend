package com.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String status;
    private String message;
    private String podName;
    private String imageName;
    private String timezone = "Asia/Kolkata";
    private String timestamp;


    public ApiResponse(String status, String message, String podName,  String imageName) {
        this.status = status;
        this.message = message;
        this.podName = podName;
        this.imageName = imageName;
        this.timestamp = this.getTimestamp();

    }

    public String getTimestamp() {
        // Get current timestamp in India timezone
        ZonedDateTime indiaTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

        // Define the desired format including milliseconds
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS");

        // Format the timestamp
        return indiaTime.format(formatter);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";  // Return an empty JSON object on error
        }
    }
}
