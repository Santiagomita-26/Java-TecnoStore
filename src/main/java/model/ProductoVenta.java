
package model;



public class ProductoVenta {
    
    private Celular celular;
    private int cantidad;

    public ProductoVenta(Celular celular, int cantidad) {
        this.celular = celular;
        this.cantidad = cantidad;
    }

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    @Override
    public String toString() {
        return """
               Celular:                                  %s
               Cantidad:                                 %s
               """.formatted(celular, cantidad);
    }
    
}
