package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.dto.*;

public interface AuthService {
  ApiResponse<UserDto> authenticateLogin(LoginUserDto request);

  ApiResponse<UserDto> authenticateSignUp(RegisterUserDto request);

  ApiResponse<UserDto> createAdmin(RegisterUserDto request);
}
