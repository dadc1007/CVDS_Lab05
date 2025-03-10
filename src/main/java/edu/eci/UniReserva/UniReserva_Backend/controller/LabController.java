package edu.eci.UniReserva.UniReserva_Backend.controller;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.service.impl.LabServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.UniReserva.UniReserva_Backend.service.LabService;

import java.util.List;

@RestController
@RequestMapping("/labs")
public class LabController {
    private LabServiceImpl labService;

    public LabController(LabServiceImpl labService) {
        this.labService = labService;
    }

    @GetMapping("/allLabs")
    public List<Lab> getAllLabs() {
        return labService.getLabs();
    }

}
