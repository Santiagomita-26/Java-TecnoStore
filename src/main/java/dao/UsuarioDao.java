
package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Cliente;


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
    
    public boolean registrarCliente(Usuario usuario, Cliente cliente) {

        try {
            Conexion conexion = Conexion.getInstancia();

            try (
                Connection connection = conexion.conexion();
                CallableStatement statement = connection.prepareCall("{CALL registrar_cliente(?, ?, ?, ?, ?)}")
            ) {
                statement.setString(1, usuario.getCorreo());
                statement.setString(2, usuario.getPassword());
                statement.setString(3, cliente.getNombre());
                statement.setLong(4, cliente.getIdentificacion());
                statement.setString(5, cliente.getTelefono());

                statement.execute();

                return true;
            }

        } catch (SQLException e) {
            System.out.println(
                "Error al registrar el cliente: " + e.getMessage()
            );
            return false;
        }
    }
    
    public Cliente buscarPorUsuario(int usuarioId) {

    Cliente cliente = null;

    try {
        Conexion conexion = Conexion.getInstancia();
        Connection connection = conexion.conexion();

        CallableStatement statement = connection.prepareCall("{CALL buscar_cliente_por_usuario(?)}");

        statement.setInt(1, usuarioId);

        ResultSet resultado = statement.executeQuery();

        if (resultado.next()) {
            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");
            long identificacion = resultado.getLong("identificacion");
            String telefono = resultado.getString("telefono");
            String correo = resultado.getString("correo");

            Usuario usuario = new Usuario(usuarioId, correo, "", Usuario.Rol.CLIENTE);

            cliente = new Cliente(id, usuario, nombre, identificacion, telefono);
        }

        resultado.close();
        statement.close();
        connection.close();

        } catch (SQLException e) {
            System.out.println("Error al buscar el cliente: " + e.getMessage());
    }

        return cliente;
    }
    
    
}
