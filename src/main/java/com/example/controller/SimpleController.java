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
    public ResponseEntity<String> greet() {
        logger.info("Greeting endpoint called");
        return ResponseEntity.ok("WELCOME");
    }

    @GetMapping("/resource-created")
    public ResponseEntity<String> responseCreated() {
        logger.info("Resource created endpoint called");
        return ResponseEntity.status(HttpStatus.CREATED).body("CREATED");
    }

    @GetMapping("/resource-accepted")
    public ResponseEntity<String> responseAccepted() {
        logger.info("Resource accepted endpoint called");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("ACCEPTED");
    }

    @GetMapping("/resource-not-acceptable")
    public ResponseEntity<String> responseNotAcceptable() {
        logger.info("Resource Not Acceptable endpoint called");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("NOT_ACCEPTABLE");
    }

    @GetMapping("/resource-not-acceptable")
    public ResponseEntity<String> responseNotImplemented() {
        logger.info("Resource Not Implemented endpoint called");
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("NOT_IMPLEMENTED");
    }
}


