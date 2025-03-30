package edu.eci.UniReserva.UniReserva_Backend.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.eci.UniReserva.UniReserva_Backend.exceptions.UniReservaException;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
  private final ReservationRepository reservationRepository;
  private final LabRepository labRepository;
  private final UserRepository userRepository;

  /**
   * Creates a new reservation if the lab is available.
   *
   * @param reservation Object containing the reservation details.
   * @return The created reservation stored in the database.
   *     <p>Possible scenarios: - The reservation is successfully stored if the lab is available. -
   *     An exception is thrown if the lab is already booked for the requested time slot.
   */
  @Override
  public ApiResponse<Reservation> createReservation(Reservation reservation) {
    if (!labRepository.existsById(reservation.getLabId())) {
      throw new UniReservaException(UniReservaException.LAB_NOT_FOUND);
    }

    if (!userRepository.existsById(reservation.getUserId())) {
      throw new UniReservaException(UniReservaException.USER_NOT_FOUND);
    }

    if (!checkStartTime(reservation.getParsedStartTime(), reservation.getParsedDate())) {
      throw new UniReservaException(UniReservaException.INVALID_TIME);
    }

    if (!checkDate(reservation.getParsedDate())) {
      throw new UniReservaException(UniReservaException.INVALID_DATE);
    }

    if (!isAvailable(reservation)) {
      throw new UniReservaException(UniReservaException.INVALID_RESERVATION);
    }

    if (!validatePriority(reservation.getPriority())) {
      throw new UniReservaException(UniReservaException.INVALID_PRIORITY);
    }

    Reservation savedReservation = reservationRepository.save(reservation);

    addReservationToLab(reservation);
    addReservationToUser(reservation);

    return ApiResponse.<Reservation>builder()
        .status("success")
        .message("Reservation created successfully")
        .data(savedReservation)
        .build();
  }

  /**
   * Retrieves a list of reservations for a specific user, sorted by date and start time.
   *
   * @param userId the unique identifier of the user whose reservations are being fetched
   * @return a list of {@link Reservation} objects belonging to the specified user, sorted in
   *     ascending order by date and then by start time
   */
  @Override
  public ApiResponse<List<Reservation>> getReservationsByUserId(String userId) {
    if (!userRepository.existsById(userId)) {
      throw new UniReservaException(UniReservaException.USER_NOT_FOUND);
    }

    List<Reservation> reservations =
        reservationRepository.findByUserId(userId).stream()
            .sorted(
                Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTime))
            .collect(Collectors.toList());

    return ApiResponse.<List<Reservation>>builder()
        .status("success")
        .message("User reservations retrieved successfully")
        .data(reservations)
        .build();
  }

  /**
   * Cancels a reservation given its ID.
   *
   * <p>This method should: - Retrieve the reservation from the database using its ID. - Check if
   * the reservation exists, otherwise throw an appropriate exception. - Validate that the
   * reservation is not already canceled. - Update the reservation status to "CANCELLED". - Save the
   * updated reservation in the database.
   *
   * @param reservationId ID of the reservation to cancel.
   * @throws IllegalArgumentException if the reservation does not exist.
   * @throws IllegalStateException if the reservation is already cancelled.
   */
  @Override
  public ApiResponse<Reservation> cancelReservationByReservationId(String reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
    if (reservation == null)
      throw new UniReservaException(UniReservaException.RESERVATION_NOT_FOUND);

    if (reservation.getStatus().equals(ReservationStatus.CANCELED))
      throw new UniReservaException(UniReservaException.RESERVATION_ALREADY_CANCELED);

    reservation.setStatus(ReservationStatus.CANCELED);
    reservationRepository.save(reservation);

    return ApiResponse.<Reservation>builder()
        .status("success")
        .message("Reservation canceled successfully")
        .build();
  }

  public ApiResponse<List<Reservation>> getReservationsByRangeDate(
      String lab, String date1, String date2) {
    if (!labRepository.existsById(lab)) {
      throw new UniReservaException(UniReservaException.LAB_NOT_FOUND);
    }

    LocalDate startDate = LocalDate.parse(date1);
    LocalDate endDate = LocalDate.parse(date2);

    if (startDate.isAfter(endDate)) {
      throw new UniReservaException(UniReservaException.START_DATE_BEFORE_END_DATE);
    }

    List<Reservation> reservations =
        reservationRepository.findAll().stream()
            .filter(
                reservation ->
                    !reservation.getParsedDate().isBefore(startDate)
                        && !reservation.getParsedDate().isAfter(endDate)
                        && lab.equals(reservation.getLabId())
                        && reservation.getStatus().equals(ReservationStatus.CONFIRMED))
            .collect(Collectors.toList());

    return ApiResponse.<List<Reservation>>builder()
        .status("success")
        .message("Reservations retrieved successfully")
        .data(reservations)
        .build();
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
          if (reservation.getParsedStartTime().isBefore(r.getParsedEndTime())
              && reservation.getParsedEndTime().isAfter(r.getParsedStartTime())) {
            return false;
          }
        }
      }
    }

    return true;
  }

  private void addReservationToLab(Reservation reservation) {
    labRepository
        .findById(reservation.getLabId())
        .ifPresent(
            lab -> {
              if (lab.getReservations() != null
                  && !lab.getReservations().contains(reservation.getId())) {
                lab.addReservation(reservation.getId());
                labRepository.save(lab);
              }
            });
  }

  private void addReservationToUser(Reservation reservation) {
    userRepository
        .findById(reservation.getUserId())
        .ifPresent(
            user -> {
              if (user.getReservations() != null
                  && !user.getReservations().contains(reservation.getId())) {
                user.addReservationId(reservation.getId());
                userRepository.save(user);
              }
            });
  }

  private boolean validatePriority(int priority) {
    return priority >= 1 && priority <= 5;
  }

  @Override
  public ApiResponse<List<Reservation>> getAllReservations() {
    return ApiResponse.<List<Reservation>>builder()
        .status("success")
        .message("Reseervations retrieved successfully")
        .data(reservationRepository.findAll())
        .build();
  }
}
