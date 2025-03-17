package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;

public interface UserService {
  ApiResponse<UserDto> updateUser(String id, User user);

  ApiResponse<UserDto> deleteUser(String id);

  ApiResponse<UserDto> getUser(String id);
}
