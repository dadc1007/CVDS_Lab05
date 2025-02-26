package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User updateUser(String id, User user) {
        if (userRepository.existsById(id)) {
            userRepository.save(user);
            return user;
        }
        throw new RuntimeException("User not found");
    }

    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        throw new RuntimeException("User not found");
    }

    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
