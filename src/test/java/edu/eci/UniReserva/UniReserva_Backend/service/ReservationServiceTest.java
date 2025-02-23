package edu.eci.UniReserva.UniReserva_Backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;
import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import edu.eci.UniReserva.UniReserva_Backend.service.ReservationService;

public class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        // Crear el mock manualmente
        reservationRepository = Mockito.mock(ReservationRepository.class);
        
        // Inyectarlo en el servicio
        reservationService = new ReservationService(reservationRepository);

        // Inicializar una reserva de prueba con datos reales del modelo
        testReservation = new Reservation(
                "user123",
                "lab01",
                LocalDate.of(2025, 3, 1),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                1,
                false,
                "Project research",
                ReservationStatus.CONFIRMED
        );
    }

    @Test
    void shouldCreateReservationWhenAvailable() {
        // Simular que no hay una reserva en el mismo horario
        when(reservationRepository.save(testReservation)).thenReturn(testReservation);

        // Ejecutar la prueba
        Reservation createdReservation = reservationService.createReservation(testReservation);

        // Validaciones
        assertNotNull(createdReservation);
        assertEquals(testReservation.getLabId(), createdReservation.getLabId());
        assertEquals(testReservation.getDate(), createdReservation.getDate());
        assertEquals(testReservation.getStartTime(), createdReservation.getStartTime());
        assertEquals(testReservation.getEndTime(), createdReservation.getEndTime());
        assertEquals(ReservationStatus.CONFIRMED, createdReservation.getStatus());
        verify(reservationRepository, times(1)).save(testReservation);
    }

    @Test
    void shouldNotCreateReservationIfAlreadyExists() {
        when(reservationRepository.findByLabAndTime(
            testReservation.getLabId(), 
            testReservation.getStartTime(), 
            testReservation.getEndTime()
        )).thenReturn(Optional.of(testReservation));
    
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(testReservation);
        });
    
        assertEquals("A reservation for this lab and time slot already exists.", exception.getMessage());
    
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
    


    @Test
    void shouldThrowExceptionWhenReservationHasInvalidData() {
        // Creando una reserva inválida con fecha incorrecta (fin antes que inicio)
        Reservation invalidReservation = new Reservation(
            "user123", "lab456", LocalDate.now(), 
            LocalTime.of(15, 0), LocalTime.of(14, 0), // Hora de fin antes que la de inicio
            1, false, "Study session", ReservationStatus.CONFIRMED
        );

        // Verificar que se lanza una excepción al intentar guardar una reserva inválida
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(invalidReservation));
        assertEquals("End time must be after start time.", exception.getMessage());

        // Asegurar que no se intenta guardar en el repositorio
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldConfirmReservationWhenValid() {
        // Simular que el repositorio guarda la reserva y devuelve la misma
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation savedReservation = invocation.getArgument(0);
            savedReservation.setStatus(ReservationStatus.CONFIRMED); // Simula que se confirma la reserva
            return savedReservation;
        });

        // Ejecutar el método
        Reservation result = reservationService.createReservation(testReservation);

        // Validaciones
        assertNotNull(result);
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus()); // Asegurar que se confirma

        // Verificar que se llamó al repositorio para guardar la reserva
        verify(reservationRepository).save(testReservation);
    }    
}
