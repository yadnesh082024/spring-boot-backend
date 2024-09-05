package com.example.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ResponseEntity<String> handleError() {
        return new ResponseEntity<>("ERROR : NOT FOUND", HttpStatus.NOT_FOUND);
    }

    public String getErrorPath() {
        return "/error";
    }
}