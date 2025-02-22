package edu.eci.UniReserva.UniReserva_Backend.repository;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
