package com.example.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoRequest(
        @NotBlank String nombre,
        @NotBlank String apellidoPaterno,
        String apellidoMaterno,
        @NotNull @DecimalMin("0.00") BigDecimal sueldoBaseMensual,
        @NotNull @PastOrPresent LocalDate fechaIngreso
) {
}
