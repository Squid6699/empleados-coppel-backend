package com.example.backend.service;

import com.example.backend.dto.EmpleadoRequest;
import com.example.backend.model.Empleado;
import com.example.backend.repository.EmpleadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repository;

    public EmpleadoService(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public List<Empleado> findAll() {
        return repository.findAll();
    }

    public Empleado findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> notFound(id));
    }

    public Empleado create(EmpleadoRequest request) {
        return repository.create(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.sueldoBaseMensual(),
                Date.valueOf(request.fechaIngreso())
        );
    }

    public Empleado update(int id, EmpleadoRequest request) {
        return repository.update(
                id,
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.sueldoBaseMensual(),
                Date.valueOf(request.fechaIngreso())
        ).orElseThrow(() -> notFound(id));
    }

    public void delete(int id) {
        if (!repository.delete(id)) {
            throw notFound(id);
        }
    }

    public int calculateVacations(int id) {
        findById(id);
        return repository.calculateVacations(id)
                .orElseThrow(() -> notFound(id));
    }

    public BigDecimal calculateBonus(int id) {
        findById(id);
        return repository.calculateBonus(id)
                .orElseThrow(() -> notFound(id));
    }

    private ResponseStatusException notFound(int id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el empleado con id " + id);
    }
}
