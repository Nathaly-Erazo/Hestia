package menus;

import entidades.Toma;
import jdbc.JdbcToma;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuCocina {
    public static void menuCocina(Connection conn) {
        //Clase correspondiente al menú del departamento de cocina
        //Tiene la misma estructura que la clase MenuMedico
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int seccion;
        int continuar;
        try {
            MenuPrincipal.espacios();
            System.out.println("─────────────DEPARTAMENTO COCINA ──────────────");
            System.out.println("""
                    ┌──────────────────────────────────────────────┐\s
                    │  Indique a qué sección quiere acceder:       │\s
                    │    1.-Dieta                                  │\s
                    │    2.-Consultar Tomas                        │\s                   │\s
                    │    3.-Salir de Hestia                        │\s
                    └──────────────────────────────────────────────┘\s
                     """);
            seccion = sc.nextInt();
            switch (seccion) {
                case 1 -> MenuDieta.menuDieta(conn);
                case 2 -> Toma.mostrarTomas(JdbcToma.consultarToma(conn));
                case 3 -> {
                    System.out.println("════════════ HASTA PRONTO ════════════");
                    System.exit(0);
                }
                default -> {
                    System.err.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuCocina(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-No");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuCocina(conn);
                    case 2 -> {
                        System.out.println("════════════ HASTA PRONTO ════════════");
                        System.exit(0);
                    }
                    default -> {
                        System.err.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            menuCocina(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            menuCocina(conn);
        }
    }
}
