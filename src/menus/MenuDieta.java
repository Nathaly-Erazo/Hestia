package menus;

import entidades.Dieta;
import jdbc.JdbcDieta;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuDieta {
    public static void menuDieta(Connection conn) {
        //En esta clase se muestran las acciones que se pueden hacer en el menú de la dieta
        //Tiene el mismo formato que la clase MenuPaciente
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int accion;
        int continuar;
        try {
            System.out.println("───────── HA INGRESADO A LA SECCIÓN DIETA ────────");
            System.out.println("""
                    ┌──────────────────────────────────────────────┐\s
                    │  Indique qué acción quiere realizar:         │\s
                    │    1.-Añadir Dieta                           │\s
                    │    2.-Editar Dieta                           │\s
                    │    3.-Borrar Dieta                           │\s
                    │    4.-Consultar Dietas                       │\s
                    │    5.-Volver a Menú Cocina                   │\s
                    └──────────────────────────────────────────────┘\s
                     """);
            accion = sc.nextInt();
            switch (accion) {
                case 1 -> JdbcDieta.insertarDieta(conn);
                case 2 -> JdbcDieta.editarDieta(conn);
                case 3 -> JdbcDieta.borrarDieta(conn);
                case 4 -> Dieta.mostrarDietas(JdbcDieta.consultarDieta(conn));
                case 5 -> MenuCocina.menuCocina(conn);
                default -> {
                    System.err.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuDieta(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-Volver a Menú Cocina");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuDieta(conn);
                    case 2 -> MenuCocina.menuCocina(conn);
                    default -> {
                        System.err.println("Número fuera de rango, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            menuDieta(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            menuDieta(conn);
        }
    }
}
