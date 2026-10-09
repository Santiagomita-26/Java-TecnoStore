
package model;


public class Cliente {
    
    private int id ;
    private Usuario usuario;
    private String nombre;
    private long identificacion ;
    private String telefono;

    public Cliente(Usuario usuario, String nombre, long identificacion, String telefono) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.telefono = telefono;
    }

    public Cliente(int id, Usuario usuario, String nombre, long identificacion, String telefono) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.telefono = telefono;
    }
    
   

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(long identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
    @Override
    public String toString() {
        return """
               Id:                                       %s
               Usuario:                                  %s
               Nombre:                                   %s
               Identificacion:                           %s
               Telefono:                                 %s
               """.formatted(id, usuario, nombre, identificacion, telefono);
    }
    
    
    
}
