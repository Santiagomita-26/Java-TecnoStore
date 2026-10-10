
package controller;

import dao.UsuarioDao;
import model.Cliente;
import model.Usuario;
import view.Opciones;
import view.Validaciones;
import view.ingresodatos.DatosCliente;

public class GestionarLogin {

    private final Opciones opciones = new Opciones();
    private final Validaciones validaciones = new Validaciones();
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final DatosCliente datosCliente = new DatosCliente();
    private final GestionarAdministrador menuAdministrador =new GestionarAdministrador();
    private final GestionarCompras gestionarCompras =new GestionarCompras();

    public void iniciar() {

        int opcion;

        do {
            opcion = opciones.general();

            switch (opcion) {
                case 1 -> iniciarSesion();
                case 2 -> registrarCliente();
                case 3 -> System.out.println("Gracias por usar TecnoStore.");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 3);
    }

    private void iniciarSesion() {

        String correo = validaciones.validarTexto("Ingresa tu correo:");
        String password = validaciones.validarTexto("Ingresa tu contraseña:");

        Usuario usuario = usuarioDao.login(correo, password);

        if (usuario == null) {
            System.out.println("Correo o contraseña incorrectos.");
            return;
        }

        switch (usuario.getRol()) {
            case ADMIN -> menuAdministrador.iniciar();
            case CLIENTE -> gestionarCompras.iniciar(usuario); 
        }
    }

    private void registrarCliente() {

        System.out.println("\n=== REGISTRO DE CLIENTE ===");

        String nombre = datosCliente.ingresarNombre();
        long identificacion = datosCliente.ingresarIdentificacion();
        String telefono = datosCliente.ingresarTelefono();
        String correo = datosCliente.ingresarCorreo();
        String password = datosCliente.ingresarPassword();

        Usuario usuario = new Usuario(correo,password,Usuario.Rol.CLIENTE);

        Cliente cliente = new Cliente(usuario,nombre, identificacion,telefono);

        boolean registrado = usuarioDao.registrarCliente( usuario,cliente);

        if (registrado) { System.out.println("¡Cliente registrado correctamente!");
        
        } else {
            System.out.println("No se pudo registrar el cliente. "+ "Verifica el correo y la identificación.");
        }
    }
}
