

--PROCEDIMIENTO INSERTAR CELULAR--

DELIMITER //

CREATE PROCEDURE insertar_celular(
    IN p_nombre VARCHAR(100),
    IN p_marca_fk INT,
    IN p_sistema_operativo VARCHAR(50),
    IN p_gama VARCHAR(10),
    IN p_precio DECIMAL(10,2),
    IN p_stock INT
)

BEGIN

    INSERT INTO celulares (
        nombre,
        marca_fk,
        sistema_operativo,
        gama,
        precio,
        stock
    )
    VALUES (
        p_nombre,
        p_marca_fk,
        p_sistema_operativo,
        p_gama,
        p_precio,
        p_stock
    );

END //

DELIMITER ;

--PROCEDIMIENTO LISTAR CELULAR--

DELIMITER //

CREATE PROCEDURE listar_celulares()

BEGIN

    SELECT
        c.id,
        c.nombre,
        c.marca_fk,
        m.nombre AS marca_nombre,
        c.sistema_operativo,
        c.gama,
        c.precio,
        c.stock
    FROM celulares c
    INNER JOIN marcas m ON c.marca_fk = m.id
    ORDER BY c.id;

END //

DELIMITER ;


--PROCEDIMIENTO ACTUALIZAR CELULAR--


DELIMITER //

CREATE PROCEDURE actualizar_celular(
    IN p_id INT,
    IN p_nombre VARCHAR(100),
    IN p_marca_fk INT,
    IN p_sistema_operativo VARCHAR(50),
    IN p_gama VARCHAR(10),
    IN p_precio DECIMAL(10,2),
    IN p_stock INT
)

BEGIN

    UPDATE celulares
    SET
        nombre = p_nombre,
        marca_fk = p_marca_fk,
        sistema_operativo = p_sistema_operativo,
        gama = p_gama,
        precio = p_precio,
        stock = p_stock
    WHERE id = p_id;

END //

DELIMITER ;

--PROCEDIMIENTO ELIMINAR CELULAR--

DELIMITER //

CREATE PROCEDURE eliminar_celular(
    IN p_id INT
)
BEGIN
    DELETE FROM celulares
    WHERE id = p_id;
END //

DELIMITER ;



-- PROCEDIMIENTOS PARA FILTRAR POR MARCA --

DELIMITER //

CREATE PROCEDURE filtrar_celulares_marca(
    IN p_marca_fk INT
)

BEGIN

    SELECT
        c.id,
        c.nombre,
        c.marca_fk,
        m.nombre AS marca_nombre,
        c.sistema_operativo,
        c.gama,
        c.precio,
        c.stock
    FROM celulares c
    INNER JOIN marcas m ON c.marca_fk = m.id
    WHERE c.marca_fk = p_marca_fk
    ORDER BY c.id;

END //

DELIMITER ;


-- PROCEDIMIENTOS PARA FILTRAR POR GAMA --

DELIMITER //

CREATE PROCEDURE filtrar_celulares_gama(
    IN p_gama VARCHAR(10)
)
BEGIN
    SELECT
        c.id,
        c.nombre,
        c.marca_fk,
        m.nombre AS marca_nombre,
        c.sistema_operativo,
        c.gama,
        c.precio,
        c.stock
    FROM celulares c
    INNER JOIN marcas m ON c.marca_fk = m.id
    WHERE c.gama = p_gama
    ORDER BY c.id;

END //

DELIMITER ;


-- PROCEDIMIENTOS PARA FILTRAR POR MARCA --

DELIMITER //

CREATE PROCEDURE filtrar_celulares_sistema(
    IN p_sistema_operativo VARCHAR(50)
)
BEGIN
    SELECT
        c.id,
        c.nombre,
        c.marca_fk,
        m.nombre AS marca_nombre,
        c.sistema_operativo,
        c.gama,
        c.precio,
        c.stock
    FROM celulares c
    INNER JOIN marcas m ON c.marca_fk = m.id
    WHERE c.sistema_operativo = p_sistema_operativo
    ORDER BY c.id;
END //

DELIMITER ;



-- PROCEDIMIENTOS CELULARES MAS VENDIDOS --

DELIMITER //

CREATE PROCEDURE reporte_celulares_mas_vendidos()

BEGIN

    SELECT
        c.id,
        c.nombre,
        m.nombre AS marca,
        SUM(dv.cantidad) AS unidades_vendidas
    FROM detalle_ventas dv
    INNER JOIN celulares c ON dv.celular_fk = c.id
    INNER JOIN marcas m ON c.marca_fk = m.id
    GROUP BY c.id, c.nombre, m.nombre
    ORDER BY unidades_vendidas DESC, c.nombre ASC
    LIMIT 3;

END //

DELIMITER ;