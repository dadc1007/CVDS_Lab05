package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.eci.UniReserva.UniReserva_Backend.model.User;
import edu.eci.UniReserva.UniReserva_Backend.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PatchMapping("/update/{id}")
  public ResponseEntity<ApiResponse<UserDto>> updateUser(
      @RequestBody User user, @PathVariable String id) {
    return ResponseEntity.ok(userService.updateUser(id, user));
  }

  @GetMapping("/getUser/{id}")
  public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable String id) {
    return ResponseEntity.ok(userService.getUser(id));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ApiResponse<UserDto>> deleteUser(@PathVariable String id) {
    return ResponseEntity.ok(userService.deleteUser(id));
  }
}
