package jdbc;

import entidades.Dieta;
import entidades.Dpt;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static jdbc.JdbcDieta.borrarDieta;
public class JdbcDpt {
    public static ArrayList<Dpt> consultarDpt(Connection conn) throws SQLException {
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
            Dieta.mostrarDietas(JdbcDieta.consultarDieta(conn));
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
// TODO: editar esta clase para que quede igual que JdbcDieta
// TODO: crear el método editarRegistro