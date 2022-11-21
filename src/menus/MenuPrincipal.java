package menus;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {
    public static void menuPrincipal(Connection conn) {
        Scanner sc = new Scanner(System.in);
        int identificacion;
        try {
            System.out.println("""
                    Indique a qué departamento quiere acceder:\s
                    1.-Médico\s
                    2.-Nutrición\s
                    3.-Cocina
                    4.-Salir de Hestia""");
            identificacion = sc.nextInt();
            //Dependiendo de a qué departamento quiere acceder, marca una opción y le lleva al menú correspondiente
            switch (identificacion) {
                case 1 -> {
                    System.out.println("DEPARTAMENTO MÉDICO");
                    MenuMedico.menuMedico(conn);
                }
                case 2 -> {
                    System.out.println("DEPARTAMENTO NUTRICIÓN");
                    MenuNutricion.menuNutricion(conn);
                }
                case 3 -> {
                    System.out.println("DEPARTAMENTO COCINA");
                    MenuCocina.menuCocina(conn);
                }
                case 4 -> System.out.println("HASTA PRONTO");
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuPrincipal(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            menuPrincipal(conn);

        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuPrincipal(conn);
        }
    }
}
