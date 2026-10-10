
package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Usuario;

public class ClienteDao {

    public List<Cliente> listar() {
        
        List<Cliente> clientes = new ArrayList<>();

        try {
            Conexion conexion = Conexion.getInstancia();
            Connection connection = conexion.conexion();

            CallableStatement statement =connection.prepareCall("{CALL listar_clientes()}");

            ResultSet resultado = statement.executeQuery();

            while (resultado.next()) {
                
            
            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");
            long identificacion = resultado.getLong("identificacion");
            String telefono = resultado.getString("telefono");
            int usuarioId = resultado.getInt("usuario_id");
            String correo = resultado.getString("correo");

            Usuario usuario = new Usuario(usuarioId,correo,"",Usuario.Rol.CLIENTE);
            Cliente cliente = new Cliente(id,usuario, nombre,identificacion,telefono);

            clientes.add(cliente);

            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar los clientes: " + e.getMessage()
            );
        }

        return clientes;
    }

    public void actualizar(Cliente cliente) {
        try {
            Conexion conexion = Conexion.getInstancia();
            Connection connection = conexion.conexion();

            CallableStatement statement = connection.prepareCall("{CALL actualizar_cliente(?, ?, ?, ?)}");

            statement.setInt(1, cliente.getId());
            statement.setString(2, cliente.getNombre());
            statement.setLong(3, cliente.getIdentificacion());
            statement.setString(4, cliente.getTelefono());

            statement.execute();

            System.out.println("Solicitud de actualización ejecutada.");

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar el cliente: " + e.getMessage()
            );
        }
    }

    public void eliminar(int id) {
        try {
            Conexion conexion = Conexion.getInstancia();
            Connection connection = conexion.conexion();

            CallableStatement statement =connection.prepareCall("{CALL eliminar_cliente(?)}");

            statement.setInt(1, id);
            statement.execute();

            System.out.println("Solicitud de eliminación ejecutada.");

        } catch (SQLException e) {
            System.out.println(
                    "Error al eliminar el cliente: " + e.getMessage()
            );
        }
    }
}
