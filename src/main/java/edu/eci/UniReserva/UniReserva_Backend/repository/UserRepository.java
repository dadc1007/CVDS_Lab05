package edu.eci.UniReserva.UniReserva_Backend.repository;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
