package menus;


import entidades.Paciente;
import jdbc.JdbcPaciente;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPaciente {
    public static void menuPaciente(Connection conn) {
        Scanner sc = new Scanner(System.in);
        boolean seguir = true;
        int accion;
        int continuar;

        try {
            System.out.println("""
                    Indique qué acción quiere realizar:\s
                    1.-Añadir Paciente\s
                    2.-Editar Paciente\s
                    3.-Borrar Paciente\s
                    4.-Consultar Paciente\s
                    5.-Volver a Menú Médico""");

            accion = sc.nextInt();

            switch (accion) {
                case 1 -> {
                    System.out.println("AÑADIR PACIENTE");
                    JdbcPaciente.insertarPaciente(conn);
                }
                case 2 -> {
                    System.out.println("EDITAR PACIENTE");
                    JdbcPaciente.editarPaciente(conn);
                }
                case 3 -> {
                    System.out.println("BORRAR PACIENTE");
                    JdbcPaciente.borrarPaciente(conn);
                }
                case 4 -> Paciente.mostrarPacientes(JdbcPaciente.consultarPaciente(conn));
                case 5 -> MenuMedico.menuMedico(conn);
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuPaciente(conn);
                }

            }

            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-Volver a Menú Médico");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuPaciente(conn);
                    case 2 -> MenuMedico.menuMedico(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            menuPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            menuPaciente(conn);
        }
    }
}
