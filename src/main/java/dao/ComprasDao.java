
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

                // 2. Registrar cada producto y descontar su inventario.
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

            // Descontar las unidades del inventario.
        try (CallableStatement sentencia = connection.prepareCall("{CALL descontar_stock(?, ?)}")) {

            sentencia.setInt(1, idCelular);
            sentencia.setInt(2, cantidad);

            sentencia.execute();}
        }

                // 3. Confirmar todos los cambios.
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
}