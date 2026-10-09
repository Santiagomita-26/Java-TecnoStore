
package controller;

import view.Opciones;

public class GestionarCliente {

    private final Opciones opciones = new Opciones();

    public void iniciar() {

        int opcion;

        do {
            opcion = opciones.menu_cliente();

            switch (opcion) {
                case 1 -> System.out.println(
                        "Listado de celulares próximamente."
                );
                case 2 -> System.out.println(
                        "Módulo de compras próximamente."
                );
                case 3 -> System.out.println(
                        "Historial de compras próximamente."
                );
                case 4 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 4);
    }
}