package edu.eci.UniReserva.UniReserva_Backend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.eci.UniReserva.UniReserva_Backend.exceptions.UniReservaException;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.repository.UserRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.eci.UniReserva.UniReserva_Backend.model.Reservation;
import edu.eci.UniReserva.UniReserva_Backend.model.enums.ReservationStatus;
import edu.eci.UniReserva.UniReserva_Backend.repository.ReservationRepository;

public class ReservationServiceImplTest {
  private ReservationRepository reservationRepository;
  private LabRepository labRepository;
  private UserRepository userRepository;
  private ReservationServiceImpl reservationServiceImpl;
  private Reservation testReservation;

  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @BeforeEach
  void setUp() {
    reservationRepository = Mockito.mock(ReservationRepository.class);
    labRepository = Mockito.mock(LabRepository.class);
    userRepository = Mockito.mock(UserRepository.class);

    reservationServiceImpl =
        new ReservationServiceImpl(reservationRepository, labRepository, userRepository);

    testReservation =
        new Reservation("user123", "lab01", "2025-05-01", "10:00", "12:00", "Project research", 1);
  }

  @Test
  void shouldCreateReservationWhenAvailable() {
    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
    when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);
    when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

    ApiResponse<Reservation> response =
        reservationServiceImpl.createReservation(
            new Reservation(
                "user123", "lab01", "2025-05-01", "10:00", "12:00", "Project research", 1));
    assertNotNull(response);
    assertNotNull(response.getData());
    Reservation createdReservation = response.getData();
    assertNotNull(createdReservation);
    assertEquals(testReservation.getLabId(), createdReservation.getLabId());
    assertEquals(testReservation.getDate(), createdReservation.getDate());
    assertEquals(testReservation.getStartTime(), createdReservation.getStartTime());
    assertEquals(testReservation.getEndTime(), createdReservation.getEndTime());
    assertEquals(ReservationStatus.CONFIRMED, createdReservation.getStatus());

    ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
    verify(reservationRepository, times(1)).save(reservationCaptor.capture());
    Reservation savedReservation = reservationCaptor.getValue();

    assertEquals(testReservation.getUserId(), savedReservation.getUserId());
    assertEquals(testReservation.getLabId(), savedReservation.getLabId());
    assertEquals(testReservation.getDate(), savedReservation.getDate());
    assertEquals(testReservation.getStartTime(), savedReservation.getStartTime());
    assertEquals(testReservation.getEndTime(), savedReservation.getEndTime());
    assertEquals(ReservationStatus.CONFIRMED, savedReservation.getStatus());
  }

  @Test
  void shouldAllowReservationWhenSameDateAndTimeButDifferentLab() {
    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
    when(labRepository.existsById("lab02")).thenReturn(true);
    when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);

    when(reservationRepository.findByLabId(testReservation.getLabId()))
        .thenReturn(List.of(testReservation));
    when(reservationRepository.findByLabId("lab02")).thenReturn(Collections.emptyList());

    Reservation secondReservation =
        new Reservation(
            testReservation.getUserId(),
            "lab02",
            testReservation.getDate(),
            testReservation.getStartTime(),
            testReservation.getEndTime(),
            "Second reservation",
            1);

    when(reservationRepository.save(any(Reservation.class))).thenReturn(secondReservation);

    ApiResponse<Reservation> response = reservationServiceImpl.createReservation(secondReservation);
    assertNotNull(response);
    assertNotNull(response.getData());
    Reservation savedSecondReservation = response.getData();
    assertNotNull(savedSecondReservation);

    ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
    verify(reservationRepository, times(1)).save(reservationCaptor.capture());

    Reservation savedReservation = reservationCaptor.getValue();

    assertEquals("lab02", savedReservation.getLabId());
    assertEquals(testReservation.getUserId(), savedReservation.getUserId());
    assertEquals(testReservation.getDate(), savedReservation.getDate());
    assertEquals(testReservation.getStartTime(), savedReservation.getStartTime());
    assertEquals(testReservation.getEndTime(), savedReservation.getEndTime());
    assertEquals(ReservationStatus.CONFIRMED, savedReservation.getStatus());
  }

  @Test
  void shouldAllowReservationWhenPreviousIsCanceled() {
    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
    when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);

    testReservation.setStatus(ReservationStatus.CANCELED);

    when(reservationRepository.findByLabId(testReservation.getLabId()))
        .thenReturn(List.of(testReservation));

    Reservation newReservation =
        new Reservation(
            testReservation.getUserId(),
            testReservation.getLabId(),
            testReservation.getDate(),
            testReservation.getStartTime(),
            testReservation.getEndTime(),
            "Rebooking after cancellation",
            testReservation.getPriority());

    when(reservationRepository.save(any(Reservation.class))).thenReturn(newReservation);

    ApiResponse<Reservation> response = reservationServiceImpl.createReservation(newReservation);
    assertNotNull(response);
    assertNotNull(response.getData());
    Reservation savedReservation = response.getData();

    assertNotNull(savedReservation);

    ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
    verify(reservationRepository, times(1)).save(reservationCaptor.capture());

    Reservation capturedReservation = reservationCaptor.getValue();

    assertEquals(testReservation.getUserId(), capturedReservation.getUserId());
    assertEquals(testReservation.getLabId(), capturedReservation.getLabId());
    assertEquals(testReservation.getDate(), capturedReservation.getDate());
    assertEquals(testReservation.getStartTime(), capturedReservation.getStartTime());
    assertEquals(testReservation.getEndTime(), capturedReservation.getEndTime());
    assertEquals(
        ReservationStatus.CONFIRMED, capturedReservation.getStatus()); // Debe estar confirmada
  }

  @Test
  void shouldNotCreateReservationWhenLabNotExist() {
    when(labRepository.existsById(testReservation.getLabId())).thenReturn(false);

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> reservationServiceImpl.createReservation(testReservation));

    assertEquals(UniReservaException.LAB_NOT_FOUND, exception.getMessage());
  }

  @Test
  void shouldNotCreateReservationWhenUserNotExist() {
    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
    when(userRepository.existsById(testReservation.getUserId())).thenReturn(false);

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> reservationServiceImpl.createReservation(testReservation));

    assertEquals(UniReservaException.USER_NOT_FOUND, exception.getMessage());
  }

