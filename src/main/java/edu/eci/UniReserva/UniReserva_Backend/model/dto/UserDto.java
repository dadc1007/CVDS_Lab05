package edu.eci.UniReserva.UniReserva_Backend.model.dto;

import edu.eci.UniReserva.UniReserva_Backend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String name;
    private String email;
    private List<String> reservations;
    private Role rol;
}
