import bbdd.Jdbc;

import menus.MenuPrincipal;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class Main {
    public static void main(String[] args) throws SQLException {

        String databaseUrl = "jdbc:mysql://localhost:3306/hestia";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "");

        Connection dbConnection = Jdbc.getConnection(databaseUrl, info);

        System.out.println("BIENVENIDO/A A HESTIA");

        MenuPrincipal.menuPrincipal(dbConnection);
    }

}