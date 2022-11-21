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

    public static void insertarPaciente(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            boolean inserccion = true;
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Insertar Paciente\s
                    2.-Volver a Menú Médico""");
            int opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {
                    Paciente paciente = new Paciente();
                    paciente.recogerDatosPaciente();
                    String query = "INSERT INTO paciente (nhc, nombre, apellidos, observaciones, habitacion) VALUES (?,?,?,?,?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, paciente.getNhc());
                    preparedStmt.setString(2, paciente.getNombre());
                    preparedStmt.setString(3, paciente.getApellidos());
                    preparedStmt.setString(4, paciente.getObservaciones());
                    preparedStmt.setInt(5, paciente.getHabitacion());
                    do {
                        System.out.println("¿Seguro que quiere insertar este paciente? 1.-Sí 2.-No: ");
                        int insertar = sc.nextInt();
                        switch (insertar) {
                            case 1 -> {
                                preparedStmt.execute();
                                System.out.println("PACIENTE INTRODUCIDO");
                            }
                            case 2 -> MenuMedico.menuMedico(conn);
                            default -> {
                                System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                inserccion = false;
                            }
                        }
                    } while (!inserccion);
                }
                case 2 -> MenuMedico.menuMedico(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    insertarPaciente(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            insertarPaciente(conn);
        } catch (SQLException e){
            System.out.println("El paciente ya existe");
            insertarPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            insertarPaciente(conn);
        }
    }
    public static void borrarPaciente(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Borrar Paciente\s
                    2.-Volver a Menú Médico""");
            int opcion = sc.nextInt();
            boolean borrado = true;
            switch (opcion) {
                case 1 -> {
                    Paciente.mostrarPacientes(consultarPaciente(conn));
                    System.out.println("Introducir NHC del paciente que desea borrar: ");
                    int nhc = sc.nextInt();
                    if (consultarSiExiste(conn,nhc) != 0){
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
                                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                    borrado = false;
                                }
                            }
                        } while (!borrado);

                    } else {
                        System.out.println("El paciente no existe");
                        borrarPaciente(conn);
                    }
                }
                case 2 -> MenuMedico.menuMedico(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    borrarPaciente(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            borrarPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            borrarPaciente(conn);
        }
    }
    public static void editarPaciente(Connection conn) throws SQLException {
        String query = "UPDATE paciente SET ";
        boolean seguir = true;
        boolean edicion = true;
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        try {
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Editar Paciente\s
                    2.-Volver a Menú Médico""");
            int opcion = scInt.nextInt();
            switch (opcion) {
                case 1 -> {
                    Paciente.mostrarPacientes(JdbcPaciente.consultarPaciente(conn));
                    System.out.println("Introducir NHC del paciente que desea editar: ");
                    int nhc = scInt.nextInt();
                    if (consultarSiExiste(conn,nhc) != 0){
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
                                query += "nombre = ? WHERE nhc= ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, nombre);
                                preparedStmt.setInt(2, nhc);
                            }
                            case 2 -> {
                                System.out.println("Introduzca los nuevos apellidos: ");
                                String apellidos = scString.nextLine();
                                query += "apellidos = ? WHERE nhc = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, apellidos);
                                preparedStmt.setInt(2, nhc);
                            }
                            case 3 -> {
                                System.out.println("Introduzca las nuevas observaciones: ");
                                String observaciones = scString.nextLine();
                                query += "observaciones = ? WHERE nhc = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, observaciones);
                                preparedStmt.setInt(2, nhc);
                            }
                            case 4 -> {
                                System.out.println("Introduzca la nueva habitación: ");
                                String habitacion = scString.nextLine();
                                query += "habitacion = ? WHERE nhc = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, habitacion);
                                preparedStmt.setInt(2, nhc);
                            }
                            case 5 -> MenuMedico.menuMedico(conn);
                            default -> {
                                System.out.println("Número fuera de rango. vuelva a introducir un número: ");
                                editarPaciente(conn);
                            }
                        }
                        do {
                            System.out.println("¿Seguro que quiere editar este paciente? 1.-Sí 2.-No");
                            int editar = scInt.nextInt();
                            switch (editar) {
                                case 1 -> {
                                    preparedStmt.execute();
                                    System.out.println("PACIENTE EDITADO");
                                }
                                case 2 -> MenuMedico.menuMedico(conn);
                                default -> {
                                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
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
                                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                    seguir = false;
                                }
                            }
                        } while (!seguir);
                    } else {
                        System.out.println("El paciente no existe");
                        editarPaciente(conn);
                    }
                }
                case 2 -> MenuMedico.menuMedico(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    editarPaciente(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            editarPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            editarPaciente(conn);
        }
    }
    private static int consultarSiExiste(Connection conn, int nhc) throws SQLException {
        int resultado = 0;
        String query = "SELECT EXISTS (SELECT * FROM paciente WHERE nhc=" + nhc + ") AS resultado";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            resultado = rs.getInt("resultado");
        }
        return resultado;
    }
}
