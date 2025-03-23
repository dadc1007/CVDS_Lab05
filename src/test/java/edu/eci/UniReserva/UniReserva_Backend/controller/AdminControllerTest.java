package edu.eci.UniReserva.UniReserva_Backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private AuthService authService;

  private RegisterUserDto registerUserDto;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    registerUserDto = new RegisterUserDto("1", "Test User", "test@example.com", "Contra#123");
    userDto =
        new UserDto("1", "Test User", "test@example.com", Collections.emptyList(), Role.ADMIN);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldCreateUser() throws Exception {
    ApiResponse<UserDto> apiResponse =
        ApiResponse.<UserDto>builder()
            .status("success")
            .message("User created")
            .data(userDto)
            .build();

    Mockito.when(authService.createAdmin(any(RegisterUserDto.class))).thenReturn(apiResponse);

    mockMvc
        .perform(
            post("/admin/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("User created"))
        .andExpect(jsonPath("$.data.id").value("1"))
        .andExpect(jsonPath("$.data.rol").value("ADMIN"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldDenyAccessForNonAdmin() throws Exception {
    mockMvc
        .perform(
            post("/admin/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDto)))
        .andExpect(status().isForbidden());
  }
}
