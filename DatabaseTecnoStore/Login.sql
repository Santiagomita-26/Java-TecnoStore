--PROCEDIMIENTO LOGIN--

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