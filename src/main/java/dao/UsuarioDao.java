
package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioDao {

    public Usuario login(String correo, String password) {

        Usuario usuario = null;

      try {
        Conexion conexion = Conexion.getInstancia();
          Connection connection = conexion.conexion();

            CallableStatement statement = connection.prepareCall("{CALL login_usuario(?, ?)}");

            statement.setString(1, correo);
            statement.setString(2, password);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {

            int id = resultado.getInt("id");
            String correoDB = resultado.getString("correo");
            String passwordDB = resultado.getString("password");
            Usuario.Rol rol = Usuario.Rol.valueOf(resultado.getString("rol"));

            usuario = new Usuario(id, correoDB, passwordDB, rol);
            }

            resultado.close(); // libera recurso y asi leer los resultados
            statement.close();  //  libera recursos del procedimiento
            connection.close(); // libera la conexion con sql
            
            // si no se cierran puede haber problemas al ajecutar tantas consultas 

        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }
        return usuario;
    }
}
