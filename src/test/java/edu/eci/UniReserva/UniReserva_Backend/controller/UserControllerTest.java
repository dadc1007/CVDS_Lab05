package edu.eci.UniReserva.UniReserva_Backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.Role;
import edu.eci.UniReserva.UniReserva_Backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private UserService userService;

  private UserDto userDto;
  private User user;

  @BeforeEach
  void setUp() {
    userDto =
        new UserDto("1", "Test User", "test@example.com", Collections.emptyList(), Role.PROFESOR);
    user = new User("1", "Test User", "test@example.com", "password123", Role.PROFESOR);
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldUpdateUser() throws Exception {
    ApiResponse<UserDto> mockResponse =
        ApiResponse.<UserDto>builder()
            .status("success")
            .message("User updated successfully")
            .data(userDto)
            .build();

    when(userService.updateUser(eq("1"), any(User.class))).thenReturn(mockResponse);

    mockMvc
        .perform(
            patch("/user/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("User updated successfully"))
        .andExpect(jsonPath("$.data.email").value("test@example.com"));

    verify(userService, times(1)).updateUser(eq("1"), any(User.class));
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldReturnUserById() throws Exception {
    ApiResponse<UserDto> mockResponse =
        ApiResponse.<UserDto>builder()
            .status("success")
            .message("User retrieved successfully")
            .data(userDto)
            .build();

    when(userService.getUser("1")).thenReturn(mockResponse);

    mockMvc
        .perform(get("/user/getUser/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("User retrieved successfully"))
        .andExpect(jsonPath("$.data.email").value("test@example.com"));

    verify(userService, times(1)).getUser("1");
  }

  @Test
  @WithMockUser(roles = {"ADMIN"})
  void shouldDeleteUser() throws Exception {
    ApiResponse<UserDto> mockResponse =
        ApiResponse.<UserDto>builder()
            .status("success")
            .message("User deleted successfully")
            .data(userDto)
            .build();

    when(userService.deleteUser("1")).thenReturn(mockResponse);

    mockMvc
        .perform(delete("/user/delete/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("User deleted successfully"))
        .andExpect(jsonPath("$.data.email").value("test@example.com"));

    verify(userService, times(1)).deleteUser("1");
  }
}
