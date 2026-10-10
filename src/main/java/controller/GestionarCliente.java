
package controller;

import dao.ClienteDao;
import java.util.List;
import model.Cliente;
import view.Opciones;
import view.ingresodatos.DatosCliente;

public class GestionarCliente {

    private final Opciones opciones = new Opciones();
    private final DatosCliente datos = new DatosCliente();
    private final ClienteDao clienteDao = new ClienteDao();

    public void iniciar() {
        int opcion;

        do {
            System.out.println("\n=== GESTIÓN DE CLIENTES ===");
            opcion = opciones.Crud_clientes();

            switch (opcion) {
                case 1 -> listar();
                case 2 -> actualizar();
                case 3 -> eliminar();
                case 4 -> System.out.println("Saliendo de la gestión de clientes...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 4);
    }

    private void listar() {
        System.out.println("\n=== LISTADO DE CLIENTES ===");

        List<Cliente> clientes = clienteDao.listar();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println(cliente);
            System.out.println("------------------------------");
        }
    }

        private void actualizar() {
            System.out.println("\n=== ACTUALIZAR CLIENTE ===");

            List<Cliente> clientes = clienteDao.listar();

            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
                return;
            }

            for (Cliente cliente : clientes) {
                System.out.println(cliente);
                System.out.println("------------------------------");
            }

            int id = datos.ingresarId();
            Cliente existente = buscarCliente(clientes, id);

                if (existente == null) {
                    System.out.println("No existe un cliente con ese ID.");
                    return;
        }

        String nombre = datos.ingresarNombre();
        long identificacion = datos.ingresarIdentificacion();
        String telefono = datos.ingresarTelefono();

        existente.setNombre(nombre);
        existente.setIdentificacion(identificacion);
        existente.setTelefono(telefono);

        clienteDao.actualizar(existente);
    }

    private void eliminar() {
        System.out.println("\n=== ELIMINAR CLIENTE ===");

        List<Cliente> clientes = clienteDao.listar();

        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Cliente cliente : clientes) {
            System.out.println(cliente);
            System.out.println("------------------------------");
        }

        int id = datos.ingresarId();
        Cliente existente = buscarCliente(clientes, id);

        if (existente == null) {
            System.out.println("No existe un cliente con ese ID.");
            return;
        }

        clienteDao.eliminar(id);
    }

        private Cliente buscarCliente(List<Cliente> clientes, int id) {
            for (Cliente cliente : clientes) {
                if (cliente.getId() == id) {
                    return cliente;
                }
            }

            return null;
    }
}
