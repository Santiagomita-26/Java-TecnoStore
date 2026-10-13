
# TecnoStore

Aplicación de consola desarrollada en Java para administrar una tienda
de celulares. El sistema permite gestionar marcas, celulares y clientes,
registrar compras, consultar el historial de compras y generar reportes
a partir de los datos almacenados en MySQL.

## Características

### Acceso y roles

-   Inicio de sesión mediante correo y contraseña.
-   Acceso diferenciado para los roles `ADMIN` y `CLIENTE`.

### Administrador

-   Registrar, listar, actualizar y eliminar celulares.
-   Gestionar marcas.
-   Gestionar clientes.
-   Consultar el top 3 de celulares más vendidos.
-   Consultar celulares con stock bajo (menos de 5 unidades).
-   Consultar ventas completas: cliente, artículos, cantidades,
    subtotales y total final.
-   Exportar el reporte de ventas a `reporte_ventas.dat`.

### Cliente

-   Consultar celulares disponibles.
-   Seleccionar productos y cantidades para una compra.
-   Confirmar o cancelar una compra.
-   Consultar el historial de sus compras.

### Inventario y ventas

-   El total de la compra incluye el IVA del 19 %, según el flujo
    implementado.
-   Las ventas se registran junto con sus detalles.
-   El stock se descuenta al confirmar la compra.
-   El registro de la venta, sus detalles y la actualización del
    inventario se manejan en una transacción para evitar dejar una
    compra parcialmente registrada si ocurre un error.

## Tecnologías

-   Java 17.
-   Maven.
-   MySQL.
-   JDBC y `CallableStatement` para invocar procedimientos almacenados.
-   MySQL Connector/J.
-   Git y GitHub para el control de versiones.

## Arquitectura y organización

El proyecto sigue una organización MVC + DAO:

``` text
src/main/java/
├── model/        # Entidades y objetos del dominio
├── view/         # Menús, entrada de datos y validaciones
├── controller/   # Flujo de la aplicación y coordinación entre vista y DAO
└── dao/          # Acceso a MySQL mediante JDBC y procedimientos almacenados
```

Responsabilidades principales:

-   **Model:** representa entidades como `Usuario`, `Cliente`, `Marca`,`Celular`, `Venta` y `ProductoVenta`.
-   **View:** presenta menús y recoge entradas del usuario.
-   **Controller:** dirige las acciones y coordina el flujo del programa.
-   **DAO:** concentra las consultas y operaciones de persistencia.

### Conceptos aplicados

-   **POO y encapsulamiento:** las entidades agrupan sus datos y exponen métodos de acceso.
-   **Composición:** una venta contiene productos vendidos y está asociada a un cliente.
-   **MVC:** separa presentación, coordinación y dominio.
-   **DAO:** separa el acceso a datos del resto de la aplicación.
-   **Singleton:** `Conexion` centraliza el acceso a la instancia de la clase de conexión.
-   **Transacciones JDBC:** la compra se confirma con `commit()` o se revierte con `rollback()` si falla.
-   **Stream API:** se usa para filtrar celulares, por ejemplo, por marca, gama o sistema operativo.
-   **Procedimientos almacenados:** las operaciones y reportes de base de datos se invocan desde los DAO.


## Base de datos

La aplicación utiliza una base de datos MySQL llamada:

``` text
tecnostoremendoza
```

Las tablas principales del diseño actual son:

-   `usuarios`: credenciales y rol de acceso.
-   `clientes`: datos del cliente vinculados a un usuario.
-   `marcas`: marcas disponibles.
-   `celulares`: productos, marca, sistema operativo, gama, precio y stock.
-   `ventas`: información general de cada compra.
-   `detalle_ventas`: celulares, cantidades y subtotales de cada venta.

Relaciones importantes:

-   Un cliente está vinculado a un usuario.
-   Una venta pertenece a un cliente.
-   Una venta puede tener varios detalles.
-   Cada detalle hace referencia a un celular.

La base debe poder iniciarse sin datos de demostración de marcas,
celulares, clientes ni ventas. Esos registros se crean durante la
demostración desde la aplicación. Sin embargo, el inicio de sesión
necesita al menos una cuenta inicial con rol `ADMIN`, salvo que el
proyecto implemente un proceso de creación del primer administrador.
Define ese acceso inicial mediante un script de inicialización o un
mecanismo de configuración seguro.

## Instalación y ejecución

### Requisitos previos

