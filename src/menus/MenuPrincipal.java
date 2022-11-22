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
                    ╔══════════════════════════════════════════════╗\s
                    ║  Indique a qué departamento quiere acceder:  ║\s
                    ║    1.-Médico                                 ║\s
                    ║    2.-Nutrición                              ║\s
                    ║    3.-Cocina                                 ║\s
                    ║    4.-Salir de Hestia                        ║\s
                    ╚══════════════════════════════════════════════╝\s
                    """);
            identificacion = sc.nextInt();
            //Dependiendo de a qué departamento quiere acceder, marca una opción y le lleva al menú correspondiente
            switch (identificacion) {
                case 1 -> MenuMedico.menuMedico(conn);
                case 2 -> MenuNutricion.menuNutricion(conn);
                case 3 -> MenuCocina.menuCocina(conn);
                case 4 -> System.out.println("════════════ HASTA PRONTO ════════════");
                default -> {
                    System.err.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuPrincipal(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            menuPrincipal(conn);

        } catch (Exception e) {
            System.err.println("Error indeterminado");
            menuPrincipal(conn);
        }
    }

    public static void espacios() { //Método que crea espacios para limpiar la consola
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
