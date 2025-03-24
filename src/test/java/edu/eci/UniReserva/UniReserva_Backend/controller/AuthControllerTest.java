package edu.eci.UniReserva.UniReserva_Backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.LoginUserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.RegisterUserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.Role;
import edu.eci.UniReserva.UniReserva_Backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private AuthService authService;

  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userDto = new UserDto("1", "John Doe", "john@example.com", null, Role.PROFESOR);
  }

  @Test
  void shouldAuthenticateValidUser() throws Exception {
    LoginUserDto loginRequest = new LoginUserDto("john@example.com", "password123");

    ApiResponse<UserDto> response =
        ApiResponse.<UserDto>builder()
            .status("success")
            .message("Login successful")
            .data(userDto)
            .token("fake-jwt-token")
            .build();

    Mockito.when(authService.authenticateLogin(any(LoginUserDto.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("Login successful"))
        .andExpect(jsonPath("$.data.id").value("1"))
        .andExpect(jsonPath("$.data.rol").value("PROFESOR"))
        .andExpect(jsonPath("$.token").value("fake-jwt-token"));
  }

  @Test
  void shouldRejectInvalidUser() throws Exception {
    LoginUserDto loginRequest = new LoginUserDto("wrong@example.com", "wrongpassword");

    Mockito.when(authService.authenticateLogin(any(LoginUserDto.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value("The username or password is incorrect"));
  }

  @Test
  void shouldRegisterNewUser() throws Exception {
    RegisterUserDto registerRequest =
        new RegisterUserDto("1", "John Doe", "john@example.com", "password123");

    ApiResponse<UserDto> response =
        ApiResponse.<UserDto>builder()
            .status("success")
            .message("User registered successfully")
            .data(userDto)
            .build();

    Mockito.when(authService.authenticateSignUp(any(RegisterUserDto.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("User registered successfully"))
        .andExpect(jsonPath("$.data.id").value("1"))
        .andExpect(jsonPath("$.data.rol").value("PROFESOR"));
  }
}
