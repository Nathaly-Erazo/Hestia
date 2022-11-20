package jdbc;

import entidades.Paciente;
import menus.MenuMedico;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JdbcPaciente {
    //En esta clase se encuentra lo relacionado con la consulta en la base de datos de la tabla paciente
    //Tiene la misma estructura que la clase JdbcDieta
    public static ArrayList<Paciente> consultarPaciente(Connection conn) throws SQLException {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM paciente");

        while (rs.next()) {
            Paciente paciente = new Paciente();
            paciente.setNombre(rs.getString("nombre"));
            paciente.setApellidos(rs.getString("apellidos"));
            paciente.setNhc(rs.getInt("nhc"));
            paciente.setObservaciones(rs.getString("observaciones"));
            paciente.setHabitacion(rs.getInt("habitacion"));
            pacientes.add(paciente);
        }
        return pacientes;
    }
    public static void insertarPaciente(Connection conn) throws SQLException {

        Paciente paciente = new Paciente();
        paciente.recogerDatosPaciente();
        String query = "INSERT INTO paciente (nhc, nombre, apellidos, observaciones, habitacion) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, paciente.getNhc());
        preparedStmt.setString(2, paciente.getNombre());
        preparedStmt.setString(3, paciente.getApellidos());
        preparedStmt.setString(4, paciente.getObservaciones());
        preparedStmt.setInt(5, paciente.getHabitacion());

        preparedStmt.execute();
        System.out.println("PACIENTE INTRODUCIDO: \n" + paciente.toString());
    }
    public static void borrarPaciente(Connection conn) {

        try {
            Scanner sc = new Scanner(System.in);
            Paciente.mostrarPacientes(consultarPaciente(conn));
            System.out.println("Introducir NHC del paciente que desea borrar: ");
            int nhc = sc.nextInt();
            boolean borrado = true;
            do {
                System.out.println("¿Seguro que quiere borrar este registro? 1.-Sí 2.-No");
                int borrar = sc.nextInt();
                switch (borrar) {
                    case 1 -> {
                        String query = "DELETE FROM paciente WHERE nhc = ?";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, nhc);

                        preparedStmt.execute();
                        System.out.println("PACIENTE BORRADO");
                    }
                    case 2 -> borrarPaciente(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        borrado = false;
                    }
                }

            } while (!borrado);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            borrarPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            borrarPaciente(conn);
        }
    }
    public static void editarPaciente(Connection conn) throws SQLException {
        String query;
        boolean seguir = true;
        boolean edicion = true;
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        Paciente.mostrarPacientes(JdbcPaciente.consultarPaciente(conn));
        System.out.println("Introducir NHC del paciente que desea editar: ");
        int codigo = scInt.nextInt();
        try {
            System.out.println("""
                    ¿Qué campo quiere editar del paciente?:\s
                    1.-Nombre\s
                    2.-Apellidos\s
                    3.-Observaciones\s
                    4.-Habitación\s
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
                    editarPaciente(conn);
                }
            }
            do {
                System.out.println("¿Seguro que quiere editar este resgistro? 1.-Sí 2.-No");
                int editar = scInt.nextInt();
                switch (editar) {
                    case 1 -> editarPaciente(conn);
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
                    case 1 -> editarPaciente(conn);
                    case 2 -> MenuMedico.menuMedico(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        seguir = false;
                    }
                }
            } while (!seguir);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            editarPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            editarPaciente(conn);
        }
    }
}
