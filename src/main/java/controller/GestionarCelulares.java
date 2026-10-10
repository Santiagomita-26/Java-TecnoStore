
package controller;

import dao.CelularDao;
import dao.MarcaDao;
import java.util.List;
import model.Celular;
import model.Marca;
import view.Opciones;
import view.ingresodatos.DatosCelular;

public class GestionarCelulares {

    private final Opciones opciones = new Opciones();
    private final DatosCelular datos = new DatosCelular();
    private final CelularDao celularDao = new CelularDao();
    private final MarcaDao marcaDao = new MarcaDao();

    public void iniciar() {

        int opcion;

        do {
            opcion = opciones.Crud_celulares();

            switch (opcion) {
                case 1 -> registrar();
                case 2 -> listar();
                case 3 -> actualizar();
                case 4 -> eliminar();
                case 5 -> System.out.println("Los filtros se implementarán después.");
                case 6 -> System.out.println("Saliendo de la gestión de celulares...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 6);
    }

    private void registrar() {

        System.out.println("\n--- REGISTRAR CELULAR ---");

        List<Marca> marcas = marcaDao.listar();

        if (marcas.isEmpty()) {
            System.out.println("No hay marcas registradas. Registra una marca primero.");
            return;
        }

        String nombre = datos.ingresarNombre();
        Marca marca = seleccionarMarca(marcas);

        if (marca == null) {
            return;
        }

        String sistemaOperativo = datos.ingresarSistemaOperativo();
        Celular.Gama gama = datos.ingresarGama();
        double precio = datos.ingresarPrecio();
        int stock = datos.ingresarStock();

        Celular celular = new Celular(nombre,marca,sistemaOperativo, gama, precio, stock);
        celularDao.insertar(celular);
    }

    private void listar() {

        System.out.println("\n--- LISTA DE CELULARES ---");

        List<Celular> celulares = celularDao.listar();

        if (celulares.isEmpty()) {
            System.out.println("No hay celulares registrados.");
            return;
        }

        for (Celular celular : celulares) {
            System.out.println(celular);
            System.out.println("------------------------------");
        }
    }

    private void actualizar() {

        System.out.println("\n--- ACTUALIZAR CELULAR ---");

        List<Celular> celulares = celularDao.listar();

        if (celulares.isEmpty()) {
            System.out.println("No hay celulares para actualizar.");
            return;
        }

        for (Celular celular : celulares) {
            System.out.println(celular);
            System.out.println("------------------------------");
        }

        int id = datos.ingresarId();
        Celular existente = buscarCelular(celulares, id);

        if (existente == null) {
            System.out.println("No existe un celular con ese ID.");
            return;
        }

        List<Marca> marcas = marcaDao.listar();

        if (marcas.isEmpty()) {
            System.out.println("No hay marcas registradas.");
            return;
        }

        System.out.println("Ingresa los nuevos datos del celular." );

        String nombre = datos.ingresarNombre();
        Marca marca = seleccionarMarca(marcas);

        if (marca == null) {
            return;
        }

        String sistemaOperativo = datos.ingresarSistemaOperativo();
        Celular.Gama gama = datos.ingresarGama();
        double precio = datos.ingresarPrecio();
        int stock = datos.ingresarStock();

        Celular actualizado = new Celular(existente.getId(),nombre, marca,sistemaOperativo,gama,precio,stock);
        celularDao.actualizar(actualizado);
    }

    private void eliminar() {

        System.out.println("\n--- ELIMINAR CELULAR ---");

        List<Celular> celulares = celularDao.listar();

        if (celulares.isEmpty()) {
            System.out.println("No hay celulares para eliminar.");
            return;
        }

        for (Celular celular : celulares) {
            System.out.println(celular);
            System.out.println("------------------------------");
        }

        int id = datos.ingresarId();
        Celular existente = buscarCelular(celulares, id);

        if (existente == null) {
            System.out.println("No existe un celular con ese ID.");
            return;
        }

        celularDao.eliminar(id);
    }

    private Marca seleccionarMarca(List<Marca> marcas) {

        System.out.println("\n--- MARCAS DISPONIBLES ---");

        for (Marca marca : marcas) {
            System.out.println(
                    "ID: " + marca.getId()
                    + " | Nombre: " + marca.getNombre()
            );
        }
        while (true) {

            int idMarca = datos.ingresarIdMarca();

            for (Marca marca : marcas) {
                if (marca.getId() == idMarca) {
                    return marca;
                }
            }

            System.out.println("El ID ingresado no corresponde a una marca existente.");
        }
    }

    private Celular buscarCelular(
            List<Celular> celulares, int id) {

        for (Celular celular : celulares) {
            if (celular.getId() == id) {
                return celular;
            }
        }

        return null;
    }
}
