package com.example.UserAuthentication.controller;

import com.example.UserAuthentication.dto.LoginDTO;
import com.example.UserAuthentication.dto.LoginResponse;
import com.example.UserAuthentication.dto.SignupDTO;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

//    @BeforeEach
//    void setUp() {
//        // Setup your mock behavior
//        when(userService.registerNewUser(anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(new LoginResponse(true, "token", "Success")); // assume User is the expected return type
//        when(userService.login(anyString(), anyString()))
//            .thenReturn(new LoginResponse(true, "token", "Success"));
//    }

    @Test
    public void testSignup() throws Exception {
        SignupDTO signupDTO = new SignupDTO("testuser", "password", "testuser@example.com", "1234567890");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO("testuser", "password");

        // Make sure the user exists before trying to login
        userService.registerNewUser("testuser", "password", "testuser@example.com", "1234567890");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }

    // Add more tests for other scenarios
}

