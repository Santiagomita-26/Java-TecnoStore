
package view;


public class Opciones {
    
    Validaciones v = new Validaciones();
    
     public int general() {
        return v.validarEntero("""
                                1- Iniciar sesion.
                                2- Registrarse.
                                3- Salir.
                                """);
        
    }
     
     
   
     public int menu_cliente() {
        return v.validarEntero("""
                                1- Ver celulares.
                                2- Comprar.
                                3- Mis compras.
                                4- Cerrar sesion.
                                """);
        
    }
     
     public int menu_administrador() {
        return v.validarEntero("""
                                1- Gestionar celulares.
                                2- Gestionar marcas.
                                3- Gesrionar clientes.
                                4- Ver reportes.
                                5- Cerrar sesion.
                                """);
        
    }
     
     
     public int Crud_marcas() {
        return v.validarEntero("""
                                1- Añadir.
                                2- Listar.
                                3- Actualizar.
                                4- Eliminar.
                                5- Salir.
                                """);
     }
     
     public int Crud_clientes() {
        return v.validarEntero("""
                                1- Añadir.
                                2- Listar.
                                3- Actualizar.
                                4- Eliminar.
                                5- Salir.
                                """);
     }
     
     public int Crud_celulares() {
        return v.validarEntero("""
                                1- Añadir.
                                2- Listar.
                                3- Actualizar.
                                4- Eliminar.
                                5- Filtrar
                                6- Salir.
                                """);
        
    }
     
     public int Filtros() {
        return v.validarEntero("""
                                1- Por marca.
                                2- Por gama.
                                3- Por sistema operativo.
                                4- Salir.
                                """);
        
    }
     
    
}
