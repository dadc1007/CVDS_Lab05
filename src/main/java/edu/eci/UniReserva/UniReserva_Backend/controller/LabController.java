package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.model.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.UniReserva.UniReserva_Backend.service.LabService;

import java.util.List;

@RestController
@RequestMapping("/labs")
@RequiredArgsConstructor
public class LabController {
  private final LabService labService;

  @GetMapping("/allLabs")
  public ResponseEntity<ApiResponse<List<Lab>>> getAllLabs() {
    return ResponseEntity.ok(labService.getLabs());
  }

  @GetMapping("/labName/{labId}")
  public ResponseEntity<ApiResponse<String>> getLabNameById(@PathVariable String labId) {
    return ResponseEntity.ok(labService.getLabNameById(labId));
  }

  @GetMapping("/lab/{labId}")
  public ResponseEntity<ApiResponse<Lab>> getLaById(@PathVariable String labId) {
    return ResponseEntity.ok(labService.getLabById(labId));
  }
}
