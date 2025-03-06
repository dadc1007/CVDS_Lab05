package edu.eci.UniReserva.UniReserva_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private String userId;
    private String labId;
    private String date;
    private String startTime;
    private String endTime;
    private String purpose;
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public Reservation() {
    }

    public Reservation(String userId, String labId, String date, String startTime, String endTime, String purpose) {
        this.userId = userId;
        this.labId = labId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
    }

    @JsonIgnore
    public LocalDate getParsedDate() {
        return parseDate(date);
    }

    @JsonIgnore
    public LocalTime getParsedStartTime() {
        return parseTime(startTime);
    }

    @JsonIgnore
    public LocalTime getParsedEndTime() {
        return parseTime(endTime);
    }

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    private LocalTime parseTime(String time) {
        return LocalTime.parse(time, TIME_FORMATTER);
    }
}
