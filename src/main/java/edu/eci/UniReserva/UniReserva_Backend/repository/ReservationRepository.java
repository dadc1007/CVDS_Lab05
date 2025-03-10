package edu.eci.UniReserva.UniReserva_Backend.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;

/**
 * Repository interface for managing reservations in MongoDB.
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    /**
     * Removes a reservation from the database by its ID.
     *
     * @param id Unique identifier of the reservation to be removed.
     */
    @Override
    void deleteById(String id);

    /**
     * Find all reservations for a specific user.
     */
    List<Reservation> findByUserId(String userId);

    List<Reservation> findByLabId(String labId);
}
