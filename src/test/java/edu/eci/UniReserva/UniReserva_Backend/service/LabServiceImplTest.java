package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        myLab.setId("lab123");
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

    @Test
    void shouldReturnALabNameById() {
        when(labRepository.findById(myLab.getId())).thenReturn(Optional.of(myLab));

        ApiResponse<String> response = labService.getLabNameById(myLab.getId());

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Laboratorio de Física", response.getData());

        verify(labRepository, times(1)).findById(myLab.getId());
    }

    @Test
    void shouldReturnLabById() {
        when(labRepository.findById(myLab.getId())).thenReturn(Optional.of(myLab));

        ApiResponse<Lab> response = labService.getLabById(myLab.getId());

        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Lab found", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(myLab.getId(), response.getData().getId());
        assertEquals("Laboratorio de Física", response.getData().getName());

        verify(labRepository, times(1)).findById(myLab.getId());
    }

    @Test
    void shouldThrowExceptionWhenLabIdNotFound() {
        when(labRepository.findById("invalid_id")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> labService.getLabNameById("invalid_id"));

        assertEquals("Lab not found with id: invalid_id", exception.getMessage());

        verify(labRepository, times(1)).findById("invalid_id");
    }
}
