package menus;

import bbdd.Jdbc;
import entidades.Dieta;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuDieta {
    public static void menuDieta(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int accion;
        int continuar;

        try {
            System.out.println("""
                    Indique qué acción quiere realizar:\s
                    1.-Añadir Dieta\s
                    2.-Editar Dieta\s
                    3.-Borrar Dieta\s
                    4.-Consultar Dietas\s
                    5.-Volver a Menú Cocina""");

            accion = sc.nextInt();

            switch (accion) {
                case 1 -> {
                    System.out.println("AÑADIR DIETA");
                    Jdbc.insertarDieta(conn);
                }
                case 2 -> System.out.println("EDITAR DIETA");
                case 3 -> {
                    System.out.println("BORRAR DIETA");
                    Jdbc.borrarDieta(conn);
                }
                case 4 -> Dieta.mostrarDietas(Jdbc.getDieta(conn));
                case 5 -> MenuCocina.menuCocina(conn);
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuDieta(conn);
                }

            }

            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-Volver al menú anterior");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuDieta(conn);
                    case 2 -> MenuCocina.menuCocina(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            menuDieta(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuDieta(conn);
        }
    }
}
