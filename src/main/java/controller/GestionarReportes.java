package controller;

import dao.CelularDao;
import dao.ComprasDao;
import dao.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                case 5 -> System.out.println("Saliendo de los reportes...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 5);
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
                System.out.println("Cliente: "
                        + resultado.getString("nombre_cliente"));
                System.out.println("Identificación: "
                        + resultado.getLong("identificacion"));
                System.out.println("Teléfono: "
                        + resultado.getString("telefono"));
                System.out.println("\nArtículos:");
            }

            System.out.println("Celular: "
                    + resultado.getString("nombre_celular"));
            System.out.println("Cantidad: "
                    + resultado.getInt("cantidad"));
            System.out.printf("Subtotal: $%,.0f%n",
                    resultado.getDouble("subtotal"));
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
        System.out.println(
                "Error al generar el reporte de ventas: " + e.getMessage()
        );
    }
}


}