package edu.eci.UniReserva.UniReserva_Backend.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationRepositoryTest {
    @BeforeEach
    void setup() {
    reservationRepository.deleteAll(); // Limpia las reservas antes de cada prueba
    }


    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testFindByUserId() {
        
        Reservation reservation = new Reservation(
                "user123",
                "lab456",
                LocalDate.now(),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                1,
                false,
                "Study session",
                ReservationStatus.CONFIRMED
        );

        reservationRepository.save(reservation);

        
        List<Reservation> foundReservations = reservationRepository.findByUserId("user123");
        assertFalse(foundReservations.isEmpty());
        assertEquals(1, foundReservations.size());
        assertEquals("user123", foundReservations.get(0).getUserId());
    }
}

