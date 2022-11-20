package menus;


import entidades.Dieta;
import entidades.Dpt;
import entidades.Toma;
import jdbc.JdbcDieta;
import jdbc.JdbcDpt;
import jdbc.JdbcToma;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuMedico {
    public static void menuMedico(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int seccion;
        int continuar;

        try {
            System.out.println("""
                    Indique a qué sección quiere acceder:\s
                    1.-Paciente\s
                    2.-Consultar Tomas\s
                    3.-Consultar Dietas\s
                    4.-consultar Registros\s
                    5.-Salir de Hestia""");

            seccion = sc.nextInt();

            switch (seccion) {
                case 1 -> {
                    System.out.println("HA INGRESADO A LA SECCION PACIENTE");
                    MenuPaciente.menuPaciente(conn);
                }
                case 2 -> {
                    System.out.println("HA INGRESADO A LA CONSULTA DE TOMAS");
                    System.out.println("-----------------------------------");
                    Toma.mostrarTomas(JdbcToma.consultarToma(conn));
                }
                case 3 -> {
                    System.out.println("HA INGRESADO A LA CONSULTA DE DIETAS");
                    System.out.println("-----------------------------------");
                    Dieta.mostrarDietas(JdbcDieta.consultarDieta(conn));
                }
                case 4 -> {
                    System.out.println("HA INGRESADO A LA CONSULTA DE LOS REGISTROS");
                    System.out.println("-----------------------------------");
                    Dpt.mostrarRegistros(JdbcDpt.consultarDpt(conn));
                }
                case 5 -> {
                    System.out.println("HASTA PRONTO");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuMedico(conn);
                }
            }

            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-No");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuMedico(conn);
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
            menuMedico(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuMedico(conn);
        }
    }
}
