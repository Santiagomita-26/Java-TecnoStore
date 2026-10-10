
package view.ingresodatos;

import model.Celular;
import view.Validaciones;

public class DatosCelular {

    private final Validaciones validaciones = new Validaciones();

    public String ingresarNombre() {return validaciones.validarTexto("Ingresa el nombre del celular:");}
    public String ingresarSistemaOperativo() {return validaciones.validarTexto( "Ingresa el sistema operativo:");}

    public Celular.Gama ingresarGama() {
        while (true) {
            System.out.println("""
                               Selecciona la gama:
                               1- ALTA
                               2- MEDIA
                               3- BAJA
                               """);

            int opcion = validaciones.validarEntero("Selecciona una opción:");

            switch (opcion) {
                case 1 -> {return Celular.Gama.ALTA;}
                case 2 -> {return Celular.Gama.MEDIA;}
                case 3 -> { return Celular.Gama.BAJA; }
                default -> System.out.println("Opción no válida. Intenta nuevamente.");
            }
        }
    }

    public double ingresarPrecio() {return validaciones.validarDecimal("Ingresa el precio del celular:");}
    public int ingresarStock() {return validaciones.validarEntero("Ingresa la cantidad disponible:");}
    public int ingresarId() {return validaciones.validarEntero("Ingresa el ID del celular:");}
    public int ingresarIdMarca() {return validaciones.validarEntero("Ingresa el ID de la marca:");}
}
