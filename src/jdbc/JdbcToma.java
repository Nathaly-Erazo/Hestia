package jdbc;

import entidades.Toma;
import menus.MenuNutricion;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JdbcToma {
    //En esta clase se encuentra lo relacionado con las consultas a la base de datos de la tabla toma
    //Tiene la misma estructura que la clase JdbcDieta
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
    public static void insertarToma(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            boolean inserccion = true;
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Insertar Toma\s
                    2.-Volver a Menú Nutrición""");
            int opcion = sc.nextInt();
            switch (opcion){
                case 1 -> {
                    Toma toma = new Toma();
                    toma.recogerDatosToma();
                    String query = "INSERT INTO toma (codigo, nombre) VALUES (?,?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, toma.getCodigo());
                    preparedStmt.setString(2, toma.getNombre());
                    do {
                        System.out.println("¿Seguro que quiere insertar esta toma? 1.-Sí 2.-No: \n" + toma.toString());
                        int insertar = sc.nextInt();
                        switch (insertar) {
                            case 1 -> {
                                preparedStmt.execute();
                                System.out.println("TOMA INTRODUCIDA");
                            }
                            case 2 -> MenuNutricion.menuNutricion(conn);
                            default -> {
                                System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                inserccion = false;
                            }
                        }
                    } while (!inserccion);
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    insertarToma(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            insertarToma(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            insertarToma(conn);
        }
    }
    public static void borrarToma(Connection conn) {

        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Borrar Toma\s
                    2.-Volver a Menú Nutrición""");
            int opcion = sc.nextInt();
            boolean borrado = true;
            switch (opcion){
                case 1 -> {
                    Toma.mostrarTomas(consultarToma(conn));
                    System.out.println("Introducir código de la toma que desea borrar: ");
                    int codigo = sc.nextInt();
                    do {
                        System.out.println("¿Seguro que quiere borrar esta toma? 1.-Sí 2.-No");
                        int borrar = sc.nextInt();
                        switch (borrar) {
                            case 1 -> {
                                String query = "DELETE FROM toma WHERE codigo = ?";
                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, codigo);

                                preparedStmt.execute();
                                System.out.println("TOMA BORRADA");
                            }
                            case 2 -> MenuNutricion.menuNutricion(conn);
                            default -> {
                                System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                borrado = false;
                            }
                        }
                    } while (!borrado);
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    borrarToma(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            borrarToma(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            borrarToma(conn);
        }
    }
    public static void editarToma(Connection conn) {
        String query = "UPDATE toma SET nombre = ? WHERE codigo = ?";
        boolean edicion = true;
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        try {
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Editar Toma\s
                    2.-Volver a Menú Nutrición""");
            int opcion = scInt.nextInt();
            switch (opcion){
                case 1->{
                    Toma.mostrarTomas(consultarToma(conn));
                    System.out.println("Introducir código de la toma que desea editar: ");
                    int codigo = scInt.nextInt();
                    System.out.println("Introduzca el nuevo nombre: ");
                    String nombre = scString.nextLine();
                    do {
                        System.out.println("¿Seguro que quiere editar esta toma? 1.-Sí 2.-No");
                        int editar = scInt.nextInt();
                        switch (editar) {
                            case 1 -> {
                                PreparedStatement preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, nombre);
                                preparedStmt.setInt(2, codigo);

                                preparedStmt.execute();
                                System.out.println("TOMA EDITADA");
                            }
                            case 2 -> MenuNutricion.menuNutricion(conn);
                            default -> {
                                System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                edicion = false;
                            }
                        }
                    } while (!edicion);
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    editarToma(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            editarToma(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            editarToma(conn);
        }
    }

}
