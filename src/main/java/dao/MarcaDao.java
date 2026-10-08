
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
        Conexion conexion = new Conexion();
        Connection connection= conexion.conexion();
        
        CallableStatement statement = connection.prepareCall("{Call insertar_marca(?)}");
        statement.setString(1 , marca.getNombre());
        
        statement.execute();
        
    }catch (SQLException e){
            System.out.println("Error al insertar la marca" + e.getMessage());
        }
   }
    
    
public List<Marca> listar() {

    List<Marca> marcas = new ArrayList<>();

    try {
        Conexion conexion = new Conexion();
        Connection connection = conexion.conexion();

        CallableStatement statement =  connection.prepareCall("{CALL listar_marcas()}");

        ResultSet resultado = statement.executeQuery();

        while (resultado.next()) {
            

            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");

            Marca marca = new Marca(id, nombre);

            marcas.add(marca);
        }
        
        System.out.println("Marcas encontradas:");

            for (Marca marca : marcas) {
                System.out.println(marca);
            }
        

    } catch (SQLException e) {
        System.out.println("Error al listar las marcas: " + e.getMessage());
    }

    return marcas;
}


    
}
