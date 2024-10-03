package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/spring-app")
public class SimpleController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> greet() {
        logger.info("Greeting endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("status", "200");
        response.put("message", "WELCOME");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resource-created")
    public ResponseEntity<Map<String, String>> responseCreated() {
        logger.info("Resource created endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("status", "201");
        response.put("message", "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/resource-accepted")
    public ResponseEntity<Map<String, String>> responseAccepted() {
        logger.info("Resource accepted endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("status", "202");
        response.put("message", "ACCEPTED");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/resource-not-acceptable")
    public ResponseEntity<Map<String, String>> responseNotAcceptable() {
        logger.info("Resource Not Acceptable endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("status", "406");
        response.put("message", "NOT_ACCEPTABLE");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @GetMapping("/resource-not-implemented")
    public ResponseEntity<Map<String, String>> responseNotImplemented() {
        logger.info("Resource Not Implemented endpoint called");
        Map<String, String> response = new HashMap<>();
        response.put("status", "501");
        response.put("message", "NOT_IMPLEMENTED");
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }
}
