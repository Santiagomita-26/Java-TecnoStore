/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


public class Marca {
    
    private int id ;
    private String nombre ;

        public Marca(String nombre) { //CREA NUEVOS OBJETOS
        this.nombre = nombre;
    }

        public Marca(int id, String nombre) { //RECONSTRUYE LOS OBJETOS EXISTENTES DE MYSQL
        this.id = id;
        this.nombre = nombre;
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
    
    @Override
    public String toString() {
        return """
               Id:                                       %s
               Nombre:                                   %s
               """.formatted(id,nombre);
    }
}
