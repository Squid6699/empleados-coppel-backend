package com.example.backend.controller;

import com.example.backend.dto.EmpleadoRequest;
import com.example.backend.model.Empleado;
import com.example.backend.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Empleado> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Empleado findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado create(@Valid @RequestBody EmpleadoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Empleado update(@PathVariable int id, @Valid @RequestBody EmpleadoRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}/vacaciones")
    public Map<String, Integer> calculateVacations(@PathVariable int id) {
        return Map.of("diasVacaciones", service.calculateVacations(id));
    }

    @GetMapping("/{id}/aguinaldo")
    public Map<String, BigDecimal> calculateBonus(@PathVariable int id) {
        return Map.of("aguinaldo", service.calculateBonus(id));
    }
}
