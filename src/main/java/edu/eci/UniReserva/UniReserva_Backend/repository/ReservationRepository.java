package edu.eci.UniReserva.UniReserva_Backend.repository;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    /**
     * Checks if a reservation with the given ID exists in the database.
     * 
     * @param id Unique identifier of the reservation.
     * @return true if the reservation exists, false otherwise.
     */
    boolean existsById(String id);
}
