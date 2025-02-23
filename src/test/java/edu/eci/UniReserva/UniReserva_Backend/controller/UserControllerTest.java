package edu.eci.UniReserva.UniReserva_Backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private User validUser;
    private User duplicateEmail;
    private User invalidePassword;

    @BeforeEach
    void setUp() {
        validUser = new User("1037126548", "Daniel", "email@gmail.com", "Password#123");
        duplicateEmail = new User("1038944351", "Carlos", "email@gmail.com", "Password#456");
        invalidePassword = new User("1038471526", "Vicente", "vicente@gmail.com", "123");
    }

    @Test
    void shouldCreateUser() throws Exception {
        String validUserJson = objectMapper.writeValueAsString(validUser);

        when(userService.createUser(any(User.class))).thenReturn("User created successfully!");

        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON)
                .content(validUserJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User created successfully!"));
    }

    @Test
    void shouldNotCreateUserWithDuplicatedEmail() throws Exception {
        String duplicateEmailJson = objectMapper.writeValueAsString(duplicateEmail);

        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON)
                .content(duplicateEmailJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }

    @Test
    void shouldNotCreateUserWithInvalidPassword() throws Exception {
        String invalidPasswordJson = objectMapper.writeValueAsString(invalidePassword);

        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("Invalid password"));

        mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON)
                .content(invalidPasswordJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid password"));
    }
}