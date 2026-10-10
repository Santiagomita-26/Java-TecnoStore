
package model;

public class Celular {
    
    public enum Gama {
        ALTA, MEDIA, BAJA
    }
    
    private int id ;
    private String nombre;
    private Marca marca;
    private String sistemaOperativo ;
    private Gama gama;
    private double precio ;
    private int stock;

    public Celular(String nombre, Marca marca, String sistemaOperativo, Gama gama, double precio, int stock) {
        this.nombre = nombre;
        this.marca = marca;
        this.sistemaOperativo = sistemaOperativo;
        this.gama = gama;
        this.precio = precio;
        this.stock = stock;
    }
    
    public Celular(int id, String nombre, Marca marca, String sistemaOperativo, Gama gama, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.sistemaOperativo = sistemaOperativo;
        this.gama = gama;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public Gama getGama() {
        return gama;
    }

    public void setGama(Gama gama) {
        this.gama = gama;
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
    
    
        @Override
        public String toString() {
            return """
                   Id:                   %s
                   Nombre:               %s
                   Marca:                %s
                   Sistema Operativo:    %s
                   Gama:                 %s
                   Precio:               %s
                   Stock:                %s
                   """.formatted(id, nombre, marca.getNombre(),sistemaOperativo,gama, precio,stock);
                }
}

       

