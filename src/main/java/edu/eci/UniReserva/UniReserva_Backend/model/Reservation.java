package edu.eci.UniReserva.UniReserva_Backend.model;

import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.TimeBlock;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private String userId;
    private String labId;
    private LocalDate date;
    private TimeBlock block;
    private String purpose;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(String userId, String labId, LocalDate date, TimeBlock block, String purpose, ReservationStatus status) {
        this.userId = userId;
        this.labId = labId;
        this.date = date;
        this.block = block;
        this.purpose = purpose;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TimeBlock getBlock() {
        return block;
    }

    public void setBlock(TimeBlock block) {
        this.block = block;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
