package com.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SimpleController.class)
public class SimpleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String baseUri = "/spring-app";

    @Test
    public void testGreet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("WELCOME"));
    }

    @Test
    public void testResponseCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/resource-created"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201"))
                .andExpect(jsonPath("$.message").value("CREATED"));
    }

    @Test
    public void testResponseAccepted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/resource-accepted"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("202"))
                .andExpect(jsonPath("$.message").value("ACCEPTED"));
    }

    @Test
    public void testResponseNotAcceptable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/resource-not-acceptable"))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.status").value("406"))
                .andExpect(jsonPath("$.message").value("NOT_ACCEPTABLE"));
    }

    @Test
    public void testResponseNotImplemented() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/resource-not-implemented"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.status").value("501"))
                .andExpect(jsonPath("$.message").value("NOT_IMPLEMENTED"));
    }

    @Test
    public void testResponseContinue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUri + "/continue"))
                .andExpect(status().isContinue())
                .andExpect(jsonPath("$.status").value("100"))
                .andExpect(jsonPath("$.message").value("CONTINUE"));
    }
}
