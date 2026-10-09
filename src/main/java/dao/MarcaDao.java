
package dao;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Marca;

public class MarcaDao {
    
    public void insertar (Marca marca){
        
        try{
        Conexion conexion = Conexion.getInstancia(); // Patron Singleton
        Connection connection = conexion.conexion();
        
        CallableStatement statement = connection.prepareCall("{Call insertar_marca(?)}");
        statement.setString(1 , marca.getNombre());
        
        // Aqui le decimos a MYSQL que le vamos a enviar un String el cual sera el nombre de la marca
        
        statement.execute();
        
            System.out.println("  ");
            System.out.println("======Marca agregada correctamente======");
        
    }catch (SQLException e){
            System.out.println("Error al insertar la marca" + e.getMessage());
        }
   }
    
    
public List<Marca> listar() {

    List<Marca> marcas = new ArrayList<>();

        try {
        Conexion conexion = Conexion.getInstancia(); // Patron Singleton
        Connection connection = conexion.conexion();

        CallableStatement statement =  connection.prepareCall("{CALL listar_marcas()}");

        ResultSet resultado = statement.executeQuery(); 
        
        // - RESULTSET --> me permite recorrer las filas/columnas que me devuelve la consulta sql 

        while (resultado.next()) {
            
            // Este while lo que me indica es que si sigue existiendo otra fila mas entra de nuevo al while
            
            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");
            Marca marca = new Marca(id, nombre);
            
            // Con los datos obtenidos creamos el objeto marca y lo agregamos a la lista
            marcas.add(marca);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar las marcas: " + e.getMessage());
    }

    return marcas;
}


    public void actualizar(Marca marca) {

        try {
            Conexion conexion = Conexion.getInstancia(); // Patron Singleton 
            Connection connection = conexion.conexion();

            CallableStatement statement = connection.prepareCall("{CALL actualizar_marca(?, ?)}");

            statement.setInt(1, marca.getId());
            statement.setString(2, marca.getNombre());

            statement.execute();
            System.out.println("Marca actualizada correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al actualizar la marca: " + e.getMessage());
        }
    }

    public void eliminar(Marca marca) {

    try {
        Conexion conexion = Conexion.getInstancia(); // Patron Singleton
        Connection connection = conexion.conexion();

        CallableStatement statement = connection.prepareCall("{CALL eliminar_marca(?)}");

        statement.setInt(1, marca.getId());
        statement.execute();
        System.out.println("Marca eliminada correctamente.");

    } catch (SQLException e) {
        System.out.println("Error al eliminar la marca: " + e.getMessage());
        }
    }

    
}


