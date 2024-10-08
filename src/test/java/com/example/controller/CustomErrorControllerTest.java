package com.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomErrorController.class)
public class CustomErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void handleErrorShouldReturnErrorMessage() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.status").value("501"))
                .andExpect(jsonPath("$.message").value("No implementation is defined for this endpoint"))
                .andExpect(jsonPath("$.podName").value("Unknown"))
                .andExpect(jsonPath("$.imageName").value("Unknown"));
    }
}
