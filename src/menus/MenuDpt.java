package menus;

import entidades.Dpt;
import jdbc.JdbcDpt;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuDpt {
    public static void menuDpt(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int accion;
        int continuar;

        try {
            System.out.println("""
                    Indique qué acción quiere realizar:\s
                    1.-Añadir Registro\s
                    2.-Editar Registro\s
                    3.-Borrar Registro\s
                    4.-Consultar Registros\s
                    5.-Volver a Menú Nutrición""");

            accion = sc.nextInt();
            switch (accion) {
                case 1 -> {
                    System.out.println("AÑADIR REGISTRO");
                    JdbcDpt.insertarRegistro(conn);
                }
                case 2 -> {
                    System.out.println("EDITAR REGISTRO");
                    JdbcDpt.editarRegistro(conn);
                }
                case 3 -> {
                    System.out.println("BORRAR REGISTRO");
                    JdbcDpt.borrarRegistro(conn);
                }
                case 4 -> Dpt.mostrarRegistros(JdbcDpt.consultarDpt(conn));
                case 5 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuDpt(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-Volver a Menú Nutrición");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuDpt(conn);
                    case 2 -> MenuNutricion.menuNutricion(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            menuDpt(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuDpt(conn);
        }
    }
}
