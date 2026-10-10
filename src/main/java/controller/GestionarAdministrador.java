package controller;

import view.Opciones;

public class GestionarAdministrador {

    private final Opciones opciones = new Opciones();
    private final GestionarCelulares gestionarCelulares = new GestionarCelulares();
    private final GestionarCliente gestionarCliente = new GestionarCliente();
    
    public void iniciar() {
        
        GestionarMarcas gestionarMarcas = new GestionarMarcas();
        GestionarCliente gestionarCliente = new GestionarCliente();
       
            
        System.out.println("  ");
        System.out.println("=======NOS ALEGRA TENERTE EN TECNOSTORE=======");
        System.out.println("=======Bienvenido al portal de administrador=======");
        System.out.println("   ");
        
        int opcion;

        do {
            opcion = opciones.menu_administrador();
            

            switch (opcion) {
                case 1 -> gestionarCelulares.iniciar();
                
                case 2 -> gestionarMarcas.iniciar();
                
                case 3 -> gestionarCliente.iniciar();
                
                case 4 -> System.out.println("Reportes .");
                
                case 5 -> System.out.println("Cerrando sesión...");
            
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 5);
    }
}