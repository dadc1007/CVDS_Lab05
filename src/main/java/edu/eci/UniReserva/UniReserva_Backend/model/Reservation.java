package edu.eci.UniReserva.UniReserva_Backend.model;

import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private String userId;
    private String labId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int weeks;
    private boolean interleaving;
    private String purpose;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(String userId, String labId, LocalDate date, LocalTime startTime, LocalTime endTime, int weeks, boolean interleaving, String purpose, ReservationStatus status) {
        this.userId = userId;
        this.labId = labId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weeks = weeks;
        this.interleaving = interleaving;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public boolean isInterleaving() {
        return interleaving;
    }

    public void setInterleaving(boolean interleaving) {
        this.interleaving = interleaving;
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
