package menus;

import bbdd.Jdbc;
import entidades.Dieta;
import entidades.Paciente;

import java.sql.Connection;
import java.util.Scanner;


public class MenuNutricion {
    public static void menuNutricion(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int seccion;
        try {
            System.out.println("""
                    Indique a qué sección quiere acceder:\s
                    1.-Consultar Pacientes\s
                    2.-Consultar Dietas\s
                    3.-Toma\s
                    4.-Registro\s
                    5.-Salir de Hestia""");

            seccion = sc.nextInt();

            switch (seccion) {
                case 1 -> {
                    System.out.println("HA INGRESADO A LA CONSULTA DE PACIENTES");
                    System.out.println("-----------------------------------");
                    Paciente.mostrarPacientes(Jdbc.getPaciente(conn));
                }
                case 2 -> {
                    System.out.println("HA INGRESADO A LA CONSULTA DE DIETAS");
                    System.out.println("-----------------------------------");
                    Dieta.mostrarDietas(Jdbc.getDieta(conn));
                }
                case 3 -> {
                    System.out.println("HA INGRESADO A LA SECCIÓN TOMAS");
                    MenuToma.menuToma(conn);
                }
                case 4 -> {
                    System.out.println("HA INGRESADO A LA SEECIÓN REGISTRO");
                    MenuDpt.menuDpt(conn);
                }
                case 5 -> {
                    System.out.println("HASTA PRONTO");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuNutricion(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-No");
                int continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuNutricion(conn);
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

        } catch (NumberFormatException e) {
            System.out.println("Formato de número no válido");
            menuNutricion(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuNutricion(conn);
        }
    }
}
