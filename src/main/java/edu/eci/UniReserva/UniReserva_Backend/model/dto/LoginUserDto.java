package edu.eci.UniReserva.UniReserva_Backend.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    private String email;
    private String password;
}
