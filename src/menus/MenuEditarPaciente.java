package menus;


import bbdd.Jdbc;
import entidades.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuEditarPaciente {
    public static void editarCamposPaciente(Connection conn) throws SQLException {
        String query;
        boolean seguir = true;
        boolean edicion = true;
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        Paciente.mostrarPacientes(Jdbc.getPaciente(conn));
        System.out.println("Introducir código del paciente que desea editar: ");
        int codigo = scInt.nextInt();
        try {
            System.out.println("""
                    ¿Qué campo quiere editar del paciente?:\s
                    1.-Nombre\s
                    2.-Apellidos\s
                    3.-Observaciones\s
                    4.-Habitación
                    5.-Volver a Menú Médico""");
            int campo = scInt.nextInt();
            switch (campo) {
                case 1 -> {
                    System.out.println("Introduzca el nuevo nombre: ");
                    String nombre = scString.nextLine();
                    query = "UPDATE paciente SET nombre = ? WHERE nhc= ?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, nombre);
                    preparedStmt.setInt(2, codigo);

                    preparedStmt.execute();
                    System.out.println("PACIENTE EDITAD0: \n" + "Nuevo nombre: " + nombre);
                }
                case 2 -> {
                    System.out.println("Introduzca los nuevos apellidos: ");
                    String apellidos = scString.nextLine();
                    query = "UPDATE paciente SET apellidos = ? WHERE nhc = ?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, apellidos);
                    preparedStmt.setInt(2, codigo);

                    preparedStmt.execute();
                    System.out.println("PACIENTE EDITAD0: \n" + "Nuevos apellidos: " + apellidos);
                }
                case 3 -> {
                    System.out.println("Introduzca las nuevas observaciones: ");
                    String observaciones = scString.nextLine();
                    query = "UPDATE paciente SET observaciones = ? WHERE nhc = ?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, observaciones);
                    preparedStmt.setInt(2, codigo);

                    preparedStmt.execute();
                    System.out.println("PACIENTE EDITAD0: \n" + "Nuevas observaciones: " + observaciones);
                }
                case 4 -> {
                    System.out.println("Introduzca la nueva habitación: ");
                    String habitacion = scString.nextLine();
                    query = "UPDATE paciente SET habitacion = ? WHERE nhc = ?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, habitacion);
                    preparedStmt.setInt(2, codigo);

                    preparedStmt.execute();
                    System.out.println("PACIENTE EDITAD0: \n" + "Nueva habitación: " + habitacion);
                }
                case 5 -> MenuMedico.menuMedico(conn);
                default -> {
                    System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                    editarCamposPaciente(conn);
                }
            }
            do {
                System.out.println("¿Seguro que quiere editar este resgistro? 1.-Sí 2.-No");
                int editar = scInt.nextInt();
                switch (editar) {
                    case 1 -> editarCamposPaciente(conn);
                    case 2 -> MenuMedico.menuMedico(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        edicion = false;
                    }
                }

            } while (!edicion);
            do {
                System.out.println("¿Quiere editar otro campo? 1.-Sí 2.-Volver a Menú Médico");
                int continuar = scInt.nextInt();
                switch (continuar) {
                    case 1 -> editarCamposPaciente(conn);
                    case 2 -> MenuMedico.menuMedico(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            editarCamposPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            editarCamposPaciente(conn);
        }
    }
}
