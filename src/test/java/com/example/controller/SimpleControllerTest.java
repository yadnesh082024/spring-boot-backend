package com.example.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    private static final String BASE_URI = "/spring-app";
    private static final String STATUS_PATH = "$.status";
    private static final String MESSAGE_PATH = "$.message";
    private static final String POD_NAME_PATH = "$.podName";
    private static final String IMAGE_NAME_PATH = "$.imageName";
    private static final String TIMEZONE_PATH = "$.timezone";
    private static final String TIMESTAMP_PATH = "$.timestamp";

    @ParameterizedTest
    @CsvSource({
            "'/', 200, 'WELCOME'",
            "'/resource-created', 201, 'CREATED'",
            "'/resource-accepted', 202, 'ACCEPTED'",
            "'/resource-not-acceptable', 406, 'NOT_ACCEPTABLE'",
            "'/resource-not-implemented', 501, 'NOT_IMPLEMENTED'",
            "'/continue', 100, 'CONTINUE'"
    })
    public void testEndpoints(String uri, int expectedStatus, String expectedMessage) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + uri))
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath(STATUS_PATH).value(String.valueOf(expectedStatus)))
                .andExpect(jsonPath(MESSAGE_PATH).value(expectedMessage))
                .andExpect(jsonPath(POD_NAME_PATH).value("Unknown"))
                .andExpect(jsonPath(IMAGE_NAME_PATH).value("Unknown"))
                .andExpect(jsonPath(TIMEZONE_PATH).value("Asia/Kolkata"))
                .andExpect(jsonPath(TIMESTAMP_PATH).exists());
    }
}
