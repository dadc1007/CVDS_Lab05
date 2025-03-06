package edu.eci.UniReserva.UniReserva_Backend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;

public class ReservationServiceImplTest {

    private ReservationRepository reservationRepository;
    private LabRepository labRepository;
    private UserRepository userRepository;
    private ReservationServiceImpl reservationServiceImpl;
    private Reservation testReservation;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        // Crear el mock manualmente
        reservationRepository = Mockito.mock(ReservationRepository.class);
        labRepository = Mockito.mock(LabRepository.class);
        userRepository = Mockito.mock(UserRepository.class);

        // Inyectarlo en el servicio
        reservationServiceImpl = new ReservationServiceImpl(reservationRepository, labRepository, userRepository);

        // Inicializar una reserva de prueba con datos reales del modelo
        testReservation = new Reservation(
                "user123",
                "lab01",
                "2025-05-01",
                "10:00",
                "12:00",
                "Project research"
        );
    }

    @Test
    void shouldCreateReservationWhenAvailable() {
        // Simular que el laboratorio existe
        when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);

        // Simular que el usuario existe
        when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);

        // Simular que no hay una reserva en el mismo horario
        when(reservationRepository.findByLabId(testReservation.getLabId())).thenReturn(Collections.emptyList());

        // Simular guardado de la reserva
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        // Ejecutar la prueba
        Reservation createdReservation = reservationServiceImpl.createReservation(testReservation);

        // Validaciones
        assertNotNull(createdReservation);
        assertEquals(testReservation.getLabId(), createdReservation.getLabId());
        assertEquals(testReservation.getDate(), createdReservation.getDate());
        assertEquals(testReservation.getStartTime(), createdReservation.getStartTime());
        assertEquals(testReservation.getEndTime(), createdReservation.getEndTime()); // Asegurar fin correcto
        assertEquals(ReservationStatus.CONFIRMED, createdReservation.getStatus());

        // Verificar interacciones
        verify(reservationRepository, times(1)).save(testReservation);
    }

    @Test
    void shouldNotCreateReservationIfAlreadyExists() {
        // Simular que el laboratorio y usuario existen
        when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
        when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);

        // Simular que ya hay una reserva en el mismo horario
        when(reservationRepository.findByLabId(testReservation.getLabId())).thenReturn(List.of(testReservation));

        // Ejecutar y verificar excepción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationServiceImpl.createReservation(testReservation);
        });

        assertEquals("There is already a reservation in the lab selected in the time selected", exception.getMessage());

        // Verificar que no se guardó la reserva
        verify(reservationRepository, never()).save(any(Reservation.class));
    }



    @Test
    void shouldThrowExceptionWhenReservationHasInvalidData() {
    }

    @Test
    void shouldConfirmReservationWhenValid() {
    }


    @Test
    public void shouldReturnReservationsWhenUserHasReservations() {

        String userId = "user123";
        String date1 = LocalDate.now().format(dateFormatter);
        String date2 = LocalDate.now().plusDays(1).format(dateFormatter);
        Reservation res1 = new Reservation(userId, "lab1", date1, "10:00", "11:00", "Study");
        Reservation res2 = new Reservation(userId, "lab2", date2, "12:00", "13:00", "Project");

        when(reservationRepository.findByUserId(userId)).thenReturn(Arrays.asList(res1, res2));

        // Ejecutar
        List<Reservation> result = reservationServiceImpl.getReservationsByUserId(userId);

        // Verificar
        assertEquals(2, result.size(), "El usuario debería tener 2 reservas");
        verify(reservationRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void shouldNotReturnReservationsWhenUserHasNoReservations() {

        String userId = "user456";

        when(reservationRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<Reservation> result = reservationServiceImpl.getReservationsByUserId(userId);

        assertTrue(result.isEmpty(), "El usuario no debería tener reservas");
        verify(reservationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldUpdateReservationWhenItExist() {
        when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(testReservation)).thenReturn(testReservation);

        String result = reservationServiceImpl.cancelReservationByReservationId(testReservation.getId());

        assertEquals("Reservation updated successfully", result);
        assertEquals(ReservationStatus.CANCELED, testReservation.getStatus());
        verify(reservationRepository).save(testReservation);
    }

    @Test
    void testCancelReservationByReservationId_NotFound() {
        when(reservationRepository.findById("123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationServiceImpl.cancelReservationByReservationId("123");
        });

        assertEquals("Rerservation with id 123 not found.", exception.getMessage());
    }

    @Test
    void testCancelReservationByReservationId_AlreadyCancelled() {
        testReservation.setStatus(ReservationStatus.CANCELED);
        when(reservationRepository.findById("123")).thenReturn(Optional.of(testReservation));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationServiceImpl.cancelReservationByReservationId("123");
        });

        assertEquals("This reservation is already cancelled", exception.getMessage());
    }

}

