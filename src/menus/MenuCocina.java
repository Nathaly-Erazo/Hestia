package menus;

import bbdd.Jdbc;
import entidades.Toma;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuCocina {
    public static void menuCocina(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int seccion;
        int continuar;

        try {
            System.out.println("""
                    Indique a qué sección quiere acceder:\s
                    1.-Dieta\s
                    2.-Consultar Toma\s
                    3.-Salir de Hestia""");

            seccion = sc.nextInt();

            switch (seccion) {
                case 1 -> {
                    System.out.println("HA INGRESADO A LA SECCION DIETA");
                    MenuDieta.menuDieta(conn);
                }
                case 2 -> {
                    System.out.println("HA INGRESADO A LA CONSULTA DE TOMAS");
                    System.out.println("-----------------------------------");
                    Toma.mostrarTomas(Jdbc.getToma(conn));
                }
                case 3 -> {
                    System.out.println("HASTA PRONTO");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuCocina(conn);
                }
            }

            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-No");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuCocina(conn);
                    case 2 -> {
                        System.out.println("HASTA PRONTO");
                        System.exit(0);
                    }
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            menuCocina(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuCocina(conn);
        }
    }
}
