package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository UserRepository;
    @Mock
    private LabRepository labRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation testReservation;

    @BeforeEach
    void setUp() {
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
        // Simular que la reserva ya existe en la base de datos
        when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));

        // Ejecutar la prueba y esperar una excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(testReservation);
        });

        // Validaciones
        assertEquals("Lab is already booked for the requested time slot.", exception.getMessage());
        verify(reservationRepository, never()).save(testReservation);
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
