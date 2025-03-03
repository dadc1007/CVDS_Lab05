package edu.eci.UniReserva.UniReserva_Backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthServiceImpl authServiceImpl;

    @Test
    void testLoginSuccess() throws Exception {
        String email = "test@example.com";
        String password = "password123";
        String requestBody = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        when(authServiceImpl.authenticateLogin(email, password)).thenReturn("Login successful");
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Login successful"));
    }

    @Test
    void testLoginFailure() throws Exception {
        String email = "test@example.com";
        String password = "wrongpassword";
        String requestBody = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        when(authServiceImpl.authenticateLogin(any(), any()))
                .thenThrow(new IllegalArgumentException("Invalid email or password"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Invalid email or password"));
    }
}
