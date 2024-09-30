package com.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(SimpleController.class)
public class SimpleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testResponseCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("WELCOME"));
    }

    @Test
    public void testResponseAccepted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/resource-created"))
                .andExpect(status().isCreated())
                .andExpect(content().string("CREATED"));
    }

    @Test
    public void testGreet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/resource-accepted"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("ACCEPTED"));
    }
}
