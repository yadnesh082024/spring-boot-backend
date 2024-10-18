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
    private final String podName;
    private final String imageName;

    public SimpleController() {
        this.podName = System.getenv("POD_NAME") != null ? System.getenv("POD_NAME") : "Unknown";
        this.imageName = System.getenv("IMAGE_NAME") != null ? System.getenv("IMAGE_NAME") : "Unknown";
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> greet() {
        logger.info("Greeting endpoint called from pod: {} and image: {}", podName, imageName);
        ApiResponse response = new ApiResponse("200", "WELCOME", podName, imageName);
        logger.info("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resource-created")
    public ResponseEntity<ApiResponse> responseCreated() {
        logger.info("Resource created endpoint called from pod: {} and image: {}", podName, imageName);
        ApiResponse response = new ApiResponse("201", "CREATED", podName, imageName);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/resource-accepted")
    public ResponseEntity<ApiResponse> responseAccepted() {
        logger.info("Resource accepted endpoint called from pod: {} and image: {}", podName, imageName);
        ApiResponse response = new ApiResponse("202", "ACCEPTED", podName, imageName);
        logger.info("Response: {}", response);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/resource-not-acceptable")
    public ResponseEntity<ApiResponse> responseNotAcceptable() {
        logger.info("Resource Not Acceptable endpoint called from pod: {} and image: {}", podName, imageName);
        ApiResponse response = new ApiResponse("406", "NOT_ACCEPTABLE", podName, imageName);
        logger.info("Response: {}", response);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @GetMapping("/resource-not-implemented")
    public ResponseEntity<ApiResponse> responseNotImplemented() {
        logger.info("Resource Not Implemented endpoint called from pod: {} and image: {}", podName, imageName);
        ApiResponse response = new ApiResponse("501", "NOT_IMPLEMENTED", podName, imageName);
        logger.info("Response: {}", response);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }

    @GetMapping("/continue")
    public ResponseEntity<ApiResponse> responseContinue() {
        logger.info("Continue endpoint called from pod: {} and image: {}", podName, imageName);
        ApiResponse response = new ApiResponse("100", "CONTINUE", podName, imageName);
        logger.info("Response: {}", response);
        return ResponseEntity.status(HttpStatus.CONTINUE).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> responseTest() {
        logger.info("test endpoint called from pod: {} and image: {}", podName, imageName);
        ApiResponse response = new ApiResponse("200", "TEST", podName, imageName);
        logger.info("Response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
