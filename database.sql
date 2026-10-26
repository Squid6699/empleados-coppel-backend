CREATE TABLE empleados (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    sueldo_base_mensual NUMERIC(12, 2) NOT NULL CHECK (sueldo_base_mensual >= 0),
    fecha_ingreso DATE NOT NULL
);


-- FUNCION PARA CREAR UN EMPLEADO
CREATE OR REPLACE FUNCTION fn_empleado_crear(
    p_nombre VARCHAR(100),
    p_apellido_paterno VARCHAR(100),
    p_apellido_materno VARCHAR(100),
    p_sueldo_base_mensual NUMERIC(12, 2),
    p_fecha_ingreso DATE
)
RETURNS SETOF empleados
LANGUAGE sql
AS $$
    INSERT INTO empleados (
        nombre,
        apellido_paterno,
        apellido_materno,
        sueldo_base_mensual,
        fecha_ingreso
    )
    VALUES (
        p_nombre,
        p_apellido_paterno,
        p_apellido_materno,
        p_sueldo_base_mensual,
        p_fecha_ingreso
    )
    RETURNING *;
$$;

-- FUNCION PARA LISTAR TODOS LOS EMPLEADOS
CREATE OR REPLACE FUNCTION fn_empleados_listar()
RETURNS SETOF empleados
LANGUAGE sql
STABLE
AS $$
    SELECT *
    FROM empleados
    ORDER BY id;
$$;

-- FUNCION PARA OBTENER UN EMPLEADO POR ID
CREATE OR REPLACE FUNCTION fn_empleado_obtener(
    p_id INTEGER
)
RETURNS SETOF empleados
LANGUAGE sql
STABLE
AS $$
    SELECT *
    FROM empleados
    WHERE id = p_id;
$$;

-- FUNCION PARA ACTUALIZAR UN EMPLEADO
CREATE OR REPLACE FUNCTION fn_empleado_actualizar(
    p_id INTEGER,
    p_nombre VARCHAR(100),
    p_apellido_paterno VARCHAR(100),
    p_apellido_materno VARCHAR(100),
    p_sueldo_base_mensual NUMERIC(12, 2),
    p_fecha_ingreso DATE
)
RETURNS SETOF empleados
LANGUAGE sql
AS $$
    UPDATE empleados
    SET
        nombre = p_nombre,
        apellido_paterno = p_apellido_paterno,
        apellido_materno = p_apellido_materno,
        sueldo_base_mensual = p_sueldo_base_mensual,
        fecha_ingreso = p_fecha_ingreso
    WHERE id = p_id
    RETURNING *;
$$;

-- FUNCION PARA ELIMINAR UN EMPLEADO
CREATE OR REPLACE FUNCTION fn_empleado_eliminar(
    p_id INTEGER
)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM empleados
    WHERE id = p_id;

    RETURN FOUND;
END;
$$;

-- FUNCION PARA OBTENER LAS VACACIONES DE UN EMPLEADO
CREATE OR REPLACE FUNCTION fn_empleado_calcular_vacaciones(
    p_id INTEGER
)
RETURNS INTEGER
LANGUAGE sql
STABLE
AS $$
    SELECT
        CASE
            WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '1 year'
                THEN 0
            WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '2 years'
                THEN 12
            WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '3 years'
                THEN 14
            WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '4 years'
                THEN 16
            WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '5 years'
                THEN 18
            WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '6 years'
                THEN 20
            ELSE 22
        END
    FROM empleados
    WHERE id = p_id;
$$;

-- FUNCION PARA OBTENER EL AGUINALDO DE UN EMPLEADO

CREATE OR REPLACE FUNCTION fn_empleado_calcular_aguinaldo(
    p_id INTEGER
)
RETURNS NUMERIC(12, 2)
LANGUAGE sql
STABLE
AS $$
    SELECT
        ROUND(
            CASE
                -- Menos de 6 meses: no aplica la regla indicada
                WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '6 months' THEN 0::NUMERIC

                -- De 6 meses a menos de 1 año: 10% del sueldo base mensual (sin prorrateo)
                WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '1 year' THEN sueldo_base_mensual * 0.10

                -- De 1 año a menos de 2 años: 15 días
                WHEN AGE(CURRENT_DATE, fecha_ingreso) < INTERVAL '2 years' THEN (sueldo_base_mensual / 30.0) * 15
                -- 2 años o más: 30 días
                ELSE (sueldo_base_mensual / 30.0) * 30
            END,
            2
        )
    FROM empleados
    WHERE id = p_id;
$$;
