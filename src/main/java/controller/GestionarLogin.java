package controller;

import model.Usuario;
import view.Opciones;
import view.Validaciones;

public class GestionarLogin {

    private final Opciones opciones = new Opciones();
    private final Validaciones validaciones = new Validaciones();
    private final UsuarioController usuarioController = new UsuarioController();

    private final GestionarAdministrador menuAdministrador = new GestionarAdministrador();
    private final GestionarCliente menuCliente = new GestionarCliente();

    public void iniciar() {

        int opcion;
        do {
            opcion = opciones.general();

            switch (opcion) {
                case 1 -> iniciarSesion();
                case 2 -> usuarioController.registrarCliente();
                case 3 -> System.out.println( "Gracias por usar TecnoStore.");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 3);
    }

    private void iniciarSesion() {

        String correo = validaciones.validarTexto( "Ingresa tu correo:");
        String password = validaciones.validarTexto("Ingresa tu contraseña:");

        Usuario usuario = usuarioController.login(correo, password);

        if (usuario == null) {
            System.out.println("Correo o contraseña incorrectos.");
            return;
        }

        switch (usuario.getRol()) {
            case ADMIN -> menuAdministrador.iniciar();
            case CLIENTE -> menuCliente.iniciar();
            
        }
    }
}