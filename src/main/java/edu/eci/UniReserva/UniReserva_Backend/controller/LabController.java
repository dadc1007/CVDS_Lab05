package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.service.LabService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labs")
public class LabController {
    private LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }
}
