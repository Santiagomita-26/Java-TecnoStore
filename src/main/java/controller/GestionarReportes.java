package controller;

import dao.CelularDao;
import dao.ComprasDao;
import dao.Conexion;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import view.Opciones;


public class GestionarReportes {

    private final Opciones opciones = new Opciones();
    private final CelularDao celularDao = new CelularDao();
    private final ComprasDao comprasDao = new ComprasDao();
    

    public void iniciar() {

        int opcion;

        do {
            opcion = opciones.Reportes();

            switch (opcion) {
                case 1 -> reporteCelularesMasVendidos();
                case 2 -> celularesStockBajo();
                case 3 -> reporteVentasTotales();
                case 4 -> comprasDao.exportarVentasDat();
                case 5 -> comprasDao.leerVentasDat();
                case 6 -> exportarStockDat(); // EXAMEN
                case 7 -> leerReporteStockDat(); // EXAMEN
                case 8 -> System.out.println("Saliendo de los reportes...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 8);
    }

    private void celularesStockBajo() {

        System.out.println("\n--- CELULARES CON STOCK BAJO ---");

        List<Celular> celulares = celularDao.listar();

        List<Celular> filtrados = celulares.stream().filter(celular -> celular.getStock() < 5).toList();

        if (filtrados.isEmpty()) {
            System.out.println("No hay celulares con stock inferior a 5 unidades.");
            return;
        }

        filtrados.forEach(celular -> {System.out.println(celular);System.out.println("------------------------------");});
    }

        
    public void exportarStockDat() {  // ----------------EXAMEN 

    List<String> lineasReporte = new ArrayList<>();
    Path ruta = Path.of("reporte_stock.dat");

    try {
        Conexion conexion = Conexion.getInstancia();
        Connection connection = conexion.conexion();

        if (connection == null) {
            System.out.println("No fue posible conectar con la base de datos.");
            return;
        }
        try (Connection conn = connection;
             CallableStatement sentencia = conn.prepareCall("{CALL filtrar_celulares_stock()}");
             ResultSet resultado = sentencia.executeQuery()) {
            
            boolean hayCelulares = false;


            lineasReporte.add("========== REPORTE DE STOCKS BAJO TECNOSTORE ==========");
            lineasReporte.add("");

            while (resultado.next()) {


                    hayCelulares = true;

                    lineasReporte.add("CELULAR = ");
                    lineasReporte.add("Nombre: " + resultado.getString("c.nombre"));
                    lineasReporte.add("marca: " + resultado.getInt("c.marca_fk"));
                    lineasReporte.add("Sistema Operativo: "+ resultado.getString("c.sistema_operativo"));
                    lineasReporte.add("Gama: "+ resultado.getString("c.gama"));
                    lineasReporte.add("Precio: "+ resultado.getLong("c.precio"));
                    lineasReporte.add("Stock: "+ resultado.getInt("c.stock"));
                    lineasReporte.add("");

                }
        
            if (hayCelulares) {
                
                System.out.println(" Reporte ");
                
            } else {
                lineasReporte.add("No hay reporte de celulares con stock bajo registradas.");
            }
        }

        // Serializar la lista en el archivo .dat
        
        try (ObjectOutputStream salida = new ObjectOutputStream(
                Files.newOutputStream(ruta))) { salida.writeObject(lineasReporte);
        }

        System.out.println("Reporte serializado correctamente.");
        System.out.println("Ubicación: " + ruta.toAbsolutePath());

    } catch (SQLException | IOException e) {
        System.out.println("Error al exportar el reporte: " + e.getMessage());
    }
}
    
    
    
    public void leerReporteStockDat() { // -------- EXAMEN 
    try (ObjectInputStream entrada = new ObjectInputStream(
            new FileInputStream("reporte_stock.dat"))) {

        List<String> lineasReporte =
                (List<String>) entrada.readObject();

        lineasReporte.forEach(System.out::println);

    } catch (IOException | ClassNotFoundException e) {
        System.out.println(
            "Error al leer el reporte: " + e.getMessage()
        );
    }
 }
    
    
//      DELIMITER //
//
//      CREATE PROCEDURE filtrar_celulares_stock()
//      BEGIN
//      SELECT
//        c.nombre,
//        c.marca_fk,
//        c.sistema_operativo,
//        c.gama,
 //        c.precio,
//        c.stock
 //    	FROM celulares c where c.stock <= 5 ;
//    
//          END //
//
//          DELIMITER ;  
    
      
    
    
    
    
    public void reporteCelularesMasVendidos() {

    try {
        Conexion conexion = Conexion.getInstancia();
        Connection connection = conexion.conexion();

        CallableStatement sentencia = connection.prepareCall("{CALL reporte_celulares_mas_vendidos()}");

        ResultSet resultado = sentencia.executeQuery();

        System.out.println("\n--- TOP 3 CELULARES MÁS VENDIDOS ---");

        boolean hayResultados = false;

        while (resultado.next()) {hayResultados = true;

            System.out.println("ID: " + resultado.getInt("id"));
            System.out.println("Celular: " + resultado.getString("nombre"));
            System.out.println("Marca: " + resultado.getString("marca"));
            System.out.println("Unidades vendidas: "+ resultado.getInt("unidades_vendidas"));
            System.out.println("-----------------------------------");
        }

        if (!hayResultados) {
            System.out.println("Todavía no hay ventas registradas.");
        }

        resultado.close();
        sentencia.close();
        connection.close();

    } catch (SQLException e) { System.out.println("Error al generar el reporte: " + e.getMessage());
    }
}

        
                
public void reporteVentasTotales() {

    try {
        Conexion conexion = Conexion.getInstancia();
        Connection connection = conexion.conexion();

        CallableStatement sentencia = connection.prepareCall("{CALL reporte_ventas_totales()}");

        ResultSet resultado = sentencia.executeQuery();

        int ventaActual = -1;
        boolean hayVentas = false;
        double totalVenta = 0;

        while (resultado.next()) {

            int idVenta = resultado.getInt("venta_id");

            if (ventaActual != idVenta) {

                if (ventaActual != -1) {
                    System.out.printf("TOTAL FINAL: $%,.0f%n", totalVenta);
                    System.out.println("===================================\n");
                }

                ventaActual = idVenta;
                totalVenta = resultado.getDouble("total");
                hayVentas = true;

                System.out.println("\n========== VENTA #" + idVenta + " ==========");
                System.out.println("Fecha: " + resultado.getTimestamp("fecha"));
                System.out.println("Cliente: " + resultado.getString("nombre_cliente"));
                System.out.println("Identificación: " + resultado.getLong("identificacion"));
                System.out.println("Teléfono: " + resultado.getString("telefono"));
                System.out.println("\nArtículos:");
            }

            System.out.println("Celular: " + resultado.getString("nombre_celular"));
            System.out.println("Cantidad: " + resultado.getInt("cantidad"));
            System.out.printf("Subtotal: $%,.0f%n", resultado.getDouble("subtotal"));
            System.out.println("-----------------------------------");
        }

        if (hayVentas) {
            System.out.printf("TOTAL FINAL: $%,.0f%n", totalVenta);
            System.out.println("===================================\n");
        } else {
            System.out.println("Todavía no hay ventas registradas.");
        }

        resultado.close();
        sentencia.close();
        connection.close();

    } catch (SQLException e) {
        System.out.println("Error al generar el reporte de ventas: " + e.getMessage()
        );
    }
}


}