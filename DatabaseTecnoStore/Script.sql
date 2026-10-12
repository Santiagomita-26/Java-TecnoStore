CREATE DATABASE IF NOT EXISTS tecnostoremendoza;

USE tecnostoremendoza;

-- =====================================================
-- LIMPIAR BASE DE DATOS
-- =====================================================

DROP TABLE IF EXISTS detalle_ventas;
DROP TABLE IF EXISTS ventas;
DROP TABLE IF EXISTS celulares;
DROP TABLE IF EXISTS marcas;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS usuarios;


-- =====================================================
-- TABLA: USUARIOS
-- =====================================================

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'CLIENTE') NOT NULL
);


-- =====================================================
-- TABLA: CLIENTES
-- =====================================================

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


-- =====================================================
-- TABLA: MARCAS
-- =====================================================

CREATE TABLE marcas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);


-- =====================================================
-- TABLA: CELULARES
-- =====================================================

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


-- =====================================================
-- TABLA: VENTAS
-- =====================================================

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


-- =====================================================
-- TABLA: DETALLE_VENTAS
-- =====================================================

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


-- =====================================================
-- USUARIO ADMINISTRADOR INICIAL
-- =====================================================

INSERT INTO usuarios (correo, password, rol)
VALUES ('admin@tecnostore.com', 'admin123', 'ADMIN');


-- =====================================================
-- MARCAS INICIALES
-- =====================================================



-- =====================================================
-- VERIFICACIÓN
-- =====================================================

SHOW TABLES;

DESCRIBE usuarios;
DESCRIBE clientes;
DESCRIBE marcas;
DESCRIBE celulares;
DESCRIBE ventas;
DESCRIBE detalle_ventas;