
package view.ingresodatos;

import view.Validaciones;

public class DatosMarca {
    

    private final Validaciones validaciones = new Validaciones();

    public String ingresarNombre() {
        return validaciones.validarTexto("Ingresa el nombre de la marca:");}

    public int ingresarId() {
        return validaciones.validarEntero("Ingresa el ID de la marca:");}

    public String ingresarNuevoNombre() {
        return validaciones.validarTexto("Ingresa el nuevo nombre de la marca:");}
}
