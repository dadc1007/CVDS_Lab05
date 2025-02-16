package edu.eci.UniReserva.UniReserva_Backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "labs")
public class Lab {
    @Id
    private String id;
    private String name;
    private int capacity;

    private static final String LOCATION = "B";

    public Lab() {}

    public Lab(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
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

    public String getLocation() {
        return LOCATION;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
