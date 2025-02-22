package edu.eci.UniReserva.UniReserva_Backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Document(collection = "labs")
public class Lab {
    @Id
    private String id;
    private String name;
    private int capacity;
    private List<String> reservations;
    private HashMap<String, Integer> equipment;

    private static final String LOCATION = "B";

    public Lab() {}

    public Lab(String name, int capacity, HashMap<String, Integer> equipment) {
        this.name = name;
        this.capacity = capacity;
        this.reservations = new ArrayList<>();
        this.equipment = equipment;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getReservations() {
        return reservations;
    }

    public void addReservation(String reservationId) {
        this.reservations.add(reservationId);
    }

    public HashMap<String, Integer> getEquipment() {
        return equipment;
    }

    public void setEquipment(HashMap<String, Integer> equipment) {
        this.equipment = equipment;
    }

    public String getLocation() {
        return LOCATION;
    }
}
