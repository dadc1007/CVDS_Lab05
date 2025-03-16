package edu.eci.UniReserva.UniReserva_Backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Document(collection = "labs")
@Getter
@Setter
public class Lab {
  @Id private String id;
  private String name;
  private int capacity;
  private List<String> reservations = new ArrayList<>();
  private HashMap<String, Integer> equipment;

  private static final String LOCATION = "B";

  public Lab() {}

  public Lab(String name, int capacity, HashMap<String, Integer> equipment) {
    this.name = name;
    this.capacity = capacity;
    this.equipment = equipment;
  }

  public void addReservation(String reservationId) {
    this.reservations.add(reservationId);
  }
}
