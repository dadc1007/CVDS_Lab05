package edu.eci.UniReserva.UniReserva_Backend.service.impl;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public UserServiceImpl(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public User createUser(User user) {
        if (emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!validPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        User existingUser = userOptional.get();

        if (user.getEmail() != null) {
            throw new IllegalArgumentException("The email cannot be updated");
        }

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        if (user.getPassword() != null) {
            if (!validPassword(user.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }
            existingUser.setPassword(user.getPassword());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public String deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        if (hasRepository(id)){
            throw new IllegalArgumentException("User has Repository, can't be deleted");
        }
        userRepository.deleteById(id);
        return "User with ID " + id + " deleted successfully";
    }

    @Override
    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean validPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    }

    private boolean hasRepository(String id) {
        User usuario = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(usuario.getReservations() == null) {
            return false;
        }
        if(!(usuario.getReservations().size()>0)){
            return false;
        }
        return true;
    }
}
