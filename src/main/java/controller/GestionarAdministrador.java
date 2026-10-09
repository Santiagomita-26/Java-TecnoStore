package controller;

import view.Opciones;

public class GestionarAdministrador {

    private final Opciones opciones = new Opciones();

    public void iniciar() {
            
        System.out.println("  ");
        System.out.println("=======Bienvenido al portal de administrador=======");
        System.out.println("   ");
        
        int opcion;

        do {
            opcion = opciones.menu_administrador();
            
            

            switch (opcion) {
                case 1 -> System.out.println(
                        "Gestión de celulares próximamente."
                );
                case 2 -> System.out.println(
                        "Gestión de marcas próximamente."
                );
                case 3 -> System.out.println(
                        "Gestión de clientes próximamente."
                );
                case 4 -> System.out.println(
                        "Reportes próximamente."
                );
                case 5 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 5);
    }
}