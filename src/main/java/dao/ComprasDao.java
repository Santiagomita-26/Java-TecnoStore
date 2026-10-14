
package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Celular;
import model.Cliente;
import model.ProductoVenta;
import model.Venta;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ComprasDao {

    public boolean registrarVenta(Venta venta) {

        Connection conexion = Conexion.getInstancia().conexion();

        if (conexion == null) {System.out.println("No fue posible conectar con la base de datos.");
            return false;
        }

        try (Connection connection = conexion) {

            connection.setAutoCommit(false);

            try {
                int idVenta;

                // 1. Registrar la venta principal.
    try (CallableStatement sentencia = connection.prepareCall("{CALL registrar_venta(?, ?, ?)}")) {

        sentencia.setInt(1, venta.getCliente().getId());
        sentencia.setDouble(2, venta.getTotal());
         sentencia.registerOutParameter(3, Types.INTEGER);

        sentencia.execute();
        idVenta = sentencia.getInt(3);
        }

                //  Registrar cada producto y descontar su inventario.
                
            for (ProductoVenta producto : venta.getProductos()) {

            int idCelular = producto.getCelular().getId();
            int cantidad = producto.getCantidad();

             double subtotal = producto.getCelular().getPrecio()* cantidad;

                    // Guardar el detalle de la venta.
                    
                    try (CallableStatement sentencia = connection.prepareCall("{CALL insertar_detalle_venta(?, ?, ?, ?)}")) {

                        sentencia.setInt(1, idVenta);
                        sentencia.setInt(2, idCelular);
                        sentencia.setInt(3, cantidad);
                        sentencia.setDouble(4, subtotal);

                        sentencia.execute();
        }

            // Aqui descuento las unidades del inventario.
            
        try (CallableStatement sentencia = connection.prepareCall("{CALL descontar_stock(?, ?)}")) {

            sentencia.setInt(1, idCelular);
            sentencia.setInt(2, cantidad);

            sentencia.execute();}
        }

               // Confirmamos todos los cambios.
               
                connection.commit();

                System.out.println("Venta registrada correctamente.");
                System.out.println("Número de venta: " + idVenta);

                return true;

            } catch (SQLException e) {

                connection.rollback();

                System.out.println("No se pudo registrar la compra: " + e.getMessage());
                System.out.println("Se cancelaron los cambios de la venta.");

                return false;
            }

        } catch (SQLException e) {System.out.println( "Error al procesar la venta: " + e.getMessage());

            return false;
        }
    }
    
    
    public List<Venta> listarComprasCliente(Cliente cliente) {

            List<Venta> ventas = new ArrayList<>();

        try {
            Conexion conexion = Conexion.getInstancia();
            Connection connection = conexion.conexion();

            CallableStatement sentencia = connection.prepareCall( "{CALL listar_compras_cliente(?)}");

            sentencia.setInt(1, cliente.getId());
            ResultSet resultado = sentencia.executeQuery();

            Venta ventaActual = null;

        while (resultado.next()) {

            int idVenta = resultado.getInt("venta_id");

            // Si encontramos una venta diferente, creamos otra.
            
            if (ventaActual == null || ventaActual.getId() != idVenta) {

                LocalDateTime fecha = resultado.getTimestamp("fecha") .toLocalDateTime();

                double total = resultado.getDouble("total");

                ventaActual = new Venta( idVenta,cliente,fecha, new ArrayList<>(),total);

                ventas.add(ventaActual);
            }

            // Obtener los datos del celular comprado.
            
            String nombreCelular = resultado.getString("nombre_celular");
            int cantidad = resultado.getInt("cantidad");
            double subtotal = resultado.getDouble("subtotal");

            Celular celular = new Celular(0,nombreCelular, null, "",null,subtotal / cantidad,0);

            ProductoVenta producto = new ProductoVenta(celular, cantidad);
            ventaActual.getProductos().add(producto);
        }

        resultado.close();
        sentencia.close();
        connection.close();

    } catch (SQLException e) {
        System.out.println(
                "Error al consultar las compras: " + e.getMessage()
        );
    }

    return ventas;
    }
    
    
            
public void exportarVentasDat() {

    Path ruta = Path.of("reporte_ventas.dat");

    try {
        Conexion conexion = Conexion.getInstancia();
        Connection connection = conexion.conexion();

        if (connection == null) {
            System.out.println("No fue posible conectar con la base de datos.");
            return;
        }

        try (Connection conn = connection;
             CallableStatement sentencia = conn.prepareCall("{CALL reporte_ventas_totales()}");
             ResultSet resultado = sentencia.executeQuery();
             BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {

            int ventaActual = -1;
            double totalVenta = 0;
            boolean hayVentas = false;

            escritor.write("========== REPORTE DE VENTAS TECNOSTORE ==========");
            escritor.newLine();
            escritor.newLine();

            while (resultado.next()) {

                int idVenta = resultado.getInt("venta_id");

                if (ventaActual != idVenta) {

                    if (ventaActual != -1) {
                        escritor.write(String.format("TOTAL FINAL: $%,.0f", totalVenta));
                        escritor.newLine();
                        escritor.write("==================================================");
                        escritor.newLine();
                        escritor.newLine();
                    }

                    ventaActual = idVenta;
                    totalVenta = resultado.getDouble("total");
                    hayVentas = true;

                    escritor.write("VENTA #" + idVenta);
                    escritor.newLine();
                    escritor.write("Fecha: "+ resultado.getTimestamp("fecha"));
                    escritor.newLine();escritor.write("Cliente: "+ resultado.getString("nombre_cliente"));
                    escritor.newLine();escritor.write("Identificacion: "+ resultado.getLong("identificacion"));
                    escritor.newLine();escritor.write("Telefono: "+ resultado.getString("telefono"));
                    escritor.newLine();
                    escritor.newLine();
                    escritor.write("ARTICULOS:");
                    escritor.newLine();
                }

                escritor.write("Celular: "+ resultado.getString("nombre_celular"));escritor.newLine();
                escritor.write("Cantidad: "+ resultado.getInt("cantidad"));
                escritor.newLine();
                escritor.write(String.format("Subtotal: $%,.0f",resultado.getDouble("subtotal")));
                escritor.newLine();
                escritor.write("----------------------------------------");
                escritor.newLine();
            }

            if (hayVentas) {
                escritor.write(String.format("TOTAL FINAL: $%,.0f", totalVenta));
                escritor.newLine();
                escritor.write("==================================================");
            } else {
                escritor.write("No hay ventas registradas.");
            }

            escritor.newLine();
        }

        System.out.println("Reporte exportado correctamente.");
        System.out.println("Ubicación: " + ruta.toAbsolutePath());

    } catch (SQLException | IOException e) {
        System.out.println("Error al exportar el reporte: " + e.getMessage());
    }
}

    
    
    
}


        