//  @Test
//  void shouldNotCreateReservationWhenStartTimeIsBeforeCurrentTime() {
//    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
//    when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);
//
//    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
//    LocalTime pastTime = LocalTime.now().minusHours(5);
//
//    Reservation pastReservation =
//        new Reservation(
//            "user123",
//            "lab01",
//            LocalDate.now().toString(),
//            pastTime.format(timeFormatter),
//            LocalTime.now().plusHours(5).format(timeFormatter),
//            "Past reservation",
//            1);
//
//    Exception exception =
//        assertThrows(
//            UniReservaException.class,
//            () -> reservationServiceImpl.createReservation(pastReservation));
//
//    assertEquals(UniReservaException.INVALID_TIME, exception.getMessage());
//  }

  @Test
  void shouldNotCreateReservationWhenDateIsBeforeCurrentDate() {
    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
    when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);

    Reservation pastDateReservation =
        new Reservation(
            "user123",
            "lab01",
            LocalDate.now().minusDays(1).toString(), // Fecha en el pasado
            "10:00",
            "12:00",
            "Past date reservation",
            1);

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> reservationServiceImpl.createReservation(pastDateReservation));

    assertEquals(UniReservaException.INVALID_DATE, exception.getMessage());
  }

  @Test
  void shouldNotCreateReservationIfAlreadyExists() {
    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
    when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);

    when(reservationRepository.findByLabId(testReservation.getLabId()))
        .thenReturn(List.of(testReservation));

    Exception exception =
        assertThrows(
            UniReservaException.class,
            () -> {
              reservationServiceImpl.createReservation(testReservation);
            });

    assertEquals(UniReservaException.INVALID_RESERVATION, exception.getMessage());

    verify(reservationRepository, never()).save(any(Reservation.class));
  }

  @Test
  void shouldNotCreateReservationIfPriorityIsInvalid() {
    testReservation.setPriority(0);

    when(labRepository.existsById(testReservation.getLabId())).thenReturn(true);
    when(userRepository.existsById(testReservation.getUserId())).thenReturn(true);

    Exception exception =
        assertThrows(
            UniReservaException.class,
            () -> {
              reservationServiceImpl.createReservation(testReservation);
            });

    assertEquals(UniReservaException.INVALID_PRIORITY, exception.getMessage());

    verify(reservationRepository, never()).save(any(Reservation.class));
  }

  @Test
  public void shouldReturnReservationsWhenUserHasReservations() {
    String userId = "user123";
    String date1 = LocalDate.now().format(dateFormatter);
    String date2 = LocalDate.now().plusDays(1).format(dateFormatter);
    Reservation res1 = new Reservation(userId, "lab1", date1, "10:00", "11:00", "Study", 1);
    Reservation res2 = new Reservation(userId, "lab2", date2, "12:00", "13:00", "Project", 1);

    when(reservationRepository.findByUserId(userId)).thenReturn(Arrays.asList(res1, res2));
    when(userRepository.existsById(userId)).thenReturn(true);

    ApiResponse<List<Reservation>> response =
        reservationServiceImpl.getReservationsByUserId(userId);
    assertNotNull(response);
    assertNotNull(response.getData());

    assertEquals(2, response.getData().size(), "El usuario debería tener 2 reservas");
    verify(reservationRepository, times(1)).findByUserId(userId);
  }

  @Test
  public void shouldNotReturnReservationsWhenUserHasNoReservations() {
    String userId = "user456";
    when(userRepository.existsById(userId)).thenReturn(true);
    when(reservationRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

    ApiResponse<List<Reservation>> response =
        reservationServiceImpl.getReservationsByUserId(userId);
    assertNotNull(response);
    assertNotNull(response.getData());

    assertTrue(response.getData().isEmpty(), "El usuario no debería tener reservas");
    verify(reservationRepository, times(1)).findByUserId(userId);
  }

  @Test
  void getReservationsByUserId_ShouldThrowException_WhenUserNotFound() {
    when(userRepository.existsById("nonexistent-user")).thenReturn(false);

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> {
              reservationServiceImpl.getReservationsByUserId("nonexistent-user");
            });

    assertEquals(UniReservaException.USER_NOT_FOUND, exception.getMessage());
  }

  @Test
  void shouldCancelReservationWhenItExist() {
    when(reservationRepository.findById(testReservation.getId()))
        .thenReturn(Optional.of(testReservation));
    when(reservationRepository.save(testReservation)).thenReturn(testReservation);
    ApiResponse<Reservation> response =
        reservationServiceImpl.cancelReservationByReservationId(testReservation.getId());
    assertNotNull(response);
    Reservation result = response.getData();
    assertEquals("success", response.getStatus());
    assertEquals("Reservation canceled successfully", response.getMessage());
    assertEquals(ReservationStatus.CANCELED, testReservation.getStatus());
    verify(reservationRepository).save(testReservation);
  }

  @Test
  void shouldNotCancelAReservationWhenItNotExist() {
    when(reservationRepository.findById("123")).thenReturn(Optional.empty());

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> {
              reservationServiceImpl.cancelReservationByReservationId("123");
            });

    assertEquals(UniReservaException.RESERVATION_NOT_FOUND, exception.getMessage());
  }

  @Test
  void shouldNotCancelAReservationWhenAlreadyIsCancelled() {
    testReservation.setStatus(ReservationStatus.CANCELED);
    when(reservationRepository.findById("123")).thenReturn(Optional.of(testReservation));

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> {
              reservationServiceImpl.cancelReservationByReservationId("123");
            });

    assertEquals(UniReservaException.RESERVATION_ALREADY_CANCELED, exception.getMessage());
  }

  @Test
  void shouldReturnConfirmedReservationsWithinRange() {
    when(labRepository.existsById("lab01")).thenReturn(true);

    Reservation res1 =
        new Reservation("user1", "lab01", "2025-05-02", "10:00", "12:00", "Study", 1);
    res1.setStatus(ReservationStatus.CONFIRMED);

    Reservation res2 =
        new Reservation("user2", "lab01", "2025-05-05", "14:00", "16:00", "Research", 1);
    res2.setStatus(ReservationStatus.CONFIRMED);

    Reservation res3 =
        new Reservation("user3", "lab01", "2025-05-11", "10:00", "12:00", "Out of range", 1);
    res3.setStatus(ReservationStatus.CONFIRMED);

    Reservation res4 =
        new Reservation("user4", "lab01", "2025-05-04", "09:00", "10:00", "Not confirmed", 1);
    res4.setStatus(ReservationStatus.CANCELED);

    when(reservationRepository.findAll()).thenReturn(Arrays.asList(res1, res2, res3, res4));

    ApiResponse<List<Reservation>> response =
        reservationServiceImpl.getReservationsByRangeDate("lab01", "2025-05-01", "2025-05-10");

    assertNotNull(response);
    assertNotNull(response.getData());
    assertEquals(2, response.getData().size());
    assertTrue(response.getData().contains(res1));
    assertTrue(response.getData().contains(res2));
  }

  @Test
  void shouldThrowExceptionWhenLabDoesNotExist() {
    when(labRepository.existsById("lab01")).thenReturn(false);

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> {
              reservationServiceImpl.getReservationsByRangeDate(
                  "lab01", "2025-05-01", "2025-05-10");
            });

    assertEquals(UniReservaException.LAB_NOT_FOUND, exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
    when(labRepository.existsById("lab01")).thenReturn(true);

    UniReservaException exception =
        assertThrows(
            UniReservaException.class,
            () -> {
              reservationServiceImpl.getReservationsByRangeDate(
                  "lab01", "2025-05-10", "2025-05-01");
            });

    assertEquals(UniReservaException.START_DATE_BEFORE_END_DATE, exception.getMessage());
  }
}
