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
        System.out.println("────────── CONSULTA TOMA ──────────");
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
            boolean insercion = true;
            System.out.println("───────── AÑADIR TOMA ─────────");
            System.out.println("""
                    ┌───────────────────────────────┐\s
                    │  Elija una opción:            │\s
                    │    1.-Insertar Toma           │\s
                    │    2.-Volver a Menú Nutrición │\s
                    └───────────────────────────────┘\s
                     """);
            int opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {
                    Toma toma = new Toma();
                    toma.recogerDatosToma();
                    String query = "INSERT INTO toma (codigo, nombre) VALUES (?,?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, toma.getCodigo());
                    preparedStmt.setString(2, toma.getNombre());
                    do {
                        System.out.println("¿Seguro que quiere insertar esta toma? 1.-Sí 2.-No: ");
                        int insertar = sc.nextInt();
                        switch (insertar) {
                            case 1 -> {
                                preparedStmt.execute();
                                System.out.println("✓✓✓ TOMA INTRODUCIDA ✓✓✓");
                            }
                            case 2 -> insertarToma(conn);
                            default -> {
                                System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                                insercion = false;
                            }
                        }
                    } while (!insercion);
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                    insertarToma(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            insertarToma(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            insertarToma(conn);
        }
    }

    public static void borrarToma(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("────────── BORRAR TOMA ─────────");
            System.out.println("""
                    ┌───────────────────────────────┐\s
                    │  Elija una opción:            │\s
                    │    1.-Borrar Toma             │\s
                    │    2.-Volver a Menú Nutrición │\s
                    └───────────────────────────────┘\s
                     """);
            int opcion = sc.nextInt();
            boolean borrado = true;
            switch (opcion) {
                case 1 -> {
                    Toma.mostrarTomas(consultarToma(conn));
                    System.out.println("Introducir código de la toma que desea borrar: ");
                    int codigo = sc.nextInt();
                    if (consultarSiExiste(conn, codigo) != 0) {
                        do {
                            System.out.println("¿Seguro que quiere borrar esta toma? 1.-Sí 2.-No");
                            int borrar = sc.nextInt();
                            switch (borrar) {
                                case 1 -> {
                                    String query = "DELETE FROM toma WHERE codigo = ?";
                                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                                    preparedStmt.setInt(1, codigo);

                                    preparedStmt.execute();
                                    System.out.println("✓✓✓ TOMA BORRADA ✓✓✓");
                                }
                                case 2 -> borrarToma(conn);
                                default -> {
                                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                                    borrado = false;
                                }
                            }
                        } while (!borrado);
                    } else {
                        System.err.println("La toma no existe");
                        borrarToma(conn);
                    }
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                    borrarToma(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            borrarToma(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            borrarToma(conn);
        }
    }

    public static void editarToma(Connection conn) {
        String query = "UPDATE toma SET nombre = ? WHERE codigo = ?";
        boolean edicion = true;
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        try {
            System.out.println("───────── EDITAR TOMA ───────── ");
            System.out.println("""
                    ┌───────────────────────────────┐\s
                    │  Elija una opción:            │\s
                    │    1.-Editar Toma             │\s
                    │    2.-Volver a Menú Nutrición │\s
                    └───────────────────────────────┘\s
                     """);
            int opcion = scInt.nextInt();
            switch (opcion) {
                case 1 -> {
                    Toma.mostrarTomas(consultarToma(conn));
                    System.out.println("Introducir código de la toma que desea editar: ");
                    int codigo = scInt.nextInt();
                    if (consultarSiExiste(conn, codigo) != 0) {
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
                                    System.out.println("✓✓✓ TOMA EDITADA ✓✓✓");
                                }
                                case 2 -> editarToma(conn);
                                default -> {
                                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                                    edicion = false;
                                }
                            }
                        } while (!edicion);
                    } else {
                        System.err.println("La toma no existe");
                        editarToma(conn);
                    }
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                    editarToma(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            editarToma(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            editarToma(conn);
        }
    }

    private static int consultarSiExiste(Connection conn, int codigo) throws SQLException {
        int resultado = 0;
        String query = "SELECT EXISTS (SELECT * FROM toma WHERE codigo=" + codigo + ") AS resultado";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            resultado = rs.getInt("resultado");
        }
        return resultado;
    }

}
