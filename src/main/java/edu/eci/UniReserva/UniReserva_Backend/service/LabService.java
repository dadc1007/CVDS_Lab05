package edu.eci.UniReserva.UniReserva_Backend.service;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LabService {
  ApiResponse<List<Lab>> getLabs();

  ApiResponse<String> getLabNameById(String id);

  ApiResponse<Lab> getLabById(String id);
}
