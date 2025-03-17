package edu.eci.UniReserva.UniReserva_Backend.service.impl;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.LabService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabServiceImpl implements LabService {
  private final LabRepository labRepository;

  public LabServiceImpl(LabRepository labRepository) {
    this.labRepository = labRepository;
  }

  @Override
  public ApiResponse<List<Lab>> getLabs() {
    return ApiResponse.<List<Lab>>builder()
        .status("success")
        .message("Laboratories retrieved successfully")
        .data(labRepository.findAll())
        .build();
  }

  @Override
  public ApiResponse<String> getLabNameById(String labId) {
    Lab lab =
        labRepository
            .findById(labId)
            .orElseThrow(() -> new IllegalArgumentException("Lab not found with id: " + labId));

    return ApiResponse.<String>builder()
        .status("success")
        .message("Lab found")
        .data(lab.getName())
        .build();
  }
}
