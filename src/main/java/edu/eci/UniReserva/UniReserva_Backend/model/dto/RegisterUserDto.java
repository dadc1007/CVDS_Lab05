package edu.eci.UniReserva.UniReserva_Backend.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {
    private String id;
    private String name;
    private String email;
    private String password;
}
