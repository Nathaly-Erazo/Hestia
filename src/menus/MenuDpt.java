package menus;

import entidades.Dpt;
import jdbc.JdbcDpt;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuDpt {
    public static void menuDpt(Connection conn) {
        //En esta clase se muestran las acciones que se pueden hacer en el menú de registros
        //Tiene el mismo formato que la clase MenuPaciente
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int accion;
        int continuar;
        try {
            System.out.println("─────── HA INGRESADO A LA SECCIÓN REGISTRO ───────");
            System.out.println("""
                    ┌──────────────────────────────────────────────┐\s
                    │  Indique qué acción quiere realizar:         │\s
                    │    1.-Añadir Registro                        │\s
                    │    2.-Editar Registro                        │\s
                    │    3.-Borrar Registro                        │\s
                    │    4.-Consultar RegistroS                    │\s
                    │    5.-Volver a Menú Nutrición                │\s
                    └──────────────────────────────────────────────┘\s
                     """);
            accion = sc.nextInt();
            switch (accion) {
                case 1 -> JdbcDpt.insertarRegistro(conn);
                case 2 -> JdbcDpt.editarRegistro(conn);
                case 3 -> JdbcDpt.borrarRegistro(conn);
                case 4 -> Dpt.mostrarRegistros(JdbcDpt.consultarDpt(conn));
                case 5 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.err.println("Número fuera de rango, vuelva a introducir un número: ");
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
                        System.err.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            menuDpt(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            menuDpt(conn);
        }
    }
}
