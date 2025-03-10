package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.User;

public interface AuthService {
    User authenticateLogin(String username, String password);

    User authenticateSignUp(User user);
}
