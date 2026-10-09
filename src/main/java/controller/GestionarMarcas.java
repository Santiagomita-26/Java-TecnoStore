
package controller;

import dao.MarcaDao;
import java.util.List;
import model.Marca;
import view.Opciones;
import view.ingresodatos.DatosMarca;

public class GestionarMarcas {

    private final MarcaDao marcaDao = new MarcaDao();
    private final Opciones opciones = new Opciones();
    private final DatosMarca datosMarca = new DatosMarca();

    public void iniciar() {
        int opcion;

        do {
            System.out.println("\n=== GESTIÓN DE MARCAS ===");
            System.out.println("   ");
            opcion = opciones.Crud_marcas();

            switch (opcion) {
                
                case 1 -> añadirMarca(); 
                case 2 -> listarMarcas(); 
                case 3 -> actualizarMarca();
                case 4 -> eliminarMarca();
                case 5 ->  System.out.println("Saliendo de gestión de marcas...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 5);
    }

   private void añadirMarca() {
       
    System.out.println("\n=== AÑADIR MARCA ===");
        String nombre = datosMarca.ingresarNombre();

        Marca marca = new Marca(nombre);
            marcaDao.insertar(marca);
}

    private void listarMarcas() {
        System.out.println("\n=== LISTADO DE MARCAS ===");

        List<Marca> marcas = marcaDao.listar();

        if (marcas.isEmpty()) {
            System.out.println("No hay marcas registradas.");
            return;
        }

        for (Marca marca : marcas) {
            System.out.println(marca);
        }
    }
    
    private void actualizarMarca() {
        System.out.println("\n=== ACTUALIZAR MARCA ===");

        listarMarcas();

        int id = datosMarca.ingresarId();
            String nombre = datosMarca.ingresarNuevoNombre();

        Marca marca = new Marca(id, nombre);
            marcaDao.actualizar(marca);
    
    }
    
    private void eliminarMarca() {
        System.out.println("\n=== ELIMINAR MARCA ===");

        listarMarcas();

        int id = datosMarca.ingresarId();

        Marca marca = new Marca(id, "");
            marcaDao.eliminar(marca);
    }
    
}
