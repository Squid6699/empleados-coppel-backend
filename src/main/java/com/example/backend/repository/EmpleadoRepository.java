package com.example.backend.repository;

import com.example.backend.model.Empleado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmpleadoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Empleado> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM fn_empleados_listar()",
                this::mapRow
        );
    }

    public Optional<Empleado> findById(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM fn_empleado_obtener(?)",
                this::mapRow,
                id
        ).stream().findFirst();
    }

    public Empleado create(String nombre, String apellidoPaterno, String apellidoMaterno,
                           BigDecimal sueldoBaseMensual, Date fechaIngreso) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM fn_empleado_crear(?, ?, ?, ?, ?)",
                this::mapRow,
                nombre, apellidoPaterno, apellidoMaterno, sueldoBaseMensual, fechaIngreso
        );
    }

    public Optional<Empleado> update(int id, String nombre, String apellidoPaterno, String apellidoMaterno,
                                     BigDecimal sueldoBaseMensual, Date fechaIngreso) {
        return jdbcTemplate.query(
                "SELECT * FROM fn_empleado_actualizar(?, ?, ?, ?, ?, ?)",
                this::mapRow,
                id, nombre, apellidoPaterno, apellidoMaterno, sueldoBaseMensual, fechaIngreso
        ).stream().findFirst();
    }

    public boolean delete(int id) {
        Boolean deleted = jdbcTemplate.queryForObject(
                "SELECT fn_empleado_eliminar(?)",
                Boolean.class,
                id
        );
        return Boolean.TRUE.equals(deleted);
    }

    public Optional<Integer> calculateVacations(int id) {
        return jdbcTemplate.query(
                "SELECT fn_empleado_calcular_vacaciones(?)",
                (resultSet, rowNum) -> (Integer) resultSet.getObject(1),
                id
        ).stream().findFirst();
    }

    public Optional<BigDecimal> calculateBonus(int id) {
        return jdbcTemplate.query(
                "SELECT fn_empleado_calcular_aguinaldo(?)",
                (resultSet, rowNum) -> resultSet.getBigDecimal(1),
                id
        ).stream().findFirst();
    }

    private Empleado mapRow(java.sql.ResultSet resultSet, int rowNum) throws java.sql.SQLException {
        return new Empleado(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("apellido_paterno"),
                resultSet.getString("apellido_materno"),
                resultSet.getBigDecimal("sueldo_base_mensual"),
                resultSet.getDate("fecha_ingreso").toLocalDate()
        );
    }
}
