
package model;

import java.time.LocalDateTime;
import java.util.List;

public class Venta {
    
    private int id;
    private Cliente cliente;
    private LocalDateTime fecha;
    List<ProductoVenta> productos;
    private double total;

    public Venta( Cliente cliente, LocalDateTime fecha, List<ProductoVenta> productos, double total) {
        this.id = GeneradorIds.nuevaId();
        this.cliente = cliente;
        this.fecha = fecha;
        this.productos = productos;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<ProductoVenta> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoVenta> productos) {
        this.productos = productos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
    @Override
    public String toString() {
        return """
               Id:                                       %s
               Cliente:                                  %s
               Fecha:                                    %s
               Productos:                                %s
               Total;                                    %s

               """.formatted(id, cliente, fecha, productos, total);
    }
    
}
