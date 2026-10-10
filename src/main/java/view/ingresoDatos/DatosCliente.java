package view.ingresodatos;

import view.Validaciones;

public class DatosCliente {

    private final Validaciones v = new Validaciones();

    public String ingresarNombre() {return v.validarTexto( "Ingresa tu nombre completo:" );}
    public long ingresarIdentificacion() {return v.validarEnteroGrande( "Ingresa tu identificación:" ); }
    public String ingresarTelefono() {return v.validarTexto("Ingresa tu teléfono:" ); }
    public String ingresarCorreo() {return v.validarTexto("Ingresa tu correo:" );}
    public String ingresarPassword() {return v.validarTexto("Ingresa tu contraseña:");}
    
    public int ingresarId() { return v.validarEntero("Ingresa el ID del cliente:");}
}
