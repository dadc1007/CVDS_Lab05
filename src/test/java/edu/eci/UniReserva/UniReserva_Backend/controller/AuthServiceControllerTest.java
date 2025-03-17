//package edu.eci.UniReserva.UniReserva_Backend.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import edu.eci.UniReserva.UniReserva_Backend.model.User;
//import edu.eci.UniReserva.UniReserva_Backend.service.impl.AuthServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(AuthController.class)
//public class AuthServiceControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private AuthServiceImpl authServiceImpl;
//
//
//    private User validUser;
//    private User duplicateEmail;
//    private User invalidePassword;
//    private User validUser2;
//
//
//    @BeforeEach
//    void setUp() {
//        validUser = new User("1037126548", "Daniel", "email@gmail.com", "Password#123");
//        duplicateEmail = new User("1038944351", "Carlos", "email@gmail.com", "Password#456");
//        invalidePassword = new User("1038471526", "Vicente", "vicente@gmail.com", "123");
//        validUser2 = new User("103847152678", "Vicente", "vicente2@gmail.com", "Password#456");
//    }
//
//    @Test
//    void testLoginSuccess() throws Exception {
//        String validUserJson = objectMapper.writeValueAsString(validUser);
//        when(authServiceImpl.authenticateLogin(validUser.getEmail(), validUser.getPassword())).thenReturn(validUser);
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(validUserJson))
//                .andExpect(status().isOk())
//                .andExpect(content().json(validUserJson));
//    }
//
//    @Test
//    void testLoginFailure() throws Exception {
//        String email = "test@example.com";
//        String password = "wrongpassword";
//        String requestBody = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
//        when(authServiceImpl.authenticateLogin(any(), any()))
//                .thenThrow(new IllegalArgumentException("Invalid email or password"));
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("{\"error\":\"Invalid email or password\"}"));
//    }
//
//    @Test
//    void shouldCreateUser() throws Exception {
//        String validUserJson = objectMapper.writeValueAsString(validUser2);
//        when(authServiceImpl.authenticateSignUp(any(User.class))).thenReturn(validUser2);
//        mockMvc.perform(post("/auth/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(validUserJson))
//                .andExpect(status().isCreated())
//                .andExpect(content().json(validUserJson));
//    }
//
//    @Test
//    void shouldNotCreateUserWithDuplicatedEmail() throws Exception {
//        String duplicateEmailJson = objectMapper.writeValueAsString(duplicateEmail);
//
//        when(authServiceImpl.authenticateSignUp(any(User.class))).thenThrow(new IllegalArgumentException("Email already exists"));
//
//        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON)
//                .content(duplicateEmailJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("{\"error\":\"Email already exists\"}"));
//    }
//
//    @Test
//    void shouldNotCreateUserWithInvalidPassword() throws Exception {
//        String invalidPasswordJson = objectMapper.writeValueAsString(invalidePassword);
//
//        when(authServiceImpl.authenticateSignUp(any(User.class))).thenThrow(new IllegalArgumentException("Invalid password"));
//
//        mockMvc.perform(post("/auth/signup").contentType(MediaType.APPLICATION_JSON)
//                .content(invalidPasswordJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("{\"error\":\"Invalid password\"}"));
//    }
//}
