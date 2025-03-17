package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.LabServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LabServiceImplTest {
    @Mock
    private LabRepository labRepository;

    @InjectMocks
    private LabServiceImpl labService;
    private Lab myLab;

    @BeforeEach
    void setUp() {
        HashMap<String, Integer> equipment = new HashMap<>();
        equipment.put("Microscopios", 10);
        equipment.put("Computadoras", 15);
        equipment.put("Proyectores", 2);
        myLab = new Lab("Laboratorio de Física", 30, equipment);
    }

    @Test
    void shouldReturnListOfLabs() {
        List<Lab> mockLabs = List.of(
                myLab,
                new Lab()
        );
        when(labRepository.findAll()).thenReturn(mockLabs);
        ApiResponse<List<Lab>> response = labService.getLabs();
        assertNotNull(response);
        assertNotNull(response.getData());
        List<Lab> result = response.getData();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laboratorio de Física", result.get(0).getName());
    }
}
