/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Usuario
 */
public class Modelo {
    
    private int id ;
    private String nombre ;
    private Marca marca ;

    public Modelo( String nombre, Marca marca) {
        this.id = GeneradorIds.nuevaId();
        this.nombre = nombre;
        this.marca = marca;
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
    
    @Override
    public String toString() {
        return """
               Id:                                       %s
               Nombre:                                   %s
               Marca:                                    %s
               """.formatted(id, nombre, marca);
    }
    
    
    
}
