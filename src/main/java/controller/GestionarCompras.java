
package controller;

import dao.CelularDao;
import dao.ClienteDao;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import model.Cliente;
import model.ProductoVenta;
import model.Usuario;
import view.Opciones;
import view.Validaciones;



public class GestionarCompras {

   private final Opciones opciones = new Opciones();
    private final Validaciones validaciones = new Validaciones();
    private final CelularDao celularDao = new CelularDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private Cliente cliente;
    
    public void iniciar(Usuario usuario) {
       cliente = clienteDao.buscarPorUsuario(usuario.getId());

        if (cliente == null) {
            System.out.println("No se encontró el cliente asociado a este usuario.");
            return;
        }

    int opcion;

    do {
        System.out.println("\n=== BIENVENIDO AL PORTAL DE COMPRA ===");

        opcion = opciones.menu_cliente();

        switch (opcion) {
            case 1 -> mostrarCelulares();
            case 2 -> comprar();
            case 3 -> System.out.println("Consulta de compras.");
            case 4 -> System.out.println("Cerrando sesión...");
            default -> System.out.println("Opción no válida.");
        }

    } while (opcion != 4);
}

    private void mostrarCelulares() {
        System.out.println("\n=== CATÁLOGO DE CELULARES ===");

        List<Celular> celulares = celularDao.listar();

        if (celulares.isEmpty()) {
            System.out.println("No hay celulares registrados.");
            return;
        }

        boolean disponibles = false;

        for (Celular celular : celulares) {
            if (celular.getStock() > 0) {
                System.out.println(celular);
                System.out.println("------------------------------");
                disponibles = true;
            }
        }

        if (!disponibles) {  System.out.println("No hay celulares disponibles para comprar.");
        }
    }
    
    
    
    
    
    private void comprar() {

    List<ProductoVenta> productos = new ArrayList<>();
    List<Celular> celulares = celularDao.listar();

    if (celulares.isEmpty()) {System.out.println("No hay celulares registrados.");return;}

    int id;

    do {
        
        System.out.println("\n=== CELULARES DISPONIBLES ===");

        boolean disponibles = false;

        for (Celular celular : celulares) {
            if (celular.getStock() > 0) {
                System.out.println(celular);
                disponibles = true;
            }
        }

        if (!disponibles) {
            System.out.println("No hay celulares disponibles.");
            return;
        }

        id = validaciones.validarEntero( "Ingresa el ID del celular que deseas comprar (0 para terminar):");

        if (id == 0) {
            break;
        }

        Celular seleccionado = null;

        for (Celular celular : celulares) {
            if (celular.getId() == id && celular.getStock() > 0) {
                seleccionado = celular;
                break;
            }
        }

        if (seleccionado == null) {
            System.out.println("El ID no existe o el celular no tiene stock.");
            continue;
        }

        int cantidad = validaciones.validarEntero(
            "¿Cuántas unidades deseas comprar?"
        );

        if (cantidad <= 0) {
            System.out.println("La cantidad debe ser mayor que cero.");
            continue;
        }

        if (cantidad > seleccionado.getStock()) {
            System.out.println("Stock insuficiente. Disponible: "
                    + seleccionado.getStock());
            continue;
        }

        productos.add(new ProductoVenta(seleccionado, cantidad));
        System.out.println("Producto agregado al carrito.");

    } while (true);

        if (productos.isEmpty()) {
            System.out.println("No agregaste productos a la compra.");
            return;
    }

    double subtotalCompra = 0;

    System.out.println("\n=== RESUMEN DE LA COMPRA ===");

    for (ProductoVenta producto : productos) {
        double subtotalProducto =
                producto.getCelular().getPrecio() * producto.getCantidad();

        subtotalCompra += subtotalProducto;

        System.out.println(
            producto.getCelular().getNombre()
            + " | Cantidad: " + producto.getCantidad()
            + " | Subtotal: $" + subtotalProducto
        );
    }

    double iva = subtotalCompra * 0.19;
    double total = subtotalCompra + iva;

    System.out.printf("Subtotal: $%,.0f%n", subtotalCompra);
    System.out.printf("IVA (19 %%): $%,.0f%n", iva);
    System.out.printf("Total: $%,.0f%n", total);
    }     
}
