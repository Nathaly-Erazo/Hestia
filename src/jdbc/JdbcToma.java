package jdbc;

import entidades.Toma;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JdbcToma {
    public static ArrayList<Toma> consultarToma(Connection conn) throws SQLException {
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
            Toma.mostrarTomas(consultarToma(conn));
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
            Toma.mostrarTomas(consultarToma(conn));
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

}
