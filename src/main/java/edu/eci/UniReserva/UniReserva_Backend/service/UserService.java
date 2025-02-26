package edu.eci.UniReserva.UniReserva_Backend.service;

import org.springframework.stereotype.Service;

import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
}
