package com.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @GetMapping("/error")
    public ResponseEntity<ApiResponse> handleError(HttpServletRequest request) {
        String uri = request.getRequestURI();
        logger.info("Unmapped endpoint called: " + uri);
        ApiResponse response = new ApiResponse("501", "No implementation is defined for this endpoint");
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }
}
