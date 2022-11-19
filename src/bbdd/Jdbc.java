package bbdd;

import entidades.Dieta;
import entidades.Dpt;
import entidades.Paciente;
import entidades.Toma;
import menus.MenuEditarPaciente;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

//En esta clase se introducirá lo relacionado con la base de datos
public class Jdbc {


    public static Connection getConnection(String databaseUrl, Properties info) {
        Connection dbConnection = null;

        try {
            dbConnection = DriverManager.getConnection(databaseUrl, info);

            if (dbConnection != null) {
                System.out.println("Conexión satisfactoria a la base de datos SQL: " + databaseUrl);
            }

        } catch (SQLException e) {
            System.out.println("Ha ocurrido un error mientras se hacía la conexión a la base de datos SQl");
            e.printStackTrace();
        }

        return dbConnection;

    }

    public static ArrayList<Paciente> getPaciente(Connection conn) throws SQLException {
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

    public static ArrayList<Dieta> getDieta(Connection conn) throws SQLException {
        ArrayList<Dieta> dietas = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM dieta");
        while (rs.next()) {
            Dieta dieta = new Dieta();
            dieta.setCodigo(rs.getInt("codigo"));
            dieta.setNombre(rs.getString("nombre"));
            dieta.setDesayuno(rs.getString("desayuno"));
            dieta.setComida(rs.getString("comida"));
            dieta.setMerienda(rs.getString("merienda"));
            dieta.setCena(rs.getString("cena"));
            dietas.add(dieta);
        }
        return dietas;
    }

    public static ArrayList<Toma> getToma(Connection conn) throws SQLException {
        ArrayList<Toma> tomas = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM toma");
        while (rs.next()) {
            Toma toma = new Toma();
            toma.setCodigo(rs.getInt("codigo"));
            toma.setNombre(rs.getString("nombre"));
            tomas.add(toma);
        }
        return tomas;
    }

    public static ArrayList<Dpt> getDpt(Connection conn) throws SQLException {
        ArrayList<Dpt> registros = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM dieta_paciente_toma");

        while (rs.next()) {
            Dpt registro = new Dpt();
            registro.setCodigo(rs.getInt("codigo"));
            registro.setFecha(rs.getString("fecha"));
            registro.setCodigoDieta(rs.getInt("codigo_dieta"));
            registro.setCodigoToma(rs.getInt("codigo_toma"));
            registro.setNhcPaciente(rs.getInt("nhc_paciente"));
            registros.add(registro);
        }
        return registros;
    }

    public static void insertarToma(Connection conn) throws SQLException {

        Toma toma = new Toma();
        toma.recogerDatosToma();
        String query = "INSERT INTO toma (codigo, nombre) VALUES (?,?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, toma.getCodigo());
        preparedStmt.setString(2, toma.getNombre());

        preparedStmt.execute();

        System.out.println("TOMA INTRODUCIDA: \n" + toma.toString());
    }

    public static void borrarToma(Connection conn) {

        try {
            Scanner sc = new Scanner(System.in);
            Toma.mostrarTomas(Jdbc.getToma(conn));
            System.out.println("Introducir código de la toma que desea borrar: ");
            int codigo = sc.nextInt();
            boolean borrado = true;
            do {
                System.out.println("¿Seguro que quiere borrar este resgistro? 1.-Sí 2.-No");
                int borrar = sc.nextInt();
                switch (borrar) {
                    case 1 -> {
                        String query = "DELETE FROM toma WHERE codigo = ?";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, codigo);

                        preparedStmt.execute();
                        System.out.println("TOMA BORRADA");
                    }
                    case 2 -> borrarToma(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        borrado = false;
                    }
                }

            } while (!borrado);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            borrarToma(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            borrarToma(conn);
        }
    }

    public static void editarToma(Connection conn) {
        try {
            Toma toma = new Toma();
            Scanner scInt = new Scanner(System.in);
            Scanner scString = new Scanner(System.in);
            Toma.mostrarTomas(Jdbc.getToma(conn));
            System.out.println("Introducir código de la toma que desea editar: ");
            int codigo = scInt.nextInt();
            System.out.println("Introduzca el nuevo nombre: ");
            String nombre = scString.nextLine();
            boolean edicion = true;
            do {
                System.out.println("¿Seguro que quiere editar este resgistro? 1.-Sí 2.-No");
                int editar = scInt.nextInt();
                switch (editar) {
                    case 1 -> {
                        String query = "UPDATE toma SET nombre = ? WHERE codigo = ?";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(1, nombre);
                        preparedStmt.setInt(2, codigo);

                        preparedStmt.execute();
                        System.out.println("TOMA EDITADA: \n" + toma.toString());
                    }
                    case 2 -> editarToma(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        edicion = false;
                    }
                }

            } while (!edicion);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            editarToma(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            editarToma(conn);
        }
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
            Paciente.mostrarPacientes(Jdbc.getPaciente(conn));
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

    public static void editarPaciente(Connection conn) {
        try {
            Scanner scInt = new Scanner(System.in);



        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            editarPaciente(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            editarPaciente(conn);
        }
    }

    public static void insertarDieta(Connection conn) throws SQLException {

        Dieta dieta = new Dieta();
        dieta.recogerDatosDieta();
        String query = "INSERT INTO dieta (nombre, desayuno, comida, merienda, cena) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, dieta.getNombre());
        preparedStmt.setString(2, dieta.getDesayuno());
        preparedStmt.setString(3, dieta.getComida());
        preparedStmt.setString(4, dieta.getMerienda());
        preparedStmt.setString(5, dieta.getCena());

        preparedStmt.execute();
        System.out.println("DIETA INTRODUCIDA: \n" + dieta.toString());
    }

    public static void borrarDieta(Connection conn) {

        try {
            Scanner sc = new Scanner(System.in);
            Dieta.mostrarDietas(Jdbc.getDieta(conn));
            System.out.println("Introducir código de la dieta que desea borrar: ");
            int codigo = sc.nextInt();
            boolean borrado = true;
            do {
                System.out.println("¿Seguro que quiere borrar este resgistro? 1.-Sí 2.-No");
                int borrar = sc.nextInt();
                switch (borrar) {
                    case 1 -> {
                        String query = "DELETE FROM dieta WHERE codigo = ?";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, codigo);

                        preparedStmt.execute();
                        System.out.println("DIETA BORRADA");
                    }
                    case 2 -> borrarDieta(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        borrado = false;
                    }
                }

            } while (!borrado);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            borrarDieta(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            borrarDieta(conn);
        }
    }

    public static void insertarRegistro(Connection conn) throws SQLException {

        Dpt registro = new Dpt();
        registro.recogerDatosDpt();
        String query = "INSERT INTO dieta_paciente_toma (nhc_paciente, codigo_dieta, codigo_toma, nhc_paciente,fecha) VALUES (?,?,?,?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, registro.getNhcPaciente());
        preparedStmt.setInt(2, registro.getCodigoDieta());
        preparedStmt.setInt(3, registro.getCodigoToma());
        preparedStmt.setDate(4, java.sql.Date.valueOf(registro.getFecha()));

        preparedStmt.execute();
        System.out.println("DATOS INTRODUCIDOS: \n" + registro.toString());
    }

    public static void borrarRegistro(Connection conn) {

        try {
            Scanner sc = new Scanner(System.in);
            Dieta.mostrarDietas(Jdbc.getDieta(conn));
            System.out.println("Introducir código del registro que desea borrar: ");
            int codigo = sc.nextInt();
            boolean borrado = true;
            do {
                System.out.println("¿Seguro que quiere borrar este resgistro? 1.-Sí 2.-No");
                int borrar = sc.nextInt();
                switch (borrar) {
                    case 1 -> {
                        String query = "DELETE FROM dieta_paciente_toma WHERE codigo = ?";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, codigo);

                        preparedStmt.execute();
                        System.out.println("REGISTRO BORRADO");
                    }
                    case 2 -> borrarDieta(conn);
                    default -> {
                        System.out.println("Número no válido, Vuelva a intoducirlo");
                        borrado = false;
                    }
                }

            } while (!borrado);

        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            borrarDieta(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            borrarDieta(conn);
        }
    }

}
