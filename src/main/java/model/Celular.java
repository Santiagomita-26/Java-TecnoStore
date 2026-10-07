
package model;
import model.GeneradorIds;

public class Celular {
    
    public enum Gama {
        ALTA, MEDIA, BAJA
    }
    
    private int id ;
    private Modelo modelo;
    private String sistemaOperativo ;
    private Gama gama;
    private double precio ;
    private int stock;

    public Celular(Modelo modelo, String sistemaOperativo, Gama gama, double precio, int stock) {
        this.id = GeneradorIds.nuevaId();
        this.modelo = modelo;
        this.sistemaOperativo = sistemaOperativo;
        this.gama = gama;
        this.precio = precio;
        this.stock = stock;
    }

  

    public int getId() {
        return id;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
        public Gama getGama() {
        return gama;
    }

    public void setGama(Gama gama) {
        this.gama = gama;
    }
    
    @Override
    public String toString() {
        return """
               Id:                                      %s
               Modelo:                                  %s
               Sistema Operativo:                       %s
               Gama:                                    %s
               Precio:                                  %s
               Stock:                                   %s
               """.formatted(id, modelo, sistemaOperativo, gama, precio, stock);
    }

}
       

