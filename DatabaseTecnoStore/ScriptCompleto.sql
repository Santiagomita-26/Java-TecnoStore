CREATE DATABASE IF NOT EXISTS tecnostoremendoza;

USE tecnostoremendoza;



DROP TABLE IF EXISTS detalle_ventas;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS celulares;
DROP TABLE IF EXISTS marcas;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS usuarios;




CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'CLIENTE') NOT NULL
);



CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_fk INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(30) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,

    CONSTRAINT fk_cliente_usuario
        FOREIGN KEY (usuario_fk)
        REFERENCES usuarios(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);



CREATE TABLE marcas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);




CREATE TABLE celulares (
    id INT AUTO_INCREMENT PRIMARY KEY,
    marca_fk INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    sistema_operativo VARCHAR(50) NOT NULL,
    gama ENUM('ALTA', 'MEDIA', 'BAJA') NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,

    CONSTRAINT fk_celular_marca
        FOREIGN KEY (marca_fk)
        REFERENCES marcas(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_celular_precio
        CHECK (precio > 0),

    CONSTRAINT chk_celular_stock
        CHECK (stock >= 0)
);




CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_fk INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_venta_cliente
        FOREIGN KEY (cliente_fk)
        REFERENCES clientes(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_venta_total
        CHECK (total >= 0)
);




CREATE TABLE detalle_ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_fk INT NOT NULL,
    celular_fk INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_detalle_venta
        FOREIGN KEY (venta_fk)
        REFERENCES ventas(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_detalle_celular
        FOREIGN KEY (celular_fk)
        REFERENCES celulares(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_detalle_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_detalle_subtotal
        CHECK (subtotal >= 0)
);



INSERT INTO usuarios (correo, password, rol)
VALUES ('admin@tecnostore.com', 'admin123', 'ADMIN');


DELIMITER //

CREATE PROCEDURE insertar_marca(
    IN p_nombre VARCHAR(100)
)

BEGIN

    INSERT INTO marcas(nombre)
    VALUES (p_nombre);

END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE listar_marcas()

BEGIN
    SELECT id, nombre
    FROM marcas;

END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE eliminar_marca(
    IN p_id INT
)
BEGIN

    DELETE FROM marcas
    WHERE id = p_id;

END //

DELIMITER ;



DELIMITER //

CREATE PROCEDURE eliminar_marca(
    IN p_id INT
)
BEGIN
    DELETE FROM marcas
    WHERE id = p_id;
    
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE login_usuario(
    IN p_correo VARCHAR(100),
    IN p_password VARCHAR(100)
)
BEGIN

    SELECT id, correo, password, rol
    FROM usuarios
    WHERE correo = p_correo
      AND password = p_password;
      
END //

DELIMITER ;


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


DELIMITER //

CREATE PROCEDURE eliminar_cliente(
    IN p_id INT
)
BEGIN
    DELETE FROM clientes
    WHERE id = p_id;

END //

DELIMITER ;


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


DELIMITER //

CREATE PROCEDURE eliminar_celular(
    IN p_id INT
)
BEGIN
    DELETE FROM celulares
    WHERE id = p_id;

END //

DELIMITER ;


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