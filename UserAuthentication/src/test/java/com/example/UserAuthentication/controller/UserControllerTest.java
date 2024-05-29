package com.example.UserAuthentication.controller;

import com.example.UserAuthentication.dto.ApiResponse;
import com.example.UserAuthentication.dto.EditUserDTO;
import com.example.UserAuthentication.dto.UserInfoDTO;
import com.example.UserAuthentication.enums.UserRoles;
import com.example.UserAuthentication.model.User;
import com.example.UserAuthentication.service.UserService;
import com.example.UserAuthentication.utility.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        // Setup common mock behaviors here
        // For example, authentication token handling
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("testUser");
        when(jwtUtil.validateToken(anyString())).thenReturn(true);
    }

//    @Test
//    void testGetUserInfo() throws Exception {
//        User user = new User(); // Setup user details
//        user.setUsername("testUser");
//        user.setEmail("test@example.com");
//        user.setPhoneNumber("1234567890");
//        // Assume UserRole is an enum
//        user.setRole(UserRoles.USER);
//
//        when(userService.findByUsername("testUser")).thenReturn(user);
//
//        mockMvc.perform(get("/api/user/get-user-info")
//                        .cookie(new Cookie("jwt", "token")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.username").value("testUser"));
//    }
//
//    @Test
//    void testUpdateUser() throws Exception {
//        EditUserDTO editUserDTO = new EditUserDTO("testUser", "newUser", "new@example.com", "1234567890", "USER");
//        when(userService.editUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
//                .thenReturn(new ApiResponse<>(true, "User updated successfully", null));
//        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("testUser");
//        when(jwtUtil.getRoleFromToken(anyString())).thenReturn("ADMIN");
//        mockMvc.perform(post("/api/user/update-user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(editUserDTO))
//                        .cookie(new Cookie("jwt", "valid-token")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("User updated successfully"));
//    }

//    @Test
//    void testGetUsers() throws Exception {
//        when(jwtUtil.getRoleFromToken("valid-token")).thenReturn("ADMIN");
//        when(jwtUtil.validateToken("valid-token")).thenReturn(true);
//
//        // Prepare the response from the service
//        ApiResponse<List<UserInfoDTO>> apiResponse = new ApiResponse<>(true, "Success", List.of(new UserInfoDTO("testUser", "test@example.com", "1234567890", "USER")));
//        when(userService.getAllUsers("ADMIN")).thenReturn((ApiResponse) apiResponse);
//
//        mockMvc.perform(get("/api/user/users")
//                        .cookie(new Cookie("jwt", "valid-token")))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].username").value("testUser"));
//
//    }
}

//when(userService.deleteUser(anyString(), anyString(), anyString()))
//        .thenReturn((ApiResponse) new ApiResponse<>(true, "User deleted successfully", Boolean.FALSE));