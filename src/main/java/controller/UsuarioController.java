
package controller;

import dao.UsuarioDao;
import model.Cliente;
import model.Usuario;
import view.ingresodatos.DatosCliente;


public class UsuarioController {
    
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final DatosCliente datosCliente = new DatosCliente();
    
    public Usuario login(String correo , String password){
        return usuarioDao.login(correo, password);
    }
    
    
    public void registrarCliente() {

        System.out.println("\n=== REGISTRO DE CLIENTE ===");

        String nombre = datosCliente.ingresarNombre();
        long identificacion = datosCliente.ingresarIdentificacion();
        String telefono = datosCliente.ingresarTelefono();
        String correo = datosCliente.ingresarCorreo();
        String password = datosCliente.ingresarPassword();

        Usuario usuario = new Usuario(correo, password,Usuario.Rol.CLIENTE);
        Cliente cliente = new Cliente(usuario, nombre, identificacion,telefono);

        boolean registrado = usuarioDao.registrarCliente(usuario,cliente);

        if (registrado) {
            System.out.println("¡Cliente registrado correctamente!");
        } else {
            System.out.println(
                    "No se pudo registrar el cliente. "
                    + "Verifica el correo y la identificación."
            );
        }
    }
}
    

