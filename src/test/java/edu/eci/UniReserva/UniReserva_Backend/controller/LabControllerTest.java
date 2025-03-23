package edu.eci.UniReserva.UniReserva_Backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.service.LabService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LabControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private LabService labService;

  private Lab lab1;
  private Lab lab2;

  @BeforeEach
  void setUp() {
    lab1 = new Lab("Lab Computación", 30, new HashMap<>());
    lab1.setId("1");
    lab2 = new Lab("Lab Electrónica", 25, new HashMap<>());
    lab2.setId("2");
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldReturnListOfLabs() throws Exception {
    List<Lab> labs = Arrays.asList(lab1, lab2);
    ApiResponse<List<Lab>> mockResponse =
        ApiResponse.<List<Lab>>builder()
            .status("success")
            .message("Labs retrieved")
            .data(labs)
            .build();

    when(labService.getLabs()).thenReturn(mockResponse);

    mockMvc
        .perform(get("/labs/allLabs").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("Labs retrieved"))
        .andExpect(jsonPath("$.data.length()").value(2));
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldReturnLabName() throws Exception {
    String labId = "1";
    ApiResponse<String> mockResponse =
        ApiResponse.<String>builder()
            .status("success")
            .message("Lab name retrieved")
            .data("Lab Computación")
            .build();

    when(labService.getLabNameById(labId)).thenReturn(mockResponse);

    mockMvc
        .perform(get("/labs/labName/" + labId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("Lab name retrieved"))
        .andExpect(jsonPath("$.data").value("Lab Computación"));
  }

  @Test
  @WithMockUser(roles = {"ADMIN", "PROFESOR"})
  void shouldReturnLabDetails() throws Exception {
    String labId = "2";
    ApiResponse<Lab> mockResponse =
        ApiResponse.<Lab>builder()
            .status("success")
            .message("Lab details retrieved")
            .data(lab2)
            .build();

    when(labService.getLabById(labId)).thenReturn(mockResponse);

    mockMvc
        .perform(get("/labs/lab/" + labId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("success"))
        .andExpect(jsonPath("$.message").value("Lab details retrieved"))
        .andExpect(jsonPath("$.data.id").value("2"));
  }
}
