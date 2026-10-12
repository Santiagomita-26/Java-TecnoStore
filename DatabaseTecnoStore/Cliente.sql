

--PROCEDIMIENTO REGISTRAR CLIENTE--

DELIMITER //

CREATE PROCEDURE registrar_cliente(
    IN p_correo VARCHAR(100),
    IN p_password VARCHAR(100),
    IN p_nombre VARCHAR(100),
    IN p_identificacion BIGINT,
    IN p_telefono VARCHAR(20)
)
BEGIN
    DECLARE v_usuario_id INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;

    INSERT INTO usuarios (correo, password, rol)
    VALUES (p_correo, p_password, 'CLIENTE');

    SET v_usuario_id = LAST_INSERT_ID();

    INSERT INTO clientes (
        usuario_fk,
        nombre,
        identificacion,
        telefono
    )
    VALUES (
        v_usuario_id,
        p_nombre,
        p_identificacion,
        p_telefono
    );

    COMMIT;
END //

DELIMITER ;



---------- CRUD DEL ADMINISTRADOR ---------------------



USE tecnostoremendoza;

--PROCEDIMIENTO LISTAR CLIENTE--

DELIMITER //

CREATE PROCEDURE listar_clientes()
BEGIN

    SELECT
        c.id,
        c.nombre,
        c.identificacion,
        c.telefono,
        u.id AS usuario_id,
        u.correo
    FROM clientes c
    INNER JOIN usuarios u ON c.usuario_fk = u.id
    ORDER BY c.id;

END //

DELIMITER ;


---PROCEDIMIENTO ACTUALIZAR CLIENTE--

DELIMITER //

CREATE PROCEDURE actualizar_cliente(
    IN p_id INT,
    IN p_nombre VARCHAR(100),
    IN p_identificacion BIGINT,
    IN p_telefono VARCHAR(20)
)

BEGIN

    UPDATE clientes
    SET
        nombre = p_nombre,
        identificacion = p_identificacion,
        telefono = p_telefono
    WHERE id = p_id;

END //

DELIMITER ;

--PROCEDIMIENTO ELIMINAR CLIENTE--

DELIMITER //

CREATE PROCEDURE eliminar_cliente(
    IN p_id INT
)
BEGIN
    DELETE FROM clientes
    WHERE id = p_id;
END //

DELIMITER ;

-- PROCEDIMIENTO BUSCAR CLIENTE POR EL USUARIO --


DELIMITER //

CREATE PROCEDURE buscar_cliente_por_usuario(
    IN p_usuario_id INT
)

BEGIN

    SELECT
        c.id,
        c.nombre,
        c.identificacion,
        c.telefono,
        u.correo
    FROM clientes c
    INNER JOIN usuarios u ON c.usuario_fk = u.id
    WHERE c.usuario_fk = p_usuario_id;
END //

DELIMITER ;