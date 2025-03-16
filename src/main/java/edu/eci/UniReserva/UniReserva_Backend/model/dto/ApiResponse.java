package edu.eci.UniReserva.UniReserva_Backend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  private String status;
  private String message;
  private T data;
  private String token;
}
