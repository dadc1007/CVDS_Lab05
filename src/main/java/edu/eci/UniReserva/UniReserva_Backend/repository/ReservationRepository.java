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
     * Checks if a reservation with the given ID exists in the database.
     *
     * @param id Unique identifier of the reservation.
     * @return true if the reservation exists, false otherwise.
     */
    @Override
    boolean existsById(String id);

    /**
     * Removes a reservation from the database by its ID.
     *
     * @param id Unique identifier of the reservation to be removed.
     */
    @Override
    void deleteById(String id);

    /**
     * Finds reservations that overlap with the given time range for a specific lab.
     * This method checks for conflicting reservations by ensuring no reservation
     * exists in the same lab where:
     * - `startTime` is before the requested `endTime`, AND
     * - `endTime` is after the requested `startTime`.
     *
     * @param labId     Unique identifier of the lab.
     * @param startTime Start time of the requested reservation.
     * @param endTime   End time of the requested reservation.
     * @return List of conflicting reservations.
     */
    @Query("{ 'labId': ?0, '$or': [ { 'startTime': { '$lt': ?2 }, 'endTime': { '$gt': ?1 } } ] }")
    List<Reservation> findConflictingReservations(String labId, LocalTime startTime, LocalTime endTime);

    @Query("SELECT r FROM Reservation r WHERE r.labId = :labId AND r.startTime = :startTime AND r.endTime = :endTime")
    Optional<Reservation> findByLabAndTime(String labId, LocalTime startTime, LocalTime endTime);

    /**
     * Find all reservations for a specific user.
     */
    List<Reservation> findByUserId(String userId);
}
