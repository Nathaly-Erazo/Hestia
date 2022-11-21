package menus;

import entidades.Toma;
import jdbc.JdbcToma;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuToma {
    public static void menuToma(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int accion;
        int continuar;

        try {
            System.out.println("""
                    Indique qué acción quiere realizar:\s
                    1.-Añadir Toma\s
                    2.-Editar Toma\s
                    3.-Borrar Toma\s
                    4.-Consultar Tomas\s
                    5.-Volver a Menu Nutrición""");

            accion = sc.nextInt();
            switch (accion) {
                case 1 -> {
                    System.out.println("AÑADIR TOMA");
                    JdbcToma.insertarToma(conn);
                }
                case 2 -> {
                    System.out.println("EDITAR TOMA");
                    JdbcToma.editarToma(conn);
                }
                case 3 -> {
                    System.out.println("BORRAR TOMA");
                    JdbcToma.borrarToma(conn);
                }
                case 4 -> Toma.mostrarTomas(JdbcToma.consultarToma(conn));
                case 5 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuToma(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-Volver a Menú Nutrición");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuToma(conn);
                    case 2 -> MenuNutricion.menuNutricion(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            menuToma(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuToma(conn);
        }
    }
}

