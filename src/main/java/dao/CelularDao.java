

package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Celular;
import model.Marca;

public class CelularDao {
    
    
public void insertar(Celular celular) {
    try {
        Conexion conexion = Conexion.getInstancia();
        Connection connection = conexion.conexion();

        CallableStatement statement = connection.prepareCall("{CALL insertar_celular(?, ?, ?, ?, ?, ?)}");

        statement.setString(1, celular.getNombre());
        statement.setInt(2, celular.getMarca().getId());
        statement.setString(3, celular.getSistemaOperativo());
        statement.setString(4, celular.getGama().name());
        statement.setDouble(5, celular.getPrecio());
        statement.setInt(6, celular.getStock());

        statement.execute();

        System.out.println("Celular registrado correctamente.");

    } catch (SQLException e) {
        System.out.println("Error al insertar el celular: " + e.getMessage());
    }
}


public List<Celular> listar() {
    List<Celular> celulares = new ArrayList<>();

    try {
        Conexion conexion = Conexion.getInstancia();
        Connection connection = conexion.conexion();

        CallableStatement statement = connection.prepareCall("{CALL listar_celulares()}");

        ResultSet resultado = statement.executeQuery();

        while (resultado.next()) {
            
            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");

            int idMarca = resultado.getInt("marca_fk");
            String nombreMarca = resultado.getString("marca_nombre");

            Marca marca = new Marca(idMarca, nombreMarca);
            
            String sistemaOperativo = resultado.getString("sistema_operativo");

            Celular.Gama gama = Celular.Gama.valueOf(resultado.getString("gama"));

            double precio = resultado.getDouble("precio");
            int stock = resultado.getInt("stock");

            Celular celular = new Celular(id,nombre, marca,sistemaOperativo,gama,precio,stock);
            celulares.add(celular);
        }

    } catch (SQLException e) {
        System.out.println("Error al listar los celulares: " + e.getMessage());
    }

    return celulares;
}

    public void actualizar(Celular celular) {
    
        try {
            Conexion conexion = Conexion.getInstancia();
            Connection connection = conexion.conexion();

            CallableStatement statement = connection.prepareCall("{CALL actualizar_celular(?, ?, ?, ?, ?, ?, ?)}");

            statement.setInt(1, celular.getId());
            statement.setString(2, celular.getNombre());
            statement.setInt(3, celular.getMarca().getId());
            statement.setString(4, celular.getSistemaOperativo());
            statement.setString(5, celular.getGama().name());
            statement.setDouble(6, celular.getPrecio());
            statement.setInt(7, celular.getStock());

            statement.execute();

            System.out.println("Solicitud de actualización ejecutada.");

        } catch (SQLException e) {
            System.out.println("Error al actualizar el celular: " + e.getMessage());
        }

      }

    public void eliminar(int id) {
        try {
            
            Conexion conexion = Conexion.getInstancia();
            Connection connection = conexion.conexion();

            CallableStatement statement = connection.prepareCall("{CALL eliminar_celular(?)}");

            statement.setInt(1, id);
            statement.execute();

            System.out.println("Solicitud de eliminación ejecutada.");

        } catch (SQLException e) {
            System.out.println("Error al eliminar el celular: " + e.getMessage());
        }
    }

}
