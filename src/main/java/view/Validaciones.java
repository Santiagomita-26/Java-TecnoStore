
package view;

import java.util.Scanner;


public class Validaciones {
    
    public String validarTexto(String mensaje) {
    Scanner x = new Scanner(System.in);
    String texto;

    do {
        System.out.println(mensaje);
        texto = x.nextLine();

        if (texto.isBlank()) {
            System.out.println("Error, el campo no puede estar vacío.");
        }

    } while (texto.isBlank());
    return texto;
}
    
    
    public double validarDecimal(String mensaje) {
    Scanner x = new Scanner(System.in);
    System.out.println(mensaje);

    while (!x.hasNextDouble()) {
        System.out.println("Error, se espera un valor decimal");
        x.next();
    }
    return x.nextDouble();
}
    
    
    public int validarEntero(String mensaje) {
    Scanner x = new Scanner(System.in);
    int numero;
    
    do {
        System.out.println(mensaje);

        while (!x.hasNextInt()) {
            System.out.println("Error, se espera un valor entero");
            x.next();
        }

        numero = x.nextInt();

        if (numero < 0) {
            System.out.println("Error, el valor no puede ser negativo");
        }

    } while (numero < 0);

    return numero;
}

    
    public long validarEnteroGrande(String mensaje) {
    Scanner x = new Scanner(System.in);
    long numero;
    do {
        System.out.println(mensaje);

        while (!x.hasNextLong()) {
            System.out.println("Error, se espera un valor entero");
            x.next();
        }
        numero = x.nextLong();

        if (numero < 0) {
            System.out.println("Error, el valor no puede ser negativo");
        }

    } while (numero < 0);

    return numero;
}
    
}
