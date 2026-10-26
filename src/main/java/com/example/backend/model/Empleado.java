package com.example.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Empleado(
        Integer id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        BigDecimal sueldoBaseMensual,
        LocalDate fechaIngreso
){}
