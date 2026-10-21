
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    // Aqui usamoos el patron de diseño Singleton

    private static Conexion instancia;

    private Conexion() {
    }

    public static Conexion getInstancia() { 
                                           // Si todavia no tengo un objeto conexion crealo
        if (instancia == null) {           // O si ya tengo uno devuelveme el que ya existe
            instancia = new Conexion();    // Asi no haremos en todo momento new Conexion();
        }
        return instancia;
    }

    public Connection conexion() {
        Connection c = null;

        try {
            c = DriverManager.getConnection( "jdbc:mysql://localhost:3306/tecnostoremendoza", "campus2023", "campus2023");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }
}