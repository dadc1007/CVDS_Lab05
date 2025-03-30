package edu.eci.UniReserva.UniReserva_Backend.exceptions;

public class UniReservaException extends RuntimeException {
  public static final String INVALID_PASSWORD_EMAIL = "Contraseña o correo incorrecto";
  public static final String EMAIL_EXISTS = "El correo ya existe";
  public static final String INVALID_PASSWORD = "Contraseña incorrecta";
  public static final String LAB_NOT_FOUND = "Laboratorio no encontrado";
  public static final String USER_NOT_FOUND = "Usuario no encontrado";
  public static final String INVALID_TIME = "No se puede crear una reservacion con una hora pasada";
  public static final String INVALID_DATE = "No se puede seleccionar una fecha pasada para la reservacion.";
  public static final String INVALID_RESERVATION = "Ya existe una reservacion en la hora seleccionada";
  public static final String INVALID_PRIORITY = "La prioridad debe estar en el rango 1-5";
  public static final String RESERVATION_NOT_FOUND = "Reservacion no encontrada";
  public static final String RESERVATION_ALREADY_CANCELED = "La reservacion ya esta cancelada";
  public static final String START_DATE_BEFORE_END_DATE = "La fecha de inicio no puede ser posterior a la fecha de finalizacion";
  public static final String EMAIL_CANNOT_BE_UPDATED = "El correo no se puede modificar";
  public static final String USER_CANNOT_BE_DELETED = "No se puede eliminar un usuario con reservaciones hechas";

  public UniReservaException(String message) {
    super(message);
  }
}
