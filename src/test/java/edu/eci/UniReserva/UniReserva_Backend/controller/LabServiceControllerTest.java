//package edu.eci.UniReserva.UniReserva_Backend.controller;
//
//import edu.eci.UniReserva.UniReserva_Backend.jwt.JwtService;
//import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
//import edu.eci.UniReserva.UniReserva_Backend.model.User;
//import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
//import edu.eci.UniReserva.UniReserva_Backend.service.impl.LabServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(LabController.class)
//public class LabServiceControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private LabServiceImpl labService;
//    @MockitoBean
//    private JwtService jwtService;
//    private User mockUser;
//
//
//    @BeforeEach
//    void setup() {
//        User mockUser = new User("123", "Test User", "test@example.com", "password123");
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                mockUser,
//                null,
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    @Test
//    void shouldReturnAllLabs() throws Exception {
//        List<Lab> labs = List.of(
//                new Lab("Lab de Física", 30, new HashMap<>()),
//                new Lab("Lab de Química", 25, new HashMap<>())
//        );
//        ApiResponse<List<Lab>> mockResponse = ApiResponse.<List<Lab>>builder()
//                .status("success")
//                .message("Laboratories retrieved successfully")
//                .data(labs)
//                .build();
//
//        when(labService.getLabs()).thenReturn(mockResponse);
//        mockMvc.perform(get("/allLabs"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Laboratories retrieved successfully"))
//                .andExpect(jsonPath("$.data.length()").value(2))
//                .andExpect(jsonPath("$.data[0].name").value("Lab de Física"))
//                .andExpect(jsonPath("$.data[1].name").value("Lab de Química"));
//    }
//}
