package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.User;

public interface UserService {
    User createUser(User user);

    User updateUser(String id, User user);

    String deleteUser(String id);

    User getUser(String id);
}
