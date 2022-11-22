package menus;


import entidades.Paciente;
import jdbc.JdbcPaciente;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPaciente {
    public static void menuPaciente(Connection conn) {
        //En esta clase se muestran las acciones que se pueden hacer en el menú del paciente
        Scanner sc = new Scanner(System.in);
        boolean seguir = true; //Controlar que quiere seguir en este menú
        int accion;
        int continuar;
        try {
            System.out.println("─────── HA INGRESADO A LA SECCIÓN PACIENTE ───────");
            System.out.println("""
                    ┌──────────────────────────────────────────────┐\s
                    │  Indique qué acción quiere realizar:         │\s
                    │    1.-Añadir Paciente                        │\s
                    │    2.-Editar Paciente                        │\s
                    │    3.-Borrar Paciente                        │\s
                    │    4.-Consultar Pacientes                    │\s
                    │    5.-Volver a Menú Médico                   │\s
                    └──────────────────────────────────────────────┘\s
                     """);
            accion = sc.nextInt(); //Elige lo que desea realizar
            switch (accion) {
                case 1 -> JdbcPaciente.insertarPaciente(conn);
                case 2 -> JdbcPaciente.editarPaciente(conn);
                case 3 -> JdbcPaciente.borrarPaciente(conn);
                case 4 -> Paciente.mostrarPacientes(JdbcPaciente.consultarPaciente(conn));
                case 5 -> MenuMedico.menuMedico(conn);
                default -> {
                    System.err.println("Número fuera de rango, vuelva a introducir un número: ");
                    menuPaciente(conn);
                }
            }
            do {
                System.out.println("¿Quiere hacer otra acción? 1.-Sí 2.-Volver a Menú Médico");
                continuar = sc.nextInt();
                switch (continuar) {
                    case 1 -> menuPaciente(conn); //Le devulve a este menú
                    case 2 -> MenuMedico.menuMedico(conn); //Le devuelve al menú médico
                    default -> {
                        System.err.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            menuPaciente(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            menuPaciente(conn);
        }
    }
}
