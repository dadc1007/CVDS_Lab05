package edu.eci.UniReserva.UniReserva_Backend.service.impl;

import edu.eci.UniReserva.UniReserva_Backend.model.Lab;
import edu.eci.UniReserva.UniReserva_Backend.repository.LabRepository;
import edu.eci.UniReserva.UniReserva_Backend.service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabServiceImpl implements LabService {
    private final LabRepository labRepository;

    public LabServiceImpl(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    @Override
    public List<Lab> getLabs() {
        return labRepository.findAll();
    }

}
