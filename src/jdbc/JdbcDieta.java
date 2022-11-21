package jdbc;

import entidades.Dieta;
import menus.MenuCocina;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JdbcDieta {
    //En esta clase se encuentra lo relacionado con las consultas a la base de datos de la tabla dieta
    public static ArrayList<Dieta> consultarDieta(Connection conn) throws SQLException {
        //Se crea el método  de tipo array ya que son campos de diferente tipo
        ArrayList<Dieta> dietas = new ArrayList<>();
        Statement stmt = conn.createStatement(); //Objeto que sale de la conexion y se utiliza para declarar la consulta
        ResultSet rs = stmt.executeQuery("SELECT * FROM dieta"); //Consulta para mostrar todos los datos de tabla
        while (rs.next()) { //While para rellenar el objeto dieta
            Dieta dieta = new Dieta(); //Se crea un objeto dieta de tipo Dieta
            //Con las siguientes instrucciones se guarda en el objeto dieta todos los campos
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

    public static void insertarDieta(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            boolean insercion = true;
            //Se le da la opción al usuario de volver al menú anterioir por si se hubiera confudido
            System.out.println(""" 
                    ELIJA UNA OPCIÓN:\s
                    1.-Insertar Dieta\s
                    2.-Volver a Menú Cocina""");
            int opcion = sc.nextInt();
            switch (opcion) {
                case 1 -> {
                    Dieta dieta = new Dieta(); //Se crea un objeto dieta en el que se va ha guardar las inserciones
                    dieta.recogerDatosDieta(); //Se llama al método que se encuentra en la clase dieta para rellenar los datos
                    //Con la siguiente consulta se insertan los datos en la tabla
                    String query = "INSERT INTO dieta (nombre, desayuno, comida, merienda, cena) VALUES (?,?,?,?,?)";
                    //Es lo mismo que el stament pero indicado para hacer modificaciones de la tabla
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, dieta.getNombre());
                    preparedStmt.setString(2, dieta.getDesayuno());
                    preparedStmt.setString(3, dieta.getComida());
                    preparedStmt.setString(4, dieta.getMerienda());
                    preparedStmt.setString(5, dieta.getCena());

                    do {
                        System.out.println("¿Seguro que quiere insertar esta dieta? 1.-Sí 2.-No: ");
                        int insertar = sc.nextInt();
                        switch (insertar) {
                            case 1 -> {
                                preparedStmt.execute(); //Se insertan los datos
                                //Se muestra lo que se ha introducido
                                System.out.println("DIETA INTRODUCIDA");
                            }
                            case 2 -> MenuCocina.menuCocina(conn); //Le devuelve al menú cocina
                            default -> {
                                System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                insercion = false;
                            }
                        }
                    } while (!insercion);
                }
                case 2 -> MenuCocina.menuCocina(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    insertarDieta(conn); //Le devulve a este mismo menú
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            insertarDieta(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            insertarDieta(conn);
        }
    }

    public static void borrarDieta(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Borrar Dieta\s
                    2.-Volver a Menú Cocina""");
            int opcion = sc.nextInt();
            boolean borrado = true;
            switch (opcion) {
                case 1 -> {
                    Dieta.mostrarDietas(consultarDieta(conn)); //Se muestran todas las dietas
                    System.out.println("Introduzca el código de la dieta que desea borrar: ");
                    int codigo = sc.nextInt();
                    if (consultarSiExiste(conn, codigo) != 0) { //Se controla que la dieta exista
                        do {
                            System.out.println("¿Seguro que quiere borrar esta dieta? 1.-Sí 2.-No");
                            int borrar = sc.nextInt();
                            switch (borrar) {
                                case 1 -> {
                                    //Consulta para borrar una dieta según el código que ha introducido el usuario
                                    String query = "DELETE FROM dieta WHERE codigo = ?";
                                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                                    preparedStmt.setInt(1, codigo);

                                    preparedStmt.execute();
                                    System.out.println("DIETA BORRADA");
                                }
                                case 2 -> MenuCocina.menuCocina(conn); //Se le devulve al menú cocina
                                default -> {
                                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                    borrado = false;
                                }
                            }
                        } while (!borrado); //Se controla que la opción insertada sea válida
                    } else {
                        System.out.println("La dieta no existe");
                        borrarDieta(conn); //Si no existe se le devuelve a este menú
                    }
                }
                case 2 -> MenuCocina.menuCocina(conn); //Se le devulve al menú cocina
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    borrarDieta(conn); //Si el númro no es válido se le devuelve a este menú
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            borrarDieta(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            borrarDieta(conn);
        }
    }

    public static void editarDieta(Connection conn) throws SQLException {
        String query = "UPDATE dieta SET "; //Se inicializa la query y luego se completará según el campo a editar
        boolean seguir = true; //Controlar que quiere seguir editando o no
        boolean edicion = true; //Controlar que quiere realizar la edición
        Scanner scInt = new Scanner(System.in);
        Scanner scString = new Scanner(System.in);
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        try {
            System.out.println("""
                    ELIJA UNA OPCIÓN:\s
                    1.-Editar Dieta\s
                    2.-Volver a Menú Cocina""");
            int opcion = scInt.nextInt();
            switch (opcion) {
                case 1 -> {
                    Dieta.mostrarDietas(JdbcDieta.consultarDieta(conn)); //Muestra todas las dietas
                    System.out.println("Introducir código de la dieta que desea editar: ");
                    int codigo = scInt.nextInt();
                    if (consultarSiExiste(conn, codigo) != 0) { //Controlar que la dieta exista
                        System.out.println("""
                                ¿Qué campo quiere editar de la dieta?:\s
                                1.-Nombre\s
                                2.-Desayuno\s
                                3.-Comida\s
                                4.-Merienda\s
                                5.-Cena\s
                                6.-Volver a Menú Cocina""");
                        int campo = scInt.nextInt();
                        switch (campo) {
                            case 1 -> {
                                System.out.println("Introduzca el nuevo nombre: ");
                                String nombre = scString.nextLine();
                                query += "nombre = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, nombre);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 2 -> {
                                System.out.println("Introduzca el nuevo desayuno: ");
                                String desayuno = scString.nextLine();
                                query += "desayuno = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, desayuno);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 3 -> {
                                System.out.println("Introduzca la nueva comida: ");
                                String comida = scString.nextLine();
                                query += "comida = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, comida);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 4 -> {
                                System.out.println("Introduzca la nueva merienda: ");
                                String merienda = scString.nextLine();
                                query += "merienda = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, merienda);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 5 -> {
                                System.out.println("Introduzca la nueva cena: ");
                                String cena = scString.nextLine();
                                query += "cena = ? WHERE codigo = ?";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, cena);
                                preparedStmt.setInt(2, codigo);
                            }
                            case 6 -> MenuCocina.menuCocina(conn);
                            default -> {
                                System.out.println("Número fuera de rango, vuelva a introducir un número: ");
                                editarDieta(conn);
                            }
                        }
                        do {
                            System.out.println("¿Seguro que quiere editar esta dieta? 1.-Sí 2.-No");
                            int editar = scInt.nextInt();
                            switch (editar) {
                                case 1 -> {
                                    preparedStmt.execute(); //Se inserta el campo que se ha editado
                                    System.out.println("DIETA EDITADA");
                                }
                                case 2 -> MenuCocina.menuCocina(conn);
                                default -> {
                                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                    edicion = false;
                                }
                            }
                        } while (!edicion);
                        do {
                            System.out.println("¿Quiere editar otro campo? 1.-Sí 2.-Volver a Menú Cocina");
                            int continuar = scInt.nextInt();
                            switch (continuar) {
                                case 1 -> editarDieta(conn);
                                case 2 -> MenuCocina.menuCocina(conn);
                                default -> {
                                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                                    seguir = false;
                                }
                            }
                        } while (!seguir);
                    } else {
                        System.out.println("La dieta no existe");
                        editarDieta(conn);//Si no existe se le devuleve a este menú
                    }
                }
                case 2 -> MenuCocina.menuCocina(conn);
                default -> {
                    System.out.println("Número fuera de rango. Vuelva a intoducirlo");
                    editarDieta(conn);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Formato de número no válido");
            editarDieta(conn);
        } catch (Exception e) {
            System.out.println("Error indeterminado");
            editarDieta(conn);
        }
    }

    private static int consultarSiExiste(Connection conn, int codigo) throws SQLException {
        //Esta clase sirve para comprobar si existe la dieta que se quiere borrar o editar
        int resultado = 0;
        //Consulta para saber si existe. Se crea un alias (resultado) para referenciarlo como si fuera una columna
        String query = "SELECT EXISTS (SELECT * FROM dieta WHERE codigo=" + codigo + ") AS resultado";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            resultado = rs.getInt("resultado");
        }
        return resultado;
    }
}
