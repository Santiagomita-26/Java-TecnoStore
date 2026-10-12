



--PROCEDIMIENTO REGISTRAR VENTA--

DELIMITER //

CREATE PROCEDURE registrar_venta(
    IN p_cliente_fk INT,
    IN p_total DECIMAL(10,2),
    OUT p_venta_id INT
)
BEGIN
    INSERT INTO ventas (cliente_fk, total)
    VALUES (p_cliente_fk, p_total);

    SET p_venta_id = LAST_INSERT_ID();

END //

DELIMITER :

--PROCEDIMIENTO INSERTAR DETALLE DE VENTA --

DELIMITER //

CREATE PROCEDURE insertar_detalle_venta(
    IN p_venta_fk INT,
    IN p_celular_fk INT,
    IN p_cantidad INT,
    IN p_subtotal DECIMAL(10,2)
)
BEGIN
    INSERT INTO detalle_ventas (
        venta_fk,
        celular_fk,
        cantidad,
        subtotal
    )
    VALUES (
        p_venta_fk,
        p_celular_fk,
        p_cantidad,
        p_subtotal
    );

END //

DELIMITER ;

--PROCEDIMIENTO DESCONTAR STOCK--

DELIMITER //

CREATE PROCEDURE descontar_stock(
    IN p_celular_fk INT,
    IN p_cantidad INT
)
BEGIN
    UPDATE celulares
    SET stock = stock - p_cantidad
    WHERE id = p_celular_fk
      AND stock >= p_cantidad;

    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Stock insuficiente o celular inexistente';
    END IF;

END //

DELIMITER ;

--PROCEDIMIENTO LISTAR LAS COMPRAS DEL CLIENTE--

DELIMITER //

CREATE PROCEDURE listar_compras_cliente(IN p_cliente_id INT)

BEGIN
    SELECT
        v.id AS venta_id,
        v.fecha,
        v.total,
        ce.nombre AS nombre_celular,
        dv.cantidad,
        dv.subtotal
    FROM ventas v
    INNER JOIN detalle_ventas dv
        ON v.id = dv.venta_fk
    INNER JOIN celulares ce
        ON dv.celular_fk = ce.id
    WHERE v.cliente_fk = p_cliente_id
    ORDER BY v.fecha DESC, v.id DESC;

END //

DELIMITER ;

-- PROCEDIMIENTO VENTAS TOTALES --


DELIMITER //

CREATE PROCEDURE reporte_ventas_totales()
BEGIN
    SELECT
        v.id AS venta_id,
        v.fecha,
        c.nombre AS nombre_cliente,
        c.identificacion,
        c.telefono,
        ce.nombre AS nombre_celular,
        dv.cantidad,
        dv.subtotal,
        v.total
    FROM ventas v
    INNER JOIN clientes c ON v.cliente_fk = c.id
    INNER JOIN detalle_ventas dv ON v.id = dv.venta_fk
    INNER JOIN celulares ce ON dv.celular_fk = ce.id
    ORDER BY v.fecha DESC, v.id DESC;
END //

DELIMITER ;
