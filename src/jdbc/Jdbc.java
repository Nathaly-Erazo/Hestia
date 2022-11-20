package jdbc;

import java.sql.*;
import java.util.Properties;

//En esta clase se encuentra la conexión con la base de datos
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


}