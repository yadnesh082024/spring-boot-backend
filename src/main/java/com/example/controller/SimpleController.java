package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @GetMapping("/")
    public ResponseEntity<String> greet() {
        return  new ResponseEntity<>("WELCOME", HttpStatus.ACCEPTED);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> greetHello() {
        return  new ResponseEntity<>("HELLO", HttpStatus.ACCEPTED);
    }
}
