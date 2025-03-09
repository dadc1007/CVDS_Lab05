package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.LabServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LabController.class)
public class LabServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LabServiceImpl labService;

    @Test
    void shouldReturnAllLabs() throws Exception {
        List<Lab> labs = List.of(
                new Lab("Lab de Física", 30, new HashMap<>()),
                new Lab("Lab de Química", 25, new HashMap<>())
        );
        when(labService.getLabs()).thenReturn(labs);
        mockMvc.perform(get("/labs/allLabs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Lab de Física"))
                .andExpect(jsonPath("$[1].name").value("Lab de Química"));
    }
}
