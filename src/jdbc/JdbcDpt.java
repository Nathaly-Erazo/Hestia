package jdbc;

import entidades.Dieta;
import entidades.Dpt;
import menus.MenuNutricion;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class JdbcDpt {
    //En esta clase se encuentra lo relaconado con las consultas a la base de datos de la tabla dieta_paciente_toma
    //Tiene la misma estructura que la clase JdbcDieta
    public static ArrayList<Dpt> consultarDpt(Connection conn) throws SQLException, ParseException {
        System.out.println("─────── CONSULTA REGISTROS ────────");
        ArrayList<Dpt> registros = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM dieta_paciente_toma");

        while (rs.next()) {
            Dpt registro = new Dpt();
            registro.setCodigo(rs.getInt("codigo"));
            registro.setNhcPaciente(rs.getInt("nhc_paciente"));
            registro.setCodigoDieta(rs.getInt("codigo_dieta"));
            registro.setCodigoToma(rs.getInt("codigo_toma"));
            registro.setFecha(rs.getString("fecha"));
            registros.add(registro);
        }
        return registros;
    }

    public static void insertarRegistro(Connection conn) {

        try {
            Scanner sc = new Scanner(System.in);
            boolean insercion = true;
            System.out.println("─────── AÑADIR REGISTRO ───────");
            System.out.println("""
                    ┌───────────────────────────────┐\s
                    │  Elija una opción:            │\s
                    │    1.-Insertar registro       │\s
                    │    2.-Volver a Menú Nutrición │\s
                    └───────────────────────────────┘\s
                     """);
            int opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {
                    Dpt registro = new Dpt();
                    registro.recogerDatosDpt();
                    String query = "INSERT INTO dieta_paciente_toma (nhc_paciente, codigo_dieta, codigo_toma, fecha) VALUES (?,?,?,?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, registro.getNhcPaciente());
                    preparedStmt.setInt(2, registro.getCodigoDieta());
                    preparedStmt.setInt(3, registro.getCodigoToma());
                    preparedStmt.setDate(4, java.sql.Date.valueOf(registro.getFecha()));

                    do {
                        System.out.println("¿Seguro que quiere ingresar este registro? 1.-Sí 2.-No: ");
                        int insertar = sc.nextInt();
                        switch (insertar) {
                            case 1 -> {
                                preparedStmt.execute();
                                System.out.println("✓✓✓ REGISTRO INTRODUCIDO ✓✓✓");
                            }
                            case 2 -> insertarRegistro(conn);
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
                    insertarRegistro(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            insertarRegistro(conn);
        } catch (Exception e) {
            System.err.println("Formato o valor no válido");
            insertarRegistro(conn);
        }
    }

    public static void borrarRegistro(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("─────── BORRAR REGISTRO ─────── ");
            System.out.println("""
                    ┌───────────────────────────────┐\s
                    │  Elija una opción:            │\s
                    │    1.-Borrar Registro         │\s
                    │    2.-Volver a Menú Nutrición │\s
                    └───────────────────────────────┘\s
                     """);
            int opcion = sc.nextInt();
            boolean borrado = true;
            switch (opcion) {
                case 1 -> {
                    Dpt.mostrarRegistros(JdbcDpt.consultarDpt(conn));
                    System.out.println("Introducir código del registro que desea borrar: ");
                    int codigo = sc.nextInt();
                    if (consultarSiExiste(conn, codigo) != 0) {
                        do {
                            System.out.println("¿Seguro que quiere borrar este resgistro? 1.-Sí 2.-No");
                            int borrar = sc.nextInt();
                            switch (borrar) {
                                case 1 -> {
                                    String query = "DELETE FROM dieta_paciente_toma WHERE codigo = ?";
                                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                                    preparedStmt.setInt(1, codigo);

                                    preparedStmt.execute();
                                    System.out.println("✓✓✓ REGISTRO BORRADO ✓✓✓");
                                }
                                case 2 -> borrarRegistro(conn);
                                default -> {
                                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                                    borrado = false;
                                }
                            }
                        } while (!borrado);
                    } else {
                        System.err.println("El registro no existe");
                        borrarRegistro(conn);
                    }
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                    borrarRegistro(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            borrarRegistro(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            borrarRegistro(conn);
        }
    }

    public static void editarRegistro(Connection conn) throws SQLException {
        String query = "UPDATE dieta_paciente_toma SET ";
        boolean seguir = true;
        boolean edicion = true;
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        try {
            System.out.println("─────── EDITAR REGISTRO ─────── ");
            System.out.println("""
                    ┌───────────────────────────────┐\s
                    │  Elija una opción:            │\s
                    │    1.-Editar Registro         │\s
                    │    2.-Volver a Menú Nutrición │\s
                    └───────────────────────────────┘\s
                     """);
            int opcion = scInt.nextInt();
            switch (opcion) {
                case 1 -> {
                    Dpt.mostrarRegistros(JdbcDpt.consultarDpt(conn));
                    System.out.println("Introducir código del registro que desea editar: ");
                    int codigo = scInt.nextInt();
                    if (consultarSiExiste(conn, codigo) != 0) {
                        System.out.println("""
                                ┌─────────────────────────────────────────────┐\s
                                │  ¿Qué campo quiere editar del registro?:    │\s
                                │    1.-Paciente                              │\s
                                │    2.-Dieta                                 │\s
                                │    3.-Toma                                  │\s
                                │    4.-Fecha                                 │\s
                                │    5.-Volver a Menú Nutrición               │\s
                                └─────────────────────────────────────────────┘\s
                                  """);
                        int campo = scInt.nextInt();
                        switch (campo) {
                            case 1 -> {
                                System.out.println("Introduzca el NHC del nuevo paciente: ");
                                int nhc = scInt.nextInt();
                                query += "nhc_paciente = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, nhc);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 2 -> {
                                System.out.println("Introduzca el código de la nueva dieta: ");
                                int dieta = scInt.nextInt();
                                query += "codigo_dieta = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, dieta);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 3 -> {
                                System.out.println("Introduzca el código de la nueva toma: ");
                                int toma = scInt.nextInt();
                                query += "codigo_toma = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, toma);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 4 -> {
                                System.out.println("Introduzca la nueva fecha: ");
                                String fecha = scString.nextLine();
                                query += "fecha = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setDate(1, java.sql.Date.valueOf(fecha));
                                preparedStmt.setInt(2, codigo);
                            }
                            case 5 -> MenuNutricion.menuNutricion(conn);
                            default -> {
                                System.err.println("Número fuera de rango, vuelva a introducir un número: ");
                                editarRegistro(conn);
                            }
                        }
                        do {
                            System.out.println("¿Seguro que quiere editar esta dieta? 1.-Sí 2.-No");
                            int editar = scInt.nextInt();
                            switch (editar) {
                                case 1 -> {
                                    preparedStmt.execute();
                                    System.out.println("✓✓✓ REGISTRO EDITADO ✓✓✓");
                                }
                                case 2 -> editarRegistro(conn);
                                default -> {
                                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                                    edicion = false;
                                }
                            }
                        } while (!edicion);
                        do {
                            System.out.println("¿Quiere editar otro campo? 1.-Sí 2.-Volver a Menú Nutrición");
                            int continuar = scInt.nextInt();
                            switch (continuar) {
                                case 1 -> editarRegistro(conn);
                                case 2 -> MenuNutricion.menuNutricion(conn);
                                default -> {
                                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                                    seguir = false;
                                }
                            }
                        } while (!seguir);
                    } else {
                        System.err.println("El registro no existe");
                        editarRegistro(conn);
                    }
                }
                case 2 -> MenuNutricion.menuNutricion(conn);
                default -> {
                    System.err.println("Número fuera de rango. Vuelva a intoducirlo");
                    editarRegistro(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.err.println("Formato de número no válido");
            editarRegistro(conn);
        } catch (Exception e) {
            System.err.println("Error indeterminado");
            editarRegistro(conn);
        }
    }

    private static int consultarSiExiste(Connection conn, int codigo) throws SQLException {
        int resultado = 0;
        String query = "SELECT EXISTS (SELECT * FROM dieta_paciente_toma WHERE codigo=" + codigo + ") AS resultado";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            resultado = rs.getInt("resultado");
        }
        return resultado;
    }
}