1.  Instalar Java 17 o superior.
2.  Instalar Maven (o utilizar el Maven integrado de NetBeans).
3.  Instalar MySQL Server y MySQL Workbench.
4.  Tener disponible el controlador MySQL Connector/J en el `pom.xml`.

### 1. Crear la base de datos

En MySQL Workbench, ejecutar primero el script SQL del proyecto que crea
la base de datos, tablas, restricciones y procedimientos almacenados.

El esquema debe incluir los procedimientos utilizados por la aplicación,
por ejemplo, los de inicio de sesión, gestión de marcas y celulares,
registro de ventas, detalle de ventas, descuento de stock, historial de
compras y reportes.


### 2. Configurar la conexión

Revisa la clase `dao/Conexion.java`. La URL debe apuntar a
`tecnostoremendoza` y las credenciales deben coincidir con tu
instalación local de MySQL.

No publiques contraseñas reales en GitHub. Para una entrega compartida,
utiliza variables de entorno o un archivo local de configuración
excluido del repositorio. Si actualmente las credenciales están escritas
directamente en el código, reemplázalas antes de publicar el proyecto.

### 3. Preparar el usuario administrador

Crea o configura la cuenta inicial con rol `ADMIN` mediante el mecanismo
previsto por el proyecto. Si el sistema guarda contraseñas con un
formato específico, utiliza el mismo procedimiento que espera
`login_usuario`; no insertes una contraseña en un formato distinto al
que valida la aplicación.

### 4. Abrir y ejecutar

1.  Abrir el proyecto Maven `Java-TecnoStore` en NetBeans.
2.  Esperar a que Maven descargue las dependencias.
3.  Verificar la conexión con MySQL.
4.  Ejecutar la clase principal del proyecto.
5.  Iniciar sesión con la cuenta administradora configurada.

Los nombres exactos de la clase principal y de los scripts SQL dependen
de los archivos incluidos en el repositorio.

## Flujo sugerido para probar el sistema desde una base vacía

1.  Iniciar sesión como administrador.
2.  Registrar una o varias marcas.
3.  Registrar celulares con sus precios y stock inicial.
4.  Registrar los clientes mediante las opciones disponibles.
5.  Cerrar sesión e ingresar como cliente.
6.  Consultar el catálogo y realizar una compra de prueba.
7.  Consultar el historial de compras del cliente.
8.  Volver al administrador y revisar el top 3 de celulares más
    vendidos.
9.  Revisar el reporte de stock bajo.
10. Consultar las ventas completas y exportarlas a `reporte_ventas.dat`.
11. Abrir el archivo generado y comprobar los datos de la compra.

Este orden permite demostrar que los datos se crean y se relacionan
dinámicamente, en lugar de depender de registros de ejemplo
preinsertados.

## Reportes disponibles

  -----------------------------------------------------------------------
  Reporte                             Contenido
  ----------------------------------- -----------------------------------
  Celulares más vendidos              Top 3 por unidades vendidas

  Stock bajo                          Celulares con menos de 5 unidades

  Ventas totales                      Cada venta, datos del cliente,
                                      artículos, cantidades, subtotales y
                                      total final

  Archivo `.dat`                      Exportación del reporte completo de
                                      ventas a `reporte_ventas.dat`
  -----------------------------------------------------------------------

El archivo `.dat` generado por la implementación actual es un archivo de
texto legible con extensión `.dat`; se crea en el directorio de trabajo
desde el que se ejecuta la aplicación y se sobrescribe al exportar de
nuevo.

## Manejo de errores y consistencia

-   Se manejan excepciones SQL en las operaciones de persistencia.
-   El proceso de compra utiliza una transacción para confirmar o revertir sus cambios.
-   El descuento de stock comprueba que exista inventario suficiente.
-   Las restricciones de integridad de MySQL ayudan a mantener las
    relaciones entre registros.

## Estructura recomendada del repositorio

``` text
Java-TecnoStore/
├── src/
│   └── main/
│       └── java/
│           ├── model/
│           ├── view/
│           ├── controller/
│           └── dao/
├── pom.xml
├── README.md
└── TecnoStoreDb/
    └── scripts SQL del proyecto
```

Ajusta el nombre de la carpeta de scripts a la estructura real del
repositorio. Evita subir contraseñas, archivos temporales, carpetas de
compilación (`target/`) o datos personales reales.

## Autor

**Santiago Mendoza**\
Proyecto académico de desarrollo de software.
