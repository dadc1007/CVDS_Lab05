package edu.eci.UniReserva.UniReserva_Backend.repository;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabRepository extends MongoRepository<Lab, String> {
  Optional<Lab> findById(String id);
}
