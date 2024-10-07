package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring-app")
public class SimpleController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    @GetMapping("/")
    public ResponseEntity<ApiResponse> greet() {
        logger.info("Greeting endpoint called");
        ApiResponse response = new ApiResponse("200", "WELCOME");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resource-created")
    public ResponseEntity<ApiResponse> responseCreated() {
        logger.info("Resource created endpoint called");
        ApiResponse response = new ApiResponse("201", "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/resource-accepted")
    public ResponseEntity<ApiResponse> responseAccepted() {
        logger.info("Resource accepted endpoint called");
        ApiResponse response = new ApiResponse("202", "ACCEPTED");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/resource-not-acceptable")
    public ResponseEntity<ApiResponse> responseNotAcceptable() {
        logger.info("Resource Not Acceptable endpoint called");
        ApiResponse response = new ApiResponse("406", "NOT_ACCEPTABLE");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @GetMapping("/resource-not-implemented")
    public ResponseEntity<ApiResponse> responseNotImplemented() {
        logger.info("Resource Not Implemented endpoint called");
        ApiResponse response = new ApiResponse("501", "NOT_IMPLEMENTED");
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }
}