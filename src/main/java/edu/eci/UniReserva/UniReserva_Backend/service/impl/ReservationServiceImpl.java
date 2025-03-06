package edu.eci.UniReserva.UniReserva_Backend.service.impl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.ReservationService;
import org.springframework.stereotype.Service;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final LabRepository labRepository;
    private final UserRepository userRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, LabRepository labRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.labRepository = labRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new reservation if the lab is available.
     * 
     * @param reservation Object containing the reservation details.
     * @return The created reservation stored in the database.
     * 
     * Possible scenarios:
     * - The reservation is successfully stored if the lab is available.
     * - An exception is thrown if the lab is already booked for the requested time slot.
     */
    @Override
    public Reservation createReservation(Reservation reservation) {
        if (!labRepository.existsById(reservation.getLabId())) {
            throw new IllegalArgumentException("The lab does not exist");
        }

        if (!userRepository.existsById(reservation.getUserId())) {
            throw new IllegalArgumentException("The user does not exist");
        }

        if (!checkStartTime(reservation.getParsedStartTime(), reservation.getParsedDate())) {
            throw new IllegalArgumentException("The start time must be in the future. You cannot create a reservation with a past time");
        }

        if (!checkDate(reservation.getParsedDate())) {
            throw new IllegalArgumentException("You cannot select a past date for your reservation");
        }

        if (!isAvailable(reservation)) {
            throw new IllegalArgumentException("There is already a reservation in the lab selected in the time selected");
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        addReservationToLab(reservation);
        addReservationToUser(reservation);

        return savedReservation;
    }

    /**
     * Retrieves a list of reservations for a specific user, sorted by date and start time.
     *
     * @param userId the unique identifier of the user whose reservations are being fetched
     * @return a list of {@link Reservation} objects belonging to the specified user,
     *         sorted in ascending order by date and then by start time
     */
    @Override
    public List<Reservation> getReservationsByUserId(String userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Reservation::getDate)
                        .thenComparing(Reservation::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * Cancels a reservation given its ID.
     *
     * This method should:
     * - Retrieve the reservation from the database using its ID.
     * - Check if the reservation exists, otherwise throw an appropriate exception.
     * - Validate that the reservation is not already canceled.
     * - Update the reservation status to "CANCELLED".
     * - Save the updated reservation in the database.
     *
     * @param reservationId ID of the reservation to cancel.
     * @throws IllegalArgumentException if the reservation does not exist.
     * @throws IllegalStateException if the reservation is already cancelled.
     */
    @Override
    public String  cancelReservationByReservationId(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if(reservation == null) throw new IllegalArgumentException("Rerservation with id " + reservationId + " not found.");
        if(reservation.getStatus().equals(ReservationStatus.CANCELED)) throw new IllegalArgumentException("This reservation is already cancelled");

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        return "Reservation updated successfully";
    }

    private boolean checkDate(LocalDate date) {
        return !date.isBefore(LocalDate.now());
    }

    private boolean checkStartTime(LocalTime startTime, LocalDate date) {
        return !date.equals(LocalDate.now()) || startTime.isAfter(LocalTime.now());
    }

    private boolean isAvailable(Reservation reservation) {
        List<Reservation> reservations = reservationRepository.findByLabId(reservation.getLabId());

        if (reservations.isEmpty()) return true;

        for (Reservation r : reservations) {
            if (reservation.getParsedDate().equals(r.getParsedDate())) {
                if (r.getStatus().equals(ReservationStatus.CONFIRMED)) {
                    if (reservation.getParsedStartTime().isBefore(r.getParsedEndTime()) && reservation.getParsedEndTime().isAfter(r.getParsedStartTime())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void addReservationToLab(Reservation reservation) {
        labRepository.findById(reservation.getLabId()).ifPresent(lab -> {
            if (lab.getReservations() != null && !lab.getReservations().contains(reservation.getId())) {
                lab.addReservation(reservation.getId());
                labRepository.save(lab);
            }
        });
    }

    private void addReservationToUser(Reservation reservation) {
        userRepository.findById(reservation.getUserId()).ifPresent(user -> {
            if (user.getReservations() != null && !user.getReservations().contains(reservation.getId())) {
                user.addReservationId(reservation.getId());
                userRepository.save(user);
            }
        });
    }
}
